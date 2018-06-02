package ru.spbau.des.roguelike.dom.environment;

/**
 * A unit, hitting which ends the game
 */
public class FinishUnit implements Unit {
    @Override
    public HitResult takeHit(int strength) {
        return new FinishResult();
    }

    @Override
    public boolean isDead() {
        return false;
    }
}
