package utility;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/**
 * Handles loading files from inside and outside the game jar
 * @author Pascal
 *
 */
public class Loader {

	/**
	 * Loads an image resource from a string path
	 * @param path - the path to the desired resource
	 * @return a BufferedImage loaded from the path, null if the load fails
	 */
	public static BufferedImage loadImage(String path) {
		try {
			return ImageIO.read(Loader.class.getResourceAsStream(path));
		} catch (Exception e) {
			System.out.println("Resource at " + path + " failed to load");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Loads an image resource from a file path
	 * @param path - the path to the desired resource
	 * @return a BufferedImage loaded from the path, null if the load fails
	 */
	public static BufferedImage loadImageFromFile(String path) {
		try {
			return ImageIO.read(new File(path));
		} catch (Exception e) {
			System.out.println("Resource at " + path + " failed to load");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Loads a BufferedReader linked to a file at the path
	 * @param path - the path to the desired resource
	 * @return a BufferedReader linked to the path location, null if the load fails
	 */
	public static BufferedReader loadText(String path) {
		try {
			return new BufferedReader(new InputStreamReader(Loader.class.getResourceAsStream(path)));
		} catch (Exception e) {
			System.out.println("Resource at " + path + " failed to load");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Loads an InputStream linked to a file at the path
	 * @param path - the path to the desired resource
	 * @return a InputStream linked to the path location, null if the load fails
	 */
	public static InputStream loadStream(String path) {
		try {
			return Loader.class.getResourceAsStream(path);
		} catch (Exception e) {
			System.out.println("Resource at " + path + " failed to load");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Loads an AudioInputStream linked to a file at the path
	 * @param path - the path to the desired resource
	 * @return a AudioInputStream linked to the path location, null if the load fails
	 */
	public static AudioInputStream loadURLAudio(String path) {
		URL url = Loader.class.getResource(path);
		try {
			return AudioSystem.getAudioInputStream(url);
		} catch (Exception e) {
			System.out.println("Resource at " + path + " failed to load");
			e.printStackTrace();
			return null;
		}

	}
	
	/**
	 * Loads a BufferedReader linked to a file at the specified path OUTSIDE the game jar/resource folder
	 * @param path - the path to the desired resource
	 * @return a BufferedReader linked to the path location, null if the load fails
	 */
	public static BufferedReader loadTextFromFile(String path) {
		File file = new File(path);
		FileReader f = null;
		try {
			f = new FileReader(file);
		} catch (FileNotFoundException e) {
			System.out.println("Resource at " + path + " failed to load");
			e.printStackTrace();
		}
		BufferedReader read = new BufferedReader(f);
		
		return read;
	}
	
	
	/**
	 * Loads a InputStreamReader linked to a file at the specified path OUTSIDE the game jar/resource folder
	 * @param path - the path to the desired resource
	 * @param encoding - the encoding standard to use for the reader
	 * @return an InputStreamReader with utf8 enabled linked to the path location, null if the load fails
	 */
	public static BufferedReader loadTextFromFile(String path, Charset encoding) {
		File file = new File(path);
		FileInputStream f = null;
		try {
			f = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			System.out.println("Resource at " + path + " failed to load");
			e.printStackTrace();
		}
		BufferedReader read = new BufferedReader(new InputStreamReader(f, encoding));
		
		return read;
	}
	
	/**
	 * @returns the location this jar was run from in the filepath
	 */
	public static String getJarLocation() {
		try {
			return new File(Loader.class.getProtectionDomain().getCodeSource().getLocation()
				    .toURI()).getPath();
		} catch (URISyntaxException e) {
			System.out.println("Classpath fetch failed");
			e.printStackTrace();
			return null;
		}
	}
}
