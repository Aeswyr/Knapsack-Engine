package entity;

import java.io.Serializable;
import gfx.DrawGraphics;
import map.BaseTile_KS;
import runtime.Handler;

/**
 * object which controls movement for an entity via x and y velocity and
 * acceleration components
 * 
 * @author Pascal
 *
 */
public class Vector_KS implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7160300851222198335L;
	double Ax, Ay; // in tiles per second per second
	double Vx, Vy; // in tiles per second;
	transient Entity_KS linked;
	int mass = 0;
	boolean ghost = false;
	Entity_KS lastCol = null;

	static double GLOBAL_GRAVITY = 0;
	static int COLLISION_PROXIMITY = 4;

	/*
	 * stores values to do with force, index 0 is x acceleration, index 1 is y
	 * acceleration and index 2 is the number of frames to apply for
	 */
	double[][] forceBuffer = new double[5][3];
	int iframes = 0;
	int fframes = 0;

	/**
	 * initializes a vector
	 * 
	 * @param e    - entity to attach this vector to
	 * @param mass - mass for force and friction calculations. 0 mass to disable
	 *             friction
	 */
	public Vector_KS(Entity_KS e, int mass) {
		this.linked = e;
		this.mass = mass;
	}

	/**
	 * increments the velocity component relative to the acceleration component then
	 * updates the linked entity's position
	 */
	public void update() {
		lastCol = null;
		if (fframes > 0)
			fframes--;
		else {
			Vx += Ax / 60;
			Vy += Ay / 60 - GLOBAL_GRAVITY / 60;

			for (int i = 0; i < 5; i++) {
				if (forceBuffer[i][2] > 0) {
					Vx += forceBuffer[i][0];
					Vy += forceBuffer[i][1];
					forceBuffer[i][2]--;
				}
			}

			if (iframes > 0)
				iframes--;

			if (mass > 0) {
				if (Math.abs(Vx) > 0.1)
					Vx *= 0.85;
				else
					Vx = 0;
				if (Math.abs(Vy) > 0.1)
					Vy *= 0.85;
				else
					Vy = 0;
			}

			Ax = 0;
			Ay = 0;
		}

		if (linked != null && linked.hasHitbox()) {
			if (!linked.hitbox.tileYCollide((int) (Vy * BaseTile_KS.TILE_SIZE / 60)) || ghost)
				linked.y += Vy * BaseTile_KS.TILE_SIZE / 60;
			else {
				while (linked.hitbox.tileYCollide((int) (Vy * BaseTile_KS.TILE_SIZE / 60))
						&& Math.abs(Vy) > 60 / BaseTile_KS.TILE_SIZE)
					if (Vy > 0)
						Vy--;
					else
						Vy++;
				if (Math.abs(Vy) > 60 / BaseTile_KS.TILE_SIZE)
					linked.y += Vy * BaseTile_KS.TILE_SIZE / 60;
				Ay = 0;
				Vy = 0;
			}

			if (!linked.hitbox.tileXCollide((int) (Vx * BaseTile_KS.TILE_SIZE / 60)) || ghost)
				linked.x += Vx * BaseTile_KS.TILE_SIZE / 60;
			else {
				while (linked.hitbox.tileXCollide((int) (Vx * BaseTile_KS.TILE_SIZE / 60))
						&& Math.abs(Vx) > 60 / BaseTile_KS.TILE_SIZE)
					if (Vx > 0)
						Vx--;
					else
						Vx++;
				if (Math.abs(Vx) > 60 / BaseTile_KS.TILE_SIZE)
					linked.x += Vx * BaseTile_KS.TILE_SIZE / 60;
				Ax = 0;
				Vx = 0;
			}
		}
		if (mass > 0) {
			if (Math.abs(Vx) > 0)
				Vx *= 0.85;
			else
				Vx = 0;
			if (Math.abs(Vy) > 0)
				Vy *= 0.85;
			else
				Vy = 0;
		}

		Ax = 0;
		Ay = 0;
	}

	/**
	 * sets the x acceleration of this vector
	 * 
	 * @param i - new value for x acceleration
	 */
	public void setAccelX(double i) {
		Ax = i;
	}

	/**
	 * sets the y acceleration for this vector
	 * 
	 * @param i - new value for y acceleration
	 */
	public void setAccelY(double i) {
		Ay = i;
	}

	/**
	 * adjusts the x acceleration for this vector by summing the current component
	 * and the input value
	 * 
	 * @param i - the amount to change the current acceleration by
	 */
	public void adjAccelX(double i) {
		Ax += i;
	}

	/**
	 * adjusts the y acceleration for this vector by summing the current component
	 * and the input value
	 * 
	 * @param i - the amount to change the current acceleration by
	 */
	public void adjAccelY(double i) {
		Ay += i;
	}

	/**
	 * sets the x velocity for this vector
	 * 
	 * @param i - new value for x velocity
	 */
	public void setVelocityX(double i) {
		Vx = i;
	}

	/**
	 * sets the y velocity for this vector
	 * 
	 * @param i - new value for y velocity
	 */
	public void setVelocityY(double i) {
		Vy = i;
	}

	/**
	 * @returns x acceleration for this vector
	 */
	public double Ax() {
		return Ax;
	}

	/**
	 * @returns y acceleration for this vector
	 */
	public double Ay() {
		return Ay;
	}

	/**
	 * @returns x velocity for this vector
	 */
	public double Vx() {
		return Vx;
	}

	/**
	 * @returns y velocity for this vector
	 */
	public double Vy() {
		return Vy;
	}

	/**
	 * applies a force to this object in a variable direction. An object can
	 * experience up to 5 forces simultaneously, after which attempting to add a new
	 * force will fail until one of the existing forces has finished. If the object
	 * has vector iframes, new force applications are ignored.
	 * 
	 * @param force - force to apply in newtons
	 * @param dir   - direction to apply the force in radians
	 * @param time  - ticks to apply force for
	 */
	public void applyForce(int force, double dir, int time) {
		if (iframes == 0) {
			double[] search;
			for (int i = 0; i < 5; i++) {
				search = forceBuffer[i];
				if (search[2] == 0) {
					search[0] = Math.cos(dir) * force;
					search[1] = Math.sin(dir) * force;
					search[2] = time;
					break;
				}
			}
		}
	}

	public void setIFrames(int frames) {
		iframes = frames;
	}

	public void setFreezeFrames(int frames) {
		fframes = frames;
	}

	/**
	 * draws this vector to the screen by representing its velocity component as a
	 * blue line and its acceleration component as a red line
	 * 
	 * @param g - the DrawGraphics component associated with the renderer
	 */
	public void render(DrawGraphics g) {
		int x = linked.getX() - Handler.getCamera().xOffset();
		int y = linked.getY() - Handler.getCamera().yOffset();
		g.drawLine(x, y, (int) (x + Ax), (int) (y + Ay), 0xffff0000);
		g.drawLine(x, y, (int) (x + Vx), (int) (y + Vy), 0xff0000ff);
	}

	/**
	 * sets this entity as a ghost, which means it ignores tile collisions //TODO
	 * not implemented
	 * 
	 * @param b - true to enable ghost, false to disable
	 */
	public void setGhost(boolean b) {
		ghost = b;
	}

	public static void setGlobalGravity(double g) {
		GLOBAL_GRAVITY = g;
	}

	/**
	 * called when this vector is loaded from a file, reassigns the owner entity
	 * 
	 * @param e - entity to link to this vector
	 */
	public void load(Entity_KS e) {
		this.linked = e;
	}

	/**
	 * change the range at which hitboxes check for movement-restricting collisions.
	 * Higher values will cause slowdown, but lower values may miss collisions with
	 * larger hitboxes
	 * 
	 * @param proxy - range to check (in increments of 64 pixels)
	 */
	public static void adjustCollisionProximity(int proxy) {
		COLLISION_PROXIMITY = proxy;
	}

	public static int xReject(Hitbox_KS primary, Hitbox_KS secondary) {
		int n0 = 0;
		if (primary.x > secondary.x) {
			while (!primary.projectColliding(secondary, n0, 0))
				n0--;
			n0++;
		} else {
			while (!primary.projectColliding(secondary, n0, 0))
				n0++;
			n0--;
		}
		return n0;
	}

	public static int yReject(Hitbox_KS primary, Hitbox_KS secondary) {
		int n0 = 0;
		if (primary.y > secondary.y) {
			while (!primary.projectColliding(secondary, 0, n0))
				n0--;
			n0++;
		} else {
			while (!primary.projectColliding(secondary, 0, n0))
				n0++;
			n0--;
		}
		return n0;
	}

	public static int[] dReject(Hitbox_KS primary, Hitbox_KS secondary) {
		int n0 = 0;
		boolean my = primary.y > secondary.y;
		boolean mx = primary.x > secondary.x;
		if (my && mx) {
			while (!primary.projectColliding(secondary, n0, n0))
				n0--;
			n0++;
			return new int[] { n0, n0 };
		} else if (!my && !mx) {
			while (!primary.projectColliding(secondary, n0, n0))
				n0++;
			n0--;
			return new int[] { n0, n0 };
		} else if (my && !mx) {
			while (!primary.projectColliding(secondary, -n0, n0))
				n0--;
			n0++;
			return new int[] { -n0, n0 };
		} else {
			while (!primary.projectColliding(secondary, n0, -n0))
				n0--;
			n0++;
			return new int[] { n0, -n0 };
		}

	}

	boolean grounded = false;

	public boolean grounded() {
		return grounded;
	}

	public Entity_KS collision() {
		return lastCol;
	}
}