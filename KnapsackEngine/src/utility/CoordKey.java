package utility;

/**
 * A value intended for use as a key in various data storage types which
 * calculates both hashcodes and equality based on a set x, y or x, y z
 * coordinate
 * 
 * @author Pascal
 *
 */
public class CoordKey {
	int x, y, z;

	/**
	 * initializes a CoordKey using the given x, y and z coordinates
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public CoordKey(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * initializes a CoordKey using the given x and y coordinates. z will be set to
	 * 0;
	 * 
	 * @param x
	 * @param y
	 */
	public CoordKey(int x, int y) {
		this.x = x;
		this.y = y;
		z = 1;
	}

	/**
	 * @returns the hashcode of this CoordKey
	 *          
	 * @Override
	 */
	public int hashCode() {
		int hash = 0;
		hash = (x >> 8) ^ y + (y >> 8) ^ z + (z >> 8) ^ x;
		hash = (hash | 0x00005875) ^ (x + y + z);
		hash = (hash >> 8) ^ hash * 1000111627;
		return hash;
	}

	/**
	 * calculates equality between this CoordKey and another object. If the second
	 * object is also a coordkey, uses it's x, y and z coordinates instead of its
	 * object value
	 * @returns true if equal, false otherwise
	 * @Override
	 */
	public boolean equals(Object obj) {
		if (obj instanceof CoordKey) {
			CoordKey c = (CoordKey) obj;
			return x == c.x && y == c.y && z == c.z;
		}
		return super.equals(obj);
	}
	
	public void update(int x, int y, int z ) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
