package se.dsve.item.enchantments;
import se.dsve.item.decorator.WeaponComponent;
import se.dsve.item.decorator.WeaponDecorator;

public class MagicEnchantment extends WeaponDecorator {

    public MagicEnchantment(WeaponComponent inner) {
        super(inner);
    }

    @Override
    public String getName() {
        return "🔅 " + super.getName();
    }

    @Override
    public int getDamage() {
        return super.getDamage() + 12;
    }
}
