package entity;

import java.io.Serializable;
import java.util.List;

import entity.Entity_KS;
import gfx.DrawGraphics;
import gfx.Sprite;
import input.Controller;
import map.WorldMap_KS;
import runtime.Handler;
import runtime.Light;
import utility.Utility;

/**
 * hitbox class for entity collision
 * 
 * @author Pascal
 *
 */
public class Hitbox_KS implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8268649890127915848L;
	transient private Entity_KS e;
	private int xoff, yoff, cx, cy, width, height;
	protected int x, y;

	protected int colID = 0;

	public static final int COLLISION_NONE = 0;
	public static final int COLLISION_ENVIRONMENT = 1;
	public static final int COLLISION_PROJECTILE = 2;
	public static final int COLLISION_ENTITY = 3;

	/**
	 * contains a list of points which outline this hitbox in clockwise order.
	 */
	private int[][] points;

	/**
	 * contains a list of vectors each corresponding to the normal of a vector
	 * within the shape
	 */
	private int[][] axis;

	/**
	 * contains rotation matrixes based on each axis vector for quickly rotating and
	 * testing other hitboxes
	 */
	private double[][] rot;

	/**
	 * contains the projection values for this shape's projection onto its axis,
	 * accounting for rotation, where the first index is the offset, and the second
	 * is the width of the projection
	 */
	private int[][] proj;

	/**
	 * determines the size in pixels for grouping hitboxes
	 */
	public static int BOXCHECK = 64;

	/**
	 * initializes a hitbox for an entity
	 * 
	 * @param xOffset - offset from the entity's x to place the hitbox's first point
	 * @param yOffset - offset from the entity's y to place the hitbox's first point
	 * @param points  - list of points outlining the hitbox in the clockwise
	 *                direction
	 * @param e       - entity which this hitbox is bound to
	 */
	public Hitbox_KS(int xOffset, int yOffset, int[][] points, Entity_KS e) {
		this.points = points;
		this.e = e;
		xoff = xOffset;
		yoff = yOffset;

		int x0 = 0, y0 = 0;

		for (int i = 0; i < points.length; i++) {
			for (int j = 0; j < points.length; j++) {
				if (Math.abs(points[i][0] - points[(i + j) % points.length][0]) > x0) {
					x0 = Math.abs(points[i][0] - points[(i + j) % points.length][0]);
				}
				if (Math.abs(points[i][1] - points[(i + j) % points.length][1]) > y0) {
					y0 = Math.abs(points[i][1] - points[(i + j) % points.length][1]);
				}
			}
		}

		cx = x0 / 2 + points[0][0] + xoff;
		cy = y0 / 2 + points[0][1] + yoff;

		setupAxis();

	}

	/**
	 * initializes a hitbox which isnt bound to any entity
	 * 
	 * @param points - list of points outlining the hitbox in the clockwise
	 *               direction
	 */
	public Hitbox_KS(int[][] points) {
		this.points = points;

		int x0 = 0, y0 = 0;

		for (int i = 0; i < points.length; i++) {
			for (int j = 0; j < points.length; j++) {
				if (Math.abs(points[i][0] - points[(i + j) % points.length][0]) > x0) {
					x0 = Math.abs(points[i][0] - points[(i + j) % points.length][0]);
				}
				if (Math.abs(points[i][1] - points[(i + j) % points.length][1]) > y0) {
					y0 = Math.abs(points[i][1] - points[(i + j) % points.length][1]);
				}
			}
		}

		cx = x0 / 2 + points[0][0];
		cy = y0 / 2 + points[0][1];

		setupAxis();

	}

	/**
	 * establishes the axises for SAT, as well as the projections for this shape in
	 * each of its rotations and its rotational matrices
	 */
	private void setupAxis() {
		axis = new int[points.length][2];

		rot = new double[axis.length][4];
		proj = new int[axis.length][2];

		double theta;

		for (int i = 0; i < axis.length; i++) {
			axis[i] = Utility.norm2d(points[i], points[(i + 1) % points.length]);
			theta = rot[i][0] = -axis[i][0] / Utility.len(axis[i]);
			rot[i][1] = axis[i][1] / Utility.len(axis[i]);
			rot[i][2] = -axis[i][1] / Utility.len(axis[i]);
			rot[i][3] = -axis[i][0] / Utility.len(axis[i]);

			double[] p0 = new double[points.length];

			for (int j = 0; j < p0.length; j++) {
				p0[j] = Utility.linearMatrixTransform2d(points[j], rot[i])[0];
			}

			for (int j = 0; j < p0.length; j++) {
				if (Math.abs(p0[j] - p0[i]) > proj[i][1]) {
					proj[i][1] = (int) Math.abs(p0[j] - p0[i]);

					if (p0[j] > p0[i])
						proj[i][0] = (int) p0[i];
					else
						proj[i][0] = (int) p0[j];

				}
			}
		}

		int ymi = Integer.MAX_VALUE, yma = Integer.MIN_VALUE, xmi = Integer.MAX_VALUE, xma = Integer.MIN_VALUE;
		for (int i = 0; i < points.length; i++) {
			if (points[i][0] > xma)
				xma = points[i][0];
			if (points[i][0] < xmi)
				xmi = points[i][0];
			if (points[i][1] > yma)
				yma = points[i][1];
			if (points[i][1] < ymi)
				ymi = points[i][1];
		}
		width = xma - xmi;
		height = yma - ymi;
	}

	/**
	 * sets this hitbox position to the entity's position
	 */
	public void update() {
		updatePos(e.getX(), e.getY());
	}

	/**
	 * checks if this hitbox contains the specified hitbox
	 * 
	 * @param h - the hitbox to test
	 * @returns true if the hitboxes collide, false otherwise
	 */
	public boolean colliding(Hitbox_KS h) {
		return checkCol(h) && h.checkCol(this);
	}

	/**
	 * projects if there will be a collision between this and another hitbox if an x
	 * or y offset were to be applied to this hitbox
	 * 
	 * @param h
	 * @param dx
	 * @param dy
	 * @return
	 */
	protected boolean projectColliding(Hitbox_KS h, int dx, int dy) {
		x += dx;
		y += dy;
		boolean col = colliding(h);
		x -= dx;
		y -= dy;
		return col;
	}

	/**
	 * checks collision between this hitbox and another from the perspective of this
	 * hitbox utilizing the separating axis theorem
	 * 
	 * @param h - hitbox to check collisions with
	 * @returns true if colliding, false otherwise
	 */
	protected boolean checkCol(Hitbox_KS h) {
		int[] p0 = new int[h.points.length]; // contains all x projections for h
		int off = 0, w, r0, r1;
		for (int q = 0; q < axis.length; q++) { // loops through each axis to to check for separation
			r0 = (int) Utility.linearMatrixTransform2d(new int[] { x + xoff, y + yoff }, rot[q])[0]; // establishes
																										// offset for
																										// this
			r1 = (int) Utility.linearMatrixTransform2d(new int[] { h.x + h.xoff, h.y + h.yoff }, rot[q])[0]; // establishes
																												// offsest
																												// for h
			for (int j = 0; j < h.points.length; j++) {
				p0[j] = (int) Utility.linearMatrixTransform2d(h.points[j], rot[q])[0] + r1; // sets up all the x
																							// projections for h
			}
			w = 0;

			for (int i = 0; i < p0.length; i++) { // for each combination of points in the h projection, find the two
													// which are farthest apart and record
				for (int j = i; j < p0.length; j++) {
					if (Math.abs(p0[j] - p0[i]) > w) {
						w = (int) Math.abs(p0[j] - p0[i]);

						if (p0[j] > p0[i])
							off = p0[i];
						else
							off = p0[j];

					}
				}
			}

			if (proj[q][0] + r0 > off + w || off > proj[q][0] + proj[q][1] + r0) // check for non-overlap along the
																					// projection, return false if the
																					// axis is separating
				return false;
		}
		return true;
	}

	public List<Entity_KS> localCollisions() {
		List<Entity_KS> hit = Handler.getEntityManager().getProximityEntities(x, y, 3);
		for (int i = 0; i < hit.size(); i++) {
			if (hit.get(i).hasHitbox() && !hit.get(i).getHitbox().colliding(this)) {
				hit.remove(i);
				i--;
			}
		}
		return hit;
	}

	public boolean tileXCollide(int offset) {

		int x = this.x + xoff + offset;
		int y = this.y + yoff;

		WorldMap_KS m = Handler.getLoadedWorld();
		for (int i = 0; i < m.getLayers(); i++) {
			try {
				if (m.getTile(x, y, i).getCollidable() || m.getTile(x + width, y, i).getCollidable()
						|| m.getTile(x + width, y + height, i).getCollidable()
						|| m.getTile(x, y + height, i).getCollidable())
					return true;
			} catch (NullPointerException e) {

			}
		}
		return false;
	}

	public boolean tileYCollide(int offset) {

		int x = this.x + xoff;
		int y = this.y + yoff + offset;

		WorldMap_KS m = Handler.getLoadedWorld();
		for (int i = 0; i < m.getLayers(); i++) {
			try {
				if (m.getTile(x, y, i).getCollidable() || m.getTile(x + width, y, i).getCollidable()
						|| m.getTile(x + width, y + height, i).getCollidable()
						|| m.getTile(x, y + height, i).getCollidable())
					return true;
			} catch (NullPointerException e) {

			}
		}
		return false;
	}

	/**
	 * checks if this hitbox contains the mouse cursor //TODO rewrite
	 * 
	 * @returns true if the cursor is within the hitbox, false otherwise
	 */
	public boolean containsMouse() {
		int[] p = { Controller.getAdjX(), Controller.getAdjY() };

		for (int i = 0; i < points.length; i++) {
			int x0 = (int) Utility.linearMatrixTransform2d(p, rot[i])[0];
			int r0 = (int) Utility.linearMatrixTransform2d(new int[] { x + xoff, y + yoff }, rot[i])[0];

			if (x0 < proj[i][0] + r0 || x0 > proj[i][0] + proj[i][1] + r0)
				return false;
		}
		return true;
	}

	/**
	 * checks if this hitbox contains the mouse cursor, adjusted for camera position
	 * //TODO rewrite
	 * 
	 * @returns true if the cursor is within the hitbox, false otherwise
	 */
	public boolean containsMouseAdj() {
		int[] p = { Controller.getAdjX() + Handler.getCamera().xOffset(),
				Controller.getAdjY() + Handler.getCamera().yOffset() };

		for (int i = 0; i < points.length; i++) {
			int x0 = (int) Utility.linearMatrixTransform2d(p, rot[i])[0];
			int r0 = (int) Utility.linearMatrixTransform2d(new int[] { x + xoff, y + yoff }, rot[i])[0];

			if (x0 < proj[i][0] + r0 || x0 > proj[i][0] + proj[i][1] + r0)
				return false;
		}

		return true;
	}

	// Getters and Setters

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
		int x0 = Handler.getCamera().xOffset() - xoff - x;
		int y0 = Handler.getCamera().yOffset() - yoff - y;
		for (int i = 0; i < points.length; i++) {
			g.drawLine(points[i][0] - x0, points[i][1] - y0, points[(i + 1) % points.length][0] - x0,
					points[(i + 1) % points.length][1] - y0, 0x77ff00ff, Sprite.createLightData(999999, Light.IGNORE));
		}
	}

	/**
	 * draws this hitbox to the screen based on raw screen coordinate
	 * 
	 * @param g - the DrawGraphics component associated with the renderer
	 */
	public void renderStill(DrawGraphics g) {
		if (Handler.devMode)
			for (int i = 0; i < points.length; i++) {
				g.drawLine(points[i][0] + xoff + x, points[i][1] + yoff + y,
						x + points[(i + 1) % points.length][0] + xoff, points[(i + 1) % points.length][1] + yoff + y,
						0x77ff00ff, Sprite.createLightData(999999, Light.IGNORE));
			}
	}

	/**
	 * sets the entity who this hitbox belongs to
	 * 
	 * @param e - new entity
	 */
	public void setEntity(Entity_KS e) {
		this.e = e;
	}

	/**
	 * gets the entity who this hitbox belongs to
	 */
	public Entity_KS getEntity() {
		return e;
	}

	/**
	 * sets the collision type for this hitbox
	 * 
	 * @param typeID - Collision id value
	 */
	public void setCollisionType(int typeID) {
		this.colID = typeID;
	}
}
