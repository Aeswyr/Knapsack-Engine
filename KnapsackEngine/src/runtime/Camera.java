package runtime;

import entity.Entity;

/**
 * calculates offsets for onscreen objects to keep a specified entity centered onscreen
 * @author Pascal
 *
 */
public class Camera {
	Entity target;
	private int xOffset;
	private int yOffset;

	int w, h;

	public Camera() {
		w = Handler.getWidth();
		h = Handler.getHeight();
	}

	/**
	 * calculates a new offset based on where the target entity's location is
	 */
	public void update() {
		if (target != null) {
			xOffset += (target.getX() - xOffset) / 4;
			yOffset += (target.getY() - yOffset) / 4;
		}
	}
	
	/**
	 * centers the camera on the specified point until it is moved or reassigned an entity target
	 * @param x - x position to center on
	 * @param y - y position to center on
	 */
	public void center(int x, int y) {
		xOffset = (x);
		yOffset = (y);
		target = null;
	}

	/**
	 * determines which entity will be the camera target
	 * @param e - the entity to target
	 */
	public void centerOnEntity(Entity e) {
		target = e;
	}

	/**
	 * @returns the x offset for rendering
	 */
	public int xOffset() {
		return xOffset - w / 2;
	}

	/**
	 * @returns the y offset for rendering
	 */
	public int yOffset() {
		return yOffset - h / 2;
	}
	
	/**
	 * @returns the raw x offset, not adjusted for screen centering
	 */
	public int xOffsetAdj() {
		return xOffset;
	}
	
	/**
	 * @returns the raw y offset, not adjusted for screen centering
	 */
	public int yOffsetAdj() {
		return yOffset;
	}
}
