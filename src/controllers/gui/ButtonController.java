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
            //If b was clicked
            if (position.x > b.getPosition().x && position.y > b.getPosition().y && position.x < b.getPosition().x + b.getSize().x && position.y < b.getPosition().y + b.getSize().y) {
                //If b is a header menu then it may have to close
                if (b instanceof HeaderMenu) {
                    //If b was a different menu than the currently open one add the open menu to toClose
                    if (openMenu != b && openMenu != null) {
                        toClose.add(openMenu);
                    }
                    openMenu = (HeaderMenu) b;
                }
                //If a menu is open close it
                else if(openMenu != null){
                    toClose.add(openMenu);
                    openMenu = null;
                }
                //If the button is in the same window as the click was then do it's on press operations
                if(b.getWindow() == window) {
                    b.onPress();
                    buttonPressed = true;
                }
                break;
            }
        }
        //If no button was pressed close all header menus
        if (!buttonPressed){
            for(Button b : buttons){
                if(b instanceof HeaderMenu){
                    if(((HeaderMenu) b).isOpen){
                        toClose.add(openMenu);
                    }
                }
            }
        }
        //Close any header menus asked to be closed
        for(HeaderMenu b : toClose){
            b.close();
        }
    }

    /**
     * Processes each button to determine whether it was hovered
     * @param window
     * @param position
     */
    public static void hover(long window, Vector2f position){
        for(int i = buttons.size()-1; i >= 0; i--){
            Button b = buttons.get(i);
            if(b.getWindow() == window) {
                //If the cursor is hovering over the button
                if (position.x > b.getPosition().x && position.y > b.getPosition().y && position.x < b.getPosition().x + b.getSize().x && position.y < b.getPosition().y + b.getSize().y) {
                    //Set an appropriate cursor icon
                    //TODO: Consider which cursor icon should be used
                    Mouse.setHand();
                    //If the button is highlightable then perform highlight operations if it is not highlighted
                    if (b instanceof HighlightableButton && !((HighlightableButton) b).isHighlighted()) {
                        ((HighlightableButton) b).highlight();
                    }
                }
                //If the cursor is not hovering the button
                else {
                    //If the button is highlightable then perform unhighlight operations if it is highlighted
                    if (b instanceof HighlightableButton && ((HighlightableButton) b).isHighlighted()) {
                        ((HighlightableButton) b).unhighlight();
                    }
                }
            }
        }
    }


    public static List<Button> getButtons(){
        return buttons;
    }

}
