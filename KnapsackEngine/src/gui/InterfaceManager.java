package gui;

import java.util.ArrayList;

import gfx.DrawGraphics;

/**
 * Class for managing all active UI components
 * 
 * @author Pascal
 *
 */
public class InterfaceManager {

	private ArrayList<UIObject> list;
	private ArrayList<UIObject> remove;

	public InterfaceManager() {
		list = new ArrayList<UIObject>();
		remove = new ArrayList<UIObject>();
	}

	/**
	 * draws all active UI components
	 * 
	 * @param g - the DrawGraphics object associated with the renderer
	 */
	public void render(DrawGraphics g) {
		for (int i = 0; i < list.size(); i++) {
			list.get(i).render(g);
		}
	}
	
	/**
	 * renders all ui objects in developer mode
	 */
	public void renderDevMode(DrawGraphics g) {
		for (int i = 0; i < list.size(); i++) {
			list.get(i).renderDevMode(g);
		}
	}

	/**
	 * updates all active UI components and cleans up inactive ones
	 */
	public void update() {
		for (int i = 0; i < list.size(); i++)
			list.get(i).update();
		for (UIObject o : remove)
			list.remove(o);
		remove.clear();
	}

	// Array interaction

	/**
	 * adds a UI component to the active list
	 * 
	 * @param o - the UIObject to add
	 */
	public void addObject(UIObject o) {
		list.add(o);
	}

	/**
	 * gets an active UIObject based on it's index
	 * 
	 * @param index - index of the object in the array
	 * @returns a UIObject at the specified index
	 */
	public UIObject getObject(int index) {
		return list.get(index);
	}

	/**
	 * removes an object from the list of active UI components
	 * 
	 * @param index - index of the object to remove
	 */
	public void removeObject(int index) {
		remove.add(list.get(index));
	}

	/**
	 * removes an object from the list of active UI components
	 * 
	 * @param e - the object to remove
	 */
	public void removeObject(UIObject e) {
		remove.add(e);
	}

	/**
	 * removes all active UI components
	 */
	public void flushObjects() {
		list.clear();
	}

	/**
	 * @returns the number of active UI components
	 */
	public int totalObjects() {
		return list.size();
	}

}
