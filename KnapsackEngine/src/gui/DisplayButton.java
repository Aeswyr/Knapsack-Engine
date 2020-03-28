package gui;

import gfx.DrawGraphics;
import gfx.Sprite;

/**
 * a button which also displays an image
 * 
 * @author Pascal King
 *
 */
public class DisplayButton extends Button {

	Sprite s;

	/**
	 * create a button with text
	 * 
	 * @param text   - text to display on the button
	 * @param action - action to perform on click
	 * @param x      - x position of this button
	 * @param y      - y position of this button
	 * @param width  - width of the button hitbox
	 * @param height - height of the button hitbox
	 * @param n1     - nine-slice for the button in idle position
	 * @param n2     - nine-slice for the button when hovered over
	 * @param s      - sprite to display over the button
	 */
	public DisplayButton(ClickListener action, int x, int y, int width, int height, NineSlice n1, NineSlice n2,
			Sprite s) {
		super(action, x, y, width, height, n1, n2);
		this.s = s;
	}

	@Override
	public void render(DrawGraphics g) {
		if (visible) {
			if (hovered)
				hover.render(x, y, g);
			else
				base.render(x, y, g);

			s.render(x, y, g);
		}
	}

}
