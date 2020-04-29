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
public abstract class Entity_KS implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5462530084929665736L;
	protected int x, y;
	protected Hitbox_KS hitbox;
	protected Vector_KS vector;
	private boolean mobEnabled;
	protected CoordKey key;
	protected short ID;

	public Entity_KS() {
		key = new CoordKey(x, y, 0);
	}

	/**
	 * updates all essential functions of this entity
	 */
	public abstract void update();

	public void tick() {
		if (hasVector() /* || rigidbodyEnabled */) { // TODO rigidbodies
			vector.update();
		}

		if (hasHitbox())
			hitbox.update();

		key.update(x / Hitbox_KS.BOXCHECK, y / Hitbox_KS.BOXCHECK, 0);
	}

	/**
	 * draws this entity and associated ui to the screen
	 * 
	 * @param g - the DrawGraphics component associated with the renderer
	 */
	public abstract void render(DrawGraphics g);

	/**
	 * commands to complete upon deserialization
	 */
	public abstract void load();

	// Getters and setters;

	/**
	 * gets this entity's hitbox
	 * 
	 * @returns this entity's main hitbox
	 */
	public Hitbox_KS getHitbox() {
		return hitbox;
	}

	/**
	 * @returns this entity's x position
	 */
	public int getX() {
		return x;
	}

	/**
	 * @returns this entity's y position
	 */
	public int getY() {
		return y;
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

	public void enableMob() {
		this.mobEnabled = true;
	}

	public boolean hasHitbox() {
		return hitbox != null;
	}

	public boolean hasVector() {
		return vector != null;
	}

	public Vector_KS getVector() {
		return vector;
	}

}
