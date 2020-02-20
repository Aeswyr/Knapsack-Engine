package utility;

public class BinaryListBool {

	int b;

	public BinaryListBool() {
		b = 0;
	}

	public BinaryListBool(boolean val) {
		if (val) {
			b = 0xffffffff;
		} else {
			b = 0;
		}
	}

	public boolean get(int index) {
		return ((b >>> index) & 0x00000001) == 1;
	}

	public void set(int index, boolean val) {
		int s = 0;
		if (val) {
			s = 0x00000001;
			b = b | (s << index);
		} else {
			s = 0xfffffffe;
			
			if (index > 0) {
				int p = 0xffffffff;
				s = (s << index) | (p >>> (32 - index));
			}

			b = b & s;
		}
	}

}
