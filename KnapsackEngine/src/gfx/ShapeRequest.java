package gfx;

import geometry.Shape;

/**
 * a request for drawing a Shape, used to order sprites before they are rendered
 * 
 * @author Pascal
 *
 */
public class ShapeRequest extends Request {

	protected static final int TYPE_LINE = 0;
	protected static final int TYPE_RECT = 1;
	protected static final int TYPE_UNIQ = 2;

	protected int type;
	protected Shape s;
	protected int x, y, x1, y1, color;

	/**
	 * creates a request to draw a shape object
	 * 
	 * @param s - shape object
	 * @param x - x coordinate to draw at
	 * @param y - y coordinate to draw at
	 */
	public ShapeRequest(Shape s, int x, int y) {
		this.s = s;
		this.y = y;
		this.x = x;
		this.z = s.toSprite().priority;
		type = TYPE_UNIQ;
	}

	/**
	 * creates a request to draw a line with an arbitrarily high z value
	 * 
	 * @param x0    - starting x of the line
	 * @param y0    - starting y of the line
	 * @param x1    - end x of the line
	 * @param y1    - end y of the line
	 * @param color - color of the line
	 */
	public ShapeRequest(int x0, int y0, int x1, int y1, int color) {
		x = x0;
		y = y0;
		this.x1 = x1;
		this.y1 = y1;
		this.color = color;
		this.z = 1000000;

		type = TYPE_LINE;
	}

	/**
	 * creates a request to draw a rectangle with an arbitrarily high z value
	 * 
	 * @param x      - x position to draw at
	 * @param y      - y position to draw at
	 * @param width  - width of the rectangle
	 * @param height - height of the rectangle
	 * @param color  - color of the rectangle
	 * @param type   - boolean determines that this is a rectangle rather than a
	 *               line (value does not matter)
	 */
	public ShapeRequest(int x, int y, int width, int height, int color, boolean type) {
		this.x = x;
		this.y = y;
		this.x1 = width;
		this.y1 = height;
		this.color = color;
		this.z = 1000000;

		this.type = TYPE_RECT;
	}

	/**
	 * creates a request to draw a line with a specified z coordinate
	 * 
	 * @param x0    - starting x of the line
	 * @param y0    - starting y of the line
	 * @param x1    - end x of the line
	 * @param y1    - end y of the line
	 * @param color - color of the line
	 * @param z     - the z coordinate of the line
	 */
	public ShapeRequest(int x0, int y0, int x1, int y1, int color, int z) {
		x = x0;
		y = y0;
		this.x1 = x1;
		this.y1 = y1;
		this.color = color;
		this.z = z;
		type = TYPE_LINE;
	}

	/**
	 * creates a request to draw a rectangle with a specified z coordinate
	 * 
	 * @param x      - x position to draw at
	 * @param y      - y position to draw at
	 * @param width  - width of the rectangle
	 * @param height - height of the rectangle
	 * @param color  - color of the rectangle
	 * @param z      - the z coordinate of this rectangle
	 * @param type   - boolean determines that this is a rectangle rather than a
	 *               line (value does not matter)
	 */
	public ShapeRequest(int x, int y, int width, int height, int color, int z, boolean type) {
		this.x = x;
		this.y = y;
		this.x1 = width;
		this.y1 = height;
		this.color = color;
		this.z = z;

		this.type = TYPE_RECT;
	}

}
