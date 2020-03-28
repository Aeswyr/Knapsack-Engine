package gui;

import gfx.DrawGraphics;
import gfx.Sprite;

/**
 * a frame which also displays an image
 * 
 * @author Pascal King
 *
 */
public class Display extends UIObject {
	Sprite base, holding;
	boolean visible;

	/**
	 * Initializes a display with an invisible background
	 * 
	 * @param x      - x position of the frame
	 * @param y      - y position of the frame
	 * @param width  - width of the frame
	 * @param height - height of the frame
	 * @param hold   - sprite to display within the frame
	 * @param type   - render type for gui sprite
	 */
	public Display(int x, int y, int width, int height, Sprite hold, int type) {
		this.x = x;
		this.y = y;

		visible = false;
		holding = hold;
	}

	/**
	 * Initializes a display
	 * 
	 * @param x      - x position of the frame
	 * @param y      - y position of the frame
	 * @param width  - width of the frame
	 * @param height - height of the frame
	 * @param n      - nineslice to use to construct the
	 * @param hold   - sprite to display within the frame
	 * @param type   - render type for gui sprite
	 */
	public Display(int x, int y, int width, int height, NineSlice n, Sprite hold, int type) {
		this.x = x;
		this.y = y;

		visible = true;
		base = n.build(width, height, type);
		holding = hold;
	}

	@Override
	public void render(DrawGraphics g) {
		if (visible)
			base.render(x, y, g);
		holding.render(x, y, g);

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

}
