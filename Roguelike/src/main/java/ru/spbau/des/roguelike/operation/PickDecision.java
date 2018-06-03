package ru.spbau.des.roguelike.operation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.spbau.des.roguelike.dom.characters.Player;
import ru.spbau.des.roguelike.dom.equipment.Item;

/**
 * PickDecision allows to pick an item
 */
public class PickDecision {
    private static final String LOG_PICKED = "Picked {}";
    private final Player player;
    private final Item item;
    private boolean used;
    private final static Logger logger = LogManager.getLogger();

    public PickDecision(Player player, Item item) {
        this.player = player;
        this.item = item;
        used = false;
    }

    public void pick() {
        if (!used) {
            player.addItem(item);
            used = true;
            logger.debug(LOG_PICKED, item.getName());
        }
    }

    public Item getItem() {
        return item;
    }
}
