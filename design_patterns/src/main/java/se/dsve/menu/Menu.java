package se.dsve.menu;
import se.dsve.character.Player;
import se.dsve.game.*;
import se.dsve.game.engine_utils.Flow;
import se.dsve.presentation.Log;
import se.dsve.presentation.UI;
import se.dsve.presentation.render.RenderCommon;
import se.dsve.shop.Shop;

public class Menu {
    private final Shop shop = new Shop();

    public void start(Player player, GameEngine gameEngine) {
        Flow flow = Flow.CONTINUE;
        while (flow == Flow.CONTINUE) {
            int choice;
            choice = UI.mainChoices();
            flow = choiceHandler(choice, player, gameEngine);

            if (flow == Flow.END_GAME) Log.shutDown();
        }
    }

    public Flow choiceHandler(int choice, Player player, GameEngine gameEngine) {
        switch (choice) {
            case 1:
                return gameEngine.gameLoop(player);
            case 2:
                player.showInfo();
                return Flow.CONTINUE;
            case 3:
                shop.menu(player);
                return Flow.CONTINUE;
            case 4:
                DifficultyMenu.setDifficulty();
                return Flow.CONTINUE;
            case 0:
                return Flow.END_GAME;
            default:
                RenderCommon.invalidInput();
                return Flow.CONTINUE;
        }
    }
}
