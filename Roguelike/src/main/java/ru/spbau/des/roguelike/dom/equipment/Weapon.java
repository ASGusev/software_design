package ru.spbau.des.roguelike.dom.equipment;

import ru.spbau.des.roguelike.dom.characters.Player;

public class Weapon implements Item {
    private final static String DESCRIPTION_TEMPLATE = "A weapon with damage %d.";
    private final int power;
    private final String name;

    public Weapon(int power, String name) {
        this.power = power;
        this.name = name;
    }

    public int getPower() {
        return power;
    }

    @Override
    public void apply(Player player) {
        player.setWeapon(this);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return String.format(DESCRIPTION_TEMPLATE, power);
    }
}
