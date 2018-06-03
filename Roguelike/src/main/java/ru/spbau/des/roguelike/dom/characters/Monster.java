package ru.spbau.des.roguelike.dom.characters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.spbau.des.roguelike.dom.environment.Direction;
import ru.spbau.des.roguelike.dom.environment.HitResult;
import ru.spbau.des.roguelike.dom.environment.Position;
import ru.spbau.des.roguelike.dom.environment.Unit;
import ru.spbau.des.roguelike.dom.environment.DistanceNavigator;
import ru.spbau.des.roguelike.dom.environment.Field;

/**
 * Monster is an enemy unit. It chases the player unless the distance is too big. If
 * the monster meets the player, it attacks with its power.
 */
public class Monster implements Unit {
    private final static int MAX_CHASE_DIST = 8;
    private static final String LOG_ATTACKS = "Monster {} attacks ({}, {})";
    private static final String LOG_DIED = "Monster {} died at ({}, {})";

    private final int id;
    private final int power;
    private int health;
    private boolean dead;
    private Position position;
    private Logger logger = LogManager.getLogger();

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
            logger.debug(LOG_DIED, id, position.getX(), position);
            return new ScoreResult(this.power);
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
            if (field.free(nextPosition) &&
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
            logger.debug(LOG_ATTACKS, id, targetPosition.getX(), targetPosition.getY());
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
