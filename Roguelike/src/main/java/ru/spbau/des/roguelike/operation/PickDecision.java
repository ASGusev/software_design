package ru.spbau.des.roguelike.operation;

import ru.spbau.des.roguelike.dom.characters.Player;
import ru.spbau.des.roguelike.dom.equipment.Item;

/**
 * PickDecision allows to pick an item
 */
public class PickDecision {
    private final Player player;
    private final Item item;

    public PickDecision(Player player, Item item) {
        this.player = player;
        this.item = item;
    }

    public void pick() {
        item.apply(player);
    }

    public Item getItem() {
        return item;
    }
}
