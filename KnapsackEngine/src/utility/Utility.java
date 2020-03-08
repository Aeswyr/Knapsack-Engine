package utility;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

/**
 * Contains a variety of useful methods
 * 
 * @author Pascal
 *
 */
public class Utility {

	/**
	 * checks if two arrays contain the same value
	 * 
	 * @param <T> - array type
	 * @param a   - list a
	 * @param b   - list b
	 * @returns true if there are matching elements in the arrays, false otherwise
	 */
	public static <T> boolean listOverlaps(T[] a, T[] b) {
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < b.length; j++) {
				if (a[i].equals(b[j]))
					return true;
			}
		}
		return false;
	}

	/**
	 * checks if a string of tags contains values which match
	 * 
	 * @param a - string of tags 1
	 * @param b - string of tags 2
	 * @returns true if there are matching elements in the strings, false otherwise
	 */
	public static boolean tagOverlaps(String a, String b) {
		String[] listA = a.split(" ");
		String[] listB = b.split(" ");

		for (int i = 0; i < listA.length; i++) {
			for (int j = 0; j < listB.length; j++) {
				if (listA[i].equals(listB[j]))
					return true;
			}
		}
		return false;
	}

	/**
	 * replaces a specified line within a text document
	 * 
	 * @param input   - new value of the line
	 * @param lineNum - line number to replace
	 * @param path    - path to the text file which is edited
	 * @returns true if the change is successful, false otherwise
	 */
	public static boolean editText(String input, int lineNum, String path) {
		try {
			// input the (modified) file content to the StringBuffer "input"
			BufferedReader file = Loader.loadTextFromFile(path, StandardCharsets.UTF_8);
			StringBuffer inputBuffer = new StringBuffer();
			String line;

			int count = 0;
			while ((line = file.readLine()) != null) {
				if (count == lineNum)
					line = input;
				inputBuffer.append(line);
				inputBuffer.append('\n');
				count++;
			}
			file.close();

			// write the new string with the replaced line OVER the same file
			OutputStreamWriter fileOut = new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8);
			fileOut.write(inputBuffer.toString());
			fileOut.close();

		} catch (Exception e) {
			System.out.println("Problem reading file.");
			return false;
		}

		return true;
	}

	/**
	 * checks the sign of the input value
	 * 
	 * @param a - value to check
	 * @returns sign of the input value, 1 for positive, -1 for negative and 0 for
	 *          0;
	 */
	public static int getSign(double a) {
		if (a == 0)
			return 0;
		return (int) (a / Math.abs(a));
	}

	/**
	 * returns the radian theta associated with the sine between the width and
	 * height of a right triangle
	 * 
	 * @param w - width of the triangle
	 * @param h - height of the triangle
	 * @returns the radian theta associated with the triangle
	 */
	public static double sin(double w, double h) {
		return h * h / (w * w + h * h);
	}

	/**
	 * returns the radian theta associated with the cosine between the width and
	 * height of a right triangle
	 * 
	 * @param w - width of the triangle
	 * @param h - height of the triangle
	 * @returns the radian theta associated with the triangle
	 */
	public static double cos(double w, double h) {
		return w * w / (w * w + h * h);
	}

	/**
	 * calculates the dot product between two 1d matrices a and b
	 * 
	 * @returns the scalar value which represents the dot product of the two
	 *          matrices
	 */
	public static double dot(double[] a, double[] b) {
		double val = 0;
		for (int i = 0; i < a.length; i++) {
			val += a[i] * b[i];
		}
		return val;
	}

	/**
	 * calculates the dot product between two 1d matrices a and b
	 * 
	 * @returns the scalar value which represents the dot product of the two
	 *          matrices
	 */
	public static int dot(int[] a, int[] b) {
		int val = 0;
		for (int i = 0; i < a.length; i++) {
			val += a[i] * b[i];
		}
		return val;
	}

	/**
	 * calculates the magnitude (length) of a vector represented by the input matrix
	 * 
	 * @param a - a 1d matrix representing a vector
	 * @returns the scalar value corresponding to the magnitude of the vector
	 */
	public static double len(int[] a) {
		int val = 0;
		for (int i = 0; i < a.length; i++) {
			val += a[i] * a[i];
		}
		return Math.sqrt(val);
	}

	/**
	 * calculates the magnitude (length) of a vector represented by the input matrix
	 * 
	 * @param a - a 1d matrix representing a vector
	 * @returns the scalar value corresponding to the magnitude of the vector
	 */
	public static double len(double[] a) {
		int val = 0;
		for (int i = 0; i < a.length; i++) {
			val += a[i] * a[i];
		}
		return Math.sqrt(val);
	}

	/**
	 * calculates the normal of the vector represented by the input points
	 * 
	 * @param a - an array representing an x, y point pair, with index 0
	 *          corresponding to x, and 1 corresponding to y
	 * @param b - an array representing an x, y point pair, with index 0
	 *          corresponding to x, and 1 corresponding to y
	 * @returns an array representation of the normal vector
	 */
	public static int[] norm2d(int[] a, int[] b) {
		int[] c = { a[1] - b[1], a[0] - b[0] };
		return c;
	}

	/**
	 * calculates the normal vector to the input vector
	 * 
	 * @param a - a 1d array with 2 elements, representing a 2d vector
	 * @returns an array representation of the normal vector
	 */
	public static int[] norm2d(int[] a) {
		int[] c = { a[1], a[0] };
		return c;
	}

	/**
	 * multiplies the input matrix by an input rotational or other transformational
	 * matrix
	 * 
	 * @param a - a 1d matrix corresponding to a vector to be transformed
	 * @param b - a 1d matrix with four elements representing a transformational
	 *          matrix
	 * @returns a new 1d vector array with the rotated values
	 */
	public static double[] linearMatrixTransform2d(double[] a, double[] b) {
		double[] c = { a[0] * b[0] + a[1] * b[1], a[0] * b[2] + a[1] * b[3] };
		return c;
	}

	/**
	 * multiplies the input matrix by an input rotational or other transformational
	 * matrix
	 * 
	 * @param a - a 1d matrix corresponding to a vector to be transformed
	 * @param b - a 1d matrix with four elements representing a transformational
	 *          matrix
	 * @returns a new 1d vector array with the rotated values
	 */
	public static double[] linearMatrixTransform2d(int[] a, double[] b) {
		double[] c = { a[0] * b[0] + a[1] * b[1], a[0] * b[2] + a[1] * b[3] };
		return c;
	}
}
