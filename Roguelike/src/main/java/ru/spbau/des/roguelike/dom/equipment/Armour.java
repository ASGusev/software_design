package ru.spbau.des.roguelike.dom.equipment;

import ru.spbau.des.roguelike.dom.characters.Player;

public class Armour implements Item {
    private final static String NAME = "Armour";
    private final static String DESCRIPTION_TEMPLATE = "%d%% damage reduction.";
    private final int protection;

    public Armour(int protection) {
        this.protection = protection;
    }

    public int getProtection() {
        return protection;
    }

    @Override
    public void apply(Player player) {
        player.setArmour(this);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return String.format(DESCRIPTION_TEMPLATE, protection);
    }
}
