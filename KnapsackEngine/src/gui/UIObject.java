package gui;

import gfx.DrawGraphics;
import runtime.Handler;

/**
 * base class for all GUI componenets
 * 
 * @author Pascal
 *
 */
public abstract class UIObject {
	
	protected int x, y;

	/**
	 * Draws this UI component to the screen
	 * 
	 * @param g - the DrawGraphics component associated with the renderer
	 */
	public abstract void render(DrawGraphics g);

	/**
	 * Updates this UI component
	 */
	public abstract void update();
	
	public void display() {
		Handler.getUI().addObject(this);
	}
	
	public void close() {
		Handler.getUI().removeObject(this);
	}

}
