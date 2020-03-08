package entity;

import java.io.Serializable;
import gfx.DrawGraphics;
import utility.CoordKey;

/**
 * represents any object in the game which has a position and can be interacted
 * with in some way
 * 
 * @author Pascal
 *
 */
public abstract class Entity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5462530084929665736L;
	public int id;
	protected double x, y;
	protected Hitbox hitbox;
	protected Vector vector;
	private boolean mobEnabled, vectorEnabled, hitboxEnabled, rigidbodyEnabled;
	protected CoordKey key;
	
	
	public Entity() {
		key = new CoordKey((int)x, (int)y, 0);
	}

	/**
	 * updates all essential functions of this entity
	 */
	public abstract void update();
	
	public void tick() {
		if (vectorEnabled || rigidbodyEnabled) {
			vector.update();
			key.update((int)x, (int)y, 0);
		}
		if (hitboxEnabled) hitbox.update();
		update();
	}

	/**
	 * draws this entity and associated ui to the screen
	 * 
	 * @param g - the DrawGraphics component associated with the renderer
	 */
	public abstract void render(DrawGraphics g);

	// Getters and setters;

	/**
	 * gets this entity's hitbox
	 * 
	 * @returns this entity's main hitbox
	 */
	public Hitbox getHitbox() {
		return hitbox;
	}

	/**
	 * @returns this entity's x position
	 */
	public int getX() {
		return (int) x;
	}

	/**
	 * @returns this entity's y position
	 */
	public int getY() {
		return (int) y;
	}

	/**
	 * sets this entity's x to a new position
	 * 
	 * @param x - the new x value
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * sets this entity's y to a new position
	 * 
	 * @param x - the new y value
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	public boolean isMob() {
		return mobEnabled;
	}
	
	public boolean hasHitbox() {
		return hitboxEnabled;
	}
	
	public boolean hasVector() {
		return vectorEnabled;
	}
	
	public Vector getVector() {
		return vector;
	}
	
}
