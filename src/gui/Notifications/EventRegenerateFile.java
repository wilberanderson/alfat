package gui.Notifications;

import controllers.ApplicationController;

public class EventRegenerateFile extends Observer {

    public EventRegenerateFile(Notifications subject){
        this.subject = subject;
        this.subject.register(this);
    }

    @Override
    public void update() {
        if (subject.getEvent() == AppEvents.REGENERATE_FROM_FILE) {

            ApplicationController.getHeader().setNotificationText("Flowchart regenerated from source file.");

            if (!subject.isClearTimerRunning()) {
                subject.setClearTimer(true);
                Runnable myRunnable =
                        new Runnable(){
                            boolean continueWait;
                            public void run(){
                                do {
                                    try {
                                        Thread.sleep(1500);
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
