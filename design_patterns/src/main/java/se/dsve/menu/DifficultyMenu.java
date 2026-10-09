package se.dsve.menu;
import se.dsve.game.GameState;
import se.dsve.game.engine_utils.Difficulty;
import se.dsve.presentation.Log;
import se.dsve.presentation.render.RenderCommon;
import se.dsve.presentation.UI;

/***
 * Legacy, moved here to make Menu as clean as possible and for it to only manage the main app loop.
 */
public class DifficultyMenu {
    public static void setDifficulty() {
        int choice = UI.difficultyMenu();
        Difficulty difficulty = switch (choice) {
            case 1 -> Difficulty.EASY;
            case 2 -> Difficulty.MEDIUM;
            case 3 -> Difficulty.HARD;
            default -> {
                RenderCommon.invalidInput();
                yield null;
            }
        };

        if (difficulty != null) {
            GameState.getInstance().setDifficulty(difficulty);
            Log.difficultyInfo();
        }
    }
}
