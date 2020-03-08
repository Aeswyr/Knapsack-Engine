package core;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import gfx.DrawGraphics;
import runtime.Handler;

/**
 * thread for rendering the game
 * 
 * @author Pascal
 *
 */
public class Renderer implements Runnable {

	protected static boolean openglEnabled = false;
	private boolean running = false;
	private int ticks;
	private boolean capped = true;
	private int frames;
	protected Screen screen;
	private BufferStrategy bs;
	protected DrawGraphics draw;

	public Renderer(Screen screen, DrawGraphics canvas) {
		this.screen = screen;
		this.draw = canvas;
	}

	@Override
	public void run() {
		long currentTime;
		long lastTime = System.nanoTime();
		long delta = 100;

		while (running) {
			currentTime = System.nanoTime();
			if (currentTime - lastTime > delta) {
				if (ticks > 0 || !capped) {
					if (openglEnabled)
						rendergl();
					else
						render();
					frames++;

					if (capped)
						ticks--;
				}
				System.out.print("");
				lastTime = System.nanoTime();
			}

		}

	}

	/**
	 * starts this thread
	 */
	public synchronized void start() {
		running = true;
		Thread t = new Thread(this);
		t.start();
	}

	Graphics g;

	/**
	 * first queues all objects to render for that frame, sorts them by z levels,
	 * then uses a buffer strategy and the canvas graphics object to draw them to
	 * the screen
	 */
	public void render() {
		bs = screen.getBufferStrategy();
		if (bs == null) {
			screen.createBufferStrategy(3);
			return;
		}

		g = bs.getDrawGraphics();
		draw.clear();

		Handler.render(draw);
		draw.process();
		draw.render(g);
		bs.show();
		g.dispose();
	}

	public void rendergl() {

		glfwPollEvents();
		
		Handler.render(draw);
		draw.processgl();
		draw.rendergl();
		
	}

	/**
	 * increments the number of ticks that the update method has been running ahead
	 * of the render method
	 */
	public void tick() {
		ticks++;
	}

	/**
	 * sets whether or not to cap fps
	 * 
	 * @param capped - true to cap fps, false to uncap
	 */
	protected void setCapped(boolean capped) {
		this.capped = capped;
	}

	/**
	 * gets the number of times the renderer has run since the last call to
	 * getFrames()
	 * 
	 * @returns the integer number of frames since the last call to this method
	 */
	protected int getFrames() {
		int hold = frames;
		frames = 0;
		return hold;
	}

	/**
	 * stops this thread
	 */
	public void stop() {
		this.running = false;
	}

}
