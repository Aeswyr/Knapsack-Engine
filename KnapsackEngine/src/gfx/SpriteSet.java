package gfx;

/**
 * A spriteset is a collection of individual sprites which are used for a single
 * purpose. For instance, a tile would use a spriteset with different cells for
 * variations on the tile.
 * 
 * @author Pascal
 *
 */
public class SpriteSet {

	protected Sprite[] sprites;
	
	/**
	 * Constructs a spriteset
	 * 
	 * @param x      - y position on the sheet
	 * @param y      - x position on the sheet
	 * @param cell   - size in pixels of one edge of a square cell (one sprite
	 *               within the set)
	 * @param size  - width of set in cells
	 * @param height - height of set in cells
	 */
	public SpriteSet(int x, int y, int cell, int size, SpriteSheet sheet) {
		sprites = new Sprite[size * size];
		for (int j = 0; j < size; j++) {
			for (int i = 0; i < size; i++) {
				sprites[j * size + i] = new Sprite(x + cell * i, y + cell * j, cell, sheet);
			}
		}
	}
	
	/**
	 * gets the sprite at a specific index
	 * @param index - the index of the sprite in the set
	 * @returns a sprite
	 */
	public Sprite get(int index) {
		return sprites[index];
	}

}
