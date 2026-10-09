package se.dsve.game;

import se.dsve.game.engine_utils.Difficulty;

/***
 * To hold information that need to be global and available to be called upon.
 * Will start refactoring Player, I need the Difficulty data accessible to
 * call on the LevelXP values in player to handle LevelUp logic without the
 * involvement of GameEngine. Player should not involve GameEngine in level
 * up logic at all. Player should handle level ups on its own and call upon the
 * required amount that depends on the Difficulty.
 */
public class GameState {
    private static final GameState INSTANCE = new GameState();
    private Difficulty difficulty = Difficulty.MEDIUM;
    private boolean gameStarted;

    private GameState() {}

    /***
     * A singleton that tracks difficulty
     * @return : Returns the singleton instance
     */
    public static GameState getInstance(){
        return INSTANCE;
    }

    /**Ä
     *
     * @return : Returns the current difficulty
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /***
     * Used to change the difficulty
     * @param difficulty : Takes a Difficulty enum
     */
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    /***
     * Legacy method, only moved here to track
     * @return : returns a boolean true if game is running
     */
    public boolean isGameRunning() {
        return gameStarted;
    }

    /***
     * Legacy method, formerly handled in GameEngine
     * @param gameStarted : set to true/false
     */
    public void setGameRunning(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }
}
