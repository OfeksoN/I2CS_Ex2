import java.awt.*;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * This class represents a 2D map (int[w][h]) as a "screen" or a raster matrix or maze over integers.
 * This is the main class needed to be implemented.
 * @author boaz.benmoshe
 */
public class Map implements Map2D, Serializable {
    private int width;
    private int height;
    private int[][] cells;
    private int amount;
    private int DEFAULT = 0;

    /**
     * Constructs a w*h 2D raster map with an init value v.
     * @param w
     * @param h
     * @param v
     */
    public Map(int w, int h, int v) {
        init(w, h, v);
    }

    /**
     * Constructs a square map (size*size).
     * @param size
     */
    public Map(int size) {
        this(size, size, 0);
    }

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
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < width; x++) {
                cells[y][x] = v;
            }
        }
    }

    @Override

    public void init(int[][] arr) {
       if (arr == null || arr.length == 0 || arr[0] == null)
           throw new IllegalArgumentException("input array must be non-null and rectangular");
        int H = arr.length;
        int W = arr[0].length;
        for (int y = 1; y < H; y++) {
            if (arr[y] == null || arr[y].length != W)
                 throw new IllegalArgumentException("input array must be rectangular");
       }
        this.width = W;
        this.height = H;
        this.cells = new int[H][W];

//        for (int y = 0; y < H; y++) {
//            this.cells[y] = Arrays.copyOf(arr[y], H);
//        }
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                this.cells[y][x] = arr[y][x];
            }
        }
    }


    @Override

    public int[][] getMap() {
        int ans[][] = null;
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
    /**
     * Draws the outline of a circle centered at {@code center} with radius {@code rad}
     * on the grid using the specified color. Uses the midpoint (Bresenham) circle algorithm
     * with 8-way symmetry for efficient integer rasterization.
     *
     * Behavior:
     * - Rounds {@code rad} to the nearest integer.
     * - If r < 0, throws IllegalArgumentException.
     * - If r == 0, colors only the center pixel.
     * - If r > 0, plots the circle perimeter by setting symmetric points around the center.
     *
     * @param center the circle center (x,y).
     * @param rad    the desired radius (will be rounded to nearest integer).
     * @param color  the color value to apply to the circle’s perimeter.
     *
     * @implNote Integer-only midpoint circle algorithm; plots 8 symmetric points per step.
     * @complexity O(r) time; O(1) extra space.
     */
    public void drawCircle(Pixel2D center, double rad, int color) {
        int cx = center.getX();
        int cy = center.getY();
        int r = (int) Math.round(rad);
        if (r < 0) {
            throw new IllegalArgumentException("Radius must be >= 0");
        }
        if (r == 0) {
            setPixel(cx, cy, color);
        }
        if (r > 0) {
            int y = 0;
            int x = r;
            int err = 1 - x;
            while (x >= y) {
                setPixel(cx + x, cy + y, color);
                setPixel(cx + y, cy + x, color);
                setPixel(cx - y, cy + x, color);
                setPixel(cx - y, cy - x, color);
                setPixel(cx - x, cy + y, color);
                setPixel(cx - x, cy - y, color);
                setPixel(cx + y, cy - x, color);
                setPixel(cx + x, cy - y, color);
                y++;
                if (err < 0) {
                    err += ((2 * y) + 1);
                } else {
                    x--;
                    err += ((2 * (y - x)) + 1);
                }
            }
        }
    }

    @Override
    /**
     * Draws a straight line between two pixels (p1 and p2) on the grid using the specified color.
     * Implements Bresenham's line algorithm for accurate rasterization.
     *
     * Behavior:
     * - Colors all pixels along the shortest path between p1 and p2.
     * - Works for horizontal, vertical, and diagonal lines.
     *
     * @param p1    starting pixel of the line.
     * @param p2    ending pixel of the line.
     * @param color the color value to apply to the line.
     *
     * @implNote Uses Bresenham's algorithm for efficient integer-based line drawing.
     */
    public void drawLine(Pixel2D p1, Pixel2D p2, int color) {
        int x0 = p1.getX(), y0 = p1.getY();
        int x1 = p2.getX(), y1 = p2.getY();
        int dx = Math.abs(x1 - x0), sx = x0 < x1 ? 1 : -1;
        int dy = -Math.abs(y1 - y0), sy = y0 < y1 ? 1 : -1;
        int err = dx + dy;
        while (true) {
            setPixel(x0, y0, color);
            if (x0 == x1 && y0 == y1) break;
            int e2 = 2 * err;
            if (e2 >= dy) {
                err += dy;
                x0 += sx;
            }
            if (e2 <= dx) {
                err += dx;
                y0 += sy;
            }
        }
    }

    @Override
    /**
     * Draws the outline of a rectangle on the grid using the specified color.
     * The rectangle is defined by two corner pixels (p1 and p2), regardless of order.
     *
     * Behavior:
     * - If p1 == p2, sets that single pixel to the given color.
     * - Otherwise, computes the rectangle bounds and colors its perimeter:
     *   - Horizontal edges: from x1 to x2 at y1 and y2.
     *   - Vertical edges: from y1 to y2 at x1 and x2.
     *
     * @param p1    one corner of the rectangle.
     * @param p2    opposite corner of the rectangle.
     * @param color the color value to apply to the rectangle's border.
     */
    public void drawRect(Pixel2D p1, Pixel2D p2, int color) {
        if (p1 == p2) {
            setPixel(p1.getX(), p1.getY(), color);
        } else {
            int x1 = Math.min(p1.getX(), p2.getX());
            int y1 = Math.min(p1.getY(), p2.getY());
            int x2 = Math.max(p1.getX(), p2.getX());
            int y2 = Math.max(p1.getY(), p2.getY());
            for (int x = x1; x <= x2; x++) {
                setPixel(x, y1, color);
                setPixel(x, y2, color);
            }
            for (int y = y1; y <= y2; y++) {
                setPixel(x1, y, color);
                setPixel(x2, y, color);
            }
        }
    }

    @Override
    /**
     * Compares this Map object to another for equality.
     * Two maps are considered equal if:
     * - The other object is an instance of Map.
     * - Both maps have the same width and height.
     * - All corresponding cells contain the same values.
     *
     * @param ob the object to compare with this map.
     * @return true if the maps are equal in size and content; false otherwise.
     */
    public boolean equals(Object ob) {
        boolean ans = true;
        if(ob instanceof Map){
            Map m = (Map)ob;
            if(m.width == this.width && m.height == this.height) {
                for (int y = 0; y < this.height; y++) {
                    for (int x = 0; x < this.width; x++) {
                        if (this.cells[y][x] != m.cells[y][x]) {
                            return false;
                        }
                    }
                }
            }else{
                return  false;
            }
        }else{
            return false;
        }
        return ans;
    }

    @Override
    /**
     * Flood-fills a connected region (4-neighbor adjacency) starting at xy,
     * replacing all cells equal to the starting cell's value with new_v.
     * Supports optional cyclic wrap-around at grid edges.
     *
     * Behavior:
     * - Starting from (fx, fy), visits all reachable cells whose value equals
     *   the original value at (fx, fy) and sets them to new_v.
     * - Returns the number of cells changed.
     * - If (fx, fy) is out of bounds or the original value already equals new_v,
     *   no changes are made and 0 is returned.
     * - If cyclic == true, neighbor moves wrap across borders (torus);
     *   otherwise, out-of-bounds neighbors are ignored.
     * Implementation:
     * - BFS with a queue and a visited[][] mask over 4 directions: {±1,0}, {0,±1}.
     * - Each cell is processed at most once; only cells matching the original value are enqueued.
     *
     * @param xy      starting pixel (x,y).
     * @param new_v   replacement value to write into the region.
     * @param cyclic  true for wrap-around edges; false for bounded grid.
     * @return        the count of cells that were updated.
     *
     * @complexity    O(H*W) time and O(H*W) space in the worst case (grid size).
     *
     * Fills this map with the new color (new_v) starting from p.
     * https://en.wikipedia.org/wiki/Flood_fill
     */
    public int fill(Pixel2D xy, int new_v, boolean cyclic) {
        int ans = 0;
        final int H = cells.length;
        final int W = cells[0].length;
        final int fx = xy.getX();
        final int fy = xy.getY();
        if (fy < 0 || fy >= cells[0].length || fx < 0 || fx >= cells.length) {
            return ans;
        }
        final int old = cells[fy][fx];
        if (old == new_v) {
            return ans;
        }
        final boolean[][] visited = new boolean[H][W];
        final ArrayDeque<int[]> q = new ArrayDeque<>();
        visited[fy][fx] = true;
        q.add(new int[]{fx, fy});
        final int[][] directions = {{ 1,  0}, {-1,  0}, { 0,  1}, { 0, -1}};
        while (!q.isEmpty()) {
            int[] cur = q.removeFirst();
            int x = cur[0];
            int y = cur[1];
            if (cells[y][x] == old) {
                cells[y][x] = new_v;
                ans++;
            }
            for (int[] d : directions) {
                int nx = x + d[0];
                int ny = y + d[1];
                if (cyclic) {
                    nx = ((nx % W) + W) % W;
                    ny = ((ny % H) + H) % H;
                } else {
                    if (nx < 0 || nx >= W || ny < 0 || ny >= H)
                        continue;
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
     * Finds a shortest Manhattan (4-neighbor) path between two cells in a 2D grid,
     * optionally with cyclic wrap-around. Obstacles (cells equal to obsColor) are blocked.
     * Behavior:
     * - Returns an array of Pixel2D from p1 to p2 (inclusive) representing a shortest path.
     * - If p1 == p2, returns a single-element array { p1 }.
     * - If either endpoint is out of bounds, on an obstacle, or p2 is unreachable,
     *   returns null.
     * - If cyclic == true, stepping off an edge wraps to the opposite side (torus);
     *   otherwise, moves leaving the grid are ignored.
     * Implementation:
     * - Uses BFS with a queue and a visited matrix to discover the target.
     * - Stores parent coordinates (parentX/parentY) to reconstruct the path once p2 is found.
     * - Reconstruction walks backward from p2 to p1, then reverses the list.
     *
     * @param p1       start pixel (x,y).
     * @param p2       target pixel (x,y).
     * @param obsColor grid value denoting obstacles (non-traversable cells).
     * @param cyclic   true for wrap-around edges; false for bounded grid.
     * @return Pixel2D[] shortest path from p1 to p2, or null if no path exists or inputs invalid.
     *
     * @complexity O(H*W) time and O(H*W) space in the worst case (grid size).
     *
     * BFS like shortest the computation based on iterative raster implementation of BFS, see:
     * https://en.wikipedia.org/wiki/Breadth-first_search
     */
    public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor, boolean cyclic) {
        Pixel2D[] ans = null;// the result.
        if (p1 == null || p2 == null) return null;
        final int H = cells.length;
        final int W = cells[0].length;
        final int sx = p1.getX(), sy = p1.getY();
        final int ex = p2.getX(), ey = p2.getY();
        if (sx < 0 || sx >= W || sy < 0 || sy >= H) return null;
        if (ex < 0 || ex >= W || ey < 0 || ey >= H) return null;
        if (cells[sy][sx] == obsColor || cells[ey][ex] == obsColor) return null;
        if (sx == ex && sy == ey) return new Pixel2D[]{p1};
        final boolean[][] visited = new boolean[H][W];
        final int[][] parentX = new int[H][W];
        final int[][] parentY = new int[H][W];
        for (int y = 0; y < H; y++) {
            java.util.Arrays.fill(parentX[y], -1);
            java.util.Arrays.fill(parentY[y], -1);
        }
        final int[][] directions = {{ 1,  0}, {-1,  0}, { 0,  1}, { 0, -1}};
        final java.util.ArrayDeque<int[]> q = new java.util.ArrayDeque<>();
        visited[sy][sx] = true;
        q.addLast(new int[]{sx, sy});
        boolean found = false;
        while (!q.isEmpty()) {
            int[] cur = q.removeFirst();
            int x = cur[0], y = cur[1];

            if (x == ex && y == ey) {
                found = true;
                break;
            }
            for (int[] d : directions) {
                int nx = x + d[0];
                int ny = y + d[1];
                if (cyclic) {
                    nx = (nx % W + W) % W;
                    ny = (ny % H + H) % H;
                } else {
                    if (nx < 0 || nx >= W || ny < 0 || ny >= H) continue;
                }
                if (!visited[ny][nx] && cells[ny][nx] != obsColor) {
                    visited[ny][nx] = true;
                    parentX[ny][nx] = x;
                    parentY[ny][nx] = y;
                    q.addLast(new int[]{nx, ny});
                }
            }
        }
        if (!found) return null;
        java.util.ArrayList<Pixel2D> path = new java.util.ArrayList<>();
        int cx = ex, cy = ey;
        while (true) {
            path.add(new Index2D(cx, cy));
            if (cx == sx && cy == sy) break; // reached start
            int px = parentX[cy][cx];
            int py = parentY[cy][cx];
            if (px == -1 && py == -1) return null; // safety
            cx = px;
            cy = py;
        }
        java.util.Collections.reverse(path);
        ans = path.toArray(new Pixel2D[0]);
        return ans;
    }
    @Override
    /**
     * Computes shortest Manhattan (4‑neighbor) distances from a starting cell over a 2D grid.
     * Obstacles (cells equal to obsColor) are not traversable. Optionally wraps at edges.
     *
     * Behavior:
     * - Returns an int[][] wrapped in Map2D where:
     *   - dist[sy][sx] = 0 at start (if inside and not an obstacle).
     *   - dist[y][x] = k is the minimum number of steps from start to (x, y).
     *   - dist[y][x] = -1 for unreachable non‑obstacle cells.
     *   - dist[y][x] = obsColor for obstacle cells (copied from input).
     * - If start is out of bounds or on an obstacle, no BFS is run; only obstacles are marked.
     * - If cyclic == true, the grid is toroidal (moves wrap with modulo); otherwise borders are hard.
     *
     * @param start    starting pixel (x,y).
     * @param obsColor grid value denoting obstacles (blocked cells).
     * @param cyclic   true for wrap‑around edges; false for bounded grid.
     * @return Map2D containing the distance matrix as described above.
     *
     * @implNote Uses BFS with a queue and a visited matrix.
     * @complexity O(H*W) time and O(H*W) space.
     */
    public Map2D allDistance(Pixel2D start, int obsColor, boolean cyclic) {
        Map2D ans = null;  // the result.
        final int H = cells.length;
        final int W = cells[0].length;
        final int sx = start.getX(), sy = start.getY();
        int[][] dist = new int[H][W];
        for (int y = 0; y < H; y++) java.util.Arrays.fill(dist[y], -1);
        if (sx < 0 || sx >= W || sy < 0 || sy >= H) {
            return new Map(dist);
        }
        if (cells[sy][sx] == obsColor) {
            for (int y = 0; y < H; y++)
                for (int x = 0; x < W; x++)
                    if (cells[y][x] == obsColor) dist[y][x] = obsColor;
            return new Map(dist);
        }
        for (int y = 0; y < H; y++)
            for (int x = 0; x < W; x++)
                if (cells[y][x] == obsColor) dist[y][x] = obsColor;
        final boolean[][] visited = new boolean[H][W];
        final ArrayDeque<int[]> q = new ArrayDeque<>();
        final int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        visited[sy][sx] = true;
        dist[sy][sx] = 0;
        q.add(new int[]{sx, sy});
        while (!q.isEmpty()) {
            int[] cur = q.removeFirst();
            int x = cur[0], y = cur[1];

            for (int[] d : directions) {
                int nx = x + d[0], ny = y + d[1];

                if (cyclic) {
                    nx = ((nx % W) + W) % W;
                    ny = ((ny % H) + H) % H;
                } else {
                    if (nx < 0 || nx >= W || ny < 0 || ny >= H) continue;
                }
                if (visited[ny][nx]) continue;
                if (cells[ny][nx] == obsColor) continue;
                visited[ny][nx] = true;
                dist[ny][nx] = dist[y][x] + 1;
                q.add(new int[]{nx, ny});
            }
        }
        ans = new Map(dist);
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
}
