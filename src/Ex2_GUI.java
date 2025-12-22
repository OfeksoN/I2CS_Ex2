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
            // 1) Canvas & scaling
            StdDraw.enableDoubleBuffering();            // smoother
            StdDraw.setCanvasSize(Math.max(512, W*8),   // pick something reasonable
                    Math.max(512, H*8));
            StdDraw.setXscale(0, W);
            StdDraw.setYscale(0, H);

            // Optional: background
            StdDraw.clear(Color.WHITE);

            // 2) Draw each cell as a filled unit square
            for (int y = 0; y < H; y++) {
                for (int x = 0; x < W; x++) {
                    int v = map.getPixel(x, y);

                    // Map your cell value -> a color
                    Color c = colorForValue(v);

                    StdDraw.setPenColor(c);
                    // If your y=0 is at the TOP of your data, flip Y:
                    int drawY = y; // or: int drawY = (H - 1) - y;
                    StdDraw.filledRectangle(x + 0.5, drawY + 0.5, 0.5, 0.5);
                }
            }

            // 3) Optional: draw grid lines
            StdDraw.setPenColor(new Color(0,0,0,40)); // subtle
            StdDraw.setPenRadius(0.0015);
            for (int x = 0; x <= W; x++) {
                StdDraw.line(x, 0, x, H);
            }
            for (int y = 0; y <= H; y++) {
                StdDraw.line(0, y, W, y);
            }

            // 4) Show the frame
            StdDraw.show();
        }
    }

    /**
     * @param mapFileName
     * @return
     */
    public static Map2D loadMap(String mapFileName) {
        Map2D ans = null;

        return ans;
    }

    /**
     *
     * @param map
     * @param mapFileName
     */
    public static void saveMap(Map2D map, String mapFileName) {
        int W = map.getWidth();
        int H = map.getHeight();
        try {
            FileWriter myWriter = new FileWriter(mapFileName);
            myWriter.write("Text file named: "+mapFileName+"\n");
            Pixel2D p1 = new Index2D(1, 2) {
            };
            myWriter.write("Point "+p1.toString());
            myWriter.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        for (int y = 0; y < H; y++) {
            StringBuilder line = new StringBuilder();
            for (int x = 0; x < W; x++) {
                if (x > 0) line.append(' ');
                line.append(map.getPixel(x, y));
            }
        }
    }

    public static void main(String[] a) {
        String mapFile = "map.txt";
        Map2D map = loadMap(mapFile);
        Ex2_GUI.drawMap(map);
    }
    ///////////////// Private functions ///////////////

    public static void drawPath(Pixel2D[] path, Color color) {
        if (path == null) return;
        StdDraw.setPenColor(color);
        for (Pixel2D p : path) {
            double cx = p.getX() + 0.5;
            double cy = p.getY() + 0.5; // flip if needed
            StdDraw.filledCircle(cx, cy, 0.35); // or a smaller square        StdDraw.filledCircle(cx, cy, 0.35); // or a smaller square
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
