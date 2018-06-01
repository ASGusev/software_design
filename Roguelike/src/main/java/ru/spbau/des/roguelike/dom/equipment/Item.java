package ru.spbau.des.roguelike.dom.equipment;

import ru.spbau.des.roguelike.dom.base.HitReturn;
import ru.spbau.des.roguelike.dom.characters.Player;

public interface Item extends HitReturn {
    void apply(Player player);

    String getName();

    String getDescription();
}
