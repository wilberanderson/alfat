package gui.buttons;

import gui.GUIFilledBox;
import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class VerticalScrollBar {
    Vector2f position;
    float width;
    float height;
    float fullRange;
    float factor;
    float currentPos;
    float windowHeight;
    float contentsHeight;
    GUIFilledBox filledBox;
    float offset = 0;

    //TODO: Gut this method
    public VerticalScrollBar(Vector2f position, float width, float fullRange, float windowHeight, float contentsHeight, float currentPos) {
        this.position = position;
        this.width = width;
        this.fullRange = fullRange;
        this.windowHeight = windowHeight;
        this.contentsHeight = contentsHeight;
        //The scroll bars height should be proportional to the ratio between the height of visible contents vs the full height of the contents
        if (windowHeight < contentsHeight) {
            factor = windowHeight / contentsHeight;
        } else {
            factor = 1;
        }
        height = fullRange * factor;

        //Create the filled box which is used for the visible scroll bar
        filledBox = new GUIFilledBox(new Vector2f(position.x, position.y + fullRange - height), new Vector2f(width, height), GeneralSettings.USERPREF.getScrollBarColor3f());

    }

    /**
     * Updates the scroll bar when window is resized
     * @param width the new width of the scroll bar
     * @param windowHeight the new height of the window
     * @param fullRange the new range where the scroll bar may travel
     * @param contentsHeight the new height of the contents
     */
    public void updateAspectRatio(float width, float windowHeight, float fullRange, float contentsHeight){
        //Update the position of the bottom left corner of the scroll bars travel
        position.x = position.x + this.width - width;
        position.y = -1 + windowHeight - fullRange;

        //Update the scroll bars size and position
        filledBox.getSize().x = width;
        filledBox.getPosition().x = position.x + offset;

        //Update class members
        this.width = width;
        this.windowHeight = windowHeight;
        this.fullRange = fullRange;
        this.contentsHeight = contentsHeight;

        //Update the scroll bars height
        if(windowHeight < contentsHeight){
            factor = windowHeight / contentsHeight;
        }else{
            factor = 1;
        }
        height = fullRange * factor;
        //Account for full range vs window height
        factor *= (fullRange/windowHeight);

        filledBox.getSize().y = height;

        //Set the scroll bar to be at the top of the window
        //TODO: When resizing doesn't reset the height of the text make this update appropriately
        filledBox.getPosition().y = position.y + fullRange - height;
    }

    /**
     * Updates the position of the scrollbar
     * @param change the amount the height of the scrollbar should change in the OpenGL coordinate system
     */
    public void changePosition(float change) {
        filledBox.getPosition().y -= change * factor;

        //Without these lines of code only the vertical scroll bar will have it's position gradually shift in use

        if(change == 0){
            if(filledBox.getPosition().y > 0){
                filledBox.getPosition().y = windowHeight - 1 - height;
            }else{
                filledBox.getPosition().y = windowHeight - fullRange - 1;
            }
        }
    }

    /**
     * Used to change the position when the width of the code window is changed
     * @param change
     */
    public void changeXPosition(float change){
        filledBox.getPosition().x += change;
        offset += change;
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
    public void changeContentsHeight(float change) {
        //Update the scroll bars factor
        contentsHeight += change;
        if(windowHeight < contentsHeight){
            factor = windowHeight / contentsHeight;
        }else{
            factor = 1;
        }
        //Account for full range vs window height
        factor *= (fullRange/windowHeight);
        //Change height while accounting for the size of the scrollbar
        filledBox.getPosition().y += filledBox.getSize().y;
        filledBox.getSize().y = fullRange*factor;
        filledBox.getPosition().y -= filledBox.getSize().y;
    }

    /**
     * Moves the scroll bar appropriately for splitscreen view
     */
    public void goSplitScreen(){
        //The scroll bar should be in the middle of the screen minus the width of the scroll bar
        position.x = 0f-width;
        filledBox.getPosition().x = 0f-width;
    }

    /**
     * Moves the scroll bar appropriately for minimized code window
     */
    public void minimize(){
        //move the scroll bar out of the visible screen
        position.x = -2f;
        filledBox.getPosition().x = -2f;
    }

    /**
     * Moves the scroll bar appropriately for maximized code window
     */
    public void maximize(){
        //The scroll bar should be at the right side of the screen minus the width of the scroll bar
        position.x = 1f-width;
        filledBox.getPosition().x = 1f-width;
    }

    public float getOffset(){
        return offset;
    }

    /**
     * Change the background color
     * @param color Vector3f
     * */
    public void setBackgroundColor(Vector3f color) {
        this.filledBox.setColor(color);
    }

}
