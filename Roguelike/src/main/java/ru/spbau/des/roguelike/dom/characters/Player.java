package ru.spbau.des.roguelike.dom.characters;

import ru.spbau.des.roguelike.dom.environment.Direction;
import ru.spbau.des.roguelike.dom.environment.HitResult;
import ru.spbau.des.roguelike.dom.environment.Position;
import ru.spbau.des.roguelike.dom.equipment.Item;
import ru.spbau.des.roguelike.dom.environment.Unit;
import ru.spbau.des.roguelike.dom.equipment.Armour;
import ru.spbau.des.roguelike.dom.equipment.Weapon;
import ru.spbau.des.roguelike.dom.environment.Field;

public class Player implements Unit {
    private Armour armour;
    private Weapon weapon;
    private int health;
    private boolean dead;
    private Position position;
    private Field field;

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
