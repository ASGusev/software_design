package ru.spbau.des.roguelike.dom.equipment;

import ru.spbau.des.roguelike.dom.characters.Player;

/**
 * When applied, Potion updates the player's heath.
 */
public class Potion implements Item {
    private static final String SHORT_DESCRIPTION = "Potion";
    private final int value;
    private final String name;
    private final String description;

    /**
     * Creates a new instance
     * @param value the value of the health change apter application
     * @param name the name for the new drug
     * @param description the description for the drug
     */
    public Potion(int value, String name, String description) {
        this.value = value;
        this.name = name;
        this.description = description;
    }

    @Override
    public void apply(Player player) {
        player.updateHealth(value);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getShortDescription() {
        return SHORT_DESCRIPTION;
    }
}
