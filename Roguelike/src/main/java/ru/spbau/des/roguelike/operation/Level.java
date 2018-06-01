package ru.spbau.des.roguelike.operation;

import ru.spbau.des.roguelike.dom.environment.Position;
import ru.spbau.des.roguelike.dom.characters.Monster;
import ru.spbau.des.roguelike.dom.environment.Field;

import java.util.List;

public class Level {
    private final Field field;
    private final Position start;
    private final Position finish;
    private final List<Monster> monsters;

    public Level(Field field, Position start, Position finish, List<Monster> monsters) {
        this.field = field;
        this.start = start;
        this.finish = finish;
        this.monsters = monsters;
    }

    public Field getField() {
        return field;
    }

    public Position getStart() {
        return start;
    }

    public Position getFinish() {
        return finish;
    }

    public List<Monster> getMonsters() {
        return monsters;
    }
}
