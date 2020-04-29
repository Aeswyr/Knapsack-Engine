package entity;

public class HitSquare_KS extends Hitbox_KS {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6709194723348757692L;
	int w, h;

	public HitSquare_KS(int xOffset, int yOffset, int width, int height, Entity_KS e) {
		super(xOffset, yOffset, new int[][] { { 0, 0 }, { width, 0 }, { width, height }, { 0, height } }, e);
		w = width;
		h = height;
	}

	public HitSquare_KS(int xOffset, int yOffset, int[][] points, Entity_KS e) {
		super(xOffset, yOffset, points, e);
		w = points[2][0];
		h = points[2][1];
	}

	public HitSquare_KS(int[][] points) {
		super(points);
		w = points[2][0];
		h = points[2][1];
	}

	public HitSquare_KS(int width, int height) {
		super(new int[][] { { 0, 0 }, { width, 0 }, { width, height }, { 0, height } });
		w = width;
		h = height;
	}

	@Override
	protected boolean checkCol(Hitbox_KS hi) {
		if (hi instanceof HitSquare_KS) {
			HitSquare_KS q = (HitSquare_KS) hi;
			return !(x > q.x + q.w || x + w < q.x || y > q.y + q.h || y + h < q.y);
		} else
			return super.checkCol(hi);
	}
}
