package particle;

import java.util.Random;

import gfx.DrawGraphics;
import gfx.Sprite;
import runtime.Handler;

/**
 * Object which manages a cluster of similar particles
 * @author pmkgb
 *
 */
public class Particle {

	public static Random rng = new Random();

	public static final int DISPERSE_BURST = 0;
	public static final int DISPERSE_TICK = 1;
	public static final int SHAPE_LINE = 2;
	public static final int SHAPE_CIRCLE = 3;
	public static final int DIRECTION_RANDOM = 4;
	public static final int DIRECTION_SWEEP = 5;
	public static final int DIRECTION_FLOAT = 8;
	public static final int LIFETIME_SET = 6;
	public static final int LIFETIME_VARIABLE = 7;

	int x, y, x0, y0;
	int max;
	int lifetime;
	int data[][]; // pos 0 is x, pos 1 is y, pos 2 is life
	boolean textEnabled = false;

	private int size;
	private int color;
	private Sprite sprite;

	boolean dead = false;

	String text;

	Behavior behavior;

	/**
	 * Initializes a particle using a sprite for each particle
	 * @param s - sprite for the particles
	 * @param max - maximum number of particles
	 * @param x - origin x point for particles
	 * @param y - origin y point for particles
	 * @param x0 - final x point for particles
	 * @param y0 - final y point for particles
	 * @param handler - handler for the game
	 * @param b - behavior object (describes the origin and motion of the particles)
	 */
	public Particle(Sprite s, int max, int x, int y, int x0, int y0, Behavior b) {
		this.sprite = s;
		this.max = max;

		data = new int[max][3];
		this.x = x;
		this.x0 = x0;
		this.y = y;
		this.y0 = y0;
		size = max;
		this.behavior = b;

		behavior.initial(x, y, x0, y0, data);

	}

	/**
	 * Initializes a particle with both text and a sprite for each particle
	 * @param s - sprite for the particles
	 * @param st - text string
	 * @param color - color of the text
	 * @param max - maximum number of particles
	 * @param x - origin x point for particles
	 * @param y - origin y point for particles
	 * @param x0 - final x point for particles
	 * @param y0 - final y point for particles
	 * @param handler - handler for the game
	 * @param b - behavior object (describes the origin and motion of the particles)
	 */
	public Particle(Sprite s, String st, int color, int max,  int x, int y, int x0, int y0, Behavior b) {
		this(s, max, x, y, x0, y0, b);
		this.text = st;
		this.color = color;
		this.textEnabled = true;

	}

	/**
	 * Initializes a particle with only text
	 * @param st - text string
	 * @param color - color of the text
	 * @param max - maximum number of particles
	 * @param x - origin x point for particles
	 * @param y - origin y point for particles
	 * @param x0 - final x point for particles
	 * @param y0 - final y point for particles
	 * @param handler - handler for the game
	 * @param b - behavior object (describes the origin and motion of the particles)
	 */
	public Particle(String st, int color, int max, int x, int y, int x0, int y0, Behavior b) {
		this(((Sprite) null), max, x, y, x0, y0,  b);
		this.text = st;
		this.color = color;
		this.textEnabled = true;

	}

	/**
	 * draws all particles to the screen
	 * 
	 * @param g - the DrawGraphics component associated with the renderer
	 */
	public void render(DrawGraphics g) {

		int xOff = Handler.getCamera().xOffset();
		int yOff = Handler.getCamera().yOffset();
		for (int i = 0; i < size; i++) {
			if (data[i][2] > 0) {
				if (sprite != null)
					sprite.render(data[i][0] - xOff, data[i][1] - yOff, g);
				if (this.textEnabled)
					g.write(text, data[i][0] - xOff, data[i][1] - yOff, color);
			}
		}
	}

	/**
	 * updates all particles
	 */
	public void update() {
		dead = true;
		for (int i = 0; i < size; i++) {
			data[i][2]--;
			behavior.update(x, y, x0, y0, data[i], i);
			if (data[i][2] > 0)
				dead = false;
		}

	}

	/**
	 * @returns true if there are no active particles, false otherwise
	 */
	public boolean isDead() {
		return dead;
	}

	/**
	 * begins updating and rendering this particle
	 */
	public void start() {
		Handler.getParticles().add(this);
	}

	/**
	 * Used to mathematically describe origin points for each particle as well as their motion
	 * @author pmkgb
	 *
	 */
	public interface Behavior {
		/**
		 * determines where each particle begins
		 * @param x - origin x point of particles
		 * @param y - origin y point of particles
		 * @param x0 - end x point of particles
		 * @param y0 - end y point of particles
		 * @param data - particle data
		 */
		public void initial(int x, int y, int x0, int y0, int[][] data);

		/**
		 * determines the movement of an individual particle in a frame
		 * @param x - origin x point of particles
		 * @param y - origin y point of particles
		 * @param x0 - end x point of particles
		 * @param y0 - end y point of particles
		 * @param data - particle data
		 * @param index - the index of the currently updating particle
		 */
		public void update(int x, int y, int x0, int y0, int[] data, int index);
	}
}
