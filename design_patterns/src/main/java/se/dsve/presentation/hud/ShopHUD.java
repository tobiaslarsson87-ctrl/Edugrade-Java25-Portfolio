package se.dsve.presentation.hud;
import se.dsve.character.Player;
import se.dsve.game.engine_utils.GameConfig;
import se.dsve.utils.InputHandler;

/***
 * This fits better as a separate Class so that implementation of Commands would be easier.
 * This should be the ideal for every menu BUT time constraints.
 */
public class ShopHUD {
    public int call(Player player){
        System.out.println("=== SHOP ===");
        System.out.println(player.getName() + " has: " + player.getGoldAmount() + " 💰 gold. ");
        System.out.println("[1] - ⚔️ Sharpen Weapon | COST: " + GameConfig.UPGRADE_WEAPON_COST + " 💰 gold");
        System.out.println("[2] - ❤️ Recover all HP | COST: " + GameConfig.RESTORE_HP_COST + " 💰 gold");
        System.out.println("[3] - 🧪 Buy Potion | COST: " + GameConfig.POTION_COST + " 💰 gold");
        System.out.println("[4] - 🕷️ Buy Venom Enchantment | COST: " + GameConfig.VENOM_ENCHANTMENT_COST + " 💰 gold");
        System.out.println("[5] - 🔥 Buy Fire Enchantment | COST: " + GameConfig.FIRE_ENCHANTMENT_COST + " 💰 gold");
        System.out.println("[6] - 🔅 Buy Magic Enchantment | COST: " + GameConfig.MAGIC_ENCHANTMENT_COST + " 💰 gold");
        System.out.println("[0] - 🔙 Return to main menu");

        int choice;
        while(true){
            choice = InputHandler.getInt();
            if (choice >= 0 && choice <= 6){
                return choice;
            } else System.out.println("❌VALID CHOICES ARE 0-6❌");
        }
    }
}
