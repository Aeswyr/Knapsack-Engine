package utility;

import geometry.Rect;
import gfx.DrawGraphics;
import gfx.Sprite;
import runtime.Handler;
import runtime.Light;
import runtime.Scene;

/**
 * creates loading screens
 * @author Pascal
 *
 */
public class LoadingScreen extends Scene{
	
	private int loadProgress;
	private final int MAXLOAD;
	private String displayText;
	private String tip;
	private Rect back;
	private Rect bar;
	private Rect load;
	
	/**
	 * creates and displays a loading screen which will have a load bar with a set number of increments
	 * @param MAXLOAD - number of increments in the load bar
	 */
	public LoadingScreen(int MAXLOAD) {
		this.MAXLOAD = MAXLOAD;
		displayText = "";
		Handler.setFPSCap(false);
		
		back = new Rect(Handler.getWidth(), Handler.getHeight(), 0xff333333, Sprite.createLightData(100000, Light.IGNORE));
		bar = new Rect(300, 20, 0xff000000, Sprite.createLightData(100000, Light.IGNORE));
		load = new Rect(1, 16, 0xff990000, Sprite.createLightData(100000, Light.IGNORE));
		
		Handler.setLoadingScreen(this);
	}
	
	/**
	 * draws the loading screen
	 * @param g - the DrawGraphics component to draw with
	 */
	public void render(DrawGraphics g) {
		back.render(0, 0, g);
		bar.render(40, 40, g);
		load.render(42, 42, g);
		g.write(displayText, 60, 60, 0xff009900);
		g.write(tip, 20, 120, 0xffffffff);
	}
	
	/**
	 * closes the currently displayed loading screen
	 */
	public void close() {
		Handler.closeLoadingScreen();
		Handler.setFPSCap(true);
	}
	
	/**
	 * increments the loading bar by the set value
	 * @param value
	 */
	public void increment(int value) {
		this.loadProgress += value;
		double loadFactor = 1.0 * loadProgress / MAXLOAD;
		load.resize((int)(295 * loadFactor) + 1, 16);
	}
	
	/**
	 * displays text beneath the loading bar
	 * @param text
	 */
	public void displayText(String text) {
		this.displayText = text;
	}

	@Override
	public void init(String data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
	
}
