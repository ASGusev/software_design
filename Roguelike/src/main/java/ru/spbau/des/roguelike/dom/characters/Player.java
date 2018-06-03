package ru.spbau.des.roguelike.dom.characters;

import ru.spbau.des.roguelike.dom.environment.Direction;
import ru.spbau.des.roguelike.dom.environment.HitResult;
import ru.spbau.des.roguelike.dom.environment.Position;
import ru.spbau.des.roguelike.dom.equipment.Item;
import ru.spbau.des.roguelike.dom.environment.Unit;
import ru.spbau.des.roguelike.dom.equipment.Armour;
import ru.spbau.des.roguelike.dom.equipment.Weapon;
import ru.spbau.des.roguelike.dom.environment.Field;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The player's game character. Carries a weapon, a piece armour. Has a position on
 * a field, health.
 */
public class Player implements Unit {
    private final static int MAX_BAG_SIZE = 9;

    private Armour armour;
    private Weapon weapon;
    private int health;
    private boolean dead;
    private Position position;
    private Field field;
    private final LinkedList<Item> bag = new LinkedList<>();

    public Player(Armour armour, Weapon weapon, int health) {
        this.armour = armour;
        this.weapon = weapon;
        this.health = health;
        dead = false;
    }

    public int getHealth() {
        return health;
    }

    public int getPower() {
        return weapon.getPower();
    }

    public int getArmour() {
        return armour.getProtection();
    }

    /**
     * Adds delta to health
     * @param delta value to add to health
     */
    public void updateHealth(int delta) {
        health += delta;
        if (health <= 0) {
            dead = true;
        }
    }

    public void setArmour(Armour armour) {
        this.armour = armour;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setField(Field field) {
        this.field = field;
    }

    /**
     * Runs a game step with provided direction. If the pointed field is empty, the
     * character enters it, otherwise it attacks the unit in that field.
     * @param step the direction chosen for the step
     * @return null if the field was empty, otherwise the value returned by the
     * attacked unit
     */
    public HitResult step(Direction step) {
        Position nextPosition = position.resolve(step);
        if (field.free(nextPosition)) {
            move(nextPosition);
            return null;
        } else {
            Unit target = field.get(nextPosition);
            HitResult hitResult = weapon.hit(target);
            if (target.isDead()) {
                field.clear(nextPosition);
            }
            if (hitResult instanceof Item) {
                move(nextPosition);
            }
            return hitResult;
        }
    }

    private void move(Position nextPosition) {
        field.move(position, nextPosition);
        position = nextPosition;
    }

    public List<Item> getBag() {
        return bag;
    }

    public void addItem(Item item){
        if (bag.size() == MAX_BAG_SIZE) {
            bag.pollLast();
        }
        bag.addFirst(item);
    }

    public void applyItem(int index) {
        if (index < bag.size()) {
            Item activeItem = bag.get(index);
            bag.remove(index);
            activeItem.apply(this);
        }
    }

    public void dropItem(int index) {
        bag.remove(index);
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public Item takeHit(int strength) {
        double damageCoefficient = (100 - getArmour()) / 100.;
        int damage = (int)(damageCoefficient * strength);
        updateHealth(-damage);
        return null;
    }

    @Override
    public boolean isDead() {
        return dead;
    }
}
