package se.dsve.character.observer;
import se.dsve.character.Player;
import java.util.ArrayList;
import java.util.List;

/***
 *Takes a list of PlayerObserver objects that check for specific conditions. The EventBus must be called
 * after every relevant action. If any condition observed is met it updates the relevant object,
 * in this case it is certain player milestones that can be reached.
 */
public class PlayerEventBus implements PlayerSubject {
    private final List<PlayerObserver> observers = new ArrayList<>();

    @Override
    public void registerObserver(PlayerObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(PlayerObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Player player) {
        for(PlayerObserver o : observers){
            o.update(player);
        }
    }
}
