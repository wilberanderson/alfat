package gui.Notifications;
/**This is a observer class for notifications
 * @see gui.Notifications.Notifications*/
public abstract class Observer {
    protected Notifications subject;
    public abstract void update();
}