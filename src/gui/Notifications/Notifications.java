package gui.Notifications;

import controllers.ApplicationController;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Notifications, this is a simple observer pattern. This class acts has the subject.
 * (1) Handles sets and gets application events
 * (2) registers observers
 * (3) and notifies observers
 * @see gui.Notifications.Observer
 * @see gui.Notifications.AppEvents
 * */
public class Notifications {
    //This is the list of registered observers
    private List<Observer> observers = new ArrayList<Observer>();
    //The current application event
    private AppEvents appEvent;
    //A toggle to set a timer set in a observer
    private boolean clearTimer = false;
    //A queue to prevent multiple timer threads from spawning (probably a bad way to do this TO BAD!)
    protected Queue<Boolean> eventsQueue  = new LinkedList<Boolean>();


    public Notifications() {
        //Set the observers here
        new EventFileOpenSuccess(this);
        new EventFileOpenFailure(this);
        new EventSyntaxFileFailure(this);
        new EventFixedFormNotSet(this);

    }


    /** Returns whether a timer thread has already been set*/
    protected boolean isClearTimerRunning() { return clearTimer; }
    /**Sets the timer thread*/
    protected void setClearTimer(boolean clearTimer) { this.clearTimer = clearTimer; }
    /**Returns the current appEvent that was set*/
    public AppEvents getEvent() { return appEvent; }

    /**Set a app event and notify all observers*/
    public void setEvent(AppEvents appEvent) {
        this.appEvent = appEvent;
        eventsQueue.add(true); //This adds a padding for the timer to prevent multiple timer threads from spawning
        notifyAllObservers();
    }

    /**register a observer*/
    public void register(Observer observer) { observers.add(observer); }

    /**notify all observers of the current event*/
    public void notifyAllObservers() {
        //TODO: Should make threaded to allow a popupWindow notification system that does not cause two popup windows to open at once
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
