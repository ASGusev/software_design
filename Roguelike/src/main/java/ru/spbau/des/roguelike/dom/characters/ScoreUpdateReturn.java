package ru.spbau.des.roguelike.dom.characters;

import ru.spbau.des.roguelike.dom.base.HitReturn;

public class ScoreUpdateReturn implements HitReturn {
    private final int scoreDelta;

    public ScoreUpdateReturn(int scoreDelta) {
        this.scoreDelta = scoreDelta;
    }

    public int getScoreDelta() {
        return scoreDelta;
    }
}
