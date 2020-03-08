package core;

import java.io.File;

import javax.swing.JFileChooser;

import gfx.DrawGraphics;
import sfx.Sound;

public class Engine {

	public static String ROOT_DIRECTORY;
	private static Driver game;
	
	public static void start(int w, int h, String rootDirectory) {
		ROOT_DIRECTORY = new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + "/" + rootDirectory + "/";
		ROOT_DIRECTORY = ROOT_DIRECTORY.replace('\\', '/');
		File file = new File(ROOT_DIRECTORY);
		file.mkdir();
		
		Assets.init();
		Sound.initSound();
		game = new Driver(w, h);

		game.start();
	}
	
	public static DrawGraphics getGraphics() {
		return game.getCanvas();
	}
}
