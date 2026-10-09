package se.dsve.presentation;
import se.dsve.character.Player;
import se.dsve.combat.combat_utils.BattleAction;
import se.dsve.game.engine_utils.GameConfig;
import se.dsve.utils.InputHandler;

/***
 * Displays choices and validates input only. The only UI not here is BattleHUD and that is because that needs
 * to be mocked and therefore it can't be static
 */
public class UI {
    public static int locationMenu(){
        System.out.println("[1] - 🌳 Go to Forrest");
        System.out.println("[2] - 🗻 Go to Cave");
        System.out.println("[3] - 🏰 Go to Castle");
        System.out.println("[4] - 🔥 Go to burning Hell");
        System.out.println("[0] - 🔙 Return to main menu");

        int choice;
        while(true){
            choice = InputHandler.getInt();
            if (choice >= 0 && choice <= 4){
                return choice;
            } else System.out.println("❌VALID CHOICES ARE 0-4❌");
        }
    }

    public static int difficultyMenu(){
        System.out.println("Set difficulty");
        System.out.println("[1] - 😈 Easy");
        System.out.println("[2] - 👿 Medium");
        System.out.println("[3] - 💀 Hard");

        int choice;
        while(true){
            choice = InputHandler.getInt();
            if (choice >= 0 && choice <= 3){
                return choice;
            } else System.out.println("❌VALID CHOICES ARE 1-3❌");
        }
    }

    public static int mainChoices(){
        System.out.println("[1] - 🗺️ Go on adventure");
        System.out.println("[2] - 🧝🏻 Show info about player");
        System.out.println("[3] - 🛖 Go to the Shop");
        System.out.println("[4] - 🎮 Change difficulty");
        System.out.println("[0] - ❌ Shutdown Game");

        int choice;
        while(true){
            choice = InputHandler.getInt();
            if (choice >= 0 && choice <= 4){
                return choice;
            } else System.out.println("❌VALID CHOICES ARE 0-4❌");
        }
    }

    public static String playAgain(){
        System.out.println("Do you want to play again? [Y/N]");

        String choice;
        while(true){
            choice = InputHandler.getString();
            if (choice.equalsIgnoreCase("Y") || choice.equalsIgnoreCase("N")){
                return choice;
            } else System.out.println("❌VALID CHOICES ARE Y/N❌");
        }
    }
}
