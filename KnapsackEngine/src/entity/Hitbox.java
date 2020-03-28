package entity;

import java.io.Serializable;
import java.util.List;

import entity.Entity;
import gfx.DrawGraphics;
import input.Controller;
import runtime.Handler;
import utility.Utility;

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
	private int xoff, yoff, x, y, cx, cy;

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
	public Hitbox(int xOffset, int yOffset, int[][] points, Entity e) {
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
	public Hitbox(int[][] points) {
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
	public boolean colliding(Hitbox h) {
		return checkCol(h) && h.checkCol(this);
	}

	/**
	 * checks collision between this hitbox and another from the perspective of this
	 * hitbox utilizing the separating axis theorem
	 * 
	 * @param h - hitbox to check collisions with
	 * @returns true if colliding, false otherwise
	 */
	private boolean checkCol(Hitbox h) {
		int[] p0 = new int[h.points.length]; // contains all x projections for h
		int off = 0, w, r0, r1;
		for (int q = 0; q < axis.length; q++) { // loops through each axis to to check for separation
			r0 = (int) Utility.linearMatrixTransform2d(new int[] { x, y }, rot[q])[0]; // establishes offset for this
			r1 = (int) Utility.linearMatrixTransform2d(new int[] { h.x, h.y }, rot[q])[0]; // establishes offsest for h
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

	public List<Entity> localCollisions() {
		List<Entity> hit = Handler.getEntityManager().getProximityEntities(x, y, 3);
		for (int i = 0; i < hit.size(); i++) {
			if (!hit.get(i).getHitbox().colliding(this)) {
				hit.remove(hit.remove(i));
				i--;
			}
		}
		return hit;
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
			int r0 = (int) Utility.linearMatrixTransform2d(new int[] { x, y }, rot[i])[0];

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
			int r0 = (int) Utility.linearMatrixTransform2d(new int[] { x, y }, rot[i])[0];

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
					points[(i + 1) % points.length][1] - y0, 0x44ff00ff);
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
						0x44ff00ff);
			}
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
