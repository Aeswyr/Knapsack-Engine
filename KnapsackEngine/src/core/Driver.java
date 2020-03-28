package core;

import gfx.DrawGraphics;
import input.Controller;
import runtime.Handler;

/**
 * main engine of the game, updates and manages all processes
 * @author pmkgb
 *
 */
public class Driver implements Runnable {

	public static double xScale = 1, yScale = 1;

	private boolean running = false;
	private Renderer render;
	protected Screen screen;
	protected DrawGraphics canvas;

	public Driver(int w, int h, String title) {

		screen = new Screen(w, h, title);
		canvas = new DrawGraphics(this);

	}

	@Override
	public void run() {
		long currentTime;
		long lastTime = System.nanoTime();
		long delta = 1000000000 / 60;
		long lastReport = System.nanoTime();
		long deltaR = 1000000000;

		long updateTime = 0;
		int upd = 0;

		while (running) {
			currentTime = System.nanoTime();
			if (currentTime - lastTime > delta) {
				update();
				upd++;
				render.tick();
				updateTime += System.nanoTime() - currentTime;
				if (currentTime - lastReport > deltaR) {
					System.out.println("FPS: " + render.getFrames() + " Updates:" + upd + " Avg update time (ms): "
							+ updateTime / upd / 1000000.00);
					updateTime = 0;
					upd = 0;
					lastReport = System.nanoTime();
				}
				currentTime = System.nanoTime();
				lastTime = currentTime;
			}

		}

	}
/**
 * starts this thread and starts the renderer
 */
	public synchronized void start() {
		Handler.init(this);
		running = true;
		Thread t = new Thread(this);
		render = new Renderer(screen, canvas);
		t.start();
		screen.setClosing(this, render);
		render.start();
	}

	/**
	 * updates the lower game processes
	 */
	public void update() {
		Handler.update();
	}

	/**
	 * gets the screen width
	 * @returns the true width of the screen
	 */
	public int getWidth() {
		return screen.getWidth();
	}

	/**
	 * gets the screen height
	 * @returns the true height of the screen
	 */
	public int getHeight() {
		return screen.getHeight();
	}

	/**
	 * gets the game window
	 * @returns the screen object the game is rendered on
	 */
	public Screen getScreen() {
		return screen;
	}

	/**
	 * gets the DrawGraphics component associated with the renderer
	 * @returns the DrawGraphics component of the renderer
	 */
	public DrawGraphics getCanvas() {
		return canvas;
	}

	/**
	 * sets whether or not to cap fps
	 * @param capped - true to cap fps, false to uncap
	 */
	public void setCapped(boolean capped) {
		render.setCapped(capped);
	}

	/**
	 * sets the current input listeners
	 * @param c - the Controller with the desired KeyListener, MouseListener, and MouseMotionlistener
	 */
	public void setControls(Controller c) {
		screen.addMouseListener(c);
		screen.addMouseMotionListener(c);
		screen.addKeyListener(c);
	}

	/**
	 * closes the game
	 */
	public void close() {
		screen.close();
	}

	/**
	 * stops this thread
	 */
	public void stop() {
		this.running = false;
	}
}
