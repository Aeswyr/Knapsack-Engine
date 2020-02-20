package gfx;

import java.awt.image.BufferedImage;
import utility.Loader;

/**
 * Represents a sprite sheet within the program
 * @author Pascal
 *
 */
public class SpriteSheet {

	/**
	 * BufferedImage to hold the processed SpriteSheet
	 */
  BufferedImage b;

  /**
   * initializes a new SpriteSheet by assigning the image located at the path to the BufferedImage
   * and removing any instance of the hexes FF00FF and 7F007F, replacing them with transparent pixels
   * @param path - the path to the desired SpriteSheet
   */
  public SpriteSheet(String path) {
    BufferedImage imageBuffer = Loader.loadImage(path);
    b = new BufferedImage(imageBuffer.getWidth(), imageBuffer.getHeight(),
        BufferedImage.TYPE_INT_ARGB);
    for (int x = 0; x < b.getWidth(); x++) {
      for (int y = 0; y < b.getHeight(); y++) {
          b.setRGB(x, y, imageBuffer.getRGB(x, y));
      }
    }
  }

  /**
   * returns a sub-image from the SpriteSheet with the top left corner located at (x, y), with a height and width of size
   * @param x - the x value (in pixels) of the top left corner of the sub-image
   * @param y - the y value (in pixels) of the top left corner of the sub-image
   * @param size - the height and width of the sub-image
   * @return a BufferedImage containing the sub-image described by the parameters
   */
  public BufferedImage cut(int x, int y, int size) {
    return b.getSubimage(x, y, size, size);
  }

  /**
   * returns a sub-image from the SpriteSheet with the top left corner located at (x, y), with a height and width determined by parameters
   * @param x - the x value (in pixels) of the top left corner of the sub-image
   * @param y - the y value (in pixels) of the top left corner of the sub-image
   * @param width - the width of the sub-image
   * @param height - the height of the sub-image
   * @return a BufferedImage containing the sub-image described by the parameters
   */
  public BufferedImage cut(int x, int y, int width, int height) {
    return b.getSubimage(x, y, width, height);
  }

  /**
   * returns a reflection of the buffered image input with the top left corner reflected to the top right
   * @param b - a BufferedImage to be reflected
   * @return a new BufferedImage that is a reflected version of the input
   */
  public static BufferedImage reflect(BufferedImage b) {
    BufferedImage end = new BufferedImage(b.getWidth(), b.getHeight(), BufferedImage.TYPE_INT_ARGB);
    for (int y = 0; y < b.getHeight(); y++) {
      for (int x = 0; x < b.getWidth(); x++) {
        end.setRGB(b.getWidth() - x - 1, y, b.getRGB(x, y));
      }
    }
    return end;
  }

  /**
   * fetches the height of the sprite sheet
   * @returns the spritesheet's height dimension
   */
  public int getHeight() {
	  return b.getHeight();
  }
  
  /**
   * fetches the width of the sprite sheet
   * @returns the spritesheet's width dimension
   */
  public int getWidth() {
	  return b.getWidth();
  }
}
