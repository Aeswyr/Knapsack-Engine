package gfx;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import core.Driver;
import geometry.Shape;
import runtime.Light;

/**
 * handles rendering and ordering as well as lighting
 * 
 * @author Pascal
 *
 */
public class DrawGraphics {

	ArrayList<Request> requestList;
	ArrayList<LightRequest> lightRequest;

	int fullWidth, fullHeight;
	int width, height;
	BufferedImage screen;
	int[] raster;
	int[] zBuffer;
	int[] lightMap;
	int[] lightCollision;

	int ambientColor;

	int z = 0;

	private Font font;

	boolean lightingEnabled = false;

	public DrawGraphics(Driver d) {
		width = 960;
		height = 540;
		fullWidth = d.getWidth();
		fullHeight = d.getHeight();
		System.out.println(fullWidth + ", " + fullHeight);
		screen = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		raster = ((DataBufferInt) screen.getRaster().getDataBuffer()).getData();
		zBuffer = new int[raster.length];
		lightMap = new int[raster.length];
		lightCollision = new int[raster.length];

		requestList = new ArrayList<Request>();
		lightRequest = new ArrayList<LightRequest>();

		ambientColor = 0xff000000;
	}

	/**
	 * applies lighting then draws the screen
	 * 
	 * @param g - the Graphics object associated with the canvas
	 */
	public void render(Graphics g) {
		if (lightingEnabled)
			for (int i = 0; i < raster.length; i++) {
				raster[i] = (int) (((raster[i] >> 16) & 0xff) * (((lightMap[i] >> 16) & 0xff) / 255f)) << 16
						| (int) (((raster[i] >> 8) & 0xff) * (((lightMap[i] >> 8) & 0xff) / 255f)) << 8
						| (int) ((raster[i] & 0xff) * ((lightMap[i] & 0xff) / 255f));
			}

		g.drawImage(screen, 0, 0, fullWidth, fullHeight, null);
	}

	/**
	 * orders draw requests and light requests
	 */
	public void process() {

		Collections.sort(requestList, new Comparator<Request>() {

			@Override
			public int compare(Request i0, Request i1) {
				if (i0.z < i1.z)
					return -1;
				if (i0.z > i1.z)
					return 1;
				return 0;
			}

		});
		int temp = 0;
		for (int i = 0; i < requestList.size(); i++) {
			Request req = requestList.get(i);
			if (req.z > 50) {
				temp = i;
				break;
			}
			setZBuffer(req.z);
			if (req instanceof SpriteRequest) {
				SpriteRequest r = (SpriteRequest) req;
				draw(r.s, r.x, r.y);
			} else if (req instanceof ShapeRequest) {
				parseShape((ShapeRequest) req);
			} else if (req instanceof TextRequest) {
				TextRequest r = (TextRequest) req;
				this.writeText(r.s, r.x, r.y, r.color);
			}
		}
		for (int i = 0; i < lightRequest.size(); i++) {
			LightRequest req = lightRequest.get(i);
			drawLight(req.l, req.x, req.y);
		}
		for (int i = temp; i < requestList.size(); i++) {
			Request req = requestList.get(i);
			setZBuffer(req.z);
			if (req instanceof SpriteRequest) {
				SpriteRequest r = (SpriteRequest) req;
				drawPost(r.s, r.x, r.y);
			} else if (req instanceof ShapeRequest) {
				parseShapePost((ShapeRequest) req);
			} else if (req instanceof TextRequest) {
				TextRequest r = (TextRequest) req;
				this.writeText(r.s, r.x, r.y, r.color);
			}
		}
		requestList.clear();
		lightRequest.clear();
	}

	public void processgl() {

	}

	public void rendergl() {

	}

	/**
	 * sets the current z level for drawing
	 * 
	 * @param z - the new z level
	 */
	public void setZBuffer(int z) {
		this.z = z;
	}

