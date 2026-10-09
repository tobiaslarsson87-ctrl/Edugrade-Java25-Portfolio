package se.dsve.observer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import se.dsve.character.Player;
import se.dsve.character.observer.PlayerEventBus;

public class PlayerEventBusTest {

    @Test
    void notifyCallEventBusTest(){
        //given
        PlayerEventBus bus = new PlayerEventBus();
        Player player = new Player("Test", 100, null, bus);
        Dummy dummyObserver = new Dummy();
        bus.registerObserver(dummyObserver);
        //when
        player.notifyEventBus();
        //then
        assertTrue(dummyObserver.called);
        assertEquals(player, dummyObserver.recieved);
    }

    @Test
    void observerRemovalTest(){
        //given
        PlayerEventBus bus = new PlayerEventBus();
        Player player = new Player("Test", 100, null, bus);
        Dummy dummyObserver = new Dummy();
        bus.registerObserver(dummyObserver);
        //when
        bus.removeObserver(dummyObserver);
        player.notifyEventBus();
        //then
        assertFalse(dummyObserver.called);
    }

    @Test
    void notifyVoidDoesNotCrash(){
        //given //when
        PlayerEventBus bus = new PlayerEventBus();
        Player player = new Player("Test", 100, null, bus);
        //then
        assertDoesNotThrow(player::notifyEventBus);
    }
}
