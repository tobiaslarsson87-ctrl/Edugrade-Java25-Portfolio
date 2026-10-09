package se.dsve.factory;
import org.junit.jupiter.api.Test;
import se.dsve.character.Boss;
import se.dsve.character.Monster;
import se.dsve.character.factory.MonsterFactory;
import se.dsve.game.engine_utils.Difficulty;

import static org.junit.jupiter.api.Assertions.*;

public class MonsterFactoryTest {

    @Test
    void createMonster_shouldReturnValidMonster() {
        MonsterFactory monsterFactory = new MonsterFactory(Difficulty.MEDIUM);
        Monster monster = monsterFactory.createMonster();

        assertNotNull(monster);
        assertNotNull(monster.getName());
        assertTrue(monster.getHp() > 0);
        assertTrue(monster.getDamage() > 0);
    }

    @Test
    void createBoss_shouldReturnValidMonster() {
        MonsterFactory monsterFactory = new MonsterFactory(Difficulty.MEDIUM);
        Boss boss = monsterFactory.createBoss();

        assertNotNull(boss);
        assertTrue(boss instanceof Boss);
        assertTrue(boss.getHp() > 0);
    }

    /***
     * Test on 10x values for Hp ranges out of range of the base stats no matter what monster it is.
     */
    @Test
    void powerScaling(){
        //given
        MonsterFactory monsterFactory = new MonsterFactory(Difficulty.TEST_POWER_SCALING);
        //when
        Monster monster = monsterFactory.createMonster();
        //then
        assertTrue(monster.getHp() > 100);
    }
}
