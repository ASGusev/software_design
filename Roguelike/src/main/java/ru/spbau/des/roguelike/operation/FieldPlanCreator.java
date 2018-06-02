package ru.spbau.des.roguelike.operation;

import ru.spbau.des.roguelike.dom.environment.*;

import java.util.*;

/**
 * This class contains logic of map creation
 */
public class FieldPlanCreator {
    private static final int W = 50;
    private static final int H = 20;
    private static final int MAX_SIDE = 16;
    private static final int MIN_SIDE = 4;
    private static final int INDESTRUCTIBLE_WALLS = 15;
    private static final double MIN_SIDE_DIVISOR = 1.8;
    private static final double MAX_SIDE_DIVISOR = 1.2;
    private final Random randomGenerator = new Random();

    public FieldPlan createMap() {
        FieldPlan plan = new FieldPlan(W, H);
        strengthenBorder(plan);
        makeRooms(plan, 1, W - 1, 1, H - 1, false);
        connectRooms(plan);
        strengthenRandomWalls(plan);
        return plan;
    }

    private void makeRooms(FieldPlan plan, int x1, int x2, int y1, int y2, boolean splitX) {
        if (x2 - x1 < MIN_SIDE || y2 - y1 < MIN_SIDE){
            return;
        }
        if (x2 - x1 < MAX_SIDE && y2 - y1 < MAX_SIDE) {
            int minH = (int)((x2 - x1) / MIN_SIDE_DIVISOR);
            int maxH = (int)((x2 - x1) / MAX_SIDE_DIVISOR);
            int h = randomGenerator.nextInt(maxH - minH) + minH;
            int minW = (int)((y2 - y1) / MIN_SIDE_DIVISOR);
            int maxW = (int)((y2 - y1) / MAX_SIDE_DIVISOR);
            int w = randomGenerator.nextInt(maxW - minW) + minW;
            int sx = x1 + randomGenerator.nextInt(x2 - x1 - h);
            int sy = y1 + randomGenerator.nextInt(y2 - y1 - w);
            for (int i = sx; i <  sx + h; i++) {
                for (int j = sy; j < sy + w; j++) {
                    plan.set(i, j, FieldPlan.Cell.EMPTY);
                }
            }
        }
        else if (x2 - x1 >= MAX_SIDE && (y2 - y1 < MAX_SIDE || splitX)) {
            int xm = x1 + MIN_SIDE + randomGenerator.nextInt(x2 - x1 - 2 * MIN_SIDE);
            makeRooms(plan, x1, xm, y1, y2, !splitX);
            makeRooms(plan, xm, x2, y1, y2, !splitX);
        }
        else {
            int ym = y1 + MIN_SIDE + randomGenerator.nextInt(y2 - y1 - 2 * MIN_SIDE);
            makeRooms(plan, x1, x2, y1, ym, !splitX);
            makeRooms(plan, x1, x2, ym, y2, !splitX);
        }
    }

    private void connectRooms(FieldPlan plan) {
        Set<Position> connected = new HashSet<>();
        List<Position> allCells = new ArrayList<>();
        for (int i = 0; i < W; i++) {
            for (int j = 0; j < H; j++) {
                allCells.add(new Position(i, j));
            }
        }
        Collections.shuffle(allCells);
        for (Position position: allCells) {
            if (plan.get(position) != FieldPlan.Cell.EMPTY ||
                    connected.contains(position)) {
                continue;
            }
            if (connected.isEmpty()) {
                connected = getAccessible(plan, position);
            } else {
                Position fromConnected = connected.iterator().next();
                digTunnel(plan, fromConnected.getX(), fromConnected.getY(),
                        position.getX(), position.getY());
                connected = getAccessible(plan, position);
            }
        }
    }

    private Set<Position> getAccessible(FieldPlan plan, Position position) {
        Queue<Position> positions = new ArrayDeque<>();
        Set<Position> accessible = new HashSet<>();
        positions.offer(position);
        accessible.add(position);
        while (!positions.isEmpty()) {
            Position curPosition = positions.poll();
            for (Direction direction: Direction.values()) {
                Position nextPosition = curPosition.resolve(direction);
                if (plan.get(nextPosition) == FieldPlan.Cell.EMPTY &&
                        !accessible.contains(nextPosition)) {
                    positions.offer(nextPosition);
                    accessible.add(nextPosition);
                }
            }
        }
        return accessible;
    }

    private void digTunnel(FieldPlan plan, int sx, int sy, int tx, int ty) {
        int cx = sx;
        int cy = sy;
        while (cx != tx) {
            int dx = (tx - cx) / Math.abs(tx - cx);
            cx += dx;
            plan.set(cx, cy, FieldPlan.Cell.EMPTY);
        }
        while (cy != ty) {
            int dy = (ty - cy) / Math.abs(ty - cy);
            cy += dy;
            plan.set(cx, cy, FieldPlan.Cell.EMPTY);
        }
    }

    private void strengthenBorder(FieldPlan plan) {
        for (int i = 0; i < W; i++) {
            plan.set(i, 0, FieldPlan.Cell.INDESTRUCTIBLE_WALL);
            plan.set(i, H - 1, FieldPlan.Cell.INDESTRUCTIBLE_WALL);
        }
        for (int j = 0; j < H; j++) {
            plan.set(0, j, FieldPlan.Cell.INDESTRUCTIBLE_WALL);
            plan.set(W - 1, j, FieldPlan.Cell.INDESTRUCTIBLE_WALL);
        }
    }

    private void strengthenRandomWalls(FieldPlan plan) {
        for (int i = 0; i < INDESTRUCTIBLE_WALLS; i++) {
            int x = 0;
            int y = 0;
            while (plan.get(x, y) != FieldPlan.Cell.WALL) {
                x = randomGenerator.nextInt(W);
                y = randomGenerator.nextInt(H);
            }
            plan.set(x, y, FieldPlan.Cell.INDESTRUCTIBLE_WALL);
        }
    }
}
