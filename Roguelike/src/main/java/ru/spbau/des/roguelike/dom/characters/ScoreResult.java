package ru.spbau.des.roguelike.dom.characters;

import ru.spbau.des.roguelike.dom.environment.HitResult;

/**
 * ScoreResult is returned from takeHit method of a monster if the monster dies and
 * carries the number of points rewarded for killing the monster.
 */
public class ScoreResult implements HitResult {
    private final int scoreDelta;

    public ScoreResult(int scoreDelta) {
        this.scoreDelta = scoreDelta;
    }

    public int getScoreDelta() {
        return scoreDelta;
    }
}
