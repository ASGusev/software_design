package ru.spbau.des.roguelike.dom.characters;

import ru.spbau.des.roguelike.dom.environment.Direction;
import ru.spbau.des.roguelike.dom.environment.HitResult;
import ru.spbau.des.roguelike.dom.environment.Position;
import ru.spbau.des.roguelike.dom.environment.Unit;
import ru.spbau.des.roguelike.dom.environment.DistanceNavigator;
import ru.spbau.des.roguelike.dom.environment.Field;

public class Monster implements Unit {
    private final static int MAX_CHASE_DIST = 8;

    private final int id;
    private final int power;
    private int health;
    private boolean dead;
    private Position position;

    public Monster(int id, int power, int health, Position position) {
        this.id = id;
        this.power = power;
        this.health = health;
        this.position = position;
        dead = false;
    }

    @Override
    public HitResult takeHit(int strength) {
        health -= strength;
        if (health <= 0) {
            dead = true;
            return new ScoreUpdateResult(this.power);
        }
        return null;
    }

    @Override
    public boolean isDead() {
        return dead;
    }

    public int getHealth() {
        return health;
    }

    public void step(Field field, DistanceNavigator navigator) {
        int distance = navigator.distanceAt(position);
        if (distance > MAX_CHASE_DIST) {
            return;
        }
        Direction chosenDirection = null;
        for (Direction direction: Direction.values()) {
            Position nextPosition = position.resolve(direction);
            if (field.freeAt(nextPosition) &&
                    navigator.distanceAt(nextPosition) == distance - 1 ||
                    navigator.distanceAt(nextPosition) == 0) {
                chosenDirection = direction;
            }
        }
        if (chosenDirection == null) {
            return;
        }
        Position targetPosition = position.resolve(chosenDirection);
        if (distance == 1) {
            field.get(targetPosition).takeHit(power);
        } else {
            field.move(position, chosenDirection);
            position = targetPosition;
        }
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Monster && ((Monster) o).id == id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
