package geometry;

import gfx.Sprite;

/**
 * used for drawing square and rectangle shapes
 * 
 * @author Pascal
 *
 */
public class Rect extends Shape {

	boolean hollow = false;

	/**
	 * generates a solid color rectangle
	 * 
	 * @param width  - width of the rectangle
	 * @param height - height of the rectangle
	 * @param color  - color to fill the rectangle
	 * @param type   - sprite type for render order
	 */
	public Rect(int width, int height, int color, int type) {
		this.width = width;
		this.height = height;
		this.type = type;
		this.color = color;
		this.raster = new int[width * height];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				raster[y * width + x] = color;
			}
		}

		this.sprite = new Sprite(width, height, raster, type);
	}

	/**
	 * generates a hollow rectangle
	 * 
	 * @param width  - width of the rectangle
	 * @param height - height of the rectangle
	 * @param color  - color to fill the rectangle
	 * @param type   - sprite type for render order
	 * @param hollow - having a boolean here (no matter the value) determines that
	 *               this rectangle will only be an outline
	 */
	public Rect(int width, int height, int color, int type, boolean hollow) {
		this.width = width;
		this.height = height;
		this.type = type;
		this.color = color;
		this.raster = new int[width * height];
		this.hollow = hollow;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (x == 0 || y == 0 || x == width - 1 || y == height - 1)
					raster[y * width + x] = color;
			}
		}

		this.sprite = new Sprite(width, height, raster, type);
	}

	/**
	 * generates a filled rectangle with an outline
	 * 
	 * @param width  - width of the rectangle
	 * @param height - height of the rectangle
	 * @param color1 - color to fill the rectangle
	 * @param color2 - color to outline the rectangle
	 * @param type   - sprite type for render order
	 */
	public Rect(int width, int height, int color1, int color2, int type) {
		this.width = width;
		this.height = height;
		this.type = type;
		this.color = color1;
		this.color2 = color2;
		this.raster = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (x == 0 || y == 0 || x == width - 1 || y == height - 1)
					raster[y * width + x] = color2;
				else
					raster[y * width + x] = color;
			}
		}

		this.sprite = new Sprite(width, height, raster, type);
	}

	@Override
	public boolean contains(Shape s) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * readjusts the raster for a new width and height
	 * @param width - the new width
	 * @param height - the new height
	 */
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
		this.raster = new int[width * height];
		if (hollow) {
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (x == 0 || y == 0 || x == height - 1 || y == height - 1)
						raster[y * width + x] = color;
				}
			}
		} else if (color2 != -1) {
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (x == 0 || y == 0 || x == width - 1 || y == height - 1)
						raster[y * width + x] = color2;
					else
						raster[y * width + x] = color;
				}
			}
		} else {
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					raster[y * width + x] = color;
				}
			}
		}
		this.sprite = new Sprite(width, height, raster, type);
	}

}
