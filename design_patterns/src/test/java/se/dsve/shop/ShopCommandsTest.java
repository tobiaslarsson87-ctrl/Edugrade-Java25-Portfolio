package se.dsve.shop;
import se.dsve.character.Player;
import se.dsve.character.observer.PlayerEventBus;
import se.dsve.game.engine_utils.GameConfig;
import se.dsve.item.Weapon;
import org.junit.jupiter.api.Test;
import se.dsve.item.enchantments.FireEnchantment;
import se.dsve.item.enchantments.MagicEnchantment;
import se.dsve.item.enchantments.VenomEnchantment;
import se.dsve.shop.commands.BuyFireEnchantment;
import se.dsve.shop.commands.BuyMagicEnchantment;
import se.dsve.shop.commands.BuyVenomEnchantment;
import static org.junit.jupiter.api.Assertions.*;

public class ShopCommandsTest {

    @Test
    void playerAlreadyHasEnchantmentTest(){
        //given
        PlayerEventBus bus = new PlayerEventBus();
        Player player = new Player("Test", 100, new Weapon("Sword", 10), bus);
        // when
        player.treasure(1000);
        player.setWeapon(new VenomEnchantment(player.getWeapon()));
        BuyVenomEnchantment purchase = new BuyVenomEnchantment();
        //then
        assertFalse(purchase.canExecute(player));
    }

    @Test
    void playerGetVenomEnchantmentTest(){
        //given
        PlayerEventBus bus = new PlayerEventBus();
        Player player = new Player("Test", 100, new Weapon("Sword", 10), bus);
        player.treasure(GameConfig.VENOM_ENCHANTMENT_COST);
        BuyVenomEnchantment purchase = new BuyVenomEnchantment();
        //when
        boolean result = purchase.execute(player);
        //then
        assertTrue(result);
        assertTrue(player.getWeapon() instanceof VenomEnchantment);
        assertEquals(0, player.getGoldAmount());
    }

    @Test
    void playerGetFireEnchantmentTest(){
        //given
        PlayerEventBus bus = new PlayerEventBus();
        Player player = new Player("Test", 100, new Weapon("Sword", 10), bus);
        player.treasure(GameConfig.FIRE_ENCHANTMENT_COST);
        BuyFireEnchantment purchase = new BuyFireEnchantment();
        //when
        boolean result = purchase.execute(player);
        //then
        assertTrue(result);
        assertTrue(player.getWeapon() instanceof FireEnchantment);
        assertEquals(0, player.getGoldAmount());
    }

    @Test
    void playerGetMagicEnchantmentTest(){
        //given
        PlayerEventBus bus = new PlayerEventBus();
        Player player = new Player("Test", 100, new Weapon("Sword", 10), bus);
        player.treasure(GameConfig.MAGIC_ENCHANTMENT_COST);
        BuyMagicEnchantment purchase = new BuyMagicEnchantment();
        //when
        boolean result = purchase.execute(player);
        //then
        assertTrue(result);
        assertTrue(player.getWeapon() instanceof MagicEnchantment);
        assertEquals(0, player.getGoldAmount());
    }
}
