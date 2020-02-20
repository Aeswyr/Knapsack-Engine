package utility;

import java.util.Random;

/**
 * 
 * creates an object which can generate perlin noise as well as voronoi tesselation
 * 
 * @author Pascal
 *
 */
public class NoiseGenerator {

	private int[] permutation = { 151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36, 103,
			30, 69, 142, 8, 99, 37, 240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62, 94, 252, 219, 203,
			117, 35, 11, 32, 57, 177, 33, 88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134,
			139, 48, 27, 166, 77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55, 46, 245,
			40, 244, 102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200, 196, 135,
			130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250, 124, 123, 5, 202, 38, 147,
			118, 126, 255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42, 223, 183, 170, 213, 119,
			248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9, 129, 22, 39, 253, 19, 98, 108, 110, 79,
			113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228, 251, 34, 242, 193, 238, 210, 144, 12, 191, 179, 162,
			241, 81, 51, 145, 235, 249, 14, 239, 107, 49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204, 176, 115, 121,
			50, 45, 127, 4, 150, 254, 138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61,
			156, 180 };

	private int[] p;

	int[][] w;
	int points;
	Random rng;

	int repeat = 0;

	/**
	 * initializes a NoiseGenerator with the default values. Will always generate
	 * the same map
	 */
	public NoiseGenerator() {
		p = new int[512];
		for (int x = 0; x < 512; x++) {
			p[x] = permutation[x % 256];
		}
		rng = new Random(1L);
	}

	/**
	 * initializes a noise generator which calculates random values using a given
	 * Random
	 * 
	 * @param rng - the random used to calculate values
	 */
	public NoiseGenerator(Random rng) {
		for (int i = 0; i < permutation.length; i++) {
			permutation[i] = rng.nextInt(256);
		}
		p = new int[512];
		for (int x = 0; x < 512; x++) {
			p[x] = permutation[x % 256];
		}
		this.rng = rng;
	}

	/**
	 * generates noise in octaves, each level getting progressively stronger
	 * 
	 * @param x           - the x position of the noise point
	 * @param y           - the y position of the noise point
	 * @param octaves     - the number of octaves to generate
	 * @param persistence - how much the hoise value should increase by
	 * @returns a noise value for the specified x, y
	 */
	public double octaveNoise(double x, double y, int octaves, double persistence) {
		double total = 0;
		double frequency = 1;
		double amplitude = 1;
		double maxValue = 0; // Used for normalizing result to 0.0 - 1.0
		for (int i = 0; i < octaves; i++) {
			total += noise(x * frequency, y * frequency) * amplitude;

			maxValue += amplitude;

			amplitude *= persistence;
			frequency *= 2;
		}

		return total / maxValue;
	}

	/**
	 * generates fractal noise by layering several different noise levels onto each
	 * other at different scales
	 * 
	 * @param x           - the x position of the noise point
	 * @param y           - the y position of the noise point
	 * @param layers      - the number of layers the fractal should generate
	 *                    (recommended 8)
	 * @param startingMod - the starting value for modifying the scale. This should
	 *                    start 1:1 with the point scale
	 * @param persistence - the intensity of the noise, increasing at each layer
	 * @returns a noise value for the specified x, y
	 */
	public double fractalNoise(double x, double y, int layers, double startingMod, double persistence) {
		double mod = startingMod;
		double amp = 0;

		for (int i = 0; i < layers; i++) {

			amp += noise(x * mod, y * mod) * Math.pow(persistence, i);
			mod /= 2;
		}

		return amp / (Math.pow(persistence, layers));
	}

	private double lerp(double a, double b, double x) {
		return a + x * (b - a);
	}

	// Computes the dot product of the distance and gradient vectors.
	private double grad(int hash, double x, double y) {
		switch (hash & 0x6) {
		case 0x0:
			return x + y;
		case 0x1:
			return -x + y;
		case 0x2:
			return x - y;
		case 0x3:
			return -x - y;
		case 0x4:
			return y + x;
		case 0x5:
			return y - x;
		default:
			return 0; // never happens
		}
	}

