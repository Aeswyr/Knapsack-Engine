package gui;

import gfx.DrawGraphics;
import gfx.Sprite;
import sfx.Sound;

/**
 * jrpg style text box with scrolling text
 * 
 * @author Pascal King
 *
 */
public class TextBox extends UIObject {

	String text = "";
	int count = 0;

	Sound textsound;
	
	Sprite base;

	/**
	 * Initializes a TextBox
	 * 
	 * @param x      - x position of the frame
	 * @param y      - y position of the frame
	 * @param width  - width of the frame
	 * @param height - height of the frame
	 * @param n      - nineslice to use to construct the frame
	 */
	public TextBox(int x, int y, int width, int height, NineSlice n) {
		this.x = x;
		this.y = y;

		// TODO ui scripting system to set up sprite data correctly
		base = n.build(width, height, Sprite.createLightData(0, 0));
	}

	/**
	 * Initializes a TextBox
	 * 
	 * @param x      - x position of the frame
	 * @param y      - y position of the frame
	 * @param width  - width of the frame
	 * @param height - height of the frame
	 * @param color  - primary color of the frame
	 * @param border - border color of the frame
	 * @param type   - render type for gui sprite
	 */
	public TextBox(int x, int y, int width, int height, NineSlice n, int type) {
		this.x = x;
		this.y = y;

		base = n.build(width, height, type);
	}

	/**
	 * sets the next block of text to display and begins displaying immediately
	 * @param s - string of text to display
	 */
	public void setText(String s) {
		count = s.length() - 1;
		text = s;
		if (textsound != null) {
			textsound.stopLoop();
			textsound.loop();
		}
	}

	@Override
	public void render(DrawGraphics g) {
		base.render(x, y, g);
		g.write(text.substring(0, text.length() - count), x, y);

	}

	@Override
	public void update() {
		if (count > 0) count--;
		else if (count == 1 && textsound != null) textsound.stopLoop();
	}
	
	/**
	 * finishes the text display instantly
	 */
	public void rush() {
		if (count > 1) count = 1;
	}

}
