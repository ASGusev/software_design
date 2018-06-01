package ru.spbau.des.roguelike.dom.base;

public class FinishUnit implements Unit {
    @Override
    public HitReturn takeHit(int strength) {
        return new FinishReturn();
    }

    @Override
    public boolean isDead() {
        return false;
    }
}
