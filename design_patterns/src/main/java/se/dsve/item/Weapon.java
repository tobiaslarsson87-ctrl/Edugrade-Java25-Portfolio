package se.dsve.item;
import se.dsve.item.decorator.WeaponComponent;

public class Weapon implements WeaponComponent {
    private String name;
    private int damage;

    public Weapon(String name, int damage) {
        this.name = name;
        this.damage = damage;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public void upgrade() {
        this.damage++;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
