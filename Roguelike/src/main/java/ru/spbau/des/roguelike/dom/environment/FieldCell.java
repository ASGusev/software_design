package ru.spbau.des.roguelike.dom.environment;

public class FieldCell {
    private Unit unit;

    private FieldCell(Unit unit) {
        this.unit = unit;
    }

    public static FieldCell withUnit(Unit unit) {
        return new FieldCell(unit);
    }

    public static FieldCell wall(boolean destructible) {
        return new FieldCell(new WallUnit(destructible));
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public void clear() {
        unit = null;
    }

    public boolean isFree() {
        return unit == null;
    }
}
