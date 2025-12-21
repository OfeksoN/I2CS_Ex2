import java.io.Serializable;
import java.util.ArrayDeque;

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
    private int amount;


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
        if (w <= 0 || h <= 0) {
            throw new IllegalArgumentException("width and height must be > 0; got w=" + w + ", h=" + h);
        }
        this.width = w;
        this.height = h;
        this.cells = new int[h][w];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                cells[y][x] = v;
            }
        }
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
    int cx = center.getX();
    int cy = center.getY();
    int r = (int) Math.round(rad);
    if (r < 0) {
        throw new IllegalArgumentException("Radius must be >= 0");
    }
    if (r == 0){
        setPixel(cx,cy,color);
    }
    if (r > 0){
        int y = 0;
        int x = r;
        int err = 1-x;
        while (x>=y){
            setPixel(cx+x,cy+y,color);
            setPixel(cx+y,cy+x,color);
            setPixel(cx-y,cy+x,color);
            setPixel(cx-y,cy-x,color);
            setPixel(cx-x,cy+y,color);
            setPixel(cx-x,cy-y,color);
            setPixel(cx+y,cy-x,color);
            setPixel(cx+x,cy-y,color);
            y++;
            if (err<0){
                err += ((2*y) + 1);
            }
            else {
                x--;
                err += ((2*(y-x)) + 1);
            }
        }
    }
    }

    @Override
    public void drawLine(Pixel2D p1, Pixel2D p2, int color) {
        int x0 = p1.getX(), y0 = p1.getY();
        int x1 = p2.getX(), y1 = p2.getY();
        // Bresenham's line algorithm
        int dx = Math.abs(x1 - x0), sx = x0 < x1 ? 1 : -1;
        int dy = -Math.abs(y1 - y0), sy = y0 < y1 ? 1 : -1;
        int err = dx + dy;
        while (true) {
            setPixel(x0, y0, color);
            if (x0 == x1 && y0 == y1) break;
            int e2 = 2 * err;
            if (e2 >= dy) { err += dy; x0 += sx; }
            if (e2 <= dx) { err += dx; y0 += sy; }
        }
    }

    @Override
    public void drawRect(Pixel2D p1, Pixel2D p2, int color) {
        if (p1 == p2){
            setPixel(p1.getX(), p1.getY(), color);
        }
        else{
        int x1 = Math.min(p1.getX(), p2.getX());
        int y1 = Math.min(p1.getY(), p2.getY());
        int x2 = Math.max(p1.getX(), p2.getX());
        int y2 = Math.max(p1.getY(), p2.getY());
            for (int x = x1; x <=x2 ; x++){
                setPixel(x,y1,color);
                setPixel(x,y2,color);
    }
            for (int y = y1; y <=y2 ; y++) {
                setPixel(x1, y, color);
                setPixel(x2, y, color);
            }
        }
    }

    @Override
    public boolean equals(Object ob) {
        boolean ans = false;
        if (this == ob) {
            ans = true;
        }
        if (!(ob instanceof Map2D)) {
            ans = false;
        }
        Map2D other = (Map2D) ob;
        if (other.getWidth() != width || other.getHeight() != height)
            ans = false;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (cells[y][x] != other.getPixel(x, y)) ;
                ans = false;
            }
        }
        return ans;
    }
	@Override
	/** 
	 * Fills this map with the new color (new_v) starting from p.
	 * https://en.wikipedia.org/wiki/Flood_fill
	 */
	public int fill(Pixel2D xy, int new_v, boolean cyclic) {
        int ans = 0;
		final int fx = xy.getX();
        final int fy = xy.getY();
        final int old = cells[fy][fx];
		if (fy < 0 || fy >= cells[0].length || fx < 0 || fx >= cells.length || old == new_v){
            return ans;
        }

        final int H = cells.length;
        final int W = cells[0].length;

        final boolean[][] visited = new boolean[H][W];
        final ArrayDeque<int[]> q = new ArrayDeque<>();

        visited[fy][fx] = true;
        q.add(new int[]{fx, fy});

        final int[][] ways = {{1,0},{-1,0},{0,1},{0,-1}};

        while (!q.isEmpty()) {
            int[] cur = q.removeFirst();
            int x = cur[0], y = cur[1];

            if (cells[y][x] == old) {
                cells[y][x] = new_v;
                ans++;
            }

            for (int[] d : ways) {
                int nx = x + d[0], ny = y + d[1];

                if (cyclic) {
                    nx = ((nx % W) + W) % W;
                    ny = ((ny % H) + H) % H;
                } else {
                    if (nx < 0 || nx >= W || ny < 0 || ny >= H) continue;
                }

                if (!visited[ny][nx] && cells[ny][nx] == old) {
                    visited[ny][nx] = true;
                    q.add(new int[]{nx, ny});
                }
            }
    }
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
    private boolean inBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
    private void checkBounds(int x, int y) {
        if (!inBounds(x, y)) {
            throw new IndexOutOfBoundsException("Out of bounds: (" + x + "," + y + ") for " + width + "x" + height);
        }
    }
    private void paint(int x, int y, int color){
        if (inBounds(x, y)){
            cells[y][x] = color;
        }
    }
    private boolean CircleContains(Pixel2D p,Pixel2D center, double r) {
        return r >= p.distance2D(center);
    }
    private int fillHelper(int cells[][], int x, int y,int new_v, int v){
        if (x<0 || y<0 || x>= cells.length || y>= cells[0].length || cells[x][y] != v){
            amount+=0;
        }
        else {
            cells[x][y] = new_v;

            fillHelper(cells, x + 1, y, new_v, v);
            fillHelper(cells, x - 1, y, new_v, v);
            fillHelper(cells, x, y + 1, new_v, v);
            fillHelper(cells, x, y - 1, new_v, v);
            amount += 1;
        }
        return amount;
    }
}
