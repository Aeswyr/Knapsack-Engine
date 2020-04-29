package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import core.Driver;

public class Controller implements KeyListener, MouseListener, MouseMotionListener {

	public static final char LEFT = (char) KeyEvent.VK_LEFT;
	public static final char RIGHT = (char) KeyEvent.VK_RIGHT;
	public static final char UP = (char) KeyEvent.VK_UP;
	public static final char DOWN = (char) KeyEvent.VK_DOWN;
	public static final char SPACE = (char) KeyEvent.VK_SPACE;
	public static final char SHIFT = (char) KeyEvent.VK_SHIFT;
	public static final char CTRL = (char) KeyEvent.VK_CONTROL;
	public static final char ALT = (char) KeyEvent.VK_ALT;
	public static final char TAB = (char) KeyEvent.VK_TAB;
	public static final char ENTER = (char) KeyEvent.VK_ENTER;
	public static final char CPSLOCK = (char) KeyEvent.VK_CAPS_LOCK;
	public static final char ESC = (char) KeyEvent.VK_ESCAPE;
	public static final int MOUSELEFT = 0;
	public static final int MOUSEMIDDLE = 1;
	public static final int MOUSERIGHT = 2;
	

	static boolean[] k = new boolean[256];
	static boolean[] keys = new boolean[256];
	static boolean[] lastKeys = new boolean[256];

	static int x, y;
	static boolean mouseLeft, mouseRight, mouseMiddle;
	static boolean ml0, mm0, mr0, ml1, mm1, mr1;

	public static void update() {
		for (int i = 0; i < keys.length; i++) {
			lastKeys[i] = keys[i];
			keys[i] = k[i];
		}
		ml0 = ml1;
		ml1 = mouseLeft;
		mm0 = mm1;
		mm1 = mouseMiddle;
		mr0 = mr1;
		mr1 = mouseRight;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		x = e.getX();
		y = e.getY();

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		x = e.getX();
		y = e.getY();

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		switch (e.getButton()) {
		case MouseEvent.BUTTON1:
			mouseLeft = true;
			break;
		case MouseEvent.BUTTON2:
			mouseMiddle = true;
			break;
		case MouseEvent.BUTTON3:
			mouseRight = true;
			break;
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		switch (e.getButton()) {
		case MouseEvent.BUTTON1:
			mouseLeft = false;
			break;
		case MouseEvent.BUTTON2:
			mouseMiddle = false;
			break;
		case MouseEvent.BUTTON3:
			mouseRight = false;
			break;
		}

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		k[e.getKeyCode()] = true;

	}

	@Override
	public void keyReleased(KeyEvent e) {
		k[e.getKeyCode()] = false;
	}

	public static boolean getKeyPressed(char c) {
		if ((int) c > 96 && (int) c < 123)
			c = Character.toUpperCase(c);
		return keys[(int) c];
	}

	public static boolean getKeyTyped(char c) {
		if ((int) c > 96 && (int) c < 123)
			c = Character.toUpperCase(c);
		return !lastKeys[(int) c] && keys[(int) c];
	}

	public static boolean getMousePressed(int mouseButton) {
		switch (mouseButton) {
		case MOUSELEFT:
			return mouseLeft;
		case MOUSEMIDDLE:
			return mouseMiddle;
		case MOUSERIGHT:
			return mouseRight;
		}
		return false;
	}
	
	public static boolean getMouseTyped(int mouseButton) {
		switch (mouseButton) {
		case MOUSELEFT:
			return !ml0 && ml1;
		case MOUSEMIDDLE:
			return !mm0 && mm1;
		case MOUSERIGHT:
			return !mr0 && mr1;
		}
		return false;
	}

	/**
	 * @returns the mouse x position in pixels on the frame
	 */
	public static int getX() {
		return x;
	}

	/**
	 * @returns the mouse y position in pixels on the frame
	 */
	public static int getY() {
		return y;
	}

	/**
	 * @returns the mouse x position in pixels relative to the DrawGraphics, not the
	 *          frame
	 */
	public static int getAdjX() {
		return (int) (x / Driver.xScale);
	}

	/**
	 * @returns the mouse y position in pixels relative to the DrawGraphics, not the
	 *          frame
	 */
	public static int getAdjY() {
		return (int) (y / Driver.yScale);
	}

}
