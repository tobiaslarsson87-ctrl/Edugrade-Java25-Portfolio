package se.dsve.character;
import se.dsve.character.observer.PlayerEventBus;
import se.dsve.combat.combat_utils.Attack;
import se.dsve.item.Weapon;
import se.dsve.item.decorator.WeaponComponent;
import se.dsve.item.decorator.WeaponDecorator;
import se.dsve.presentation.Log;
import se.dsve.combat.Combatant;
import se.dsve.game.engine_utils.GameConfig;
import se.dsve.presentation.render.RenderGame;
import se.dsve.presentation.render.RenderPlayer;

/***
 * The main domain class. I try to encapsulate everything involving the player in this class
 * removing dependencies on GameEngine and Logger from this class completely only leaving
 * the Player class to handle its own logic and state and nothing else. Im not breaking these methods
 * up into manager methods. I would if this was an enterprise app, but it isn't.
 */
public class Player implements Combatant {
    private String name;
    private int level = 1;
    private int xp = 0;
    private int currentHp;
    private int totalHp;
    private int goldAmount = 0;
    private int potions = 3;
    private WeaponComponent weapon;
    private PlayerEventBus bus;

    public Player(String name, int startHP, WeaponComponent weapon, PlayerEventBus bus) {
        this.name = name;
        this.currentHp = startHP;
        this.totalHp = startHP;
        this.weapon = weapon;
        this.bus = bus;
    }

    public void notifyEventBus(){
        bus.notifyObservers(this);
    }

    @Override
    public Attack attack() {
        return new Attack(this.getWeapon().getDamage(), false);
    }

    @Override
    public void takeDamage(Attack attack) {
        setCurrentHp(currentHp - attack.damage());
    }

    @Override
    public int getHp() {
        return currentHp;
    }

    public int getTotalHp() {
        return totalHp;
    }

    private void levelUp() {
        while (xp >= xpRequirement()) {
            level++;
            xp -= xpRequirement();
            totalHp += GameConfig.LEVEL_HP_INCREASE;
            currentHp = totalHp;
            weapon.upgrade();
            RenderPlayer.levelUp();
            Log.levelUp(this);
            notifyEventBus();
        }
    }

    public int xpRequirement(){
        return GameConfig.EXP_ROOF_BASE + ( this.level * GameConfig.EXP_ROOF_AMP);
    }

    public void weaponUpgrade(int cost) {
        buyItem(cost);
        weapon.upgrade();
    }

    public void recoverHp(int cost) {
        buyItem(cost);
        restoreHp();
    }

    public void buyPotion(int cost){
        buyItem(cost);
        potions++;
    }

    /***
     * A method for my potion feature, it only changes internal state and nothing elsewhere.
     * @return return a boolean so that the CombatService can know if a potion was used or not
     */
    public boolean usePotion(){
        if (potions > 0) {
            potions--;
            restoreHp();
            return true;
        } return false;
    }

    public void buyItem(int cost) {
        this.goldAmount -= cost;
    }

    public void restoreHp() {
        this.currentHp = this.totalHp;
    }

    public void treasure(int gold) {
        goldAmount += gold;
        RenderGame.treasure(gold);
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getXp() {
        return xp;
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = Math.max(0, Math.min(currentHp, this.totalHp));
    }

    public WeaponComponent getWeapon() {
        return weapon;
    }

    public void setWeapon(WeaponComponent weapon) {
        this.weapon = weapon;
    }

    public int getGoldAmount() {
        return goldAmount;
    }

    public int getPotions() {
        return potions;
    }

    public void resetStats() {
        this.level = 1;
        this.xp = 0;
        this.totalHp = GameConfig.STARTING_HP;
        this.currentHp = this.totalHp;
        this.goldAmount = 0;
        String weaponName = this.weapon.getName();
        int weaponDamage = GameConfig.STARTING_DMG;
        this.weapon = new Weapon(weaponName, weaponDamage);
    }

    /***
     * Changes the internal state only. Handles xp, gold acquirement and checks if it's enough for a level up.
     * @param gold : the xpReward of the defeated monster
     * @param xp : the goldReward of the defeated monster
     */
    public void battleReward(int gold, int xp){
        this.goldAmount += gold;
        this.xp += xp;
        RenderPlayer.battleReward(gold, xp);
        levelUp();
    }

    public void showInfo() {
        RenderPlayer.playerInfo(this);
    }

    /***
     * Simple utility method that makes it easier to use in a battle system. No more clamping required
     * @return a boolean true if hp == 0, otherwise false
     */
    @Override
    public boolean isDead(){
        return this.currentHp == 0;
    }

    /***
     * Uses bounded generics to control if the enchantment is already purchased. This is a requirement for
     * the commands in Shop that adds these.
     * @param type : Any class that extends WeaponDecorator
     * @return : true if player.getWeapon() has the enchantment : false if they don't
     */
    public boolean hasEnchantment(Class<? extends WeaponDecorator> type){
        WeaponComponent current = weapon;
        while (current instanceof WeaponDecorator){
            WeaponDecorator decorator = (WeaponDecorator) current;
            if (decorator.getClass() == type) return true;
            current = decorator.inner;
        }
        return false;
    }
}
