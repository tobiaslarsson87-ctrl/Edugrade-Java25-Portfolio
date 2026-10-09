package se.dsve.shop.commands;
import se.dsve.character.Player;

/***
 * Commands used in the Shop menu. These are only implemented on the new features. Every Command contains
 * a boolean check so that certain conditions must be met to be able to use that shop action. In this case
 * the BuyXEnchantment commands can only be executed if the players weapon doesn't already contain a WeaponComponent
 * of the same instance.class
 */
public interface ShopCommand {
    boolean canExecute(Player player);
    boolean execute(Player player);
    int getCost();
    String getDescription();
}
