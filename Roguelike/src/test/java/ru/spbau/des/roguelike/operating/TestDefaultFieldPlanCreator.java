package ru.spbau.des.roguelike.operating;

import org.junit.Assert;
import org.junit.Test;
import ru.spbau.des.roguelike.dom.environment.Direction;
import ru.spbau.des.roguelike.dom.environment.FieldPlan;
import ru.spbau.des.roguelike.dom.environment.Position;
import ru.spbau.des.roguelike.operation.DefaultFieldPlanCreator;
import ru.spbau.des.roguelike.operation.FieldPlanCreator;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class TestDefaultFieldPlanCreator {
    @Test
    public void testConnectivity() {
        FieldPlanCreator creator = new DefaultFieldPlanCreator();
        FieldPlan plan = creator.createPlan();
        int w = plan.getW();
        int h = plan.getH();
        Set<Position> accessible = null;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (plan.get(i, j) == FieldPlan.Cell.EMPTY) {
                    Position cur = new Position(i, j);
                    if (accessible == null) {
                        accessible = collectAccessible(plan, cur);
                    } else {
                        Assert.assertTrue(accessible.contains(cur));
                    }
                }
            }
        }
        for (Position accessiblePosition: accessible) {
            Assert.assertEquals(FieldPlan.Cell.EMPTY, plan.get(accessiblePosition));
        }
    }

    private Set<Position> collectAccessible(FieldPlan plan, Position start) {
        int w = plan.getW();
        int h = plan.getH();
        Queue<Position> positions = new LinkedList<>();
        Set<Position> accessible = new HashSet<>();
        accessible.add(start);
        positions.offer(start);
        while (!positions.isEmpty()) {
            Position curPosition = positions.poll();
            for (Direction direction: Direction.values()) {
                Position nextPosition = curPosition.resolve(direction);
                if (nextPosition.getX() < 0 || nextPosition.getX() >= w ||
                        nextPosition.getY() < 0 || nextPosition.getY() >= h) {
                    continue;
                }
                if (!accessible.contains(nextPosition) &&
                        plan.get(nextPosition) == FieldPlan.Cell.EMPTY) {
                    accessible.add(nextPosition);
                    positions.offer(nextPosition);
                }
            }
        }
        return accessible;
    }
}
