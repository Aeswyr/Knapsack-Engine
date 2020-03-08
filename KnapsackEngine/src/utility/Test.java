package utility;

import core.Engine;
import entity.Hitbox;
import gfx.DrawGraphics;
import input.Controller;
import runtime.Handler;
import runtime.Scene;

public class Test extends Scene {

	public static void main(String[] args) {
		Test scene = new Test();
		Engine.start(1600, 900, "dir");
		Engine.getGraphics().setLightingEnabled(false);
		Handler.startScene(scene);
	}

	Hitbox a, b, c;
	int x = 0, y = 0;

	@Override
	public void init(String data) {
		a = new Hitbox(new int[][] { { 0, 0 }, { 32, 0 }, { 64, 32 }, { 32, 64 }, { 0, 64 } });
		b = new Hitbox(new int[][] { { 0, 0 }, { 32, 0 }, { 64, 32 }, { 32, 64 }, { 0, 64 } });
		b.updatePos(64, 64);
		c = new Hitbox(new int[][] { { 0, 0 }, { 32, 0 }, { 64, 32 }, { 32, 64 }, { 0, 64 } });
		c.updatePos(128, 128);

	}

	@Override
	public void update() {
		if (Controller.getKeyPressed('w'))
			y--;
		if (Controller.getKeyPressed('a'))
			x--;
		if (Controller.getKeyPressed('s'))
			y++;
		if (Controller.getKeyPressed('d'))
			x++;

		a.updatePos(x, y);

		if (a.colliding(b))
			bcol = true;
		else
			bcol = false;
		
		if (a.colliding(c))
			ccol = true;
		else
			ccol = false;
		
		
		Handler.getCamera().center(x, y);
	}

	boolean bcol, ccol;

	@Override
	public void render(DrawGraphics g) {
		a.render(g);
		b.render(g);
		c.render(g);

		if (bcol)
			g.drawRect(0, 0, 16, 16, 0xffff0000);
		else
			g.drawRect(0, 0, 16, 16, 0xffffffff);
		
		if (ccol)
			g.drawRect(16, 0, 16, 16, 0xff00ff00);
		else
			g.drawRect(16, 0, 16, 16, 0xffffffff);

	}

}