package gfx;
/**
 * a request for drawing a sprite, used to order sprites before they are
 * rendered
 * 
 * @author Pascal
 *
 */
public class SpriteRequest extends Request{

	protected Sprite s;
	protected int x, y;

	/**
	 * creates a request to draw a sprite with the specified x, y and z coordinates
	 * @param s - the sprite to draw
	 * @param z - the z level of the sprite
	 * @param x - the x coordinate to draw at
	 * @param y - the y coordinate to draw at
	 */
	public SpriteRequest(Sprite s, int z, int x, int y) {
		this.s = s;
		this.y = y;
		this.x = x;
		this.z = z;
	}

}
