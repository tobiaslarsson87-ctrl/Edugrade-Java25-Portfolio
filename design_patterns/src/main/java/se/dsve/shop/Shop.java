package se.dsve.shop;
import se.dsve.character.Player;
import se.dsve.game.engine_utils.GameConfig;
import se.dsve.item.enchantments.FireEnchantment;
import se.dsve.item.enchantments.MagicEnchantment;
import se.dsve.item.enchantments.VenomEnchantment;
import se.dsve.presentation.hud.ShopHUD;
import se.dsve.presentation.render.RenderCommon;
import se.dsve.presentation.render.RenderShop;
import se.dsve.shop.commands.BuyFireEnchantment;
import se.dsve.shop.commands.BuyMagicEnchantment;
import se.dsve.shop.commands.BuyVenomEnchantment;
import se.dsve.shop.commands.ShopCommand;

/***
 * Legacy with a few added methods for different purchases.
 * I changed this to instance based instead of static for easier implementation of
 * mandatory design patterns. It's now a field in Menu.
 */
public class Shop {
    private final ShopHUD hud = new ShopHUD();
    private final ShopCommand buyVenom = new BuyVenomEnchantment();
    private final ShopCommand buyFire = new BuyFireEnchantment();
    private final ShopCommand buyMagic = new BuyMagicEnchantment();

    public void menu(Player player) {
        int choice = -1;
        while (choice != 0) {
            choice = hud.call(player);
            switch (choice) {
                case 1 -> upgradeWeapon(player);
                case 2 -> restoreHp(player);
                case 3 -> buyPotion(player);
                case 4 -> buyVenomEnchantment(player);
                case 5 -> buyFireEnchantment(player);
                case 6 -> buyMagicEnchantment(player);
                case 0 -> RenderShop.mainMenuReturn();
                default -> RenderCommon.invalidInput();
            }
        }
    }

    private void upgradeWeapon(Player player) {
        if(player.getGoldAmount() < GameConfig.UPGRADE_WEAPON_COST) {
            RenderShop.insufficientGold();
            return;
        }
        player.weaponUpgrade(GameConfig.UPGRADE_WEAPON_COST);
        RenderShop.weaponUpgrade();
    }

    private void restoreHp(Player player) {
        if(player.getGoldAmount() < GameConfig.RESTORE_HP_COST) {
            RenderShop.insufficientGold();
            return;
        }
        player.recoverHp(GameConfig.RESTORE_HP_COST);
        RenderShop.healthRecovery();
    }

    private void buyPotion(Player player){
        if(player.getGoldAmount() < GameConfig.POTION_COST) {
            RenderShop.insufficientGold();
            return;
        }
        player.buyPotion(GameConfig.POTION_COST);
        RenderShop.potionPurchase();
    }

    private void buyVenomEnchantment(Player player){
        if (player.getGoldAmount() < buyVenom.getCost()){
            RenderShop.insufficientGold();
        } else {
            if(buyVenom.canExecute(player)){
                buyVenom.execute(player);
                RenderShop.venomEnchantment();
                player.notifyEventBus();
            } else {
                RenderShop.enchantmentAlreadyPurchased();
            }
        }
    }

    private void buyFireEnchantment(Player player){
        if (player.getGoldAmount() < buyFire.getCost()){
            RenderShop.insufficientGold();
        } else {
            if(buyFire.canExecute(player)){
                buyFire.execute(player);
                RenderShop.fireEnchantment();
                player.notifyEventBus();
            } else {
                RenderShop.enchantmentAlreadyPurchased();
            }
        }
    }

    private void buyMagicEnchantment(Player player){
        if (player.getGoldAmount() < buyMagic.getCost()){
            RenderShop.insufficientGold();
        } else {
            if(buyMagic.canExecute(player)){
                buyMagic.execute(player);
                RenderShop.magicEnchantment();
                player.notifyEventBus();
            } else {
                RenderShop.enchantmentAlreadyPurchased();
            }
        }
    }
}
