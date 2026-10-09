package se.dsve.item.decorator;
/***
 * Abstract decorator. Only used for type-safety by extending.
 */
public class WeaponDecorator implements WeaponComponent {
    public final WeaponComponent inner;

    public WeaponDecorator(WeaponComponent inner) {
        this.inner = inner;
    }

    @Override
    public String getName() {
        return inner.getName();
    }

    @Override
    public int getDamage() {
        return inner.getDamage();
    }
}
