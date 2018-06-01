package ru.spbau.des.roguelike.operation;

import ru.spbau.des.roguelike.dom.environment.Direction;
import ru.spbau.des.roguelike.dom.environment.Position;
import ru.spbau.des.roguelike.dom.environment.Field;
import ru.spbau.des.roguelike.dom.environment.FieldCell;

import java.util.*;

public class MapCreator {
    private static final int W = 50;
    private static final int H = 20;
    private static final int MAX_SIDE = 16;
    private static final int MIN_SIDE = 4;
    private static final double MIN_SIDE_DIVISOR = 1.8;
    private static final double MAX_SIDE_DIVISOR = 1.2;
    private static final Random RANDOM_GENERATOR = new Random();

    public Field createMap() {
        FieldCell[][] cells = new FieldCell[W][H];
        for (int i = 0; i < W; i++) {
            for (int j = 0; j < H; j++) {
                boolean destructible = i > 0 && j > 0 && i < W - 1 && j < H - 1;
                cells[i][j] = FieldCell.wall(destructible);
            }
        }
        makeRooms(cells, 1, W - 1, 1, H - 1, false);
        connectRooms(cells);
        return new Field(cells);
    }

    private void makeRooms(FieldCell[][] field, int x1, int x2, int y1, int y2, boolean splitX) {
        if (x2 - x1 < MIN_SIDE || y2 - y1 < MIN_SIDE){
            return;
        }
        if (x2 - x1 < MAX_SIDE && y2 - y1 < MAX_SIDE) {
            int minH = (int)((x2 - x1) / MIN_SIDE_DIVISOR);
            int maxH = (int)((x2 - x1) / MAX_SIDE_DIVISOR);
            int h = RANDOM_GENERATOR.nextInt(maxH - minH) + minH;
            int minW = (int)((y2 - y1) / MIN_SIDE_DIVISOR);
            int maxW = (int)((y2 - y1) / MAX_SIDE_DIVISOR);
            int w = RANDOM_GENERATOR.nextInt(maxW - minW) + minW;
            int sx = x1 + RANDOM_GENERATOR.nextInt(x2 - x1 - h);
            int sy = y1 + RANDOM_GENERATOR.nextInt(y2 - y1 - w);
            for (int i = sx; i <  sx + h; i++) {
                for (int j = sy; j < sy + w; j++) {
                    field[i][j].clear();
                }
            }
        }
        else if (x2 - x1 >= MAX_SIDE && (y2 - y1 < MAX_SIDE || splitX)) {
            int xm = x1 + MIN_SIDE + RANDOM_GENERATOR.nextInt(x2 - x1 - 2 * MIN_SIDE);
            makeRooms(field, x1, xm, y1, y2, !splitX);
            makeRooms(field, xm, x2, y1, y2, !splitX);
        }
        else {
            int ym = y1 + MIN_SIDE + RANDOM_GENERATOR.nextInt(y2 - y1 - 2 * MIN_SIDE);
            makeRooms(field, x1, x2, y1, ym, !splitX);
            makeRooms(field, x1, x2, ym, y2, !splitX);
        }
    }

    private void connectRooms(FieldCell[][] field) {
        Set<Position> connected = new HashSet<>();
        List<Position> allCells = new ArrayList<>();
        for (int i = 0; i < W; i++) {
            for (int j = 0; j < H; j++) {
                allCells.add(new Position(i, j));
            }
        }
        Collections.shuffle(allCells);
        for (Position position: allCells) {
            if (!field[position.getX()][position.getY()].isFree() ||
                    connected.contains(position)) {
                continue;
            }
            if (connected.isEmpty()) {
                connected = getAccessible(field, position);
            } else {
                Position fromConnected = connected.iterator().next();
                digTunnel(field, fromConnected.getX(), fromConnected.getY(),
                        position.getX(), position.getY());
                connected = getAccessible(field, position);
            }
        }
    }

    private Set<Position> getAccessible(FieldCell[][] field, Position position) {
        Queue<Position> positions = new ArrayDeque<>();
        Set<Position> accessible = new HashSet<>();
        positions.offer(position);
        accessible.add(position);
        while (!positions.isEmpty()) {
            Position curPosition = positions.poll();
            for (Direction direction: Direction.values()) {
                Position nextPosition = curPosition.resolve(direction);
                if (field[nextPosition.getX()][nextPosition.getY()].isFree() &&
                        !accessible.contains(nextPosition)) {
                    positions.offer(nextPosition);
                    accessible.add(nextPosition);
                }
            }
        }
        return accessible;
    }

    private void digTunnel(FieldCell[][] field, int sx, int sy, int tx, int ty) {
        int cx = sx;
        int cy = sy;
        while (cx != tx) {
            int dx = (tx - cx) / Math.abs(tx - cx);
            cx += dx;
            field[cx][cy].clear();
        }
        while (cy != ty) {
            int dy = (ty - cy) / Math.abs(ty - cy);
            cy += dy;
            field[cx][cy].clear();
        }
    }
}
