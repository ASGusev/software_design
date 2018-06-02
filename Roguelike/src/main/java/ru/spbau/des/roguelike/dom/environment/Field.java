package ru.spbau.des.roguelike.dom.environment;

public class Field {
    private final Unit[][] units;
    private final int h;
    private final int w;

    private Field(Unit[][] units, int w, int h) {
        this.units = units;
        this.w = w;
        this.h = h;
    }

    public boolean valid(Position position) {
        return position.getX() > 0 && position.getX() < w && position.getY() >= 0 &&
                position.getY() < h;
    }

    public boolean freeAt(Position position) {
        return valid(position) && units[position.getX()][position.getY()] == null;
    }

    public Unit get(int x, int y) {
        return units[x][y];
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
        units[position.getX()][position.getY()] = value;
    }

    public void clear(Position position) {
        put(position, null);
    }

    public static Field fromPlan(FieldPlan plan) {
        int w = plan.getW();
        int h = plan.getH();
        Unit[][] units = new Unit[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                switch (plan.get(i, j)) {
                    case EMPTY: {
                        units[i][j] = null;
                        break;
                    }
                    case WALL: {
                        units[i][j] = new WallUnit(true);
                        break;
                    }
                    case INDESTRUCTIBLE_WALL: {
                        units[i][j] = new WallUnit(false);
                    }
                }
            }
        }
        return new Field(units, w, h);
    }
}
