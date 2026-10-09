package se.dsve.shop.commands;
import se.dsve.character.Player;
import se.dsve.game.engine_utils.GameConfig;
import se.dsve.item.enchantments.MagicEnchantment;
import se.dsve.item.enchantments.VenomEnchantment;

public class BuyMagicEnchantment implements ShopCommand {
    @Override
    public boolean canExecute(Player player) {
        return !player.hasEnchantment(MagicEnchantment.class);

    }

    @Override
    public boolean execute(Player player) {
        player.buyItem(getCost());
        player.setWeapon(new MagicEnchantment(player.getWeapon()));
        return true;
    }

    @Override
    public int getCost() {
        return GameConfig.MAGIC_ENCHANTMENT_COST;
    }

    @Override
    public String getDescription() {
        return "🔅 Magic Enchantment === DMG + 12 ===)";
    }
}
