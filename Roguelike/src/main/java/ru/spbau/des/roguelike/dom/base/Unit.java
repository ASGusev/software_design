package ru.spbau.des.roguelike.dom.base;

public interface Unit {
    HitReturn takeHit(int strength);

    boolean isDead();
}
