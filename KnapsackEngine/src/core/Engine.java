package core;

import java.io.File;

import javax.swing.JFileChooser;

import gfx.DrawGraphics;
import runtime.Handler;
import sfx.Sound;
import utility.Event;

public class Engine {

	public static String ROOT_DIRECTORY;
	private static Driver game;
	
	/**
	 * starts the engine with the resource directory located in the root directory
	 * @param w - screen width
	 * @param h - screen height
	 * @param rootDirectory - name of game root directory
	 * @param title - title for game window
	 */
	public static void start(int w, int h, String rootDirectory, String title) {
		ROOT_DIRECTORY = new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + "/" + rootDirectory + "/";
		ROOT_DIRECTORY = ROOT_DIRECTORY.replace('\\', '/');
		File file = new File(ROOT_DIRECTORY);
		file.mkdir();
		
		Asset.init(0, null);
		Sound.initSound();
		game = new Driver(w, h, title);

		game.start();
	}
	
	/**
	 * starts the engine with the res directory relative to the jar placement
	 * @param w - screen width
	 * @param h - screen height
	 * @param rootDirectory - name of game root directory
	 * @param resDirectory - name of game resource directory
	 * @param title - title of game window
	 */
	public static void start(int w, int h, String rootDirectory, String resDirectory, String title) {
		ROOT_DIRECTORY = new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + "/" + rootDirectory + "/";
		ROOT_DIRECTORY = ROOT_DIRECTORY.replace('\\', '/');
		File file = new File(ROOT_DIRECTORY);
		file.mkdir();
		
		Asset.init(1, resDirectory);
		Sound.initSound();
		game = new Driver(w, h, title);

		game.start();
	}
	
	/**
	 * starts the engine while allowing the resources to be initialized by the main game
	 * @param w - screen width
	 * @param h - screen height
	 * @param rootDirectory - name of game root directory
	 * @param title - title of game window
	 * @param init - any value
	 */
	public static void start(int w, int h, String rootDirectory, String title, int init) {
		ROOT_DIRECTORY = new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + "/" + rootDirectory + "/";
		ROOT_DIRECTORY = ROOT_DIRECTORY.replace('\\', '/');
		File file = new File(ROOT_DIRECTORY);
		file.mkdir();

		Sound.initSound();
		game = new Driver(w, h, title);

		game.start();
		
	}
	
	public static DrawGraphics getGraphics() {
		return game.getCanvas();
	}
	
	public static void updateScale(double xScale, double yScale) {
		game.getCanvas().updateScale(xScale, yScale);
		Handler.getCamera().refresh();
	}
	
	public static void forceClose() {
		game.close();
	}
	
	public static void attachCloseEvent(Event e) {
		game.screen.addClosingEvent(e);
	}
}
