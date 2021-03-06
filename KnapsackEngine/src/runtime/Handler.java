package runtime;

import core.Driver;

import core.Screen;
import entity.EntityManager;
import gfx.DrawGraphics;
import gui.InterfaceManager;
import input.Controller;
import map.WorldMap_KS;
import networking.Processing;
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

	private static Scene activeScene;

	public static boolean devMode = false;

	private static Driver driver;

	private static Camera camera;

	private static LightManager lightManager;
	private static ParticleManager particles;
	private static EntityManager entities;

	private static InterfaceManager uiManager;

	private static LoadingScreen loadingScreen;

	private static WorldMap_KS world;

	/**
	 * initialies the driver and all its components
	 * 
	 * @param d - the main game driver
	 */
	public static void init(Driver d) {
		driver = d;

		lightManager = new LightManager();
		particles = new ParticleManager();
		uiManager = new InterfaceManager();
		entities = new EntityManager();
		camera = new Camera();

		d.setControls(new Controller());

	}

	public static void update() {
		Controller.update();
		Processing.update();
		lightManager.update();
		uiManager.update();
		particles.update();
		entities.update();
		camera.update();
		if (world != null)
			world.update();

		if (activeScene != null)
			activeScene.update();
	}

	/**
	 * draws all components of the currently active state
	 * 
	 * @param g - the DrawGraphics component associated with the renderer
	 */
	public static void render(DrawGraphics g) {
		if (loadingScreen != null)
			loadingScreen.render(g);
		else if (activeScene != null)
			activeScene.render(g);
		if (world != null)
			world.render(g);
		lightManager.render(g);
		uiManager.render(g);
		particles.render(g);
		entities.render(g);
		if (devMode) {
			entities.renderDevMode(g);
			uiManager.renderDevMode(g);
			particles.renderDevMode(g);
		}
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
		return (int) Math.ceil(driver.getWidth() / Driver.xScale);
	}

	/**
	 * @returns the height of the game square (not the screen height)
	 */
	public static int getHeight() {
		return (int) Math.ceil(driver.getHeight() / Driver.yScale);
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
	public static LightManager getLightManager() {
		return lightManager;
	}

	/**
	 * @returns the ParticleManager
	 */
	public static ParticleManager getParticleManager() {
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
	 * 
	 * @param capped - true to cap the fps at 60, false to uncap fps
	 */
	public static void setFPSCap(boolean capped) {
		driver.setCapped(capped);
	}

	/**
	 * sets the currently active loading screen
	 * 
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
	 * sets the currently active scene
	 * 
	 * @param scene - the new scene
	 */
	public static void setScene(Scene scene) {
		if (activeScene != null)
			activeScene.stop();
		scene.start();
		activeScene = scene;

	}

	/**
	 * sets the currently active scene, and initializes it
	 * 
	 * @param scene - the new scene
	 * @param data  - any data associated with the scene start
	 */
	public static void startScene(Scene scene, String data) {
		scene.init(data);
		if (activeScene != null)
			activeScene.stop();
		activeScene = scene;
	}

	/**
	 * sets the currently active scene, and initializes it
	 * 
	 * @param scene - the new scene
	 */
	public static void startScene(Scene scene) {
		scene.init(null);
		if (activeScene != null)
			activeScene.stop();
		activeScene = scene;
	}

	public static EntityManager getEntityManager() {
		return entities;
	}

	public static WorldMap_KS getLoadedWorld() {
		return world;
	}
	
	public static void setLoadedWorld(WorldMap_KS w) {
		world = w;
	}
}
