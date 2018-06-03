package ru.spbau.des.roguelike.dom.equipment;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.spbau.des.roguelike.dom.environment.HitResult;
import ru.spbau.des.roguelike.dom.environment.Unit;

/**
 * A box unit stores an Item. When hit, it returns the item and dies.
 */
public class BoxUnit implements Unit {
    private static final String LOG_HIT = "BoxUnit with {} hit";
    private final Item item;
    private boolean dead;
    private Logger logger = LogManager.getLogger();

    public BoxUnit(Item item) {
        this.item = item;
        dead = false;
    }

    @Override
    public HitResult takeHit(int strength) {
        logger.debug(LOG_HIT, item.getName());
        dead = true;
        return item;
    }

    @Override
    public boolean isDead() {
        return dead;
    }
}
