package se.dsve.presentation.render;
import se.dsve.presentation.ui_utils.MyColor;

/***
 * All console logs for Shop actions
 */
public class RenderShop {
    private static final String COLOR = MyColor.YELLOW.code();
    private static final String POSITIVE = MyColor.GREEN.code();
    private static final String HIGHLIGHT = MyColor.YELLOW.code();
    private static final String WARNING = MyColor.RED.code();
    private static final String RESET = MyColor.RESET.code();

    public static void weaponUpgrade(){
        System.out.println( POSITIVE + "⚔️ Weapon damage increased by 1" + RESET);
    }

    public static void healthRecovery(){
        System.out.println( POSITIVE + "❤️ Your health has been completely recovered!" + RESET);
    }

    public static void potionPurchase(){
        System.out.println(HIGHLIGHT + "🧪 You acquired a Potion!" + RESET);
    }

    public static void venomEnchantment(){
        System.out.println(POSITIVE + "🕷️ Venom Enchantment added to your weapon! " + HIGHLIGHT + "=== DMG + 3 ===" + RESET);
    }

    public static void fireEnchantment(){
        System.out.println(POSITIVE + "🔥 Fire Enchantment added to your weapon! " + HIGHLIGHT + "=== DMG + 6 ===" + RESET);
    }

    public static void magicEnchantment(){
        System.out.println(POSITIVE + "🔅 Magic Enchantment added to your weapon! " + HIGHLIGHT + "=== DMG + 12 ===" + RESET);
    }

    public static void enchantmentAlreadyPurchased(){
        System.out.println(WARNING + "❌ This enchantment has already been purchased!" + RESET);
    }

    public static void insufficientGold(){
        System.out.println( WARNING + "💰 You do not have enough gold for this purchase!" + RESET);
    }

    public static void mainMenuReturn(){
        System.out.println(COLOR + "Returning to main menu" + RESET);
    }
}
