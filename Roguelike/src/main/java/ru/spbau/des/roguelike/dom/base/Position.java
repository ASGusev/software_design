package ru.spbau.des.roguelike.dom.base;

public class Position {
    private static final int MAX_COORDINATE = 1 << 16;
    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Position resolve(Direction direction) {
        return new Position(x + direction.getDX(), y + direction.getDY());
    }

    @Override
    public int hashCode() {
        return x * MAX_COORDINATE + y;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Position && ((Position) o).x == x && ((Position) o).y == y;
    }
}
