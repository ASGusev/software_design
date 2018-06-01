package ru.spbau.des.roguelike.dom.environment;

public interface Unit {
    HitResult takeHit(int strength);

    boolean isDead();
}
