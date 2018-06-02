package ru.spbau.des.roguelike.dom.characters;

import ru.spbau.des.roguelike.dom.environment.HitResult;

public class ScoreResult implements HitResult {
    private final int scoreDelta;

    public ScoreResult(int scoreDelta) {
        this.scoreDelta = scoreDelta;
    }

    public int getScoreDelta() {
        return scoreDelta;
    }
}
