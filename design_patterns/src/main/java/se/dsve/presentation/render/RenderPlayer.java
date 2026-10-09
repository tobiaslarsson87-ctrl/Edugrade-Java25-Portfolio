package se.dsve.presentation.render;
import se.dsve.character.Player;
import se.dsve.game.engine_utils.GameConfig;
import se.dsve.presentation.ui_utils.MyColor;

/***
 * Console logs that communicates things about the Player objects state
 */
public class RenderPlayer {
    private static final String COLOR = MyColor.YELLOW.code();
    private static final String POSITIVE = MyColor.GREEN.code();
    private static final String RESET = MyColor.RESET.code();

    public static void playerInfo(Player player) {
        System.out.println(COLOR + "🧝🏻 Name: " + RESET + player.getName());
        System.out.println(COLOR + "⭐ Level: " + RESET + player.getLevel());
        System.out.println(COLOR + "⤴️ XP: " + RESET + player.getXp() + "/" + player.xpRequirement());
        System.out.println(COLOR + "❤️ HP: " + RESET + player.getHp() + "/" + player.getTotalHp());
        System.out.println(COLOR + "💰 Gold: " + RESET + player.getGoldAmount());
        System.out.println(COLOR + "🧪 Potions: " + RESET + player.getPotions());
        System.out.println(COLOR + "⚔️ Weapon: " + RESET + player.getWeapon().getName());
        System.out.println(COLOR + "⚔️ Weapon Damage: " + RESET + player.getWeapon().getDamage());
    }

    public static void battleReward(int gold, int xp) {
        System.out.println(COLOR + "You won the fight!" + RESET);
        System.out.println("You gained " + gold + " gold and " + xp + " XP!");
    }

    public static void levelUp(){
        System.out.println(COLOR + "⭐ You leveled up" + RESET);
        System.out.println("❤️ Your HP increased with: " + POSITIVE + GameConfig.LEVEL_HP_INCREASE + RESET);
    }
}
