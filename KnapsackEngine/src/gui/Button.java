package gui;

import entity.HitSquare_KS;
import entity.Hitbox_KS;
import gfx.DrawGraphics;
import gfx.Sprite;
import input.Controller;

/**
 * A clickable UI component which perfomes a specified action on click
 * 
 * @author Pascal
 *
 */
public class Button extends UIObject {

	private ClickListener action;
	protected boolean hovered = false;
	protected boolean visible = false;
	private Hitbox_KS hitbox;
	protected Sprite base;
	protected Sprite hover;
	String text = null;

	/**
	 * create an invisible button
	 * 
	 * @param action  - action to perform on click
	 * @param x       - x position of this button
	 * @param y       - y position of this button
	 * @param width   - width of the button hitbox
	 * @param height  - height of the button hitbox
	 * @param handler
	 */
	public Button(ClickListener action, int x, int y, int width, int height) {
		this.action = action;

		hitbox = new HitSquare_KS(new int[][]{{x, y}, {x + width, y}, {x + width, y + height}, {x, y + height}});
		
		this.x = x;
		this.y = y;
	}

	/**
	 * creates a visible, blank button
	 * 
	 * @param action  - action to perform on click
	 * @param x       - x position of this button
	 * @param y       - y position of this button
	 * @param width   - width of the button hitbox
	 * @param height  - height of the button hitbox
	 * @param n1  - nine-slice for the button in idle position
	 * @param n2  - nine-slice for the button when hovered over
	 * @param handler
	 */
	public Button(ClickListener action, int x, int y, int width, int height, NineSlice n1, NineSlice n2) {
		visible = true;
		this.action = action;
		
		hitbox = new Hitbox_KS(new int[][]{{x, y}, {x + width, y}, {x + width, y + height}, {x, y + height}});
		
		this.x = x;
		this.y = y;

		//TODO ui scripting system to set up sprite data correctly
		base = n1.build(width, height, Sprite.createLightData(0, 0));
		hover = n2.build(width, height, Sprite.createLightData(0, 0));
	}

	/**
	 * create a button with text
	 * 
	 * @param text   - text to display on the button
	 * @param action - action to perform on click
	 * @param x      - x position of this button
	 * @param y      - y position of this button
	 * @param width  - width of the button hitbox
	 * @param height - height of the button hitbox
	 * @param n1  - nine-slice for the button in idle position
	 * @param n2  - nine-slice for the button when hovered over
	 */
	public Button(String text, ClickListener action, int x, int y, int width, int height,  NineSlice n1, NineSlice n2, int type) {
		visible = true;
		this.action = action;

		hitbox = new Hitbox_KS(new int[][]{{x, y}, {x + width, y}, {x + width, y + height}, {x, y + height}});
		
		this.x = x;
		this.y = y;

		this.text = text;

		base = n1.build(width, height, type);
		hover = n2.build(width, height, type);
	}

	/**
	 * create a button with text
	 * 
	 * @param text   - text to display on the button
	 * @param action - action to perform on click
	 * @param x      - x position of this button
	 * @param y      - y position of this button
	 * @param width  - width of the button hitbox
	 * @param height - height of the button hitbox
	 * @param s0     - sprite to display
	 * @param s1     - sprite to display when hovered
	 */
	public Button(String text, ClickListener action, int x, int y, int width, int height, Sprite s0, Sprite s1) {
		visible = true;
		this.action = action;

		hitbox = new Hitbox_KS(new int[][]{{x, y}, {x + width, y}, {x + width, y + height}, {x, y + height}});
		
		this.x = x;
		this.y = y;

		this.text = text;

		base = s0;
		hover = s1;
	}

	@Override
	public void render(DrawGraphics g) {
		if (visible) {
			if (hovered)
				hover.render(x, y, g);
			else
				base.render(x, y, g);

			if (text != null)
				g.write(text, x, y, 0xff000000);
		}
	}

	boolean lastMouseFrame = false;

	@Override
	public void update() {
		if (hitbox.containsMouse()) {
			this.hovered = true;
			if (lastMouseFrame == true && !Controller.getMousePressed(Controller.MOUSELEFT))
				doClick();
			lastMouseFrame = Controller.getMousePressed(Controller.MOUSELEFT);

		} else {
			this.hovered = false;
			lastMouseFrame = false;
		}
	}

	/**
	 * @returns true if the mouse is within this button's hitbox, false otherwise
	 */
	public boolean getHovered() {
		return hovered;
	}

	/**
	 * Executes the button's onClick action
	 */
	public void doClick() {
		action.onClick(this);
	}
	
	
	@Override
	public void renderDevMode(DrawGraphics g) {
		this.hitbox.renderStill(g);
	}

//	public class Back extends Button {
//
//		public Back(ArrayList<UIObject> add, ArrayList<UIObject> remove, int x, int y) {
//			super("Back", new ClickListener() {
//
//				@Override
//				public void onClick(UIObject source) {
//					for (int i = 0; i < add.size(); i++) {
//						add.get(i).display();
//					}
//					for (int i = 0; i < remove.size(); i++) {
//						remove.get(i).close();
//					}
//				}
//
//			}, x, y, 80, 32, 0xffcccccc, 0xffaaaaaa);
//
//		}
//
//	}

}
