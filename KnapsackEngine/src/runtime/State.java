package runtime;

import gfx.DrawGraphics;

/**
 * parent class for all types of gamestates (main menu, pause, gameplay, etc.)
 * @author Pascal
 *
 */
public abstract class State {
	Handler handler;
	public State(Handler handler) {
		this.handler = handler;
	}
	
	public abstract void init(String data);
	
	public abstract void update();

	public abstract void render(DrawGraphics g);
}
