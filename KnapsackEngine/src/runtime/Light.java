package runtime;

import java.io.Serializable;

import gfx.DrawGraphics;
import gfx.LightRequest;

/**
 * Represents a single source of light
 * 
 * @author Pascal
 *
 */
public class Light implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8020722636961239307L;
	
	/**
	 * Light interaction type: pixel interacts with light normally
	 */
	public static final int NONE = 0;
	/**
	 * Light interaction type: pixel completely intercepts incoming light,
	 * preventing it from moving thru
	 */
	public static final int FULL = 1;
	/**
	 * Light interaction type: pixel ignores any changes made by light and is drawn
	 * in full light at all times
	 */
	public static final int IGNORE = 2;
	/**
	 * Light interaction type: pixel intercepts incoming lights, dimming them to a
	 * portion of their usual value and stopping them completely once they attempt
	 * to enter a NONE or FULL pixel
	 */
	public static final int DIM = 3;

	int x, y;

	int radius, diameter, color;
	int[] lightMap;

	/**
	 * Creates a circular light source
	 * 
	 * @param radius  - radius of the source
	 * @param color   - color of light
	 * @param handler
	 */
	public Light(int radius, int color) {
		this.radius = radius;
		this.diameter = radius * 2;
		this.color = color;
		lightMap = new int[diameter * diameter];

		for (int y = 0; y < diameter; y++) {
			for (int x = 0; x < diameter; x++) {
				double dist = Math.sqrt((x - radius) * (x - radius) + (y - radius) * (y - radius));
				float persistence = (float) (1 - (dist / radius));
				if (dist < radius)
					lightMap[y * diameter + x] = (int) (((color >> 16) & 0xff) * persistence) << 16
							| (int) (((color >> 8) & 0xff) * persistence) << 8 | (int) ((color & 0xff) * persistence);
				else
					lightMap[y * diameter + x] = 0;
			}
		}

	}

	/**
	 * @returns the array of pixel data for this light source
	 */
	public int[] getLightMap() {
		return lightMap;
	}

	/**
	 * @returns the diameter of this light source
	 */
	public int getDiameter() {
		return diameter;
	}

	/**
	 * @returns the radius of this light source
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 * returns the pixel data at a specific coordinate relative to the light source
	 * 
	 * @param x - x in pixels relative to the top left corner of the source
	 * @param y - y in pixels relative to the top left corner of the source
	 * @returns the hexidecimal color value of the light at the specified coordinate
	 */
	public int getLuminosity(int x, int y) {
		if (x >= diameter || x < 0 || y >= diameter || y < 0)
			return 0;
		return lightMap[y * diameter + x];

	}

	/**
	 * sets the position of this light source
	 * 
	 * @param x - new x position of the light (in pixels)
	 * @param y - new y position of the light (in pixels)
	 */
	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * causes this light source to begin emitting light
	 */
	public void light() {
		Handler.getLights().add(this);
	}

	/**
	 * stops this light source from emitting light
	 */
	public void snuff() {
		Handler.getLights().remove(this);
	}

	/**
	 * draws this light source's lighting
	 * 
	 * @param g - DrawGraphics component associated with renderer
	 */
	public void render(DrawGraphics g) {
		g.submitRequest(new LightRequest(this, x - Handler.getCamera().xOffset(), y - Handler.getCamera().yOffset()));
	}


}
