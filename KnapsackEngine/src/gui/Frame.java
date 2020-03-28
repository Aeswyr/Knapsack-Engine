package gui;

import gfx.DrawGraphics;
import gfx.Sprite;

/**
 * blank frame UI object
 * 
 * @author Pascal
 *
 */
public class Frame extends UIObject {
	private Sprite frame;

	/**
	 * Initializes a frame
	 * 
	 * @param x      - x position of the frame
	 * @param y      - y position of the frame
	 * @param width  - width of the frame
	 * @param height - height of the frame
	 * @param n      - nineslice to use to construct the frame
	 */
	public Frame(int x, int y, int width, int height, NineSlice n) {
		this.x = x;
		this.y = y;

		// TODO ui scripting system to set up sprite data correctly
		frame = n.build(width, height, Sprite.createLightData(0, 0));
	}

	/**
	 * Initializes a frame
	 * 
	 * @param x      - x position of the frame
	 * @param y      - y position of the frame
	 * @param width  - width of the frame
	 * @param height - height of the frame
	 * @param n      - nineslice to use to construct the
	 * @param type   - render type for gui sprite
	 */
	public Frame(int x, int y, int width, int height, NineSlice n, int type) {
		this.x = x;
		this.y = y;

		frame = n.build(width, height, type);
	}

	/**
	 * Initializes a frame
	 * 
	 * @param x      - x position of the frame
	 * @param y      - y position of the frame
	 * @param width  - width of the frame
	 * @param height - height of the frame
	 * @param s      - sprite to display
	 */
	public Frame(int x, int y, int width, int height, Sprite s) {
		this.x = x;
		this.y = y;

		frame = s;

	}

	@Override
	public void render(DrawGraphics g) {
		frame.render(x, y, g);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

}
