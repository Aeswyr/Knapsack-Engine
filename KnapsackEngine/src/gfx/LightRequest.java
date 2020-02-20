package gfx;

import runtime.Light;

/**
 * a request for drawing lights, used to order sprites before they are rendered
 * 
 * @author Pascal
 *
 */
public class LightRequest {

	protected Light l;
	protected int x, y;

	/**
	 * creates a request to draw a light
	 * @param l - the light to draw
	 * @param x - the x position to draw at
	 * @param y - the y position to draw at
	 */
	public LightRequest(Light l, int x, int y) {
		this.l = l;
		this.x = x;
		this.y = y;
	}

}
