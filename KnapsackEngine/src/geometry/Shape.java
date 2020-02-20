package geometry;

import gfx.DrawGraphics;
import gfx.ShapeRequest;
import gfx.Sprite;

/**
 * holds general functions for all drawable shapes
 * @author Pascal
 *
 */
public abstract class Shape {

	int[] raster;
	int width, height;
	int xpos, ypos;
	int type;
	int color, color2 = -1;
	Sprite sprite;
	
	/**
	 * returns true if this shape contains or contacts another specified shape
	 * @param s - the shape to compare to
	 * @returns true if the shapes collide, false otherwise
	 */
	public abstract boolean contains(Shape s);
	
	/**
	 * draws this shape to the screen at the specified point
	 * @param x - x coordinate of the top left of the shape
	 * @param y - y coordinate of the top left of the shape
	 * @param g - the DrawGraphics component associated with the renderer
	 */
	public void render(int x, int y, DrawGraphics g) {
		g.submitRequest(new ShapeRequest(this, x, y));
	}
	
	/**
	 * gets the array of pixel data for this shape
	 * @returns the shape raster
	 */
	public int[] getRaster() {
		return raster;
	}
	
	/**
	 * gets the width of this shape
	 * @returns the shape width in pixels
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * gets the height of this shape
	 * @returns the shape height in pixels
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * gets a sprite made from this shape's raster
	 * @returns a sprite created from this shape's raster
	 */
	public Sprite toSprite() {
		return sprite;
	}
 	
}
