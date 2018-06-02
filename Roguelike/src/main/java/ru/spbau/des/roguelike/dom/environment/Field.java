package ru.spbau.des.roguelike.dom.environment;

/**
Represents the court where the fight takes place. Stores all the units.
 */
public class Field {
    private final Unit[][] units;
    private final int h;
    private final int w;

    private Field(Unit[][] units, int w, int h) {
        this.units = units;
        this.w = w;
        this.h = h;
    }

    /**
     * Checks if a cell with provided coordinates exists.
     * @param position the coordinates to check
     * @return true if such cell exists, false if at one of the coordinate values
     * is illegal
     */
    public boolean valid(Position position) {
        return position.getX() >= 0 && position.getX() < w && position.getY() >= 0 &&
                position.getY() < h;
    }

    /**
     * Checks if the given cell is available for putting units
     * @param position coordinates of the cell
     * @return true if the cell exists and does not already contain a unit, false otherwise
     */
    public boolean free(Position position) {
        return valid(position) && units[position.getX()][position.getY()] == null;
    }

    /**
     * Gets the value of the given cell
     * @param x first coordinate of the cell
     * @param y second coordinate of the cell
     * @return `
     */
    public Unit get(int x, int y) {
        return units[x][y];
    }

    /**
     * Gets the value of the given cell
     * @param position location of the requested cell
     * @return content of the cell
     */
    public Unit get(Position position) {
        return get(position.getX(), position.getY());
    }

    /**
     * Returns the height of the field
     * @return the height of the field
     */
    public int getH() {
        return h;
    }

    /**
     * Returns the width of the field
     * @return the width of the field
     */
    public int getW() {
        return w;
    }

    /**
     * Moves content from one cell to another and leaves the first one empty
     * @param sourcePosition the cell from which content should be moved
     * @param targetPosition the cell where the content should be placed
     */
    public void move(Position sourcePosition, Position targetPosition) {
        put(targetPosition, get(sourcePosition));
        clear(sourcePosition);
    }

    /**
     * Moves content of the given cell to the cell where the provided direction points
     * if applied to the source position
     * @param sourcePosition the cell whose content should be moved
     * @param direction the direction to move the content
     */
    public void move(Position sourcePosition, Direction direction) {
        Position targetPosition = sourcePosition.resolve(direction);
        move(sourcePosition, targetPosition);
    }

    /**
     * Puts the provided unit to the provided cell
     * @param position position of the cell
     * @param value the unit to put
     */
    public void put(Position position, Unit value) {
        units[position.getX()][position.getY()] = value;
    }

    /**
     * Removes content from the given cell
     * @param position the position of the cell to clear
     */
    public void clear(Position position) {
        put(position, null);
    }

    /**
     * Creates a new Field object from the provided plan
     * @param plan a map of the field to create
     * @return a field matching the plan
     */
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
