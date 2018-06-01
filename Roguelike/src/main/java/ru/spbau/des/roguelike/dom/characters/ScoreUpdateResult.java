package ru.spbau.des.roguelike.dom.characters;

import ru.spbau.des.roguelike.dom.environment.HitResult;

public class ScoreUpdateResult implements HitResult {
    private final int scoreDelta;

    public ScoreUpdateResult(int scoreDelta) {
        this.scoreDelta = scoreDelta;
    }

    public int getScoreDelta() {
        return scoreDelta;
    }
}
