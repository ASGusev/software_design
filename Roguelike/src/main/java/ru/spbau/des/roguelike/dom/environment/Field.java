package ru.spbau.des.roguelike.dom.environment;

public class Field {
    private FieldCell[][] cells;
    private int h;
    private int w;

    public Field(FieldCell[][] cells) {
        this.cells = cells;
        w = cells.length;
        h = cells[0].length;
    }

    public boolean valid(Position position) {
        return position.getX() > 0 && position.getX() < w && position.getY() >= 0 &&
                position.getY() < h;
    }

    public boolean freeAt(Position position) {
        return valid(position) && cells[position.getX()][position.getY()].isFree();
    }

    public Unit get(int x, int y) {
        return cells[x][y].getUnit();
    }

    public Unit get(Position position) {
        return get(position.getX(), position.getY());
    }

    public int getH() {
        return h;
    }

    public int getW() {
        return w;
    }

    public void move(Position sourcePosition, Position targetPosition) {
        put(targetPosition, get(sourcePosition));
        clear(sourcePosition);
    }

    public void move(Position sourcePosition, Direction direction) {
        Position targetPosition = sourcePosition.resolve(direction);
        move(sourcePosition, targetPosition);
    }

    public void put(Position position, Unit value) {
        cells[position.getX()][position.getY()].setUnit(value);
    }

    public void clear(Position position) {
        cells[position.getX()][position.getY()].clear();
    }
}
