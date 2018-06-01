package ru.spbau.des.roguelike.dom.equipment;

import org.junit.Test;
import org.mockito.Mockito;
import ru.spbau.des.roguelike.dom.characters.Player;

public class TestArmour {
    @Test
    public void testApply() {
        Player playerMock = Mockito.mock(Player.class);
        Armour armour = new Armour(0);
        armour.apply(playerMock);
        Mockito.verify(playerMock).setArmour(armour);
    }
}
