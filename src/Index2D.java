public class Index2D implements Pixel2D {
    private int x;
    private int y;
    public Index2D(int w, int h) {
        this.x = w;
        this.y = h;

    }
    public Index2D(Pixel2D other) {
        this.x = other.getX();
        this.y = other.getY();
    }
    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public double distance2D(Pixel2D p2) {
        if (p2 == null) {
            throw new IllegalArgumentException("p2 Can't be null!");}
        int dx = (this.x - p2.getX());
        int dy = (this.y - p2.getY());
        double distance = Math.sqrt(dx*dx - dy*dy);
        return distance;
    }

    @Override
    public String toString() {
        String ans = null;
        ans = new String( "(" + x + "," + y + ")");
        return ans;
    }

    @Override
    public boolean equals(Object p) {
        boolean ans = true;
        if (this == p) return true;
        if (p == null) return false;
        if (!(p instanceof Pixel2D)) return false;
        Pixel2D other = (Pixel2D) p;
        ans = this.x == other.getX() && this.y == other.getY();
        return ans;
    }
}
