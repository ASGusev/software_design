package ru.spbau.des.roguelike.dom.equipment;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import ru.spbau.des.roguelike.dom.characters.Player;
import ru.spbau.des.roguelike.dom.environment.HitResult;
import ru.spbau.des.roguelike.dom.environment.Unit;

public class TestWeapon {
    @Test
    public void testApply() {
        Player playerMock = Mockito.mock(Player.class);
        Weapon weapon = new Weapon(0, "");
        weapon.apply(playerMock);
        Mockito.verify(playerMock).setWeapon(weapon);
    }

    @Test
    public void testHit() {
        final int power = 100;
        Weapon weapon = new Weapon(power, null);
        Unit unitMock = Mockito.mock(Unit.class);
        HitResult resultMock = Mockito.mock(HitResult.class);
        Mockito.when(unitMock.takeHit(Mockito.anyInt())).thenReturn(resultMock);
        HitResult observedResult = weapon.hit(unitMock);
        Mockito.verify(unitMock).takeHit(power);
        Assert.assertSame(resultMock, observedResult);
    }
}
