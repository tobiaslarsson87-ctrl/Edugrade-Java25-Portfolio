package se.dsve.shop.commands;
import se.dsve.character.Player;
import se.dsve.game.engine_utils.GameConfig;
import se.dsve.item.enchantments.FireEnchantment;
import se.dsve.item.enchantments.VenomEnchantment;

public class BuyFireEnchantment implements ShopCommand {
    @Override
    public boolean canExecute(Player player) {
        return !player.hasEnchantment(FireEnchantment.class);

    }

    @Override
    public boolean execute(Player player) {
        player.buyItem(getCost());
        player.setWeapon(new FireEnchantment(player.getWeapon()));
        return true;
    }

    @Override
    public int getCost() {
        return GameConfig.FIRE_ENCHANTMENT_COST;
    }

    @Override
    public String getDescription() {
        return "🔥 Fire Enchantment === DMG + 6 ===)";
    }
}
