package ru.spbau.des.roguelike.dom.equipment;

import ru.spbau.des.roguelike.dom.characters.Player;

public class Drug implements Item {
    private final int value;
    private final String name;
    private final String description;

    public Drug(int value, String name, String description) {
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
}
