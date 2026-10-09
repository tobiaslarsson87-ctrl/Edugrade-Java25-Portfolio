package se.dsve.character.observer;
import se.dsve.character.Player;

public interface PlayerSubject {
    void registerObserver(PlayerObserver observer);
    void removeObserver(PlayerObserver observer);
    void notifyObservers(Player player);
}
