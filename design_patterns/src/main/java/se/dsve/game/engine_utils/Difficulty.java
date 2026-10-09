package se.dsve.game.engine_utils;

/***
 * This feature was broken so changed it so that it s scalable and never resets the state
 * of the Player object while still increasing challange.
 */
public enum Difficulty {
    EASY(0.5, 0.75),
    MEDIUM(1.0, 0.5),
    HARD(1.5, 0.25),
    TEST_IMPOSSIBLE(100, 0),
    TEST_ALWAYS(0.1, 1),
    TEST_POWER_SCALING(10, 0.5);


    private final double monsterPower;
    private final double escapeChance;

    Difficulty(double monsterPower, double escapeChance) {
        this.monsterPower = monsterPower;
        this.escapeChance = escapeChance;
    }

    public double getMonsterPower() {
        return monsterPower;
    }

    public double getEscapeChance() {
        return escapeChance;
    }
}
