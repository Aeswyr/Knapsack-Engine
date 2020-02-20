package particle;

import java.util.ArrayList;

import gfx.DrawGraphics;
/**
 * handles updating and rendering of particles
 * @author Pascal
 *
 */
public class ParticleManager {

	private ArrayList<Particle> particles;

	public ParticleManager() {
		particles = new ArrayList<Particle>();
	}

	/**
	 * updates all active particles, then removes any particles which have expired
	 */
	public void update() {
		for (int i = 0; i < particles.size(); i++) {
			if (particles.get(i).isDead()) {
				particles.remove(i);
				i--;
			} else {
				particles.get(i).update();
			}
		}
	}

	/**
	 * draws all active particles
	 * @param g - the DrawGraphics component associated with the renderer
	 */
	public void render(DrawGraphics g) {
		for (int i = 0; i < particles.size(); i++) {
			particles.get(i).render(g);
		}
	}
	
	/**
	 * adds a new particle to the active particles list
	 * @param p - the particle to add to the list
	 */
	public void add(Particle p) {
		particles.add(p);
	}
}
