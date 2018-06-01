package ru.spbau.des.roguelike.dom.characters;

import ru.spbau.des.roguelike.dom.base.Direction;
import ru.spbau.des.roguelike.dom.base.HitReturn;
import ru.spbau.des.roguelike.dom.base.Position;
import ru.spbau.des.roguelike.dom.equipment.Item;
import ru.spbau.des.roguelike.dom.base.Unit;
import ru.spbau.des.roguelike.dom.equipment.Armour;
import ru.spbau.des.roguelike.dom.equipment.Weapon;
import ru.spbau.des.roguelike.dom.field.Field;

public class Player implements Unit {
    private Armour armour;
    private Weapon weapon;
    private int health;
    private boolean dead;
    private Position position;
    private Field field;

    public Player(Armour armour, Weapon weapon, int health, Field field) {
        this.armour = armour;
        this.weapon = weapon;
        this.health = health;
        this.field = field;
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

    public HitReturn step(Direction step) {
        Position nextPosition = position.resolve(step);
        if (field.freeAt(nextPosition)) {
            move(nextPosition);
            return null;
        } else {
            Unit target = field.get(nextPosition);
            HitReturn hitReturn = target.takeHit(getPower());
            if (target.isDead()) {
                field.clear(nextPosition);
            }
            if (hitReturn instanceof Item) {
                move(nextPosition);
            }
            return hitReturn;
        }
    }

    private void move(Position nextPosition) {
        field.move(position, nextPosition);
        position = nextPosition;
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
