package gui.Notifications;

import controllers.ApplicationController;
import gui.buttons.Button;
import gui.buttons.TextButton;
import gui.guiElements.GUIElement;
import gui.texts.GUIText;
import gui.windows.PartialWindow;
import gui.windows.PopupWindow;
import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;

public class EventFixedFormNotSet extends Observer {

    public EventFixedFormNotSet(Notifications subject) {
        this.subject = subject;
        this.subject.register(this);
    }

    @Override
    public void update() {
        if(subject.getEvent() == AppEvents.FIXED_FORM_SYNTAX_NOT_SET) {
            /*
            String contentsText = "Application is in free form mode.";
            PopupWindow foo = new PopupWindow("Attention", contentsText);
            foo.makeButton(0.1f,0.1f,"  Ok  ");
              */
            ApplicationController.getHeader().setNotificationText("Attention: Syntax in free form mode.");
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