	/**
	 * Generates perlin noise at the specified point
	 * 
	 * @param x - the x position of the noise point
	 * @param y - the y position of the noise point
	 * @returns a noise value for the specified x, y
	 */
	public double noise(double x, double y) {
		if (repeat > 0) { // If we have any repeat on, change the coordinates to their "local" repetitions
			x = x % repeat;
			y = y % repeat;
		}

		int xi = (int) x & 255;
		int yi = (int) y & 255;
		double xf = x - (int) x;
		double yf = y - (int) y;

		double u = fade(xf);
		double v = fade(yf);

		int aaa, aba, aab, abb, baa, bba, bab, bbb;
		aaa = p[p[xi] + yi];
		aba = p[p[xi] + inc(yi)];
		aab = p[p[xi] + yi];
		abb = p[p[xi] + inc(yi)];
		baa = p[p[inc(xi)] + yi];
		bba = p[p[inc(xi)] + inc(yi)];
		bab = p[p[inc(xi)] + yi];
		bbb = p[p[inc(xi)] + inc(yi)];

		double x1, x2, y1, y2;
		x1 = lerp(grad(aaa, xf, yf), grad(baa, xf - 1, yf), u);
		x2 = lerp(grad(aba, xf, yf - 1), grad(bba, xf - 1, yf - 1), u);
		y1 = lerp(x1, x2, v);

		x1 = lerp(grad(aab, xf, yf), grad(bab, xf - 1, yf), u);
		x2 = lerp(grad(abb, xf, yf - 1), grad(bbb, xf - 1, yf - 1), u);
		y2 = lerp(x1, x2, v);

		return (lerp(y1, y2, 1) + 1) / 2; // TODO
	}

	/**
	 * Creates a noise pattern based on a voronoi tesselation by increasing the
	 * return value as the input x, y coordinate approaches the nearest cell center
	 * 
	 * @param x - x position
	 * @param y - y position
	 * @returns a value between 0 and 1 (inclusive) based on the input x, y
	 *          coordinate's distance from the nearest cell center
	 */
	public double tesselationNoise(int x, int y) {
		double lastDist = Math.sqrt(Math.pow(w[0][0] - x, 2) + Math.pow(w[0][1] - y, 2));
		for (int i = 1; i < points; i++) {
			double newDist = Math.sqrt(Math.pow(w[i][0] - x, 2) + Math.pow(w[i][1] - y, 2));
			if (newDist < lastDist) {
				lastDist = newDist;
			}
		}

		double value = 1.0 / lastDist;

		if (value > 1)
			return 1;
		return value;
	}

	/**
	 * Creates a voronoi tesselation based on preset points generated in the
	 * enableTesselation method. Returns a value associated with the point closest
	 * to the input x, y coordinate
	 * 
	 * @param x - x position
	 * @param y - y position
	 * @return the index of the cell the x, y position is located in.
	 */
	public int tesselation(int x, int y) {
		int close = 0;
		double lastDist = Math.sqrt(Math.pow(w[0][0] - x, 2) + Math.pow(w[0][1] - y, 2));
		for (int i = 1; i < points; i++) {
			double newDist = Math.sqrt(Math.pow(w[i][0] - x, 2) + Math.pow(w[i][1] - y, 2));
			if (newDist < lastDist) {
				close = i;
				lastDist = newDist;
			}

		}
		return close;
	}

	/**
	 * Retrieves the cell associated with the input index from the list of cells
	 * 
	 * @param index - the index of the desired cell
	 * @return the coordinates of the cell center
	 */
	public int[] tesselationCell(int index) {
		return w[index];
	}

	/**
	 * Generates noise but reduces its possible values to a set number of levels
	 * 
	 * @param x      - the x position of the noise point
	 * @param y      - the y position of the noise point
	 * @param scale  - size of the noise
	 * @param levels - number of levels to generate
	 * @return noise rounded to the nearest level
	 */
	public double leveledNoise(double x, double y, int scale, int levels) {
		return Math.floor(noise(x / scale, y / scale) * levels) / levels;
	}

	private int inc(int num) {
		num++;
		if (repeat > 0)
			num %= repeat;

		return num;
	}

	private static double fade(double t) {
		// Fade function as defined by Ken Perlin. This eases coordinate values
		// so that they will ease towards integral values. This ends up smoothing
		// the final output.
		return t * t * t * (t * (t * 6 - 15) + 10); // 6t^5 - 15t^4 + 10t^3
	}

	/**
	 * Enables the use of tesselation methods by initializing an array of points
	 * 
	 * @param size   - the size of the area covered by the tesselation
	 * @param points - the number of points to generate
	 */
	public void enableTesselation(int size, int points) {
		this.points = points;
		w = new int[points][2];
		for (int i = 0; i < points; i++) {
			w[i][0] = (int) (size * rng.nextDouble());
			w[i][1] = (int) (size * rng.nextDouble());
		}
	}

}
