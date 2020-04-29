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
import java.util.List;

import gfx.DrawGraphics;
import runtime.Handler;

public class EntityManager {

	private ArrayList<Entity_KS> entities;
	private ArrayList<Entity_KS> remove;
	private ArrayList<Entity_KS> mobs;
	private HashTable<CoordKey, Entity_KS> proximityHash;

	/**
	 * Initializes an entityManager which handles rendering and updating entities
	 */
	public EntityManager() {
		entities = new ArrayList<Entity_KS>();
		remove = new ArrayList<Entity_KS>();
		mobs = new ArrayList<Entity_KS>();
		proximityHash = new HashTable<CoordKey, Entity_KS>(100, 0.7);
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
	 * 
	 */
	public void renderDevMode(DrawGraphics g) {
		Entity_KS e;
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
		proximityHash.clear();
		for (int i = 0; i < entities.size(); i++) {
			Entity_KS e = entities.get(i);
			proximityHash.add(e.key, e);
		}

		for (int i = 0; i < entities.size(); i++) {
			Entity_KS e = entities.get(i);
			e.tick();
			e.update();
			

		}

		for (Entity_KS e : remove)
			entities.remove(e);
		remove.clear();
	}

	// Array interaction

	/**
	 * Adds an entity to the list of entities in the current level
	 * 
	 * @param e - the entity to be added to the list
	 */
	public void addEntity(Entity_KS e) {
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
	public Entity_KS getEntity(int index) {
		return entities.get(index);
	}

	/**
	 * Gets the entity which possesses the input ID
	 * 
	 * @param id - id of the entity desired
	 * @returns the entity possessing the input id or null if none is found
	 */
	public Entity_KS getEntityID(int id) {
		for (int i = 0; i < entities.size(); i++)
			if (entities.get(i).ID == id)
				return entities.get(i);
		return null;
	}

	/**
	 * Gets an mob at a certain index of the mob array
	 * 
	 * @param index - the index of the mob to return
	 * @return the mob at the specified index
	 */
	public Entity_KS getMob(int index) {
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
	public void removeEntity(Entity_KS e) {
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
			entities = (ArrayList<Entity_KS>) stream.readObject();
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

		for (Entity_KS e : entities) {
			 e.load();
			 if (e.isMob())
			 mobs.add(e);
		}
		ls.increment(1);
		ls.close();
	}

	/**
	 * @returns a list of all active entities
	 */
	public ArrayList<Entity_KS> getEntities() {
		return entities;
	}

	/**
	 * @returns a list of all active mobs
	 */
	public ArrayList<Entity_KS> getMobs() {
		return mobs;
	}

	/**
	 * @returns a list of entities within the same hash bucket
	 */
	public List<Entity_KS> getProximityEntities(int x, int y) {
		ArrayList<Entity_KS> get = new ArrayList<Entity_KS>();
		get.addAll(proximityHash.getBucket(new CoordKey(x / Hitbox_KS.BOXCHECK, y / Hitbox_KS.BOXCHECK)));
		return get;

	}

	/**
	 * @returns a list of entities within the same hash bucket and in buckets out to
	 *          a radius
	 */
	public List<Entity_KS> getProximityEntities(int x, int y, int rad) {
		ArrayList<Entity_KS> get = new ArrayList<Entity_KS>();
		for (int i = -rad / 2; i < rad / 2; i++) {
			for (int j = -rad / 2; j < rad / 2; j++) {
				get.addAll(proximityHash.getBucket(new CoordKey(x / Hitbox_KS.BOXCHECK + i, y / Hitbox_KS.BOXCHECK + j)));
			}
		}

		return get;
	}

}
