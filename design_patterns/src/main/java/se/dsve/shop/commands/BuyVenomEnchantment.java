package se.dsve.shop.commands;
import se.dsve.character.Player;
import se.dsve.game.engine_utils.GameConfig;
import se.dsve.item.enchantments.VenomEnchantment;

public class BuyVenomEnchantment implements ShopCommand {
    @Override
    public boolean canExecute(Player player) {
        return !player.hasEnchantment(VenomEnchantment.class);

    }

    @Override
    public boolean execute(Player player) {
        player.buyItem(getCost());
        player.setWeapon(new VenomEnchantment(player.getWeapon()));
        return true;
    }

    @Override
    public int getCost() {
        return GameConfig.VENOM_ENCHANTMENT_COST;
    }

    @Override
    public String getDescription() {
        return "🕷️ Venom Enchantment === DMG + 3 ===)";
    }
}
