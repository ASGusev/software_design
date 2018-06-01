package ru.spbau.des.roguelike.dom.equipment;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class TestBoxUnit {
    @Test
    public void test() {
        Item itemMock = Mockito.mock(Item.class);
        BoxUnit boxUnit = new BoxUnit(itemMock);
        Assert.assertSame(itemMock, boxUnit.takeHit(0));
    }
}
