package ru.spbau.des.roguelike.dom.equipment;

import ru.spbau.des.roguelike.dom.environment.HitResult;
import ru.spbau.des.roguelike.dom.characters.Player;

/**
 * Item is a piece of equipment that can be returned from a unit hit and applied to
 * the player
 */
public interface Item extends HitResult {
    void apply(Player player);

    String getName();

    String getDescription();

    String getShortDescription();
}
