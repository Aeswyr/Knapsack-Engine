package runtime;

import java.util.ArrayList;

import gfx.DrawGraphics;

/**
 * Manages all active lights
 * 
 * @author Pascal
 *
 */
public class LightManager {

	ArrayList<Light> lights;
	ArrayList<Light> remove;

	/**
	 * initializes a light manager to keep track of and update lights
	 * 
	 * @param handler
	 */
	public LightManager() {
		lights = new ArrayList<Light>();
		remove = new ArrayList<Light>();
	}

	/**
	 * clears dead lights
	 */
	public void update() {
		if (!remove.isEmpty()) {
			lights.removeAll(remove);
			remove.clear();
		}
	}

	/**
	 * draws all lights
	 * 
	 * @param g - DrawGraphics component of the game
	 */
	public void render(DrawGraphics g) {
		for (int i = 0; i < lights.size(); i++) {
			lights.get(i).render(g);
		}
	}

	/**
	 * adds a light to the list of active lights
	 * 
	 * @param l - light to add
	 */
	public void add(Light l) {
		lights.add(l);
	}

	/**
	 * removes a light from the list of active lights after the current update cycle
	 * 
	 * @param l - light to remove
	 */
	public void remove(Light l) {
		remove.add(l);
	}

	/**
	 * removes all active light objects
	 */
	public void flushLights() {
		lights.clear();
	}

}
