package ru.spbau.des.roguelike.dom.environment;

import ru.spbau.des.roguelike.dom.equipment.Item;

/**
 * WallUnit represents a wall in the field.
 */
public class WallUnit implements Unit {
    private final boolean destructible;
    private boolean dead;

    /**
     * Creates a new instance. If the destructible flag is true, the new unit dies when
     * hit. Otherwise it survives
     * @param destructible a flag showing whether the wall should fall when hit
     */
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
