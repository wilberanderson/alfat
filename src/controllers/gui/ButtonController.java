package controllers.gui;

import controllers.ApplicationController;
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

    public static void remove(Button b){
        buttons.remove(b);
    }

    public static void click(Vector2f position){

        boolean buttonPressed = false;
        List<HeaderMenu> toClose = new ArrayList<>();

        for(int i = buttons.size()-1; i >= 0; i--){
            Button b = buttons.get(i);
            if(position.x > b.getPosition().x && position.y > b.getPosition().y && position.x < b.getPosition().x + b.getSize().x && position.y < b.getPosition().y + b.getSize().y){
                if (b instanceof HeaderMenu){
                    openMenu = (HeaderMenu) b;
                } else {
                    toClose.add(openMenu);
                    openMenu = null;
                }
                b.onPress();
                buttonPressed = true;
                break;
            }
        }
        if (!buttonPressed && openMenu != null){
            toClose.add(openMenu);
        }
        for(HeaderMenu b : toClose){
            b.close();
        }
    }

    public static void hover(Vector2f position){
        for(int i = buttons.size()-1; i >= 0; i--){
            Button b = buttons.get(i);
            if(b instanceof HighlightableButton) {
                if (position.x > b.getPosition().x && position.y > b.getPosition().y && position.x < b.getPosition().x + b.getSize().x && position.y < b.getPosition().y + b.getSize().y) {
                    if (!((HighlightableButton) b).isHighlighted()) {
                        ((HighlightableButton) b).highlight();
                    }
                } else {
                    if (((HighlightableButton) b).isHighlighted()){
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
