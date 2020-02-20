package core;

import java.io.File;

import javax.swing.JFileChooser;

import sfx.Sound;

public class Game {

	public static String ROOT_DIRECTORY;
	
	public static void start(String rootDirectory) {
		Assets.init();
		Sound.initSound();
		Driver game = new Driver();
		ROOT_DIRECTORY = new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + "/" + rootDirectory + "/";
		ROOT_DIRECTORY = ROOT_DIRECTORY.replace('\\', '/');
		File file = new File(ROOT_DIRECTORY);
		file.mkdir();
		game.start();
	}
	
}
