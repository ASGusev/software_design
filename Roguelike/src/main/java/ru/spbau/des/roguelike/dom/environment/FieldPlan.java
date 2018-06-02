package ru.spbau.des.roguelike.dom.environment;

public class FieldPlan {
    private final Cell[][] cells;
    private final int w;
    private final int h;

    public FieldPlan(int w, int h) {
        this.w = w;
        this.h = h;
        cells = new Cell[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                cells[i][j] = Cell.WALL;
            }
        }
    }

    public Cell get(int x, int y) {
        return cells[x][y];
    }

    public Cell get(Position position) {
        return get(position.getX(), position.getY());
    }

    public void set(int x, int y, Cell value) {
        cells[x][y] = value;
    }

    public void set(Position position, Cell value) {
        set(position.getX(), position.getY(), value);
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public enum Cell {
        EMPTY, WALL, INDESTRUCTIBLE_WALL
    }
}
