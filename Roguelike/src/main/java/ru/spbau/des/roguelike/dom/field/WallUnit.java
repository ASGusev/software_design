package ru.spbau.des.roguelike.dom.field;

import ru.spbau.des.roguelike.dom.equipment.Item;
import ru.spbau.des.roguelike.dom.base.Unit;

public class WallUnit implements Unit {
    private boolean destructible;
    private boolean dead;

    public WallUnit(boolean destructible) {
        this.destructible = destructible;
    }

    @Override
    public Item takeHit(int strength) {
        if (destructible) {
            dead = true;
        }
        return null;
    }

    @Override
    public boolean isDead() {
        return dead;
    }
}
