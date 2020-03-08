package core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

import gfx.Font;
import gfx.Sprite;
import gfx.SpriteSheet;
import sfx.Sound;
import utility.Loader;

/**
 * stores and initializes all game assets
 * 
 * @author Pascal
 *
 */
public class Assets {

	static String path;

	static File dirFont;
	static File dirSprite;
	static File dirSound;

	static HashMap<String, Sprite> sprites;
	static HashMap<String, Sound> sounds;
	static HashMap<String, Font> fonts;

	public static void init() {

		path = Engine.ROOT_DIRECTORY + "res/";

		dirFont = new File(path + "font/");
		dirSprite = new File(path + "sprite/");
		dirSound = new File(path + "sound/");

		dirFont.mkdirs();
		dirSprite.mkdirs();
		dirSound.mkdirs();

		initSprite();
		initSound();
		initFont();
	}

	private static void initSprite() {
		sprites = new HashMap<String, Sprite>();

	}

	private static void initSound() {
		sounds = new HashMap<String, Sound>();

	}

	private static void initFont() {

	}
}
