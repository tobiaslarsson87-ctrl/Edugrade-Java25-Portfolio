package se.dsve.combat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import se.dsve.combat.combat_utils.BattleAction;
import se.dsve.combat.combat_utils.CombatResult;
import se.dsve.game.GameState;
import se.dsve.game.engine_utils.Difficulty;
import se.dsve.presentation.hud.BattleMOCK;
import static org.junit.jupiter.api.Assertions.*;

public class CombatServiceTest {

    @AfterEach
    void resetDifficulty() {
        GameState.getInstance().setDifficulty(Difficulty.MEDIUM);
    }

    @Test
    void playerWinTest(){
        //given
        BattleMOCK mock = new BattleMOCK();
        mock.setNextAction(BattleAction.ATTACK);
        CombatService combat = new CombatService(mock);
        Dummy player = new Dummy(1000, 1000);
        Dummy monster = new Dummy(1, 1);
        //when
        CombatResult result = combat.fight(player, monster);
        //then
        assertEquals(CombatResult.PLAYER_WIN, result);
    }

    @Test
    void playerLossTest(){
        //given
        BattleMOCK mock = new BattleMOCK();
        mock.setNextAction(BattleAction.ATTACK);
        CombatService combat = new CombatService(mock);
        Dummy player = new Dummy(1, 1);
        Dummy monster = new Dummy(1000, 1000);
        //when
        CombatResult result = combat.fight(player, monster);
        //then
        assertEquals(CombatResult.PLAYER_LOSS, result);
    }

    @Test
    void playerEscapeSuccessTest(){
        //given
        BattleMOCK mock = new BattleMOCK();
        mock.setNextAction(BattleAction.ESCAPE);
        CombatService combat = new CombatService(mock);
        Dummy player = new Dummy(9999, 1);
        Dummy monster = new Dummy(1, 1);
        //when
        GameState.getInstance().setDifficulty(Difficulty.TEST_ALWAYS);
        CombatResult result = combat.fight(player, monster);
        //then
        assertEquals(CombatResult.PLAYER_ESCAPE, result);
    }

    @Test
    void playerEscapeFailTest(){
        //given
        BattleMOCK mock = new BattleMOCK();
        mock.setNextAction(BattleAction.ESCAPE);
        CombatService combat = new CombatService(mock);
        Dummy player = new Dummy(1, 1);
        Dummy monster = new Dummy(1, 9999);
        //when
        GameState.getInstance().setDifficulty(Difficulty.TEST_IMPOSSIBLE);
        CombatResult result = combat.fight(player, monster);
        //then
        assertEquals(CombatResult.PLAYER_LOSS, result);
    }

    @Test
    void fallBackTest(){
        //given
        BattleMOCK mock = new BattleMOCK();
        mock.setNextAction(BattleAction.ESCAPE);
        CombatService combat = new CombatService(mock);
        Dummy player = new Dummy(0, 0);
        Dummy monster = new Dummy(0, 0);
        //when
        CombatResult result = combat.fight(player, monster);
        //then
        assertEquals(CombatResult.PLAYER_ESCAPE, result);
    }

    @Test
    void playerActionExecutesFirstTest(){
        //given
        BattleMOCK mock = new BattleMOCK();
        mock.setNextAction(BattleAction.ATTACK);
        CombatService combat = new CombatService(mock);
        Dummy player = new Dummy(1, 1);
        Dummy monster = new Dummy(1, 1);
        //when
        CombatResult result = combat.fight(player, monster);
        //then
        assertEquals(CombatResult.PLAYER_WIN, result);
    }
}
