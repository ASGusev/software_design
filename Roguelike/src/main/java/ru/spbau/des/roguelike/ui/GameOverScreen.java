package ru.spbau.des.roguelike.ui;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import ru.spbau.des.roguelike.operation.Game;
import ru.spbau.des.roguelike.operation.GameStatus;

import java.io.IOException;

public class GameOverScreen {
    private final static String VICTORY_MSG = "You won!";
    private final static String LOSS_MSG = "You lost";
    private final static String SCORE_TEMPLATE = "Score: %d";
    private final static String PRESS_ANY_KEY = "Press any key to quit";

    private Game game;
    private Screen screen;

    public GameOverScreen(Game game, Screen screen) {
        this.game = game;
        this.screen = screen;
    }

    public void show() throws IOException {
        screen.clear();
        TextGraphics textGraphics = screen.newTextGraphics();
        int midRow = game.getField().getH() / 2;
        if (game.getStatus() == GameStatus.WON) {
            printInCenter(VICTORY_MSG, midRow - 1, textGraphics);
            printInCenter(String.format(SCORE_TEMPLATE, game.getScore()), midRow,
                    textGraphics);
        } else {
            printInCenter(LOSS_MSG, midRow, textGraphics);
        }
        printInCenter(PRESS_ANY_KEY, midRow + 1, textGraphics);
        screen.refresh();
        boolean done = false;
        while (!done) {
            KeyStroke keyStroke = screen.pollInput();
            done = keyStroke != null;
        }
    }

    private void printInCenter(String message, int row, TextGraphics textGraphics) {
        int column = game.getField().getW() / 2 - message.length() / 2;
        textGraphics.putString(column, row, message);
    }
}
