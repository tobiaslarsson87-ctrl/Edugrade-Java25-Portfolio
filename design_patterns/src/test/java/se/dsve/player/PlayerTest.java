package se.dsve.player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import se.dsve.character.Player;
import se.dsve.character.observer.PlayerEventBus;
import se.dsve.combat.combat_utils.Attack;
import se.dsve.item.Weapon;

public class PlayerTest {

    @Test
    void attackTest(){
        //given
        PlayerEventBus bus = new PlayerEventBus();
        Weapon weapon = new Weapon("Test", 10);
        Player player = new Player("Test", 100, weapon, bus);
        //when
        Attack attack = player.attack();
        //then
        assertEquals(attack.damage(), player.getWeapon().getDamage());
    }

    @Test
    void takeDamageTest(){
        //given
        PlayerEventBus bus = new PlayerEventBus();
        Weapon weapon = new Weapon("Test", 10);
        Player player = new Player("Test", 100, weapon, bus);
        //when
        Attack attack = new Attack(10, false);
        player.takeDamage(attack);
        //then
        assertEquals(90, player.getHp());
    }

    @Test
    void potionHealTest(){
        //given
        PlayerEventBus bus = new PlayerEventBus();
        Weapon weapon = new Weapon("Test", 10);
        Player player = new Player("Test", 100, weapon, bus);
        //when
        Attack attack = new Attack(50, false);
        player.takeDamage(attack);
        int potionsPreUse = player.getPotions();
        player.usePotion();
        int potionsPostUse = player.getPotions();
        //then
        assertEquals(100, player.getHp());
        assertTrue(potionsPreUse > potionsPostUse);
    }

    @Test
    void levelUpTest(){
        //given
        PlayerEventBus bus = new PlayerEventBus();
        Weapon weapon = new Weapon("Test", 10);
        Player player = new Player("Test", 100, weapon, bus);
        //when
        int preRewardLV = player.getLevel();
        int preRewardHP = player.getTotalHp();
        player.battleReward(200, 200);
        int postRewardLV = player.getLevel();
        int postRewardHP = player.getTotalHp();
        //then
        assertTrue(postRewardLV > preRewardLV);
        assertTrue(postRewardHP > preRewardHP);
    }

    @Test
    void upgradeWeaponTest(){
        //given
        PlayerEventBus bus = new PlayerEventBus();
        Weapon weapon = new Weapon("Test", 10);
        Player player = new Player("Test", 100, weapon, bus);
        //when
        int preDmg = player.getWeapon().getDamage();
        player.getWeapon().upgrade();
        int postDmg = player.getWeapon().getDamage();
        //then
        assertTrue(postDmg > preDmg);
    }

    @Test
    void isDeadTest(){
        //given
        PlayerEventBus bus = new PlayerEventBus();
        Weapon weapon = new Weapon("Test", 10);
        Player player = new Player("Test", 100, weapon, bus);
        Attack attack = new Attack(300, false);
        //when
        player.takeDamage(attack);
        //then
        assertTrue(player.isDead());
        assertEquals(0, player.getHp());
    }
}
