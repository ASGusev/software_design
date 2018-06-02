package ru.spbau.des.roguelike.operation;

import ru.spbau.des.roguelike.dom.characters.Player;

public interface GameConfigurator {
    Player getPlayer();

    Level getLevel(int number);

    int getLevelsNumber();
}
