package ru.spbau.des.roguelike.ui;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import ru.spbau.des.roguelike.dom.environment.Direction;
import ru.spbau.des.roguelike.dom.environment.FinishUnit;
import ru.spbau.des.roguelike.dom.environment.Position;
import ru.spbau.des.roguelike.dom.environment.Unit;
import ru.spbau.des.roguelike.dom.characters.Monster;
import ru.spbau.des.roguelike.dom.characters.Player;
import ru.spbau.des.roguelike.dom.equipment.BoxUnit;
import ru.spbau.des.roguelike.dom.environment.Field;
import ru.spbau.des.roguelike.dom.environment.WallUnit;
import ru.spbau.des.roguelike.dom.equipment.Item;
import ru.spbau.des.roguelike.operation.PickDecision;
import ru.spbau.des.roguelike.operation.Game;
import ru.spbau.des.roguelike.operation.GameStatus;

import java.io.IOException;

/**
 * The screen that interacts with the player during the game
 */
public class GameScreen {
    private static final String HEALTH_TEMPLATE = "Health: %d.";
    private static final String POWER_TEMPLATE = "Power: %d";
    private static final String ARMOUR_TEMPLATE = "Armour: %d";
    private static final String SCORE_TEMPLATE = "Score: %d";
    private static final String LEVEL_TEMPLATE = "Level: %d";
    private static final int HEALTH_OFFSET = 0;
    private static final int POWER_OFFSET = 1;
    private static final int ARMOUR_OFFSET = 2;
    private static final int SCORE_OFFSET = 3;
    private static final int LEVEL_OFFSET = 4;
    private static final int EQUIPMENT_OFFSET = 6;
    private static final Character PICK_KEY = 'p';
    private static final Character DROP_KEY = 'd';
    private static final String PICK_MESSAGE_TEMPLATE = "Press p to pick %s. %s";
    private static final String EQUIPMENT_HEADER = "Equipment:";
    private static final String DROP_MESSAGE = "Press item number to drop";

    private final Game game;
    private final Screen screen;
    private final TextGraphics textGraphics;
    private final int w;
    private final int h;
    private final int stateX;
    private final int stateY;
    private final Player player;
    private String bottomMessage;

    public GameScreen(Game game, Screen screen) throws IOException {
        this.game = game;
        this.screen = screen;
        this.textGraphics = screen.newTextGraphics();
        player = game.getPlayer();
        w = game.getField().getW();
        h = game.getField().getH();
        stateX = w + 1;
        stateY = 1;
        renderField();
        screen.refresh();
    }

    public void run() throws IOException {
        boolean changesHappened = true;
        boolean dropItem = false;
        PickDecision pickDecision = null;
        while (game.getStatus() == GameStatus.RUNNING) {
            if (changesHappened) {
                updateScreen();
                changesHappened = false;
            }
            Direction direction = null;
            KeyStroke keyStroke = screen.pollInput();
            if (keyStroke == null) {
                continue;
            } else {
                KeyType keyType = keyStroke.getKeyType();
                if (keyType == KeyType.ArrowDown) {
                    direction = Direction.DOWN;
                } else if (keyType == KeyType.ArrowUp) {
                    direction = Direction.UP;
                } else if (keyType == KeyType.ArrowRight) {
                    direction = Direction.RIGHT;
                } else if (keyType == KeyType.ArrowLeft) {
                    direction = Direction.LEFT;
                } else if (keyType == KeyType.Character) {
                    Character keyCharacter = keyStroke.getCharacter();
                    if (keyCharacter.equals(PICK_KEY)) {
                        if (pickDecision != null) {
                            pickDecision.pick();
                            pickDecision = null;
                            bottomMessage = null;
                            updateScreen();
                        }
                    } else if (keyCharacter.equals(DROP_KEY)) {
                        if (dropItem) {
                            dropItem = false;
                            bottomMessage = null;
                            updateScreen();
                        } else {
                            dropItem = true;
                            bottomMessage = DROP_MESSAGE;
                            updateScreen();
                        }
                    } else if (Character.isDigit(keyCharacter)) {
                        int itemIndex = keyCharacter - '1';
                        if (dropItem) {
                            player.dropItem(itemIndex);
                            bottomMessage = null;
                            dropItem = false;
                            updateScreen();
                        } else {
                            game.runItemStep(itemIndex);
                            updateScreen();
                        }
                    }
                }
            }
            if (direction != null) {
                pickDecision = game.runMovementStep(direction);
                bottomMessage = null;
                if (pickDecision != null) {
                    bottomMessage = makePickMessage(pickDecision.getItem());
                    screen.refresh();
                }
                changesHappened = true;
            }
        }
    }

    private String makePickMessage(Item item) {
        return String.format(PICK_MESSAGE_TEMPLATE,
                item.getName(), item.getDescription());
    }

    private void updateScreen() throws IOException {
        screen.clear();
        if (bottomMessage != null) {
            textGraphics.putString(0, h +  1, bottomMessage);
        }
        renderField();
        renderState();
        screen.refresh();
    }

    private void renderField() {
        Field field = game.getField();
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                char view = cellView(field, new Position(j, i));
                screen.setCharacter(j, h - i - 1, new TextCharacter(view));
            }
        }
    }

    private void renderState() {
        textGraphics.putString(stateX, stateY + HEALTH_OFFSET,
                String.format(HEALTH_TEMPLATE, player.getHealth()));
        textGraphics.putString(stateX, stateY + POWER_OFFSET,
                String.format(POWER_TEMPLATE, player.getPower()));
        textGraphics.putString(stateX, stateY + ARMOUR_OFFSET,
                String.format(ARMOUR_TEMPLATE, player.getArmour()));
        textGraphics.putString(stateX, stateY + SCORE_OFFSET,
                String.format(SCORE_TEMPLATE, game.getScore()));
        textGraphics.putString(stateX, stateY + LEVEL_OFFSET,
                String.format(LEVEL_TEMPLATE, game.getLevel()));

        textGraphics.putString(stateX, stateY + EQUIPMENT_OFFSET,
                EQUIPMENT_HEADER);
        int itemNumber = 1;
        for (Item item: player.getBag()) {
            textGraphics.putString(stateX,
                    stateY + EQUIPMENT_OFFSET + itemNumber,
                    makeItemListEntry(item, itemNumber));
            itemNumber++;
        }
    }

    private String makeItemListEntry(Item item, int number) {
        return number + " " + item.getName() + " " + item.getShortDescription();
    }

    private static char cellView(Field field, Position position) {
        if (field.free(position)) {
            return '.';
        }
        Unit unit = field.get(position);
        if (unit instanceof WallUnit) {
            return '#';
        }
        if (unit instanceof Monster) {
            return 'm';
        }
        if (unit instanceof BoxUnit) {
            return 'o';
        }
        if (unit instanceof FinishUnit) {
            return 'F';
        }
        return '@';
    }
}
