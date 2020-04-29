package gui;

import gfx.Sprite;

/**
 * turns a sprite into a nine-slice which is the base for all ui component
 * rendering. Also contains methods for constructing ui sprites from a
 * nine-slice
 * 
 * @author Pascal King
 *
 */
public class NineSlice {

	int w, h;
	int[] s;

	/**
	 * initializes a nine-slice used for constructing gui sprites
	 * 
	 * @param s - sprite used to construct the nine-slice
	 */
	public NineSlice(Sprite s) {
		w = s.getWidth() / 3;
		h = s.getHeight() / 3;
		this.s = s.getRawFrame();
	}

	/**
	 * returns a sprite for a gui object constructed using this nine-slice
	 * 
	 * @param width    - width of the sprite
	 * @param height   - height of the sprite
	 * @param priority - render type of the sprite
	 * @returns the sprite version of the constructed image
	 */
	public Sprite build(int width, int height, int priority) {
		int[] raster = new int[width * height];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				if (y < h) {
					if (x < w) {
						raster[y * width + x] = s[(y % h) * w * 3 + (x % w)];
					} else if (x < width - w) {
						raster[y * width + x] = s[(y % h) * w * 3 + (x % w + w)];
					} else {
						raster[y * width + x] = s[(y % h) * w * 3 + (x % w + w * 2)];
					}
				} else if (y < height - h) {
					if (x < w) {
						raster[y * width + x] = s[(y % h + h) * w * 3 + (x % w)];
					} else if (x < width - w) {
						raster[y * width + x] = s[(y % h + h) * w * 3 + (x % w + w)];
					} else {
						raster[y * width + x] = s[(y % h + h) * w * 3 + (x % w + w * 2)];
					}
				} else {
					if (x < w) {
						raster[y * width + x] = s[(y % h + 2 * h) * w * 3 + (x % w)];
					} else if (x < width - w) {
						raster[y * width + x] = s[(y % h + 2 * h) * w * 3 + (x % w + w)];
					} else {
						raster[y * width + x] = s[(y % h + 2 * h) * w * 3 + (x % w + w * 2)];
					}
				}
			}
		}

		return new Sprite(width, height, raster, priority);
	}

}
