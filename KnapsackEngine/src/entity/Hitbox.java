package entity;

import java.io.Serializable;
import entity.Entity;
import gfx.DrawGraphics;
import input.Controller;
import runtime.Handler;

/**
 * hitbox class for entity collision
 * 
 * @author Pascal
 *
 */
public class Hitbox implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8268649890127915848L;
	transient private Entity e;
	private int xoff, yoff;
	private int x, y, width, height;

	/**
	 * initializes a hitbox for an entity
	 * 
	 * @param xOffset - offset from the entity's x to place the hitbox
	 * @param yOffset - offset from the entity's y to place the hitbox
	 * @param width   - width of the hitbox
	 * @param height  - height of the hitbox
	 * @param e       - entity which this hitbox is bound to
	 * @param handler
	 */
	public Hitbox(int xOffset, int yOffset, int width, int height, Entity e) {
		this.e = e;
		this.width = width;
		this.height = height;
		xoff = xOffset;
		yoff = yOffset;
	}

	/**
	 * initializes a square hitbox for an entity
	 * 
	 * @param xOffset - offset from the entity's x to place the hitbox
	 * @param yOffset - offset from the entity's y to place the hitbox
	 * @param size    - width and height of this hitbox
	 * @param e       - entity which this hitbox is bound to
	 * @param handler
	 */
	public Hitbox(int xOffset, int yOffset, int size, Entity e) {
		this.e = e;
		this.width = size;
		this.height = size;
		xoff = xOffset;
		yoff = yOffset;
	}

	/**
	 * initializes a hitbox which isnt bound to any entity
	 * 
	 * @param x       - x pos of this hitbox
	 * @param y       - y pos of this hitbox
	 * @param width   - width of the hitbox
	 * @param height  - height of the hitbox
	 * @param handler
	 */
	public Hitbox(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/**
	 * sets this hitbox position to the entity's position
	 */
	public void update() {
		x = e.getX();
		y = e.getY();
	}

	/**
	 * checks if this hitbox contains the specified hitbox
	 * 
	 * @param h - the hitbox to test
	 * @returns true if the hitboxes collide, false otherwise
	 */
	public boolean contains(Hitbox h) {
		int[] thisC = this.getCenter();
		int[] newC = h.getCenter();

		if (Math.abs(thisC[0] - newC[0]) < (this.width + h.width) / 2
				&& Math.abs(thisC[1] - newC[1]) < (this.height + h.height) / 2)
			return true;

		return false;
	}

	/**
	 * checks if this hitbox contains the mouse cursor
	 * 
	 * @returns true if the cursor is within the hitbox, false otherwise
	 */
	public boolean containsMouse() {
		int mouseX = Controller.getAdjX();
		int mouseY = Controller.getAdjY();
		int[] bound = this.getBounds();

		if (mouseX > bound[0] && mouseX < bound[1] && mouseY > bound[2] && mouseY < bound[3])
			return true;

		return false;
	}

	// Getters and Setters

	/**
	 * gets the four corner points of this hitbox
	 * 
	 * @returns an array with the four corner points of the hitbox in the format x0,
	 *          x1, y0, y1
	 */
	private int[] getBounds() {
		int[] bounds = { x + xoff, x + xoff + width, y + yoff, y + yoff + height };
		return bounds;
	}

	/**
	 * gets the center point of this hitbox
	 * 
	 * @returns an array containing the x, y coordinate for the center of this
	 *          hitbox
	 */
	private int[] getCenter() {
		int[] bounds = { x + xoff + width / 2, y + yoff + height / 2 };
		return bounds;
	}

	/**
	 * updates this hitbox's position to a new one
	 * 
	 * @param x - the new x
	 * @param y - the new y
	 */
	public void updatePos(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * draws this hitbox to the screen adjusted for the camera
	 * 
	 * @param g - the DrawGraphics component associated with the renderer
	 */
	public void render(DrawGraphics g) {
		g.drawRect(x - Handler.getCamera().xOffset() + xoff, y - Handler.getCamera().yOffset() + yoff, width, height,
				0x44ff00ff);
	}

	/**
	 * draws this hitbox to the screen based on raw screen coordinate
	 * 
	 * @param g - the DrawGraphics component associated with the renderer
	 */
	public void renderStill(DrawGraphics g) {
		if (Handler.devMode)
			g.drawRect(x + xoff, y + yoff, width, height, 0x44ff00ff);
	}

	/**
	 * sets the entity who this hitbox belongs to
	 * 
	 * @param e - new entity
	 */
	public void setEntity(Entity e) {
		this.e = e;
	}
}
