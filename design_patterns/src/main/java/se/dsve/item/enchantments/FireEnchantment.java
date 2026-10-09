package se.dsve.item.enchantments;
import se.dsve.item.decorator.WeaponComponent;
import se.dsve.item.decorator.WeaponDecorator;

public class FireEnchantment extends WeaponDecorator {

    public FireEnchantment(WeaponComponent inner) {
        super(inner);
    }

    @Override
    public String getName() {
        return "🔥 " + super.getName();
    }

    @Override
    public int getDamage() {
        return super.getDamage() + 6;
    }
}
