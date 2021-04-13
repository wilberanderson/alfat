package gui.Notifications;

import controllers.ApplicationController;

public class EventFileOpenSuccess extends Observer{
    public EventFileOpenSuccess(Notifications subject){
        this.subject = subject;
        this.subject.register(this);
    }

    @Override
    public void update() {
        if (subject.getEvent() == AppEvents.OPEN_FILE) {
           ApplicationController.getHeader().setNotificationText("File opened successfully!");
            /* */
            if (!subject.isClearTimerRunning()) {
                subject.setClearTimer(true);
                Runnable myRunnable =
                        new Runnable(){
                            boolean continueWait;
                            public void run(){
                                do {
                                    try {
                                        Thread.sleep(3000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    continueWait = false;
                                    if(!subject.eventsQueue.isEmpty())
                                        continueWait = subject.eventsQueue.remove();
                                } while (continueWait);
                                ApplicationController.getHeader().clearNotificationText();
                                subject.setClearTimer(false);
                            }
                        };
                Thread thread = new Thread(myRunnable);
                thread.start();
            }

        }
    }
}
