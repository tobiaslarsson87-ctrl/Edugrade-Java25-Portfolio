package se.dsve.presentation.hud;
import se.dsve.combat.combat_utils.BattleAction;
import se.dsve.utils.InputHandler;

/***
 * The BattleHUD is its own class this is because it must be instance based so that it
 * can be mocked for testing
 */
public class BattleHUD {
    public BattleAction call(){
        System.out.println("[1] 🗡️ [2] 🧪 [0] 🥾");

        int choice;
        while(true){
            choice = InputHandler.getInt();
            if (choice >= 0 && choice <= 2){
                return switch (choice) {
                    case 2 -> BattleAction.USE_POTION;
                    case 0 -> BattleAction.ESCAPE;
                    default -> BattleAction.ATTACK;
                };
            } else System.out.println("❌VALID CHOICES ARE 0-2❌");
        }
    }
}
