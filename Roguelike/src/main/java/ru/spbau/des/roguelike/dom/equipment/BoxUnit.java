package ru.spbau.des.roguelike.dom.equipment;

import ru.spbau.des.roguelike.dom.base.HitReturn;
import ru.spbau.des.roguelike.dom.base.Unit;

public class BoxUnit implements Unit {
    private final Item item;
    private boolean dead;

    public BoxUnit(Item item) {
        this.item = item;
        dead = false;
    }

    @Override
    public HitReturn takeHit(int strength) {
        dead = true;
        return item;
    }

    @Override
    public boolean isDead() {
        return dead;
    }
}
