package gui;

import java.util.ArrayList;

import gfx.DrawGraphics;

/**
 * a collection of UIObjects which can be used to keep ui components grouped for easy addition and removal
 * @author Pascal King
 *
 */
public class UICollection extends UIObject {

	private ArrayList<UIObject> objects = new ArrayList<UIObject>();

	@Override
	public void render(DrawGraphics g) {
		for (int i = 0; i < objects.size(); i++)
			objects.get(i).render(g);

	}

	@Override
	public void update() {
		for (int i = 0; i < objects.size(); i++)
			objects.get(i).update();
	}
	
	/**
	 * adds a UIObject to the collection
	 * @param o
	 */
	public void add(UIObject o) {
		objects.add(o);
	}
	
	/**
	 * removes a UIObject from the collection
	 * @param o
	 */
	public void remove(UIObject o) {
		objects.remove(o);
	}

}
