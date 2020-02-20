package entity;

import utility.CoordKey;
import utility.HashTable;
import utility.LoadingScreen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import gfx.DrawGraphics;
import runtime.Handler;

public class EntityManager {

	private ArrayList<Entity> entities;
	private ArrayList<Entity> remove;
	private ArrayList<Entity> mobs;
	private HashTable<CoordKey, Entity> inorder;

	/**
	 * Initializes an entityManager which handles rendering and updating entities
	 */
	public EntityManager() {
		entities = new ArrayList<Entity>();
		remove = new ArrayList<Entity>();
		mobs = new ArrayList<Entity>();
		inorder = new HashTable<CoordKey, Entity>();
	}

	/**
	 * Renders all entities added to the current level
	 * 
	 * @param g - The graphics object used to render the game
	 */
	public void render(DrawGraphics g) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			entities.get(i).render(g);
		}
	}

	/**
	 * renders entities on the list in order based on their positions
	 * 
	 * @param x - the current x position to check
	 * @param y - the current y position to check
	 * @param g - the graphics object of the screen
	 */
	public void renderInOrder(int x, int y, DrawGraphics g) {
		synchronized (inorder) {
			CoordKey key = new CoordKey(x, y);
			if (inorder.hasBucket(key)) {
				ArrayList<Entity> list = inorder.getAll(key);
				for (int i = 0; i < list.size(); i++)
					list.get(i).render(g);
			} else {
				Entity e = inorder.get(key);
				if (e != null)
					e.render(g);
			}
		}
	}

	/**
	 * 
	 */
	public void renderDevMode(DrawGraphics g) {
		Entity e;
		for (int i = entities.size() - 1; i >= 0; i--) {
			e = entities.get(i);
			if (e.hasHitbox())
				e.getHitbox().render(g);
			if (e.hasVector())
				e.getVector().render(g);
		}
	}

	/**
	 * Updates all entities in added to the current level, then removes any entities
	 * added to the remove list
	 */
	public void update() {
		inorder.clear();
		synchronized (inorder) {
			for (int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.update();
				inorder.add(new CoordKey(e.getX(), e.getY()), e);
			}
		}
		for (Entity e : remove)
			entities.remove(e);
		remove.clear();
	}

	// Array interaction

	/**
	 * Adds an entity to the list of entities in the current level
	 * 
	 * @param e - the entity to be added to the list
	 */
	public void addEntity(Entity e) {
		entities.add(e);
		if (e.isMob())
			mobs.add(e);
	}

	/**
	 * Gets an entity at a certain index of the entity array
	 * 
	 * @param index - the index of the entity to return
	 * @return the entity at the specified index
	 */
	public Entity getEntity(int index) {
		return entities.get(index);
	}

	/**
	 * Gets an mob at a certain index of the mob array
	 * 
	 * @param index - the index of the mob to return
	 * @return the mob at the specified index
	 */
	public Entity getMob(int index) {
		return mobs.get(index);
	}

	/**
	 * removes an entity from the list of entities in the level
	 * 
	 * @param index - the index of the entity to be removed
	 */
	public void removeEntity(int index) {
		remove.add(entities.get(index));
	}

	/**
	 * removes an mob from the list of mobs in the level
	 * 
	 * @param index - the index of the mob to be removed
	 */
	public void removeMob(int index) {
		mobs.remove(index);
	}

	/**
	 * removes an entity from the list of entities in the level
	 * 
	 * @param index - the entity to be removed
	 */
	public void removeEntity(Entity e) {
		remove.add(e);
		if (e.isMob())
			mobs.remove(e);
	}

	/**
	 * removes all mobs and entities from the level
	 */
	public void flushEntities() {
		entities.clear();
		mobs.clear();
	}

	/**
	 * gets the total number of entities in the level
	 * 
	 * @return the total number of entities in the level
	 */
	public int totalEntities() {
		return entities.size();
	}

	/**
	 * gets the total number of mobs in the level
	 * 
	 * @return the total number of mobs in the level
	 */
	public int totalMobs() {
		return mobs.size();
	}

	/**
	 * saves all entities to a specified world file
	 * 
	 * @param path - the path to the specified world file
	 */
	public void saveAllEntities(String path) {
		LoadingScreen ls = new LoadingScreen(1);
		ls.displayText("Saving...");
		try {
			File f = new File(path);
			f.delete();
			f.createNewFile();
			FileOutputStream fo = new FileOutputStream(f);
			ObjectOutputStream stream = new ObjectOutputStream(fo);
			stream.writeObject(entities);
			stream.close();
			fo.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ls.increment(1);
		ls.close();

	}

	/**
	 * Loads all entities into the world from the specified path
	 * 
	 * @param path - string path to the world data file
	 */
	@SuppressWarnings("unchecked")
	public void loadAllEntities(String path, Handler h) {
		LoadingScreen ls = new LoadingScreen(2);
		ls.displayText("Loading...");
		try {
			File f = new File(path);
			FileInputStream fo = new FileInputStream(f);
			ObjectInputStream stream = new ObjectInputStream(fo);
			entities = (ArrayList<Entity>) stream.readObject();
			stream.close();
			fo.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ls.increment(1);

		for (Entity e : entities) {
			// e.load(h);
			// if (e instanceof Mob)
			// mobs.add((Mob) e);
		}
		ls.increment(1);
		ls.close();
	}

	/**
	 * @returns a list of all active entities
	 */
	public ArrayList<Entity> getEntities() {
		return entities;
	}

	/**
	 * @returns a list of all active mobs
	 */
	public ArrayList<Entity> getMobs() {
		return mobs;
	}

}
