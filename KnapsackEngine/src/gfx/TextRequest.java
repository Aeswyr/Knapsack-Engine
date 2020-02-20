package gfx;

/**
 * a request for drawing text, used to order sprites before they are
 * rendered
 * 
 * @author Pascal
 *
 */
public class TextRequest extends Request {

	protected String s;
	protected int x, y, color;

	/**
	 * creates a request for text to be drawn with an arbitrarily high z coordinate
	 * @param s - the string to draw
	 * @param x - x position to draw at
	 * @param y - y position to draw at
	 * @param color - color of the text
	 */
	public TextRequest(String s, int x, int y, int color) {
		this.s = s;
		this.x = x;
		this.y = y;
		this.color = color;
		this.z = 1000000;
	}

	/**
	 * creates a request for text to be drawn with a custom z coordinate
	 * @param s - the string to draw
	 * @param x - x position to draw at
	 * @param y - y position to draw at
	 * @param color - color of the text
	 * @param z - z level for the text
	 */
	public TextRequest(String s, int x, int y, int color, int z) {
		this.s = s;
		this.x = x;
		this.y = y;
		this.color = color;
		this.z = z;
	}

}
