package ru.spbau.des.roguelike.dom.environment;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import static ru.spbau.des.roguelike.dom.environment.FieldPlan.Cell.*;

public class TestNavigator {
    private static final FieldPlan.Cell[][] CELLS = {
            { EMPTY, EMPTY, EMPTY, EMPTY, EMPTY , WALL, EMPTY},
            { EMPTY, WALL, EMPTY, EMPTY, EMPTY, INDESTRUCTIBLE_WALL, EMPTY}};
    private static final int[][] ANSWERS = {
            {3, 2, 1, 2, 3, -1, -1},
            {4, -1, 0, 1, 2, -1, -1}};
    private static final int W = CELLS.length;
    private static final int H = CELLS[0].length;

    @Test
    public void test() {
        Field fieldMock = Mockito.mock(Field.class);
        Mockito.when(fieldMock.get(Mockito.any()))
                .thenAnswer(invocation -> {
                    Integer x = ((Position)invocation.getArgument(0)).getX();
                    Integer y = ((Position)invocation.getArgument(0)).getY();
                    if (CELLS[x][y] == WALL) {
                        return new WallUnit(true);
                    } else if (CELLS[x][y] == INDESTRUCTIBLE_WALL) {
                        return new WallUnit(false);
                    }
                    return null;
                });
        Mockito.when(fieldMock.valid(Mockito.any())).thenAnswer(invocation -> {
            Integer x = ((Position)invocation.getArgument(0)).getX();
            Integer y = ((Position)invocation.getArgument(0)).getY();
            return x >= 0 && y >= 0 && x < W && y < H;
        });
        Mockito.when(fieldMock.getH()).thenReturn(H);
        Mockito.when(fieldMock.getW()).thenReturn(W);
        DistanceNavigator navigator =
                new DistanceNavigator(fieldMock, new Position(1, 2));
        for (int i = 0; i < W; i++) {
            for (int j = 0; j < H; j++) {
                Assert.assertEquals(ANSWERS[i][j], navigator.distanceAt(new Position(i, j)));
            }
        }
    }
}
