package ru.spbau.des.roguelike.ui;

import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import ru.spbau.des.roguelike.operation.Game;

import java.io.IOException;

public class ASCIIInterface {
    private Game game;
    private Screen screen;

    public ASCIIInterface() throws IOException {
        game = new Game();
        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        screen = null;
        Terminal terminal = defaultTerminalFactory.createTerminal();
        screen = new TerminalScreen(terminal);
        screen.startScreen();
    }

    public void run() throws IOException {
        GameScreen gameScreen = new GameScreen(game, screen);
        gameScreen.run();
        GameOverScreen gameOverScreen = new GameOverScreen(game, screen);
        gameOverScreen.show();
        screen.close();
    }

    public static void main(String[] args) {
        try {
            new ASCIIInterface().run();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
