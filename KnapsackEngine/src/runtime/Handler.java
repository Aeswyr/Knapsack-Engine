package runtime;

import core.Driver;
import core.Screen;
import entity.Vector;
import gfx.DrawGraphics;
import gui.InterfaceManager;
import gui.UIObject;
import input.Controller;
import particle.ParticleManager;
import utility.LoadingScreen;

/**
 * the core of the game functions, updates and renders all things, as well as
 * holds connections between parts of the game
 * 
 * @author Pascal
 *
 */
public class Handler {

	private static State activeState;

	public static boolean devMode = false;

	private static Driver driver;

	private static Camera camera;

	private static LightManager lightManager;
	private static ParticleManager particles;

	private static InterfaceManager uiManager;

	private static LoadingScreen loadingScreen;

	/**
	 * initialies the driver and all its components
	 * @param d - the main game driver
	 */
	public static void init(Driver d) {
		driver = d;

		lightManager = new LightManager();
		particles = new ParticleManager();
		uiManager = new InterfaceManager();

		camera = new Camera();

		d.setControls(new Controller());

	}

	public static void update() {
		Controller.update();
		activeState.update();
	}

	/**
	 * draws all components of the currently active state
	 * @param g - the DrawGraphics component associated with the renderer
	 */
	public static void render(DrawGraphics g) {
		if (loadingScreen != null)
			loadingScreen.render(g);
		else
			activeState.render(g);

	}

	// Getters and Setters

	/**
	 * @returns the camera
	 */
	public static Camera getCamera() {
		return camera;
	}

	/**
	 * @returns the width of the game square (not the screen width)
	 */
	public static int getWidth() {
		return 960;
	}

	/**
	 * @returns the height of the game square (not the screen height)
	 */
	public static int getHeight() {
		return 540;
	}

	/**
	 * @returns the game window
	 */
	public static Screen getScreen() {
		return driver.getScreen();
	}

	/**
	 * @returns the DrawGraphics component of the renderer
	 */
	public static DrawGraphics getCanvas() {
		return driver.getCanvas();
	}

	/**
	 * @returns the LightManager
	 */
	public static LightManager getLights() {
		return lightManager;
	}

	/**
	 * @returns the ParticleManager
	 */
	public static ParticleManager getParticles() {
		return particles;
	}

	/**
	 * @returns the GUI manager
	 */
	public static InterfaceManager getUI() {
		return uiManager;
	}

	/**
	 * sets the capped condition of the renderer 
	 * @param capped - true to cap the fps at 60, false to uncap fps
	 */
	public static void setFPSCap(boolean capped) {
		driver.setCapped(capped);
	}

	/**
	 * sets the currently active loading screen
	 * @param l - the new loading screen
	 */
	public static void setLoadingScreen(LoadingScreen l) {
		loadingScreen = l;
	}

	/**
	 * closes a currently active loading screen
	 */
	public static void closeLoadingScreen() {
		loadingScreen = null;
	}

	/**
	 * sets the currently active state
	 * @param state - the new state
	 */
	public static void setState(State state) {
		activeState = state;
	}
}
