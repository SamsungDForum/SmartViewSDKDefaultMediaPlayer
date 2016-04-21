package samsung.defaultmediaplayer;

/**
 * @author Ankit Saini
 * Interface class for registering & deregistering to Cast status notifications.
 */
public interface ObserverHandler {
    public void registerObserver(Observer observer);
    public void removeObserver(Observer observer);
    public void castStatusChangeObserver(CastStates value);
}
