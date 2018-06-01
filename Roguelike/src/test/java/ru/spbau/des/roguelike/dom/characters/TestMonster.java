package ru.spbau.des.roguelike.dom.characters;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import ru.spbau.des.roguelike.dom.base.Direction;
import ru.spbau.des.roguelike.dom.base.Position;
import ru.spbau.des.roguelike.dom.base.Unit;
import ru.spbau.des.roguelike.dom.field.DistanceNavigator;
import ru.spbau.des.roguelike.dom.field.Field;

public class TestMonster {
    @Test
    public void testTakeHit() {
        Monster monster = new Monster(0, 0, 100, null);
        monster.takeHit(50);
        Assert.assertEquals(50, monster.getHealth());
    }

    @Test
    public void testDeath() {
        Monster monster = new Monster(0, 0, 100, null);
        Assert.assertFalse(monster.isDead());
        monster.takeHit(50);
        Assert.assertFalse(monster.isDead());
        monster.takeHit(50);
        Assert.assertTrue(monster.isDead());
    }

    @Test
    public void testStep() {
        Position zeroPosition = new Position(0, 0);
        Position targetPosition = new Position(1, 0);
        Monster monster = new Monster(0, 0, 100, zeroPosition);
        Field fieldMock = Mockito.mock(Field.class);
        Mockito.when(fieldMock.freeAt(Mockito.any())).thenReturn(false);
        Mockito.when(fieldMock.freeAt(targetPosition)).thenReturn(true);
        DistanceNavigator navigatorMock = Mockito.mock(DistanceNavigator.class);
        Mockito.when(navigatorMock.distanceAt(Mockito.any())).thenReturn(-1);
        Mockito.when(navigatorMock.distanceAt(zeroPosition)).thenReturn(2);
        Mockito.when(navigatorMock.distanceAt(targetPosition)).thenReturn(1);
        monster.step(fieldMock, navigatorMock);
        Mockito.verify(fieldMock).move(zeroPosition, Direction.RIGHT);
    }

    @Test
    public void testAttack() {
        final int power = 100;
        Position zeroPosition = new Position(0, 0);
        Position targetPosition = new Position(1, 0);
        Monster monster = new Monster(0, power, 100, zeroPosition);
        Field fieldMock = Mockito.mock(Field.class);
        DistanceNavigator navigatorMock = Mockito.mock(DistanceNavigator.class);
        Mockito.when(navigatorMock.distanceAt(Mockito.any())).thenReturn(-1);
        Mockito.when(navigatorMock.distanceAt(zeroPosition)).thenReturn(1);
        Mockito.when(navigatorMock.distanceAt(targetPosition)).thenReturn(0);
        Unit unitMock = Mockito.mock(Unit.class);
        Mockito.when(fieldMock.get(targetPosition)).thenReturn(unitMock);
        monster.step(fieldMock, navigatorMock);
        Mockito.verify(unitMock).takeHit(power);
    }
}
