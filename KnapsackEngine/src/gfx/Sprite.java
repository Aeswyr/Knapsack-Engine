package gfx;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import runtime.Light;

/**
 * Sprite object for loading images and drawing them to the screen
 * 
 * @author Pascal
 *
 */
public class Sprite implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2743802919702462394L;

	int lightInteraction = Light.IGNORE;
	int priority = 0;

	int[][] raw;
	int width, height;

	int xOff, yOff;
	

	int frames;
	long delta, lastTime;


	/**
	 * Loads a sprite using a buffered image
	 * 
	 * @param b    - the buffered image
	 * @param type - sprite render type
	 */
	public Sprite(BufferedImage b, int type) {
		settings(type);

		frames = 1;
		raw = new int[frames][];

		width = b.getWidth();
		height = b.getHeight();

		raw[0] = b.getRGB(0, 0, width, height, null, 0, width);
	}

	/**
	 * Loads a sprite from a raw image raster
	 * 
	 * @param width  - width of the image
	 * @param height - height of the image
	 * @param raw    - image raster array
	 * @param type   - sprite render type
	 */
	public Sprite(int width, int height, int[] raw, int type) {

		settings(type);

		frames = 1;

		this.raw = new int[frames][];
		this.raw[0] = raw;

		this.width = width;
		this.height = height;
	}

	/**
	 * loads a sprite from a spritesheet
	 * 
	 * @param x     - x position of the sprite on the sheet
	 * @param y     - y position of the sprite on the sheet
	 * @param size  - width and height of the sprite
	 * @param sheet - spritesheet to load from
	 */
	public Sprite(int x, int y, int size, SpriteSheet sheet) {
		frames = 1;

		width = size;
		height = size;
		BufferedImage b = sheet.cut(x, y, size);

		raw = new int[frames][];
		raw[0] = b.getRGB(0, 0, width, height, null, 0, width);

		lastTime = System.nanoTime();
	}

	/**
	 * loads a sprite from a spritesheet
	 * 
	 * @param x     - x position of the sprite on the sheet
	 * @param y     - y position of the sprite on the sheet
	 * @param size  - width and height of the sprite
	 * @param sheet - spritesheet to load from
	 * @param type  - sprite render type
	 */
	public Sprite(int x, int y, int size, SpriteSheet sheet, int type) {
		settings(type);

		frames = 1;

		width = size;
		height = size;
		BufferedImage b = sheet.cut(x, y, size);

		raw = new int[frames][];
		raw[0] = b.getRGB(0, 0, width, height, null, 0, width);

		lastTime = System.nanoTime();
	}

	/**
	 * loads a sprite from a spritesheet
	 * 
	 * @param x      - x position of the sprite on the sheet
	 * @param y      - y position of the sprite on the sheet
	 * @param width  - width of the sprite
	 * @param height - height of the sprite
	 * @param sheet  - spritesheet to load from
	 */
	public Sprite(int x, int y, int width, int height, SpriteSheet sheet) {
		frames = 1;
		BufferedImage b = sheet.cut(x, y, width, height);

		raw = new int[frames][];
		raw[0] = b.getRGB(0, 0, width, height, null, 0, width);

		this.width = width;
		this.height = height;

		lastTime = System.nanoTime();
	}

	/**
	 * loads a sprite from a spritesheet
	 * 
	 * @param x      - x position of the sprite on the sheet
	 * @param y      - y position of the sprite on the sheet
	 * @param width  - width of the sprite
	 * @param height - height of the sprite
	 * @param sheet  - spritesheet to load from
	 * @param type   - sprite render type
	 */
	public Sprite(int x, int y, int width, int height, SpriteSheet sheet, int type) {

		settings(type);

		frames = 1;
		BufferedImage b = sheet.cut(x, y, width, height);

		raw = new int[frames][];
		raw[0] = b.getRGB(0, 0, width, height, null, 0, width);

		this.width = width;
		this.height = height;

		lastTime = System.nanoTime();
	}

	/**
	 * loads an animated sprite from a spritesheet
	 * 
	 * @param x      - x position of the topmost frame of the animation
	 * @param y      - y position of the topmost frame of the animation
	 * @param width  - width of each animation frame
	 * @param height - height of each animation frame
	 * @param frames - number of frames
	 * @param fps    - number of frames per second
	 * @param sheet  - spritesheet to load from
	 * @param type   - sprite render type
	 */
	public Sprite(int x, int y, int width, int height, int frames, int fps, SpriteSheet sheet, int type) {

		settings(type);

		this.frames = frames;
		this.delta = 1000000000 / fps;

		this.width = width;
		this.height = height;

		raw = new int[frames][];
		for (int i = 0; i < frames; i++) {
			BufferedImage b = sheet.cut(x, y + height * i, width, height);
			raw[i] = b.getRGB(0, 0, width, height, null, 0, width);
		}

		lastTime = System.nanoTime();

	}

	/**
	 * loads an animated sprite from a spritesheet
	 * 
	 * @param x      - x position of the topmost frame of the animation
	 * @param y      - y position of the topmost frame of the animation
	 * @param width  - width of each animation frame
	 * @param height - height of each animation frame
	 * @param frames - number of frames
	 * @param fps    - number of frames per second
	 * @param sheet  - spritesheet to load from
	 */
	public Sprite(int x, int y, int width, int height, int frames, int fps, SpriteSheet sheet) {
		this.frames = frames;
		this.delta = 1000000000 / fps;

		this.width = width;
		this.height = height;

		raw = new int[frames][];
		for (int i = 0; i < frames; i++) {
			BufferedImage b = sheet.cut(x, y + height * i, width, height);
			raw[i] = b.getRGB(0, 0, width, height, null, 0, width);
		}

		lastTime = System.nanoTime();

	}

	byte frame;

	/**
	 * requests to draw the sprite at the specified coordinate and updates the
	 * current frame
	 * 
	 * @param x - x position to draw at
	 * @param y - y position to draw at
	 * @param g - DrawGraphics component associated with the renderer
	 */
	public void render(int x, int y, DrawGraphics g) {

		if (frames > 1) {
			if (System.nanoTime() - lastTime > delta) {
				lastTime = System.nanoTime();
				frame++;
				if (frame >= frames)
					frame = 0;
			}
		}
		g.submitRequest(new SpriteRequest(this, priority, x - xOff, y - yOff));
	}

	/**
	 * Gets a new sprite that is a scaled version of the current one
	 * 
	 * @param scale - the factor by which to scale by
	 * @returns a new sprite which is the original scaled up or down
	 */
	public Sprite getScaledSprite(int scale) {
		int[] r = new int[width * height * scale * scale];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				for (int j = 0; j < scale; j++) {
					for (int i = 0; i < scale; i++) {
						r[(y * scale + j) * width * scale + x * scale + i] = raw[frame][y * width + x];
					}
				}

			}
		}
		return new Sprite(width * scale, height * scale, r, createLightData(priority, lightInteraction));
	}

	/**
	 * sets the alpha value for the sprite
	 * 
	 * @param alpha - the new alpha value for each pixel
	 * @returns a new sprite with the new alpha value
	 */
	public Sprite setAlpha(int alpha) {
		int a = (alpha >> 24) & 0xff;
		int c;

		int[] r0 = new int[width * height];
		for (int i = 0; i < width * height; i++) {
			c = raw[frame][i];
			if (c != 0xffff00ff)
				r0[i] = a << 24 | ((c >> 16) & 0xff) << 16 | ((c >> 8) & 0xff) << 8 | (c & 0xff);
			else
				r0[i] = c;
		}

		return new Sprite(width, height, r0, createLightData(priority, lightInteraction));
	}

	/**
	 * rotates the sprite by a specified amount
	 * 
	 * @param rad - the amount to rotate by (in radians)
	 * @returns a new sprite rotated by the specified amount
	 */
	public Sprite getRotatedImage(double rad) {
		int[] r0 = new int[width * height];

		float xx, yy;
		int xt, yt;
		float c = (float) Math.cos(rad);
		float s = (float) Math.sin(rad);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				xt = x - width / 2;
				yt = y - height / 2;

				xx = (xt * c - yt * s) + width / 2;
				yy = (xt * s + yt * c) + height / 2;
				xt = (int) xx;
				yt = (int) yy;
				if (xx < width && yy < height && xx >= 0 && yy >= 0) {
					r0[yt * width + xt] = raw[frame][y * width + x];
					if (xx - xt >= 0.5 && xx + 1 < width)
						r0[yt * width + (xt + 1)] = raw[frame][y * width + x];
					if (yy - yt >= 0.5 && yy + 1 < height)
						r0[(yt + 1) * width + xt] = raw[frame][y * width + x];
				}
			}
		}

		return new Sprite(width, height, r0, createLightData(priority, lightInteraction));
	}

	/**
	 * gets this sprite's r g b channels in separate images
	 * 
	 * @returns an array of sprites, the first containing the red channel, second
	 *          green, and the third blue
	 */
	public Sprite[] getRGBSplit() {
		int length = this.raw[frame].length;
		int[] r = new int[length];
		int[] g = new int[length];
		int[] b = new int[length];

		for (int i = 0; i < length; i++) {
			int c = raw[frame][i];
			if (c == 0xffff00ff) {
				r[i] = 0xffff00ff;
				g[i] = 0xffff00ff;
				b[i] = 0xffff00ff;
			} else {
				r[i] = c & 0xffff0000;
				g[i] = c & 0xff00ff00;
				b[i] = c & 0xff0000ff;
			}
		}

		Sprite[] s = { new Sprite(width, height, r, createLightData(priority, lightInteraction)), new Sprite(width, height, g, createLightData(priority, lightInteraction)),
				new Sprite(width, height, b, createLightData(priority, lightInteraction)) };
		return s;
	}

	/**
	 * gets this sprite's r g b channels in separate images with a set alpha value
	 * 
	 * @param alpha - the new alpha value for the colored channels
	 * @returns an array of sprites, the first containing the red channel, second
	 *          green, and the third blue
	 */
	public Sprite[] getRGBSplit(int alpha) {
		int length = this.raw[frame].length;
		int[] r = new int[length];
		int[] g = new int[length];
		int[] b = new int[length];

		alpha = alpha << 24;

		for (int i = 0; i < length; i++) {
			int c = raw[frame][i];
			if (c == 0xffff00ff) {
				r[i] = 0xffff00ff;
				g[i] = 0xffff00ff;
				b[i] = 0xffff00ff;
			} else {
				c = (c & 0x00ffffff) | alpha;
				r[i] = c & 0xffff0000;
				g[i] = c & 0xff00ff00;
				b[i] = c & 0xff0000ff;
			}
		}

		Sprite[] s = { new Sprite(width, height, r, createLightData(priority, lightInteraction)), new Sprite(width, height, g, createLightData(priority, lightInteraction)),
				new Sprite(width, height, b, createLightData(priority, lightInteraction)) };
		return s;
	}

	/**
	 * @returns the raw raster data for the current frame
	 */
	protected int[] getRawFrame() {
		return raw[frame];
	}

	/**
	 * @returns the width of the sprite
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @returns the height of the sprite
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * changes the light interaction type for the sprite
	 * 
	 * @param the new light interaction type
	 */
	public void setLightInteraction(int value) {
		this.lightInteraction = value;
	}

	/**
	 * @returns the sprite's light interaction type
	 */
	public int getLightInteraction() {
		return lightInteraction;
	}

	/**
	 * sets the default z coordinate for this sprite
	 * 
	 * @param value - new default z coordinate
	 */
	public void setPriority(int value) {
		this.priority = value;
	}

	/**
	 * alters the sprite's type properties based on an input sprite type
	 * 
	 * @param type - an input sprite type
	 */
	public void settings(int type) {
		this.lightInteraction = type & 0x00000003;
		this.priority = type >> 2;
	}
	
	public static int createLightData(int renderPriority, int lightInteraction) {
		
		return (renderPriority << 2) | (lightInteraction & 0x00000003);
	}
	
	
	
}
