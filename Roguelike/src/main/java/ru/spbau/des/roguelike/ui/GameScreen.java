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
import ru.spbau.des.roguelike.operation.Dialog;
import ru.spbau.des.roguelike.operation.Game;
import ru.spbau.des.roguelike.operation.GameStatus;

import java.io.IOException;

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
    private final Game game;
    private final Screen screen;
    private final int w;
    private final int h;
    private final int stateX;
    private final int stateY;
    private final Player player;

    public GameScreen(Game game, Screen screen) throws IOException {
        this.game = game;
        this.screen = screen;
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
        while (game.getStatus() == GameStatus.RUNNING) {
            if (changesHappened) {
                screen.clear();
                renderField();
                renderState();
                screen.refresh();
                changesHappened = false;
            }
            Direction direction = getDirection();
            if (direction != null) {
                Dialog stepReturn = game.runStep(direction);
                if (stepReturn != null) {
                    YNDialog applyDialog = new YNDialog(screen,
                            stepReturn.getMessage(), 0, h);
                    if (applyDialog.ask()) {
                        stepReturn.accept();
                    } else {
                        stepReturn.deny();
                    }
                }
                changesHappened = true;
            }
        }
    }

    private Direction getDirection() throws IOException {
        KeyStroke keyStroke = screen.pollInput();
        if (keyStroke == null) {
            return null;
        }
        if (keyStroke.getKeyType() == KeyType.ArrowDown) {
            return Direction.DOWN;
        }
        if (keyStroke.getKeyType() == KeyType.ArrowUp) {
            return Direction.UP;
        }
        if (keyStroke.getKeyType() == KeyType.ArrowRight) {
            return Direction.RIGHT;
        }
        if (keyStroke.getKeyType() == KeyType.ArrowLeft) {
            return Direction.LEFT;
        }
        return null;
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
        TextGraphics textGraphics = screen.newTextGraphics();
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
    }

    private static char cellView(Field field, Position position) {
        if (field.freeAt(position)) {
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
