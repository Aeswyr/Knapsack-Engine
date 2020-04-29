package core;

import java.io.BufferedReader;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
public class Asset {

	static String path;

	static HashMap<String, Sprite> sprites;
	static HashMap<String, Sound> sounds;
	static HashMap<String, Font> fonts;

	/**
	 * Initializes all assets within the target res folder
	 * 
	 * @param dirID  - integer value corresponding to the res folder location in the
	 *               filepath 0 = dir on main path, 1 = dir in relation to jar, 2 =
	 *               res
	 * @param resDir - name of the res directory
	 */
	public static void init(int dirID, String resDir) {

		sprites = new HashMap<String, Sprite>();
		sounds = new HashMap<String, Sound>();
		fonts = new HashMap<String, Font>();

		if (dirID == 0)
			path = Engine.ROOT_DIRECTORY + "res/";
		else if (dirID == 1)
			path = Loader.getJarLocation() + "/" + resDir + "/";
		else
			path = "/";

		System.out.println("Scanning for resources at " + path);
		ArrayList<File> files = new ArrayList<File>();
		if (dirID == 0 || dirID == 1) {
			File dir = new File(path);
			collectFiles(files, dir);

			for (File f : files) {
				String n = f.getName();
				if (n.subSequence(n.length() - 5, n.length()).equals("asdat")) {
					String p = f.getPath().substring(0, f.getPath().length() - n.length());
					System.out.println("File spotted at " + p);
					File[] file = new File(p).listFiles(new FilenameFilter() {
						@Override
						public boolean accept(File current, String name) {
							return !(new File(current, name).isDirectory());
						}
					});

					for (File fi : file) {
						if (fi.getName().equals(n.substring(0, n.length() - 6))) {
							BufferedReader read = Loader.loadTextFromFile(f.getPath());

							try {
								String type = read.readLine();

								if (type.equals("spr")) {
									initSprite(read, fi);
								} else if (type.equals("sou")) {
									initSound(read, fi);
								} else if (type.equals("fon")) {
									initFont(read, fi);
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							break;
						}
					}
				}
			}

		} else {

		}
	}

	/**
	 * recursive method to add all files in the res folder and subdirectories into
	 * an array
	 * 
	 * @param f    - arraylist to place files in
	 * @param head - res directory
	 */
	private static void collectFiles(ArrayList<File> f, File head) {
		File[] dirs = head.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});

		File[] files = head.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return !(new File(current, name).isDirectory());
			}
		});

		f.addAll(Arrays.asList(files));

		for (int i = 0; i < dirs.length; i++) {
			collectFiles(f, dirs[i]);
		}
	}

	/**
	 * transforms an image from a file to a loaded sprite
	 * 
	 * @param read - buffered reader pointed to the image .asdat file
	 * @param f    - image file to transform
	 * @throws IOException
	 */
	private static void initSprite(BufferedReader read, File f) throws IOException {

		SpriteSheet sheet = new SpriteSheet(f.getAbsolutePath());

		int lines = Integer.parseInt(read.readLine());

		for (int j = 0; j < lines; j++) {
			String[] val = read.readLine().split(" ");

			Sprite sp = new Sprite(Integer.parseInt(val[1]), Integer.parseInt(val[2]), Integer.parseInt(val[3]),
					Integer.parseInt(val[4]), Integer.parseInt(val[5]), Integer.parseInt(val[6]), sheet,
					Sprite.createLightData(Integer.parseInt(val[7]), Integer.parseInt(val[8])));

			sprites.put(val[0], sp);
		}

	}

	/**
	 * transforms a sound effect from a file to a loaded sound
	 * 
	 * @param read - buffered reader pointed to the sound .asdat file
	 * @param f    - sound file to transform
	 * @throws IOException
	 */
	private static void initSound(BufferedReader read, File f) throws IOException {

		Sound sound = new Sound(f.getAbsolutePath());
		sounds.put(read.readLine(), sound);

	}

	/**
	 * transforms an image from a file to a loaded font
	 * 
	 * @param read - buffered reader pointed to the font .asdat file
	 * @param f    - image file to transform
	 * @throws IOException
	 */
	private static void initFont(BufferedReader read, File f) throws IOException {

		String[] val = read.readLine().split(" ");

		Font font = new Font(f.getAbsolutePath(),
				Sprite.createLightData(Integer.parseInt(val[1]), Integer.parseInt(val[2])));
		fonts.put(val[0], font);

	}

	/**
	 * grabs a sprite from the list of loaded sprites
	 * 
	 * @param name - reference name of the desired sprite
	 * @returns the sprite corresponding to the input reference name
	 */
	public static Sprite getSprite(String name) {
		return sprites.get(name);
	}

	/**
	 * grabs a sound from the list of loaded sounds
	 * 
	 * @param name - reference name of the desired sound
	 * @returns the sound corresponding to the input reference name
	 */
	public static Sound getSound(String name) {
		return sounds.get(name);
	}

	/**
	 * grabs a font from the list of loaded fonts
	 * 
	 * @param name - reference name of the desired font
	 * @returns the font corresponding to the input reference name
	 */
	public static Font getFont(String name) {
		return fonts.get(name);
	}
}
