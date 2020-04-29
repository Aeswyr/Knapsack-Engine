package runtime;

import gfx.DrawGraphics;

/**
 * parent class for all types of gamestates (main menu, pause, gameplay, etc.)
 * 
 * @author Pascal
 *
 */
public abstract class Scene {

	/**
	 * lines called on initial scene startup
	 * 
	 * @param data - string data which your scene may rely on to startup
	 */
	public abstract void init(String data);

	public abstract void update();

	public abstract void render(DrawGraphics g);

	/**
	 * lines executed when this scene has been previously initialized and is set as
	 * the active scene
	 */
	public abstract void start();

	/**
	 * lines executed when this scene is removed from active
	 */
	public abstract void stop();
}
