package gui.Notifications;

import controllers.ApplicationController;
import gui.windows.PopupWindow;

public class EventSyntaxFileFailure extends Observer{
    public EventSyntaxFileFailure(Notifications subject){
        this.subject = subject;
        this.subject.register(this);
    }

    @Override
    public void update() {




        if (subject.getEvent() == AppEvents.INVALID_SYNTAX_FILE) {
            /*
            String contentsText = "Code Syntax File Error: Check syntax file path.";
            PopupWindow foo = new PopupWindow("Attention", contentsText);
            foo.makeButton(0.1f,0.1f,"  Ok  ");
            */
            ApplicationController.getHeader().setNotificationText("Syntax File Error: Check file.");
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
