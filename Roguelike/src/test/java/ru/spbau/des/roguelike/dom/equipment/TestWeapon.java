package ru.spbau.des.roguelike.dom.equipment;

import org.junit.Test;
import org.mockito.Mockito;
import ru.spbau.des.roguelike.dom.characters.Player;

public class TestWeapon {
    @Test
    public void testApply() {
        Player playerMock = Mockito.mock(Player.class);
        Weapon weapon = new Weapon(0, "");
        weapon.apply(playerMock);
        Mockito.verify(playerMock).setWeapon(weapon);
    }
}
