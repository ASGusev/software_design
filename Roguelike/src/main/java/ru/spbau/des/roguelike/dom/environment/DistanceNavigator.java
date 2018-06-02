package ru.spbau.des.roguelike.dom.environment;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Gets a field and a position.
 * Calculates distances from that position to all non-wall units.
 */
public class DistanceNavigator {
    private Field field;
    private Position target;
    private int[][] distances;

    public DistanceNavigator(Field field, Position target) {
        this.field = field;
        this.target = target;
        calcDistances();
    }

    private void calcDistances() {
        distances = new int[field.getW()][field.getH()];
        for (int[] d: distances) {
            Arrays.fill(d, -1);
        }
        distances[target.getX()][target.getY()] = 0;
        Queue<Position> positions = new LinkedList<>();
        positions.offer(target);
        while (!positions.isEmpty()) {
            Position curPosition = positions.poll();
            for (Direction direction: Direction.values()) {
                Position nextPosition = curPosition.resolve(direction);
                if (!field.valid(nextPosition)) {
                    continue;
                }
                if (!(field.get(nextPosition) instanceof WallUnit)
                        && distanceAt(nextPosition) == -1) {
                    distances[nextPosition.getX()][nextPosition.getY()] =
                            distanceAt(curPosition) + 1;
                    positions.offer(nextPosition);
                }
            }
        }
    }

    public int distanceAt(Position position) {
        return distances[position.getX()][position.getY()];
    }
}
