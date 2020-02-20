package core;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import runtime.Handler;
import sfx.Sound;

/**
 * Contains the information for the game window
 * @author Pascal
 *
 */
public class Screen extends Canvas {

	/**
	 * 
	 */
	private static final long serialVersionUID = 942448191868169090L;

	private JFrame frame;
	private Dimension d;

	/**
	 * initializes the game frame and canvas with the given width and height. using
	 * -1, -1 will set up true fullscreen, while 0, 0 will set up a limited
	 * fullscreen
	 * 
	 * @param width - desired width
	 * @param height - desired height
	 */
	public Screen(int width, int height) {

		frame = new JFrame();
		d = new Dimension();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

		if (width == -1 && height == -1) {
			GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
			frame.setUndecorated(true);
			gd.setFullScreenWindow(frame);
			d.setSize(gd.getFullScreenWindow().getWidth(), gd.getFullScreenWindow().getHeight());
			this.setSize(d);
			this.setPreferredSize(d);
			this.setMaximumSize(d);
			this.setMinimumSize(d);
		} else if (width == 0 && height == 0) {
			GraphicsConfiguration gc = frame.getGraphicsConfiguration();
			frame.setUndecorated(true);
			d.setSize(gc.getBounds().getWidth(), gc.getBounds().getHeight());
			frame.setSize(d);
			frame.setPreferredSize(d);
			frame.setMinimumSize(d);
			frame.setMaximumSize(d);
			this.setSize(d);
			this.setPreferredSize(d);
			this.setMaximumSize(d);
			this.setMinimumSize(d);
		} else {
			d.setSize(width, height);
			frame.setSize(d);
			frame.setPreferredSize(d);
			frame.setMinimumSize(d);
			frame.setMaximumSize(d);
			this.setSize(d);
			this.setPreferredSize(d);
			this.setMaximumSize(d);
			this.setMinimumSize(d);
		}

		frame.setTitle("Into Darkness");
		frame.setName("Into Darkness");

		frame.add(this);
		frame.pack();

		Driver.xScale = d.getWidth() / 960;
		Driver.yScale = d.getHeight() / 540;

		frame.setVisible(true);
		System.out.println(d.width + ", " + d.height);
	}

	/**
	 * @returns the true width of the window
	 */
	public int getWidth() {
		return d.width;
	}

	/**
	 * @returns the true height of the window
	 */
	public int getHeight() {
		return d.height;
	}

	/**
	 * runs events that should occur when the game is closed
	 */
	public void close() {
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}

	/**
	 * sets up closing events
	 * 
	 * @param d - the game driver
	 * @param r - the game renderer
	 * @param h - the game handler
	 */
	public void setClosing(Driver d, Renderer r) {
		frame.addWindowListener(new WindowAdapter() // Operations to complete upon window closing
		{
			public void windowClosing(WindowEvent we) {
				d.stop();
				r.stop();
				try {
					Sound.shutdown();
				} catch (NullPointerException e) {
					e.printStackTrace();
				}	
				System.out.println("Game Closed");
			}
		});
	}
}
