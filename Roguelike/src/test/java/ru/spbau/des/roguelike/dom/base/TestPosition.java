package ru.spbau.des.roguelike.dom.base;

import org.junit.Assert;
import org.junit.Test;

public class TestPosition {
    @Test
    public void testResolution() {
        Position position = new Position(5, 6);
        Direction direction = Direction.DOWN;
        Assert.assertEquals(new Position(5, 5), position.resolve(direction));
    }

    @Test
    public void testEquality() {
        Assert.assertEquals(new Position(300, 100), new Position(300, 100));
        Assert.assertNotEquals(new Position(300, 100), new Position(100, 300));
    }
}
