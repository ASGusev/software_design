package ru.spbau.des.roguelike.dom.characters;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import ru.spbau.des.roguelike.dom.environment.Direction;
import ru.spbau.des.roguelike.dom.environment.HitResult;
import ru.spbau.des.roguelike.dom.environment.Position;
import ru.spbau.des.roguelike.dom.environment.Unit;
import ru.spbau.des.roguelike.dom.equipment.Armour;
import ru.spbau.des.roguelike.dom.equipment.Weapon;
import ru.spbau.des.roguelike.dom.environment.Field;

public class TestPlayer {
    @Test
    public void testTakeHit() {
        Player player = new Player(new Armour(0), null, 100);
        player.takeHit(50);
        Assert.assertEquals(50, player.getHealth());
    }

    @Test
    public void testArmour() {
        Player player = new Player(new Armour(20), null, 100);
        player.takeHit(50);
        Assert.assertEquals(60, player.getHealth());
    }

    @Test
    public void testDeath() {
        Player player = new Player(new Armour(0), null, 100);
        Assert.assertFalse(player.isDead());
        player.takeHit(50);
        Assert.assertFalse(player.isDead());
        player.takeHit(50);
        Assert.assertTrue(player.isDead());
    }

    @Test
    public void testStep() {
        Position zeroPosition = new Position(0, 0);
        Position targetPosition = new Position(1, 0);
        Player player = new Player(null, null, 100);
        Field fieldMock = Mockito.mock(Field.class);
        Mockito.when(fieldMock.freeAt(targetPosition)).thenReturn(true);
        player.setField(fieldMock);
        player.setPosition(zeroPosition);
        Assert.assertNull(player.step(Direction.RIGHT));
        Mockito.verify(fieldMock).freeAt(targetPosition);
        Mockito.verify(fieldMock).move(zeroPosition, targetPosition);
    }

    @Test
    public void testAttack() {
        final int power = 100;
        Position zeroPosition = new Position(0, 0);
        Position targetPosition = new Position(1, 0);
        Player player = new Player(null, new Weapon(power, ""), 100);

        HitResult returnMock = Mockito.mock(HitResult.class);
        Unit unitMock = Mockito.mock(Unit.class);
        Mockito.when(unitMock.takeHit(power)).thenReturn(returnMock);

        Field fieldMock = Mockito.mock(Field.class);
        Mockito.when(fieldMock.freeAt(targetPosition)).thenReturn(false);
        Mockito.when(fieldMock.get(targetPosition)).thenReturn(unitMock);

        player.setField(fieldMock);
        player.setPosition(zeroPosition);

        HitResult stepReturn = player.step(Direction.RIGHT);

        Assert.assertSame(returnMock, stepReturn);
        Mockito.verify(fieldMock).freeAt(targetPosition);
        Mockito.verify(unitMock).takeHit(power);
    }
}
