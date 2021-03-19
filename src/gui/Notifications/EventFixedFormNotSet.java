package gui.Notifications;

import controllers.ApplicationController;
import gui.guiElements.GUIElement;
import gui.texts.GUIText;
import gui.windows.PartialWindow;
import gui.windows.PopupWindow;
import org.lwjgl.util.vector.Vector2f;

public class EventFixedFormNotSet extends Observer {

    public EventFixedFormNotSet(Notifications subject) {
        this.subject = subject;
        this.subject.register(this);
    }

    @Override
    public void update() {
        if(subject.getEvent() == AppEvents.FIXED_FORM_SYNTAX_NOT_SET) {

            //TODO: Add a popup menu to tell user info
//            String contentsText = "Attention:\n ALFAT is in free form mode because the fixed form json information is incorrect or missing. Check JSON if you want fixed from mode.";
//            PopupWindow foo = new PopupWindow("foobar", "");
//            foo.addElement(new GUIElement(new GUIText("Attention:", 4, new Vector2f(-.9f, 0.9f), 1.8f)));
//            foo.addElement(new GUIElement(new GUIText("ALFAT is in free form mode because the fixed form json information is incorrect or missing.", 4, new Vector2f(-.9f, 0.7f), 1.8f)));
//            foo.addElement(new GUIElement(new GUIText("Check JSON if you want fixed from mode.", 4, new Vector2f(-.9f, 0.5f), 1.8f)));
//            foo.addElement(new GUIElement(new GUIText("test", 4, new Vector2f(-.9f, 0.3f), 1.8f)));
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
