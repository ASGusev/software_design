package ru.spbau.des.roguelike.operation;

import ru.spbau.des.roguelike.dom.base.*;
import ru.spbau.des.roguelike.dom.characters.Monster;
import ru.spbau.des.roguelike.dom.characters.Player;
import ru.spbau.des.roguelike.dom.characters.ScoreUpdateReturn;
import ru.spbau.des.roguelike.dom.equipment.Armour;
import ru.spbau.des.roguelike.dom.equipment.Item;
import ru.spbau.des.roguelike.dom.equipment.Weapon;
import ru.spbau.des.roguelike.dom.field.DistanceNavigator;
import ru.spbau.des.roguelike.dom.field.Field;

import java.util.List;

public class Game {
    private static final String INITIAL_WEAPON_NAME = "Fist";
    private static final String ITEM_APPLICATION_REQUEST_TEMPLATE = "Apply %s? %s";
    private static final int INITIAL_WEAPON_POWER = 5;
    private static final int INITIAL_HEALTH = 100;

    private final LevelFactory levelFactory = new LevelFactory();
    private int curLevel;
    private Field field;
    private final Player player;
    private GameStatus status;
    private List<Monster> monsters;
    private int score;

    public Game() {
        player = new Player(new Armour(0),
                new Weapon(INITIAL_WEAPON_POWER, INITIAL_WEAPON_NAME),
                INITIAL_HEALTH);
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

    public Dialog runStep(Direction step) {
        HitReturn playerReturn = player.step(step);
        DistanceNavigator navigator = new DistanceNavigator(field, player.getPosition());
        monsters.removeIf(Monster::isDead);
        monsters.forEach(monster -> monster.step(field, navigator));
        if (player.isDead()) {
            status = GameStatus.LOST;
        }
        if (playerReturn instanceof FinishReturn) {
            updateLevel();
        }
        if (playerReturn instanceof Item) {
            Item returnedItem = (Item) playerReturn;
            return new Dialog() {
                @Override
                public String getMessage() {
                    return String.format(ITEM_APPLICATION_REQUEST_TEMPLATE,
                            returnedItem.getName(), returnedItem.getDescription());
                }

                @Override
                public void accept() {
                    returnedItem.apply(player);
                }

                @Override
                public void deny() {}
            };
        }
        if (playerReturn instanceof ScoreUpdateReturn) {
            score += ((ScoreUpdateReturn) playerReturn).getScoreDelta();
        }
        return null;
    }

    public void updateLevel() {
        ++curLevel;
        if (curLevel > levelFactory.levelsNumber()) {
            status = GameStatus.WON;
            return;
        }
        Level level = levelFactory.getLevel(curLevel);
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
