import java.io.Serializable;
/**
 * This class represents a 2D map (int[w][h]) as a "screen" or a raster matrix or maze over integers.
 * This is the main class needed to be implemented.
 *
 * @author boaz.benmoshe
 *
 */
public class Map implements Map2D, Serializable{
    private int width;
    private int height;
    private int[][] cells;


    private boolean inBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
    private void checkBounds(int x, int y) {
        if (!inBounds(x, y)) {
            throw new IndexOutOfBoundsException("Out of bounds: (" + x + "," + y + ") for " + width + "x" + height);
        }
    }


    // edit this class below
	/**
	 * Constructs a w*h 2D raster map with an init value v.
	 * @param w
	 * @param h
	 * @param v
	 */
	public Map(int w, int h, int v) {init(w, h, v);}
	/**
	 * Constructs a square map (size*size).
	 * @param size
	 */
	public Map(int size) {this(size,size, 0);}
	
	/**
	 * Constructs a map from a given 2D array.
	 * @param data
	 */
	public Map(int[][] data) {
		init(data);
	}
	@Override
	public void init(int w, int h, int v) {
        v = 0;
        if (w <= 0 || h <= 0) {
            throw new IllegalArgumentException("width and height must be > 0; got w=" + w + ", h=" + h);
        }
        this.width = w;
        this.height = h;
        this.cells = new int[h][w];
        fill(v);
	}
	@Override
	public void init(int[][] arr) {
        if (arr == null || arr.length == 0 || arr[0] == null)
            throw new IllegalArgumentException("input array must be non-null and rectangular");
        int h = arr.length;
        int w = arr[0].length;
        for (int y = 1; y < h; y++) {
            if (arr[y] == null || arr[y].length != w)
                throw new IllegalArgumentException("input array must be rectangular");
        }
        this.width = w;
        this.height = h;
        this.cells = new int[h][w];
        for (int y = 0; y < h; y++) {
            System.arraycopy(arr[y], 0, this.cells[y], 0, w);
        }
    }
	@Override
	public int[][] getMap() {
		int[][] ans = null;
        ans = new int[height][width];
        for (int y = 0; y < height; y++) {
            System.arraycopy(cells[y], 0, ans[y], 0, width);
        }
        return ans;
	}
	@Override
	public int getWidth() {
        return width;
    }
	@Override
	public int getHeight() {
        return height;
    }
	@Override
	public int getPixel(int x, int y) {
        checkBounds(x, y);
        return cells[y][x];
    }
	@Override
	public int getPixel(Pixel2D p) {
        return getPixel(p.getX(), p.getY());
	}
	@Override
	public void setPixel(int x, int y, int v) {
        checkBounds(x, y);
        cells[y][x] = v;
    }
	@Override
	public void setPixel(Pixel2D p, int v) {
        setPixel(p.getX(), p.getY(), v);
	}

    @Override
    public boolean isInside(Pixel2D p) {
        int x = p.getX(), y = p.getY();
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    @Override
    public boolean sameDimensions(Map2D p) {
        return p != null && p.getWidth() == width && p.getHeight() == height;
    }

    @Override
    public void addMap2D(Map2D p) {
        if (!sameDimensions(p))
            throw new IllegalArgumentException("dimension mismatch: this=" + width + "x" + height +
                    ", other=" + p.getWidth() + "x" + p.getHeight());
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                cells[y][x] += p.getPixel(x, y);
            }
        }
    }

    @Override
    public void mul(double scalar) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                cells[y][x] = (int) Math.round(cells[y][x] * scalar);
            }
        }
    }


    @Override
    public void rescale(double sx, double sy) {
        if (sx <= 0 || sy <= 0) throw new IllegalArgumentException("scale factors must be > 0");
        int newW = Math.max(1, (int) Math.round(width * sx));
        int newH = Math.max(1, (int) Math.round(height * sy));
        int[][] out = new int[newH][newW];

        // Nearest-neighbor resampling
        for (int ny = 0; ny < newH; ny++) {
            for (int nx = 0; nx < newW; nx++) {
                int ox = Math.min(width - 1, (int) Math.round(nx / sx));
                int oy = Math.min(height - 1, (int) Math.round(ny / sy));
                out[ny][nx] = cells[oy][ox];
            }
        }
        this.width = newW;
        this.height = newH;
        this.cells = out;
    }

    @Override
    public void drawCircle(Pixel2D center, double rad, int color) {

    }

    @Override
    public void drawLine(Pixel2D p1, Pixel2D p2, int color) {

    }

    @Override
    public void drawRect(Pixel2D p1, Pixel2D p2, int color) {

    }

    @Override
    public boolean equals(Object ob) {
        boolean ans = false;

        return ans;
    }
	@Override
	/** 
	 * Fills this map with the new color (new_v) starting from p.
	 * https://en.wikipedia.org/wiki/Flood_fill
	 */
	public int fill(Pixel2D xy, int new_v,  boolean cyclic) {
		int ans = -1;

		return ans;
	}

	@Override
	/**
	 * BFS like shortest the computation based on iterative raster implementation of BFS, see:
	 * https://en.wikipedia.org/wiki/Breadth-first_search
	 */
	public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor, boolean cyclic) {
		Pixel2D[] ans = null;  // the result.

		return ans;
	}
    @Override
    public Map2D allDistance(Pixel2D start, int obsColor, boolean cyclic) {
        Map2D ans = null;  // the result.

        return ans;
    }
	////////////////////// Private Methods ///////////////////////

}
