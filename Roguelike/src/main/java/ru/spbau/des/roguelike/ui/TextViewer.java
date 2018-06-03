package ru.spbau.des.roguelike.ui;

import com.googlecode.lanterna.graphics.TextGraphics;

public class TextViewer {
    private final TextGraphics textGraphics;
    private final int column;
    private final int row;
    private final String message;

    public TextViewer(TextGraphics textGraphics, int column, int row, String message) {
        this.textGraphics = textGraphics;
        this.column = column;
        this.row = row;
        this.message = message;
    }

    public void draw() {
        textGraphics.putString(column, row, message);
    }

    @Override
    public boolean equals(Object o) {
        return this == o;
    }
}
