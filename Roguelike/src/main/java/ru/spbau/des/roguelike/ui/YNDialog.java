package ru.spbau.des.roguelike.ui;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;

/**
 * A view capable of asking user a question and returning received answer
 */
public class YNDialog {
    private static final String YN_SUFFIX = " (Y/N)";
    private final Screen screen;
    private final String message;
    private final int column;
    private final int row;

    /**
     * Creates a dialog asking the provided question
     * @param screen a screen of which the message will be shown
     * @param message the text to show user
     * @param column the column of the first letter
     * @param row the row of characters used to print the message
     */
    public YNDialog(Screen screen, String message, int column, int row) {
        this.screen = screen;
        this.message = message;
        this.column = column;
        this.row = row;
    }

    /**
     * Asks user the question and returns his answer
     * @return the player's answer
     * @throws IOException
     */
    boolean ask() throws IOException {
        TextGraphics textGraphics = screen.newTextGraphics();
        textGraphics.putString(column, row, message + YN_SUFFIX);
        screen.refresh();
        while (true) {
            KeyStroke keyStroke = screen.pollInput();
            if (keyStroke == null || keyStroke.getKeyType() != KeyType.Character) {
                continue;
            }
            if (keyStroke.getCharacter() == 'y') {
                return true;
            }
            if (keyStroke.getCharacter() == 'n') {
                return false;
            }
        }
    }
}
