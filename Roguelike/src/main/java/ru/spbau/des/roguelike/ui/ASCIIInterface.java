package ru.spbau.des.roguelike.ui;

import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.spbau.des.roguelike.operation.DefaultFieldPlanCreator;
import ru.spbau.des.roguelike.operation.Game;
import ru.spbau.des.roguelike.operation.DefaultGameConfigurator;

import java.io.IOException;

/**
 * A text interface of the game.
 */
public class ASCIIInterface {
    private static final String LOG_CREATED = "ASCIIInterface created";
    private Game game;
    private Screen screen;
    private final static Logger logger = LogManager.getLogger();

    public ASCIIInterface(Game game) throws IOException {
        this.game = game;
        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        screen = null;
        Terminal terminal = defaultTerminalFactory.createTerminal();
        screen = new TerminalScreen(terminal);
        logger.info(LOG_CREATED);
    }

    /**
     * Runs the game in a new screen
     * @throws IOException
     */
    public void run() throws IOException {
        screen.startScreen();
        GameScreen gameScreen = new GameScreen(game, screen);
        gameScreen.run();
        GameOverScreen gameOverScreen = new GameOverScreen(game, screen);
        gameOverScreen.show();
        screen.close();
    }

    public static void main(String[] args) {
        try {
            DefaultFieldPlanCreator fieldPlanCreator = new DefaultFieldPlanCreator();
            DefaultGameConfigurator gameConfigurator =
                    new DefaultGameConfigurator(fieldPlanCreator);
            Game game = new Game(gameConfigurator);
            new ASCIIInterface(game).run();
        } catch (IOException e) {
            logger.error(e);
        }
    }
}
