package ru.spbau.des.roguelike.dom.equipment;

import org.junit.Test;
import org.mockito.Mockito;
import ru.spbau.des.roguelike.dom.characters.Player;

public class TestDrug {
    @Test
    public void test() {
        final int value = 10;
        Player playerMock = Mockito.mock(Player.class);
        Drug drug = new Drug(value, "", "");
        drug.apply(playerMock);
        Mockito.verify(playerMock).updateHealth(value);
    }
}
