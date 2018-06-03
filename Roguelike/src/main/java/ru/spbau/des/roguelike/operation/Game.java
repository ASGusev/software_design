package ru.spbau.des.roguelike.operation;

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
    private static final String ITEM_APPLICATION_REQUEST_TEMPLATE = "Apply %s? %s";

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
        HitResult playerReturn = player.step(step);
        monsters.removeIf(Monster::isDead);
        if (playerReturn instanceof FinishResult) {
            updateLevel();
            return null;
        }
        runMonsters();
        if (playerReturn instanceof Item) {
            Item returnedItem = (Item) playerReturn;
            return new PickDecision(player, returnedItem);
        }
        if (playerReturn instanceof ScoreResult) {
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
            return;
        }
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
