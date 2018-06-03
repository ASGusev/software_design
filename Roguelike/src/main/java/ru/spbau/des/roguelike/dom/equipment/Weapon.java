package ru.spbau.des.roguelike.dom.equipment;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.spbau.des.roguelike.dom.characters.Player;
import ru.spbau.des.roguelike.dom.environment.HitResult;
import ru.spbau.des.roguelike.dom.environment.Unit;

/**
 * Player uses a weapon to hit other units.
 */
public class Weapon implements Item {
    private final static String DESCRIPTION_TEMPLATE = "A weapon with damage %d.";
    private final static String SHORT_DESCRIPTION_PREF = "Weapon ";
    private static final String LOG_HITTING = "Hitting with {}";

    private final int power;
    private final String name;
    private final Logger logger = LogManager.getLogger();

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

    @Override
    public String getShortDescription() {
        return SHORT_DESCRIPTION_PREF + power;
    }

    /**
     * Hits a unit with the weapon's power
     * @param unit the unit to hit
     * @return value returned by the hit unit
     */
    public HitResult hit(Unit unit) {
        logger.debug(LOG_HITTING, name);
        return unit.takeHit(power);
    }
}
