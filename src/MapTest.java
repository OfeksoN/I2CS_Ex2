import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;
/**
 * Intro2CS, 2026A, this is a very
 */
class MapTest {
    /**
     */
    private int[][] _map_3_3 = {{0,1,0}, {1,0,1}, {0,1,0}};
    private Map2D _m0, _m1, _m3_3;
    @BeforeEach
    public void setup() {
        _m3_3 = new Map(_map_3_3);
        _m0 = new Map(1,1,0);
        _m1 = new Map(1,1,0);
    }
    @Test
    @Timeout(value = 1, unit = SECONDS)
    void init() {
        int[][] bigarr = new int [500][500];
        _m1.init(bigarr);
        assertEquals(bigarr.length, _m1.getWidth());
        assertEquals(bigarr[0].length, _m1.getHeight());
        Pixel2D p1 = new Index2D(3,2);
        _m1.fill(p1,1, true);
    }

    @Test
    void testInit() {
        _m0.init(_map_3_3);
        _m1.init(_map_3_3);

        assertEquals(_m0, _m1);
    }
    @Test
    void testEquals() {
        assertEquals(_m0,_m1);
        _m0.init(_map_3_3);
        _m1.init(_map_3_3);
        assertEquals(_m0,_m1);
    }

    @Test
    void shortestPath_basic() {
        Map m = new Map(5, 5, 0);
        Pixel2D s = new Index2D(0, 0);
        Pixel2D t = new Index2D(4, 4);
        Pixel2D[] path = m.shortestPath(s, t, 1, false);
        assertNotNull(path);
        assertEquals(s.getX(), path[0].getX());
        assertEquals(s.getY(), path[0].getY());
        assertEquals(t.getX(), path[path.length-1].getX());
        assertEquals(t.getY(), path[path.length-1].getY());
    }

    @Test
    void shortestPath_checks_obstacles() {
        Map m = new Map(3, 3, 0);
        m.setPixel(1, 0, 1);
        m.setPixel(1, 1, 1);
        m.setPixel(1, 2, 1);
        Pixel2D s = new Index2D(0, 1);
        Pixel2D t = new Index2D(2, 1);
        Pixel2D[] path = m.shortestPath(s, t, 1, false);
        assertNull(path);
    }

    @Test
    void allDistance_NullTest() {
        Map m = new Map(5, 5, 0);
        int obsColor = 1;
        Pixel2D start = new Index2D(2, 2);

        Map2D distMap = m.allDistance(start, obsColor, false);
        assertNotNull(distMap);

        // Check some representative cells
        assertEquals(0, distMap.getPixel(2, 2), "Start must be distance 0");
        assertEquals(1, distMap.getPixel(3, 2));
        assertEquals(1, distMap.getPixel(1, 2));
        assertEquals(1, distMap.getPixel(2, 1));
        assertEquals(1, distMap.getPixel(2, 3));
        assertEquals(2, distMap.getPixel(4, 2));
        assertEquals(4, distMap.getPixel(0, 0));
    }
}