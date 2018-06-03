package ru.spbau.des.roguelike.operation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.spbau.des.roguelike.dom.characters.Monster;
import ru.spbau.des.roguelike.dom.characters.Player;
import ru.spbau.des.roguelike.dom.characters.ScoreResult;
import ru.spbau.des.roguelike.dom.environment.*;
import ru.spbau.des.roguelike.dom.equipment.Item;

import java.util.List;

/**
 * Represents an episode of playing the game
 */
public class Game {
    private static final String LOG_ENTERED_LEVEL = "Entered level {}";
    private static final String LOG_WON = "Game won";
    private static final String LOG_FOUND = "Item found: {}";
    private static final String LOG_POINTS = "Points received: {}";
    private static final String LOG_STEP = "Step {} from {} {}";
    private static final String LOG_CREATED = "Game created";
    private final Logger logger = LogManager.getLogger();
    private final GameConfigurator configurator;
    private int curLevel;
    private Field field;
    private final Player player;
    private GameStatus status;
    private List<Monster> monsters;
    private int score;

    public Game(GameConfigurator configurator) {
        this.configurator = configurator;
        player = configurator.getPlayer();
        curLevel = 0;
        status = GameStatus.RUNNING;
        updateLevel();
        score = 0;
        logger.info(LOG_CREATED);
    }

    public int getLevel() {
        return curLevel;
    }

    public int getScore() {
        return score;
    }

    public Field getField() {
        return field;
    }

    /**
     * Runs one step of the game
     * @param step the direction of the player
     * @return a PickDecision if a question to the gamer emerges. null otherwise
     */
    public PickDecision runMovementStep(Direction step) {
        logger.debug(LOG_STEP, step, player.getPosition().getX(),
                player.getPosition().getY());
        HitResult playerReturn = player.step(step);
        monsters.removeIf(Monster::isDead);
        if (playerReturn instanceof FinishResult) {
            updateLevel();
            return null;
        }
        runMonsters();
        if (playerReturn instanceof Item) {
            logger.debug(LOG_FOUND, ((Item) playerReturn).getName());
            Item returnedItem = (Item) playerReturn;
            return new PickDecision(player, returnedItem);
        }
        if (playerReturn instanceof ScoreResult) {
            logger.debug(LOG_POINTS,
                    ((ScoreResult) playerReturn).getScoreDelta());
            score += ((ScoreResult) playerReturn).getScoreDelta();
        }
        if (player.isDead()) {
            status = GameStatus.LOST;
        }
        return null;
    }

    public void runItemStep(int itemIndex) {
        player.applyItem(itemIndex);
        runMonsters();
    }

    private void runMonsters() {
        DistanceNavigator navigator = new DistanceNavigator(field, player.getPosition());
        monsters.forEach(monster -> monster.step(field, navigator));
    }

    public void updateLevel() {
        ++curLevel;
        if (curLevel > configurator.getLevelsNumber()) {
            status = GameStatus.WON;
            logger.info(LOG_WON);
            return;
        }
        logger.info(LOG_ENTERED_LEVEL, curLevel);
        Level level = configurator.getLevel(curLevel);
        field = level.getField();
        player.setPosition(level.getStart());
        player.setField(field);
        field.put(level.getStart(), player);
        field.put(level.getFinish(), new FinishUnit());
        monsters = level.getMonsters();
    }

    public GameStatus getStatus() {
        return status;
    }

    public Player getPlayer() {
        return player;
    }
}
