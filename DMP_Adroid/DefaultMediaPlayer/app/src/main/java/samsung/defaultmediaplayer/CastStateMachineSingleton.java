package samsung.defaultmediaplayer;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ankit Saini
 * Class to maintain the cast satus of the application.
 */
public class CastStateMachineSingleton implements ObserverHandler{
    private final String TAG = "CastStateM.Singleton";
    private CastStates mCurrentCastState;
    private static CastStateMachineSingleton mInstance = null;
    private List<Observer> listners = new ArrayList<Observer>();

    private void initCastStateMachine() {
        mCurrentCastState = CastStates.IDLE;
    }

    public CastStateMachineSingleton() {
        initCastStateMachine();
    }

    public static CastStateMachineSingleton getInstance() {
        if(mInstance == null) {
            mInstance = new CastStateMachineSingleton();
        }
        return mInstance;
    }

    public void setCurrentCastState(CastStates currentCastState) {
        this.mCurrentCastState = currentCastState;
        castStatusChangeObserver(currentCastState);
    }

    public CastStates getCurrentCastState() {
        return this.mCurrentCastState;
    }

    /*
     * Implementing Observer class..
     */
    @Override
    public void registerObserver(Observer observer) {
        Log.v(TAG, "Observer Registered.");
        listners.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        Log.v(TAG, "Observer Un-registered.");
        listners.remove(observer);
    }

    @Override
    public void castStatusChangeObserver(CastStates currentState) {
        for (Observer observer : listners) {
            observer.onCastStatusChange(currentState);
        }
    }
}
