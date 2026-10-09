package se.dsve.item.decorator;

/***
 * Interface used to decorate weapons. Every new enchantment that implements this wraps the old object
 * within the new object. All Enchantments are instanceof WeaponComponent and every implementation loads
 * the relevant fields from the super-object and expands upon it.
 */
public interface WeaponComponent {
    String getName();
    int getDamage();
    default void upgrade(){}
}
