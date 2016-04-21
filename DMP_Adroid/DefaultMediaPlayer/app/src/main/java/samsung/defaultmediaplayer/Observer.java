package samsung.defaultmediaplayer;

/**
 * @author Ankit Saini
 * Interface class for cast status change notification handler.
 */
public interface Observer {
    public void onCastStatusChange(CastStates value);
}
