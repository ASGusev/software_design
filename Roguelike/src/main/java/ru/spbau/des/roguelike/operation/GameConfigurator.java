package ru.spbau.des.roguelike.operation;

import ru.spbau.des.roguelike.dom.characters.Player;

/**
 * GameConfigurator provides game with parameters.
 */
public interface GameConfigurator {
    /**
     * Creates a player which does not yet have a field and coordinates.
     * @return a new player
     */
    Player getPlayer();

    /**
     * Creates a level with provided number. The number defines parameters affecting
     * difficulty.
     * @param number the number of the level in the game
     * @return the created level
     */
    Level getLevel(int number);

    int getLevelsNumber();
}
