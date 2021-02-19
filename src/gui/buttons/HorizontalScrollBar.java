package gui.buttons;

import gui.GUIFilledBox;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class HorizontalScrollBar {
    Vector2f position;
    float height;
    float width;
    float fullRange;
    float factor;
    float currentPos;
    float windowWidth;
    float contentsWidth;
    GUIFilledBox filledBox;
    boolean maximized = false;
    boolean minimized = false;
    float splitscreenWidth;
    float fullscreenWidth;

    public HorizontalScrollBar(Vector2f position, float height, float fullRange, float windowWidth, float contentsWidth, float currentPos) {
        this.position = position;
        this.height = height;
        this.fullRange = fullRange;
        this.windowWidth = windowWidth;
        this.contentsWidth = contentsWidth;
        //The scroll bars height should be proportional to the ratio between the height of visible contents vs the full height of the contents
        if (windowWidth < contentsWidth) {
            factor = windowWidth / contentsWidth;
        } else {
            factor = 1;
        }
        width = fullRange * factor;

        //Save widths to be used for minimizing and maximizing
        splitscreenWidth = fullRange;
        fullscreenWidth = fullRange + 1;

        //Create the filled box which is used for the visible scroll bar
        //TODO: Create color in general settings and user pref
        filledBox = new GUIFilledBox(new Vector2f(position.x, position.y), new Vector2f(width, height), new Vector3f(0.3f, 0.3f, 0.3f));
    }

    /**
     * Updates the scroll bar when window is resized
     * @param height the new height of the scroll bar
     * @param windowWidth the new height of the window
     * @param fullRange the new range where the scroll bar may travel
     */
    public void updateAspectRatio(Vector2f position, float height, float windowWidth, float fullRange){
        //Update the position of the bottom left corner of the scroll bars travel
        this.position = position;

        //Update the scroll bars size and position
        filledBox.getSize().y = height;
        filledBox.getPosition().x = position.x;

        //Update class members
        this.height = height;
        this.windowWidth = windowWidth;
        this.fullRange = fullRange;

        //Update the scroll bars width
        if(windowWidth < contentsWidth){
            factor = windowWidth / contentsWidth;
        }else{
            factor = 1;
        }
        //Account for full range vs window width
        factor *= (fullRange/windowWidth);

        width = fullRange * factor;
        filledBox.getSize().x = width;

        //Set the scroll bar to be at the top of the window
        //TODO: When resizing doesn't reset the horizontal scroll of the text make this update appropriately
        filledBox.getPosition().x = position.x;
    }

    /**
     * Updates the position of the scrollbar
     * @param change the amount the height of the scrollbar should change in the OpenGL coordinate system
     */
    public void changePosition(float change) {
        filledBox.getPosition().x -= change;
    }

    public GUIFilledBox getFilledBox() {
        return filledBox;
    }

    public float getFactor() {
        return factor;
    }

    /**
     * Updates the scrollbar to account for a change in the total contents of the window
     * @param change
     */
    public void changeContentsWidth(float change) {
        //Update the scroll bars factor
        contentsWidth += change;
        if(windowWidth < contentsWidth){
            factor = windowWidth / contentsWidth;
        }else{
            factor = 1;
        }
        //Account for full range vs window width
        factor *= (fullRange/windowWidth);
        //Change height while accounting for the size of the scrollbar
        filledBox.getPosition().x += filledBox.getSize().x;
        filledBox.getSize().x = fullRange*factor;
        filledBox.getPosition().x -= filledBox.getSize().x;
    }

    /**
     * Moves the scroll bar appropriately for splitscreen view
     */
    public boolean goSplitScreen(){
        //Only perform split screen operations if the code window was previously maximized
        if(maximized){
            //The window width and range should be adjusted by the difference between the saved splitscreen width and the total available width
            windowWidth -= fullscreenWidth - splitscreenWidth;
            fullRange -= fullscreenWidth - splitscreenWidth;

            //Update factor appropriately
            if(windowWidth < contentsWidth){
                factor = windowWidth / contentsWidth;
            }else {
                factor = 1;
            }
            //Account for full range vs window width
            factor *= (fullRange/windowWidth);
            //Update the apparent size of the scroll bar
            filledBox.getSize().x = fullRange*factor;

            //Save that the window is not maximized to prevent repeat calls of these adjustments
            maximized = false;
        }
        //If minimized the scroll bar needs to be brought back into screen
        if(minimized){
            filledBox.getPosition().y += height;
            minimized = false;
        }

        //Code window uses this return to determine whether the scroll bar should be rendered and properly adjust window contents
        return factor == 1;
    }

    /**
     * Moves the scroll bar appropriately for minimized code window
     */
    public void minimize(){
        //Move the scroll bar out of the visible screen
        if(!minimized) {
            filledBox.getPosition().y -= height;
            minimized = true;
        }
    }

    /**
     * Moves the scroll bar appropriately for maximized code window
     */
    public boolean maximize(){
        //If the window is not maximized then perform resizing operations
        if(!maximized){
            //The window width and range should be adjusted by the difference between the saved splitscreen width and the total available width
            windowWidth += fullscreenWidth - splitscreenWidth;
            fullRange += fullscreenWidth - splitscreenWidth;

            //Update factor appropriately
            if(windowWidth < contentsWidth){
                factor = windowWidth / contentsWidth;
            }else{
                factor = 1;
            }
            //Account for full range vs window width
            factor *= (fullRange/windowWidth);

            //Update the apparent size of the scroll bar
            filledBox.getSize().x = fullRange*factor;

            //Save that the window is maximized to prevent repeat calls of these adjustments
            maximized = true;
        }
        //If minimized the scroll bar needs to be brought back into screen
        if(minimized){
            filledBox.getPosition().y += height;
            minimized = false;
        }
        //Code window uses this return to determine whether the scroll bar should be rendered and properly adjust window contents
        return factor == 1;
    }
}
