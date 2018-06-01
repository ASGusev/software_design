package ru.spbau.des.roguelike.dom.equipment;

import ru.spbau.des.roguelike.dom.environment.HitResult;
import ru.spbau.des.roguelike.dom.environment.Unit;

public class BoxUnit implements Unit {
    private final Item item;
    private boolean dead;

    public BoxUnit(Item item) {
        this.item = item;
        dead = false;
    }

    @Override
    public HitResult takeHit(int strength) {
        dead = true;
        return item;
    }

    @Override
    public boolean isDead() {
        return dead;
    }
}
