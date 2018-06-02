package ru.spbau.des.roguelike.dom.environment;

/**
 * Every game entity that takes place on the field should implement the Unit class
 */
public interface Unit {
    HitResult takeHit(int strength);

    boolean isDead();
}
