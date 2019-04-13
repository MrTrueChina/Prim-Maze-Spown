package maze;

import java.awt.Point;
import java.awt.Rectangle;

@SuppressWarnings("serial")
public class Maze extends Rectangle {
    private boolean[][] _quads;

    public Maze(int width, int height) {
        super(width, height);
        _quads = new boolean[width][height];
    }

    public boolean getPassable(Point point) {
        return getPassable(point.x, point.y);
    }

    public boolean getPassable(int x, int y) {
        return _quads[x][y];
    }

    public void setPassable(boolean passable, Point point) {
        setPassable(passable, point.x, point.y);
    }

    public void setPassable(boolean passable, int x, int y) {
        _quads[x][y] = passable;
    }
}
