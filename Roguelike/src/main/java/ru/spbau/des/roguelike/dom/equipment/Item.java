package ru.spbau.des.roguelike.dom.equipment;

import ru.spbau.des.roguelike.dom.environment.HitResult;
import ru.spbau.des.roguelike.dom.characters.Player;

public interface Item extends HitResult {
    void apply(Player player);

    String getName();

    String getDescription();
}
