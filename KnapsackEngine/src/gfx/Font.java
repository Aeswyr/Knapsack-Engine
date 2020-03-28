package gfx;

import utility.Loader;

/**
 * holds info for fonts
 * 
 * @author Pascal
 *
 */
public class Font {

	private Sprite font;
	private int[] offsets;
	private int[] widths;

	/**
	 * initializes a font using the sprite at the specified path
	 * 
	 * @param path - path to the font data
	 */
	public Font(String path, int spriteData) {

		if (path.contains(":"))
			font = new Sprite(Loader.loadImageFromFile(path), spriteData);
		else
			font = new Sprite(Loader.loadImage(path), spriteData);

		offsets = new int[59];
		widths = new int[59];

		int unicode = 0;

		int[] raw = font.getRawFrame();
		for (int i = 0; i < font.getWidth(); i++) {
			if (raw[i] == 0xff0000ff)
				offsets[unicode] = i;
			if (raw[i] == 0xff00ff00) {
				widths[unicode] = i - offsets[unicode];
				unicode++;
			}
		}
	}

	/**
	 * @returns the font sprite
	 */
	protected Sprite getSprite() {
		return font;
	}

	/**
	 * @returns an array containing the widths of each character organized by their
	 *          unicode values
	 */
	protected int[] getWidths() {
		return widths;
	}

	/**
	 * @returns an array containing the offsets of each character organized by their
	 *          unicode values
	 */
	protected int[] getOffsets() {
		return offsets;
	}

}
