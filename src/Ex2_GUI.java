import java.awt.Color;
/**
 * Intro2CS_2026A
 * This class represents a Graphical User Interface (GUI) for Map2D.
 * The class has save and load functions, and a GUI draw function.
 * You should implement this class, it is recommender to use the StdDraw class, as in:
 * https://introcs.cs.princeton.edu/java/stdlib/javadoc/StdDraw.html
 *
 *
 */
public class Ex2_GUI {
    public static void drawMap(Map2D map) {
            int W = map.getWidth();
            int H = map.getHeight();
            StdDraw.enableDoubleBuffering();            // smoother
            StdDraw.setCanvasSize(Math.max(512, W*8),   // pick something reasonable
                    Math.max(512, H*8));
            StdDraw.setXscale(0, W);
            StdDraw.setYscale(0, H);

            StdDraw.clear(Color.WHITE);

            for (int y = 0; y < H; y++) {
                for (int x = 0; x < W; x++) {
                    int v = map.getPixel(x, y);

                    // Map your cell value -> a color
                    Color c = colorForValue(v);

                    StdDraw.setPenColor(c);
                    int drawY = y; // or: int drawY = (H - 1) - y;
                   //StdDraw.filledRectangle(x + 0.5, drawY + 0.5, 0.5, 0.5);
                    StdDraw.filledSquare( x + 0.5, drawY + 0.5, 0.4);

                }
            }


            StdDraw.setPenColor(new Color(0,0,0,40)); // subtle
            StdDraw.setPenRadius(0.015);
            for (int x = 0; x <= W; x++) {
                StdDraw.line(x, 0, x, H);
            }
            for (int y = 0; y <= H; y++) {
                StdDraw.line(0, y, W, y);
            }
            StdDraw.show();
        }
    }

    /**
     * @param mapFileName
     * @return
     */
    public static Map2D loadMap(String mapFileName) {
        Map2D ans = null;
            try (BufferedReader br = new BufferedReader(new FileReader(mapFileName))) {
                // Skip optional comment lines starting with '#'
                String header;
                while (true) {
                    header = br.readLine();
                    if (header == null) {
                        throw new IllegalArgumentException("Empty map file: " + mapFileName);
                    }
                    header = header.trim();
                    if (!header.isEmpty() && !header.startsWith("#")) break;
                }

                String[] parts = header.split("\\s+");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("First non-comment line must be 'W H'");
                }
                int W = Integer.parseInt(parts[0]);
                int H = Integer.parseInt(parts[1]);

                int[][] grid = new int[H][W];
                for (int y = 0; y < H; y++) {
                    String line = br.readLine();
                    if (line == null) {
                        throw new IllegalArgumentException("Unexpected EOF at row " + y);
                    }
                    line = line.trim();
                    if (line.isEmpty()) {
                        throw new IllegalArgumentException("Empty line at row " + y);
                    }
                    String[] toks = line.split("\\s+");
                    if (toks.length != W) {
                        throw new IllegalArgumentException("Row " + y + " has " + toks.length +
                                " values, expected " + W);
                    }
                    for (int x = 0; x < W; x++) {
                        grid[y][x] = Integer.parseInt(toks[x]);
                    }
                }

                // Build your Map2D (adjust constructor or init as per your class)
                ans = new Map(grid); // or: Map2D ans = new Map(W, H); ans.init(grid);
                return ans;
            } catch (Exception e) {
                e.printStackTrace();
                return null; // caller will get the earlier IllegalArgumentException from drawMap
            }
    }

    /**
     *
     * @param map
     * @param mapFileName
     */
    public static void saveMap(Map2D map, String mapFileName) {
            int W = map.getWidth();
            int H = map.getHeight();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(mapFileName))) {
                // header
                bw.write(W + " " + H);
                bw.newLine();
                // body
                for (int y = 0; y < H; y++) {
                    StringBuilder line = new StringBuilder();
                    for (int x = 0; x < W; x++) {
                        if (x > 0) line.append(' ');
                        line.append(map.getPixel(x, y));
                    }
                    bw.write(line.toString());
                    bw.newLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        public static void main(String[] a) {

            Map sample = new Map(100,100,1);
            saveMap(sample, "map.txt");


            // Now load & draw
            Map2D map = loadMap("map.txt");
            Ex2_GUI.drawMap(map);
        }

///////////////// Private functions ///////////////

    public static void drawPath(Pixel2D[] path, Color color) {
        if (path == null) return;
        StdDraw.setPenColor(color);
        for (Pixel2D p : path) {
            double cx = p.getX() + 0.5;
            double cy = p.getY() + 0.5; // flip if needed
            StdDraw.filledCircle(cx, cy, 0.35);
        }
        StdDraw.show();

    }
        public static Color colorForValue(int v) {
            // Example scheme:
            // - Obstacles: black (1)
            // - Start/Goal markers: special colors (if you encode them)
            // - Distances/other: a blue-ish gradient
            if (v == 1) return Color.BLACK;     // obstacle
            if (v == 0) return new Color(230, 230, 230); // empty

            // Simple gradient for positive values:
            // clamp v for color scaling
            int t = Math.max(1, Math.min(v, 100));
            int r = 20;
            int g = 50 + (int)(205 * (1.0 - t/100.0));
            int b = 255 - (int)(200 * (t/100.0));
            return new Color(r, g, b);
        }
