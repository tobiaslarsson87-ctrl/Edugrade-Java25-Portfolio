package se.dsve.game.engine_utils;
import se.dsve.character.Player;
import se.dsve.presentation.UI;
import se.dsve.presentation.render.RenderGame;

/***
 * This is legacy code only moved to it's own class.
 */
public class GameEndCheck {
    public static Flow check(Player player) {
        if (player.isDead()){
            RenderGame.gameOver();
            String playAgain = UI.playAgain();
            if (playAgain.equalsIgnoreCase("y")){
                player.resetStats();
                System.out.println("You are back in the game!");
                return Flow.CONTINUE;
            } else {
                System.out.println("Thanks for playing, sucker!");
                return Flow.END_GAME;
            }
        }

        if (player.getLevel() >= GameConfig.MAX_LEVEL){
            RenderGame.gameCompletion();
            String playAgain = UI.playAgain();
            if (playAgain.equalsIgnoreCase("y")){
                player.resetStats();
                System.out.println("You are back in the game!");
                return Flow.CONTINUE;
            } else {
                System.out.println("Thanks for playing, Champ!");
                return Flow.END_GAME;
            }
        }
        return Flow.CONTINUE;
    }
}
