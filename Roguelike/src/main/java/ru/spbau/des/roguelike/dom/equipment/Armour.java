package ru.spbau.des.roguelike.dom.equipment;

import ru.spbau.des.roguelike.dom.characters.Player;

/**
 * Armour is a piece of equipment that reduces the damage that players gets from an
 * attack
 */
public class Armour implements Item {
    private final static String NAME = "Armour";
    private final static String DESCRIPTION_TEMPLATE = "%d%% damage reduction.";
    private final int protection;

    /**
     * Creates an instance that reduces damage for the supplied percent
     * @param protection the percent for which the damage should be reduced
     */
    public Armour(int protection) {
        this.protection = protection;
    }

    /**
     * Gets the percent of damage reduction
     * @return
     */
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
