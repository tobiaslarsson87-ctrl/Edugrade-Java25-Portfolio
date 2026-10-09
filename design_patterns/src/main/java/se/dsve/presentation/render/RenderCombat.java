package se.dsve.presentation.render;
import se.dsve.character.Player;
import se.dsve.combat.combat_utils.Attack;
import se.dsve.combat.Combatant;

/***
 * Handles all console logs that take place before, after and during battle
 */
public class RenderCombat {

    public static void death(Combatant combatant){
        System.out.println("☠️ " + combatant.getName() + " have perished in battle!");
    }

    public static void monsterEmerges(){
        System.out.println("👹 A monster emerges!");
    }

    public static void bossEmerges(){
        System.out.println("👹 A boss emerges!");
    }

    public static void displayCombatStatus(Combatant player, Combatant monster){
        Player castedPlayer = (Player) player; //temp hack fix, getPotions() and userPotions() should be part of an interface, but time constraints
        System.out.println("=== STATUS OF FIGHTERS ===");
        System.out.println("👹 " + monster.getName() + " ❤️ " + monster.getHp());
        System.out.println("🧝🏻 " + player.getName() + " ❤️ " + player.getHp() + " 🧪 " + castedPlayer.getPotions());
        System.out.print("=== COMMENCE COMBAT ===");
    }

    public static void  displayAttack(Combatant combatant, Attack attack){
        if (attack.special()) System.out.println("💥🗡️ " + combatant.getName() + " uses special attack! " + attack.damage() + " damage dealt!");
        else {
            System.out.println("🗡️ " + combatant.getName() + " attacks! " + attack.damage() + " damage dealt!");
        }
    }

    public static void potionUse(Combatant user){
        System.out.println("❤️🧪 " + user.getName() + " uses a potion and recovers all HP!");
    }

    public static void outOfPotions(Combatant user){
        System.out.println("❌🧪 " + user.getName() + " is out of Potions!");
    }

    public static void escapeSuccess(Combatant coward){
        System.out.println("🥾 " + coward.getName() + " managed to escape!");
    }

    public static void escapeFail(Combatant coward){
        System.out.println("❌🥾 " + coward.getName() + " failed to escape!");
    }
}
