package se.dsve;
import se.dsve.character.Player;
import se.dsve.character.observer.PlayerEventBus;
import se.dsve.character.observer.milestones.Milestone_LV10;
import se.dsve.character.observer.milestones.Milestone_LV5;
import se.dsve.character.observer.milestones.Milestone_LegendaryWeapon;
import se.dsve.game.GameEngine;
import se.dsve.menu.CharacterCreation;
import se.dsve.menu.Menu;
import se.dsve.presentation.Log;
import se.dsve.presentation.render.RenderCommon;

/***
 * Legacy. But a lot of things have been moved away from here to keep main clean and simple
 */
public class Main {
    public static void main(String[] args) {
        Log.startUp();
        RenderCommon.welcome();
        GameEngine gameEngine = new GameEngine();

        //OBSERVER SETUP
        PlayerEventBus bus = new PlayerEventBus();
        bus.registerObserver(new Milestone_LV5());
        bus.registerObserver(new Milestone_LV10());
        bus.registerObserver(new Milestone_LegendaryWeapon());


        //Create Player
        Player player = CharacterCreation.start(bus);

        // Start Game
        Menu menu = new Menu();
        menu.start(player, gameEngine);
    }
}
