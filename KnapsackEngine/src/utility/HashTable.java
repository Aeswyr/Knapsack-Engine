package utility;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A hash table implementation
 * 
 * @author Pascal
 *
 * @param <K>
 * @param <T>
 */
public class HashTable<K, T> {

	int size, count;
	double threshold, max;
	Node[] list;
	boolean resizable = true;

	/**
	 * Initialize a hashtable with a starting size of 10 and a base threshold of 0.8
	 */
	@SuppressWarnings("unchecked")
	public HashTable() {
		size = 10;
		max = 0.7;
		list = (Node[]) Array.newInstance(Node.class, size);
	}

	/**
	 * Initialize a hash table with a customized maximum and threshold
	 * 
	 * @param size      - the maximum number of items within the table
	 * @param threshold - the maximum threshold of the table before it resizes
	 */
	@SuppressWarnings("unchecked")
	public HashTable(int size, double threshold) {
		this.size = size;
		max = threshold;
		list = (Node[]) Array.newInstance(Node.class, size);
	}

	/**
	 * Adds a value key pair into the hash table
	 * 
	 * @param k - the key
	 * @param t - the stored value
	 */
	public void add(K k, T t) {
		int location = k.hashCode() % size;

		if (list[location] == null)
			list[location] = new Node(k, t);
		else {
			Node find = list[location];
			while (find.next != null) {
				find = find.next;
			}
			find.next = new Node(k, t);
		}
		count++;
		threshold = 1.0 * count / size;
		if (resizable && threshold >= max)
			resize();

	}

	/**
	 * removes a value-key pair from the hash table
	 * 
	 * @param key - the key of the pair to remove
	 */
	public void remove(K key) {
		int location = key.hashCode() % size;
		Node find = list[location];
		Node prev = list[location];
		while (!find.key.equals(key)) {
			prev = find;
			find = find.next;
		}
		if (find.equals(prev)) {
			list[location] = null;
		} else {
			prev.next = find.next;
		}
		count--;
	}

	/**
	 * retrieves the value of a pair stored in the hash table
	 * 
	 * @param key - the key associated with this pair
	 * @return the value bound to the key
	 */
	public T get(K key) {
		int location = key.hashCode() % size;
		Node find = list[location];
		if (find == null)
			return null;
		while (!find.key.equals(key)) {
			find = find.next;
			if (find == null)
				return null;
		}
		return find.stored;
	}
	
	public List<T> getBucket(K key) {
		ArrayList<T> get = new ArrayList<T>();
		
		int location = key.hashCode() % size;
		Node find = list[location];
		while (find != null) {
			get.add(find.stored);
			find = find.next;
		}
		
		return get;
	}

	public ArrayList<T> getAll(K key) {
		ArrayList<T> hold = new ArrayList<T>();
		int location = key.hashCode() % size;
		Node find = list[location];
		while (find != null) {
			hold.add(find.stored);
			find = find.next;
		}
		return hold;
	}

	public boolean hasBucket(K key) {
		Node find = list[key.hashCode() % size];
		if (find == null || find.next == null)
			return false;
		return true;
	}

	/**
	 * sets if the table can be resized. If true, the table will automatically
	 * resize when its threshold hits the maximum. If false, the table will not
	 * resize and items will continue to be added to the table.
	 * 
	 * @param b - true if resizable, false otherwise
	 */
	public void setResizable(boolean b) {
		this.resizable = b;
	}

	public void clear() {
		Arrays.fill(list, null);
	}

	/**
	 * resizes a table, setting its size to twice the current value plus one then
	 * rehashing all the items in the old table
	 */
	@SuppressWarnings("unchecked")
	private void resize() {
		size = size * 2 + 1;
		count = 0;
		Node[] store = list;
		list = (Node[]) Array.newInstance(Node.class, size);
		for (int i = 0; i < store.length; i++) {
			Node find = store[i];
			while (find != null) {
				add(find.key, find.stored);
				find = find.next;
			}
		}

	}

	/**
	 * a node for the linked list buckets
	 * @author pmkgb
	 *
	 */
	private class Node {
		K key;
		T stored;
		Node next;

		private Node(K key, T stored) {
			this.key = key;
			this.stored = stored;
			this.next = null;
		}
	}
}
