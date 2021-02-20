package controllers.gui;

import controllers.ApplicationController;
import gui.Mouse;
import gui.buttons.Button;
import gui.buttons.HeaderMenu;
import gui.buttons.HighlightableButton;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class ButtonController {

    public static List<Button> buttons = new ArrayList<>();
    private static HeaderMenu openMenu;

    /**
     * Adds a button to the list
     * @param b the button to be added
     */
    public static void add(Button b){
        buttons.add(b);
    }

    /**
     * Removes a button from the list
     * @param b the button to be removed
     */
    public static void remove(Button b){
        buttons.remove(b);
    }

    /**
     * Processes each button to determine whether it should be clicked
     * @param window the window the click was detected in
     * @param position the position where the mouse is
     */
    public static void click(long window, Vector2f position){

        boolean buttonPressed = false;
        List<HeaderMenu> toClose = new ArrayList<>();

        for(int i = buttons.size()-1; i >= 0; i--){
            Button b = buttons.get(i);
            if (position.x > b.getPosition().x && position.y > b.getPosition().y && position.x < b.getPosition().x + b.getSize().x && position.y < b.getPosition().y + b.getSize().y) {
                if (b instanceof HeaderMenu) {
                    if (openMenu != b && openMenu != null) {
                        toClose.add(openMenu);
                    }
                    openMenu = (HeaderMenu) b;
                } else if(openMenu != null){
                    toClose.add(openMenu);
                    openMenu = null;
                }
                if(b.getWindow() == window) {
                    b.onPress();
                    buttonPressed = true;
                }
                break;
            }
        }
        if (!buttonPressed){
            for(Button b : buttons){
                if(b instanceof HeaderMenu){
                    if(((HeaderMenu) b).isOpen){
                        toClose.add(openMenu);
                    }
                }
            }
        }
        for(HeaderMenu b : toClose){
            b.close();
        }
    }

    public static void hover(long window, Vector2f position){
        for(int i = buttons.size()-1; i >= 0; i--){
            Button b = buttons.get(i);
            if(b.getWindow() == window) {
                if (position.x > b.getPosition().x && position.y > b.getPosition().y && position.x < b.getPosition().x + b.getSize().x && position.y < b.getPosition().y + b.getSize().y) {
                    Mouse.setPointer();
                    if (b instanceof HighlightableButton && !((HighlightableButton) b).isHighlighted()) {
                        ((HighlightableButton) b).highlight();
                    }
                } else {
                    if (b instanceof HighlightableButton && ((HighlightableButton) b).isHighlighted()) {
                        ((HighlightableButton) b).unhighlight();
                    }
                }
            }
        }
    }

    public static void init(ApplicationController controller){

    }

    public static List<Button> getButtons(){
        return buttons;
    }

}
