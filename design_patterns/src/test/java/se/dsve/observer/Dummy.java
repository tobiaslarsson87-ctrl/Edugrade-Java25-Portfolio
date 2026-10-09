package se.dsve.observer;
import se.dsve.character.Player;
import se.dsve.character.observer.PlayerObserver;

class Dummy implements PlayerObserver {
    boolean called = false;
    Player recieved = null;

    @Override
    public void update(Player player) {
        called = true;
        recieved = player;
    }
}
