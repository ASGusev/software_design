package ru.spbau.des.roguelike.operation;

import ru.spbau.des.roguelike.dom.base.Position;
import ru.spbau.des.roguelike.dom.base.Unit;
import ru.spbau.des.roguelike.dom.characters.Monster;
import ru.spbau.des.roguelike.dom.equipment.Armour;
import ru.spbau.des.roguelike.dom.equipment.BoxUnit;
import ru.spbau.des.roguelike.dom.equipment.Drug;
import ru.spbau.des.roguelike.dom.equipment.Weapon;
import ru.spbau.des.roguelike.dom.field.DistanceNavigator;
import ru.spbau.des.roguelike.dom.field.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class LevelFactory {
    private final static int MIN_TO_GO = 20;
    private static final String HERB_ITEM_NAME = "Herb";
    private static final String HERB_ITEM_DESCRIPTION = "Affects health. Usually positively.";
    private final Random random = new Random();

    private final static LevelProperties[] LEVEL_PROPERTIES = {
            new LevelProperties(7, 5, 2, 2,
                    8, 10, 10,
                    new Armour(10), new Weapon(10, "Knife")),
            new LevelProperties(7, 6, 3, 2,
                    16, 16, 10,
                    new Armour(20), new Weapon(15, "Machete")),
            new LevelProperties(7, 6, 3, 2,
                    24, 30, 10,
                    new Armour(25), new Weapon(30, "Axe")),
            new LevelProperties(7, 6, 2, 2,
                    40, 40, 10,
                    new Armour(30), new Weapon(40, "Sword"))
    };

    public int levelsNumber() {
        return LEVEL_PROPERTIES.length;
    }

    public Level getLevel(int number) {
        return makeLevel(LEVEL_PROPERTIES[number - 1]);
    }

    private Position randomPosition(Field field) {
        Position position = new Position(random.nextInt(field.getW()),
                random.nextInt(field.getH()));
        while (!field.freeAt(position)) {
            position = new Position(random.nextInt(field.getW()),
                    random.nextInt(field.getH()));
        }
        return position;
    }

    private void splitUnits(Field field, int repeats, Function<Position, Unit> unitFactory) {
        for (int i = 0; i < repeats; i++) {
            Position position = randomPosition(field);
            if (field.freeAt(position)) {
                field.put(position, unitFactory.apply(position));
            }
        }
    }

    private Level makeLevel(LevelProperties properties) {
        Field field = new MapCreator().createMap();
        MonsterFactory monsterFactory = new MonsterFactory(properties.getMonsterPower(),
                properties.getMonsterHealth());
        splitUnits(field, properties.getMonsters(),
                monsterFactory);
        splitUnits(field, properties.getDrugs(),
                new DrugFactory(properties.getAvgDrug(), HERB_ITEM_NAME,
                        HERB_ITEM_DESCRIPTION));
        splitUnits(field, properties.getArmours(),
                p -> new BoxUnit(properties.getArmour()));
        splitUnits(field, properties.getWeapons(),
                p -> new BoxUnit(properties.getWeapon()));
        Position startingPosition = randomPosition(field);
        Position finishPosition = randomPosition(field);
        DistanceNavigator distance = new DistanceNavigator(field, startingPosition);
        while (distance.distanceAt(finishPosition) < MIN_TO_GO) {
            finishPosition = randomPosition(field);
        }
        return new Level(field, startingPosition, finishPosition,
                monsterFactory.getAllMonsters());
    }

    private static class MonsterFactory implements Function<Position, Unit> {
        int counter = 1;
        private final int power;
        private final int health;
        private final List<Monster> allMonsters = new ArrayList<>();

        public MonsterFactory(int power, int health) {
            this.power = power;
            this.health = health;
        }

        @Override
        public Monster apply(Position position) {
            Monster newMonster = new Monster(counter++, power, health, position);
            allMonsters.add(newMonster);
            return newMonster;
        }

        public List<Monster> getAllMonsters() {
            return allMonsters;
        }
    }

    private class DrugFactory implements Function<Position, Unit> {
        private final double expectation;
        private final String name;
        private final String description;

        public DrugFactory(double expectation, String name, String description) {
            this.expectation = expectation;
            this.name = name;
            this.description = description;
        }

        @Override
        public Unit apply(Position position) {
            return new BoxUnit(new Drug((int)(expectation * (random.nextGaussian() + 1)),
                    name, description));
        }
    }

    private static class LevelProperties {
        private final int monsters;
        private final int drugs;
        private final int armours;
        private final int weapons;
        private final int monsterPower;
        private final int monsterHealth;
        private final int avgDrug;
        private final Armour armour;
        private final Weapon weapon;

        public LevelProperties(int monsters, int drugs, int armours,
                               int maxWeapons, int monsterPower, int monsterHealth,
                               int avgDrug, Armour armour, Weapon weapon) {
            this.monsters = monsters;
            this.drugs = drugs;
            this.armours = armours;
            this.weapons = maxWeapons;
            this.monsterPower = monsterPower;
            this.monsterHealth = monsterHealth;
            this.avgDrug = avgDrug;
            this.armour = armour;
            this.weapon = weapon;
        }

        public int getMonsters() {
            return monsters;
        }

        public int getDrugs() {
            return drugs;
        }

        public int getArmours() {
            return armours;
        }

        public int getWeapons() {
            return weapons;
        }

        public int getMonsterPower() {
            return monsterPower;
        }

        public int getMonsterHealth() {
            return monsterHealth;
        }

        public int getAvgDrug() {
            return avgDrug;
        }

        public Armour getArmour() {
            return armour;
        }

        public Weapon getWeapon() {
            return weapon;
        }
    }
}