	/**
	 * adds a new request to the request list
	 * 
	 * @param s - the request to be added
	 */
	public void submitRequest(Request s) {
		requestList.add(s);
	}

	/**
	 * adds a new light request to the request list
	 * 
	 * @param l - the light request to be added
	 */
	public void submitRequest(LightRequest l) {
		lightRequest.add(l);
	}

	/**
	 * sets the light collision value of a specific pixel
	 * 
	 * @param x     - x coordinate of the pixel
	 * @param y     - y coordinate of the pixel
	 * @param value - new light collision value
	 */
	public void setLightCollision(int x, int y, int value) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			return;
		lightCollision[y * width + x] = value;
	}

	/**
	 * adds a request to draw the specified text to the screen
	 * 
	 * @param text - text to write
	 * @param x    - x position to write at
	 * @param y    - y position to write at
	 */
	public void write(String text, int x, int y) {
		requestList.add(new TextRequest(text, x, y, 0xffffffff));
	}

	/**
	 * adds a request to draw the specified text to the screen in the specified
	 * color
	 * 
	 * @param text  - text to write
	 * @param x     - x position to write at
	 * @param y     - y position to write at
	 * @param color - color of the text
	 */
	public void write(String text, int x, int y, int color) {
		requestList.add(new TextRequest(text, x, y, color));
	}

	/**
	 * draws text to the screen
	 * 
	 * @param text  - text to draw
	 * @param xOff  - x coordinate to draw at
	 * @param yOff  - y coordinate to draw at
	 * @param color - color of the text
	 */
	private void writeText(String text, int xOff, int yOff, int color) {
		text = text.toUpperCase();

		Sprite fontSprite = font.getSprite();
		int[] off = font.getOffsets();
		int[] wid = font.getWidths();

		int offset = 0;
		for (int i = 0; i < text.length(); i++) {
			int unicode = text.codePointAt(i) - 32;
			for (int y = 0; y < fontSprite.getHeight(); y++)
				for (int x = 0; x < wid[unicode]; x++) {
					if (fontSprite.getRawFrame()[x + off[unicode] + y * fontSprite.getWidth()] == 0xffffffff) {
						drawPixel(x + xOff + offset, y + yOff, color);
						this.drawLuminosity(x + xOff + offset, y + yOff, 0xffffffff);
					}
				}
			offset += wid[unicode];
		}
	}

	/**
	 * draws a pixel at the specified position
	 * 
	 * @param x     - x position to draw at
	 * @param y     - y position to draw at
	 * @param value - hexidecimal code for the pixel data
	 */
	private void drawPixel(int x, int y, int value) {

		int alpha = (value >> 24) & 0xff;

		if ((x < 0 || x >= width || y < 0 || y >= height) || value == 0xffff00ff || alpha == 0)
			return;

		if (zBuffer[y * width + x] > z)
			return;

		if (alpha == 255)
			raster[y * width + x] = value;
		else {
			int pixel = raster[y * width + x];
			int r = ((pixel >> 16) & 0xff) - (int) ((((pixel >> 16) & 0xff) - ((value >> 16) & 0xff)) * (alpha / 255f));
			int g = ((pixel >> 8) & 0xff) - (int) ((((pixel >> 8) & 0xff) - ((value >> 8) & 0xff)) * (alpha / 255f));
			int b = (pixel & 0xff) - (int) (((pixel & 0xff) - (value & 0xff)) * (alpha / 255f));
			raster[y * width + x] = r << 16 | g << 8 | b;
		}
	}

	/**
	 * draws lighting at the specified position
	 * 
	 * @param x     - x position to draw at
	 * @param y     - y position to draw at
	 * @param value - hexidecimal code for the lighting data
	 */
	private void drawLuminosity(int x, int y, int value) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			return;

		int r, g, b;
		int pixel = lightMap[y * width + x];

		r = Math.max((pixel >> 16) & 0xff, (value >> 16) & 0xff);
		g = Math.max((pixel >> 8) & 0xff, (value >> 8) & 0xff);
		b = Math.max(pixel & 0xff, value & 0xff);

		lightMap[y * width + x] = r << 16 | g << 8 | b;
	}

	/**
	 * draws a light at the specified coordinates
	 * 
	 * @param l    - the light to draw
	 * @param xOff - x position to draw at
	 * @param yOff - y position to draw at
	 */
	public void drawLight(Light l, int xOff, int yOff) {

		int rad = l.getRadius();

		for (int i = 0; i <= l.getDiameter(); i++) {
			drawLightRay(l, rad, rad, i, 0, xOff, yOff);
			drawLightRay(l, rad, rad, i, rad * 2, xOff, yOff);
			drawLightRay(l, rad, rad, 0, i, xOff, yOff);
			drawLightRay(l, rad, rad, rad * 2, i, xOff, yOff);
		}

	}

	/**
	 * draws a single line of light emanating from the source
	 * 
	 * @param l    - the light to draw from
	 * @param x0   - the x origin of the light (in relation to the light raster)
	 * @param y0   - the y origin of the light (in relation to the light raster)
	 * @param x1   - the final x of the light (in relation to the light raster)
	 * @param y1   - the final y of the light (in relation to the light raster)
	 * @param xOff - x position to draw at
	 * @param yOff - y position to draw at
	 */
	private void drawLightRay(Light l, int x0, int y0, int x1, int y1, int xOff, int yOff) {
		boolean mark = false; // marked true when the light has passed through a wall
		boolean done = false;
		int dx = Math.abs(x1 - x0);
		int dy = Math.abs(y1 - y0);

		int sx = x0 < x1 ? 1 : -1;
		int sy = y0 < y1 ? 1 : -1;

		int err = dx - dy;
		int e2;

		int rad = l.getRadius();

		while (true) {

			int screenX = x0 - rad + xOff;
			int screenY = y0 - rad + yOff;

			if (screenX < 0 || screenX >= width || screenY < 0 || screenY >= height)
				return;

			int color = l.getLuminosity(x0, y0);
			if (color == 0)
				return;
			if (lightCollision[screenY * width + screenX] == Light.FULL)
				return;
			if (mark && lightCollision[screenY * width + screenX] == Light.NONE)
				done = true;
			if (lightCollision[screenY * width + screenX] == Light.DIM) {
				int r = ((color >> 16) & 0xff) / 2; // TODO return to 6 after testing
				int g = ((color >> 8) & 0xff) / 2; // TODO return to 6 after testing
				int b = ((color) & 0xff) / 2; // TODO return to 6 after testing
				color = r << 16 | g << 8 | b;
				mark = true;
			}

			if (!done || lightCollision[screenY * width + screenX] == Light.DIM)
				drawLuminosity(screenX, screenY, color);
			if (x0 == x1 && y0 == y1)
				break;
			e2 = 2 * err;
			if (e2 > -1 * dy) {
				err -= dy;
				x0 += sx;
			}
			if (e2 < dx) {
				err += dx;
				y0 += sy;
			}
		}
	}

	/**
	 * clears the raster, z-buffer, and lighting, resetting all values to their
	 * default
	 */
	public void clear() {
		for (int i = 0; i < raster.length; i++) {
			raster[i] = 0;
			zBuffer[i] = 0;
			lightMap[i] = ambientColor;
			lightCollision[i] = 0;
		}
	}

	/**
	 * draws a sprite at the specified coordinate. Accounts for lighting
	 * 
	 * @param s    - sprite to draw
	 * @param xOff - x position to draw at
	 * @param yOff - y position to draw at
	 */
	public void draw(Sprite s, int xOff, int yOff) {

		int xCap = 0;
		int yCap = 0;
		int widthCap = s.getWidth();
		int heightCap = s.getHeight();

		if (widthCap + xOff > width) {
			widthCap -= widthCap + xOff - width;
		}
		if (heightCap + yOff > height) {
			heightCap -= heightCap + yOff - height;
		}

		if (xCap + xOff < 0) {
			xCap -= xOff;
		}
		if (yCap + yOff < 0) {
			yCap -= yOff;
		}

		int[] r = s.getRawFrame();
		int w = s.getWidth();
		int l = s.getLightInteraction();
		for (int y = yCap; y < heightCap; y++) {
			for (int x = xCap; x < widthCap; x++) {
				drawPixel(x + xOff, y + yOff, r[y * w + x]);
				if (r[y * w + x] != 0xffff00ff)
					setLightCollision(x + xOff, y + yOff, l);
			}
		}

	}

	/**
	 * draws a sprite at the specified coordinate, ignoring any lighting changes
	 * 
	 * @param s    - the sprite to draw
	 * @param xOff - x position to draw at
	 * @param yOff - y position to draw at
	 */
	public void drawPost(Sprite s, int xOff, int yOff) {

		int xCap = 0;
		int yCap = 0;
		int widthCap = s.getWidth();
		int heightCap = s.getHeight();

		if (widthCap + xOff > width) {
			widthCap -= widthCap + xOff - width;
		}
		if (heightCap + yOff > height) {
			heightCap -= heightCap + yOff - height;
		}

		if (xCap + xOff < 0) {
			xCap -= xOff;
		}
		if (yCap + yOff < 0) {
			yCap -= yOff;
		}

		int[] r = s.getRawFrame();
		int w = s.getWidth();
		int l = s.getLightInteraction();
		for (int y = yCap; y < heightCap; y++) {
			for (int x = xCap; x < widthCap; x++) {
				drawPixel(x + xOff, y + yOff, r[y * w + x]);
				if (l == Light.IGNORE && r[y * w + x] != 0xffff00ff)
					drawLuminosity(x + xOff, y + yOff, 0xffffffff);
			}
		}

	}

	/**
	 * draws a shape at the specified coordinate, accounting for lighting
	 * 
	 * @param s    - the shape to draw
	 * @param xOff - x position to draw at
	 * @param yOff - y position to draw at
	 */
	public void draw(Shape s, int xOff, int yOff) {
		int xCap = 0;
		int yCap = 0;
		int widthCap = s.getWidth();
		int heightCap = s.getHeight();

		if (widthCap + xOff > width) {
			widthCap -= widthCap + xOff - width;
		}
		if (heightCap + yOff > height) {
			heightCap -= heightCap + yOff - height;
		}

		if (xCap + xOff < 0) {
			xCap -= xOff;
		}
		if (yCap + yOff < 0) {
			yCap -= yOff;
		}

		int[] r = s.getRaster();
		int w = s.getWidth();
		for (int y = yCap; y < heightCap; y++) {
			for (int x = xCap; x < widthCap; x++) {
				drawPixel(x + xOff, y + yOff, r[y * w + x]);
			}
		}
	}

	/**
	 * draws a rectangle
	 * 
	 * @param xOff   - x position to draw at
	 * @param yOff   - y position to draw at
	 * @param width  - width of the rectangle
	 * @param height - height of the rectangle
	 * @param color  - color of the rectangle
	 */
	private void fillRect(int xOff, int yOff, int width, int height, int color) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				this.drawPixel(xOff + x, yOff + y, color);
			}
		}
	}

	/**
	 * makes a request to draw a rectangle with an arbitrarily high z coordinate
	 * 
	 * @param x      - x position to draw at
	 * @param y      - y position to draw at
	 * @param width  - width of the rectangle
	 * @param height - height of the rectangle
	 * @param color
	 */
	public void drawRect(int x, int y, int width, int height, int color) {
		requestList.add(new ShapeRequest(x, y, width, height, color, true));
	}

	/**
	 * makes a request to draw a rectangle with a specific z coordinate
	 * 
	 * @param x      - x position to draw at
	 * @param y      - y position to draw at
	 * @param width  - width of the rectangle
	 * @param height - height of the rectangle
	 * @param z      - z level of the rectangle
	 * @param color  - color of the rectangle
	 */
	public void drawRect(int x, int y, int width, int height, int z, int color) {
		requestList.add(new ShapeRequest(x, y, width, height, color, z, true));
	}

	/**
	 * makes a request to draw a line with an arbitrarily high z coordinate
	 * 
	 * @param x0    - origin x of the line
	 * @param y0    - origin y of the line
	 * @param x1    - end x of the line
	 * @param y1    - end y of the line
	 * @param color - color of the line
	 */
	public void drawLine(int x0, int y0, int x1, int y1, int color) {
		requestList.add(new ShapeRequest(x0, y0, x1, y1, color));
	}

	/**
	 * makes a request to draw a line with a specific z coordinate
	 * 
	 * @param x0    - origin x of the line
	 * @param y0    - origin y of the line
	 * @param x1    - end x of the line
	 * @param y1    - end y of the line
	 * @param color - color of the line
	 * @param z     - z coordinate of the line
	 */
	public void drawLine(int x0, int y0, int x1, int y1, int color, int z) {
		requestList.add(new ShapeRequest(x0, y0, x1, y1, color, z));
	}

	/**
	 * draws a line
	 * 
	 * @param x0    - origin x of the line
	 * @param y0    - origin y of the line
	 * @param x1    - end x of the line
	 * @param y1    - end y of the line
	 * @param color - color of the line
	 */
	private void fillLine(int x0, int y0, int x1, int y1, int color) {

		int dx = Math.abs(x1 - x0);
		int dy = Math.abs(y1 - y0);

		int sx = x0 < x1 ? 1 : -1;
		int sy = y0 < y1 ? 1 : -1;

		int err = dx - dy;
		int e2;

		while (true) {

			int screenX = x0;
			int screenY = y0;

			if (screenX < 0 || screenX >= width || screenY < 0 || screenY >= height)
				return;

			drawPixel(screenX, screenY, color);
			if (x0 == x1 && y0 == y1)
				break;
			e2 = 2 * err;
			if (e2 > -1 * dy) {
				err -= dy;
				x0 += sx;
			}
			if (e2 < dx) {
				err += dx;
				y0 += sy;
			}
		}
	}

	/**
	 * takes a ShapeRequest and applies the correct draw method based on the type of
	 * request
	 * 
	 * @param r - ShapeRequest to parse
	 */
	private void parseShape(ShapeRequest r) {
		switch (r.type) {
		case 0:
			fillLine(r.x, r.y, r.x1, r.y1, r.color);
			break;
		case 1:
			fillRect(r.x, r.y, r.x1, r.y1, r.color);
			break;
		case 2:
			draw(r.s, r.x, r.y);
			break;
		default:

		}
	}

	/**
	 * takes a ShapeRequest and applies the correct draw method based on the type of
	 * request. Run after lighting has been applied
	 * 
	 * @param r - ShapeRequest to parse
	 */
	private void parseShapePost(ShapeRequest r) {
		switch (r.type) {
		case 0:
			fillLine(r.x, r.y, r.x1, r.y1, r.color);
			break;
		case 1:
			fillRect(r.x, r.y, r.x1, r.y1, r.color);
			break;
		case 2:
			drawPost(r.s.toSprite(), r.x, r.y);
			break;
		default:

		}
	}

	/**
	 * sets the currently active font
	 * 
	 * @param f - the font to use
	 */
	public void setFont(Font f) {
		this.font = f;
	}

	/**
	 * Sets the color for darkness, the normal lighting level
	 * 
	 * @param colorHex - color hex desired for ambient light
	 */
	public void setAmbientLightColor(int colorHex) {
		this.ambientColor = colorHex;
	}

	/**
	 * Enables or disables lighting
	 * 
	 * @param light - sets the lighting, true for enabled, false for disabled
	 */
	public void setLightingEnabled(boolean light) {
		this.lightingEnabled = light;
	}
}