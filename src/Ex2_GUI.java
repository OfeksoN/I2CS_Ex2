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
                    Color c = COLORS[v];

                    StdDraw.setPenColor(c);
                    int drawY = (H - 1) - y;
                    StdDraw.filledRectangle(x, drawY , 0.5, 0.5);
                    //StdDraw.filledSquare( x + 0.5, y + 0.5, 0.5);


                }
            }


            StdDraw.setPenColor(new Color(0,0,0,40));
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

                ans = new Map(grid);
                return ans;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
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

                bw.write(W + " " + H);
                bw.newLine();

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
            final int BLACK = 0,BLUE = 1,CYAN = 2,DARK_GRAY = 3,GRAY = 4,GREEN = 5,LIGHT_GRAY = 6,MAGENTA = 7,ORANGE = 8,PINK = 9,RED = 10,WHITE = 11,YELLOW = 12;
            Map sample = new Map(100,100,11);
            int obstacle = 5;
            Index2D p1 = new Index2D(50,50);
            Index2D p2 = new Index2D(10,10);
            Index2D p3 = new Index2D(70 , 70);
            Index2D p4 = new Index2D(40 , 30);
            Index2D p5 = new Index2D(69 , 70);
            Index2D p6 = new Index2D(69 , 99);
            Index2D p7 = new Index2D(71 , 70);
            Index2D p8 = new Index2D(71 , 99);
            Index2D p9 = new Index2D(9 , 10);
            Index2D p10 = new Index2D(9 , 0);
            Index2D p11 = new Index2D(11 , 10);
            Index2D p12 = new Index2D(11 , 0);
            Index2D p13 = new Index2D(9 , 11);
            Index2D p14 = new Index2D(11 , 11);
            Index2D p15 = new Index2D(69 , 69);
            Index2D p16 = new Index2D(71 , 69);

            sample.drawCircle(p1, 10, obstacle);
            sample.drawRect(p1,p4,obstacle);
            sample.drawLine(p5, p6, obstacle);
            sample.drawLine(p7, p8, obstacle);
            sample.drawLine(p9, p10, obstacle);
            sample.drawLine(p11, p12, obstacle);
            sample.drawLine(p13, p14, obstacle);
            sample.drawLine(p15, p16, obstacle);

            //sample.fill(p1, obstacle, false);
            sample.setPixel(p2, BLACK);
            sample.setPixel(p3, 0);
            Pixel2D[] Path = sample.shortestPath(p2,p3,obstacle,true);
            //Map2D Path1 = sample.allDistance(p2,obstacle,false);
            for (Pixel2D p:Path){sample.setPixel(p,0);}
           // sample.fill(p2,11, true);

            saveMap(sample, "map.txt");

            // Now load & draw
            Map2D map = loadMap("map.txt");
            Ex2_GUI.drawMap(map);
        }

///////////////// Private functions ///////////////
private static final Color[] COLORS = {
        Color.BLACK, Color.BLUE, Color.CYAN, Color.DARK_GRAY,
        Color.GRAY, Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA,
        Color.ORANGE, Color.PINK, Color.RED, Color.WHITE, Color.YELLOW };



