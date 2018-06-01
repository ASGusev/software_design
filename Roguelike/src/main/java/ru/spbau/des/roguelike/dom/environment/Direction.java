package ru.spbau.des.roguelike.dom.environment;

public enum Direction {
    UP {
        @Override
        int getDX() {
            return 0;
        }

        @Override
        int getDY() {
            return 1;
        }
    },
    DOWN {
        @Override
        int getDX() {
            return 0;
        }

        @Override
        int getDY() {
            return -1;
        }
    },
    RIGHT {
        @Override
        int getDX() {
            return 1;
        }

        @Override
        int getDY() {
            return 0;
        }
    },
    LEFT {
        @Override
        int getDX() {
            return -1;
        }

        @Override
        int getDY() {
            return 0;
        }
    };

    abstract int getDX();

    abstract int getDY();
}
