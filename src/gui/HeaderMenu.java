package gui;

import fontMeshCreator.FontType;
import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import rendering.text.TextMaster;
import utils.InputManager;

import java.util.List;

public class HeaderMenu extends TextButton{

    private List<TextButton> dropDownButtons;

    public HeaderMenu(Vector2f position, String text, Vector3f backgroundColor, Vector3f highlightColor, Vector3f textColor, FontType font, float fontSize, float width, float edge, List<TextButton> buttons) {
        super(position, text, backgroundColor, highlightColor, textColor, font, fontSize, width, edge);
        dropDownButtons = buttons;
        InputManager.buttons.add(this);
        Vector2f buttonPosition = new Vector2f(position.x, position.y- GeneralSettings.FONT_SIZE*GeneralSettings.FONT_SCALING_FACTOR - 2*GeneralSettings.TEXT_BUTTON_PADDING);
        float greatestWidth = 0;
        for(TextButton button : dropDownButtons){
            if(button.getSize().x > greatestWidth){
                greatestWidth = button.getSize().x;
            }
        }
        Vector2f buttonSize = new Vector2f(greatestWidth*2 + 2*GeneralSettings.TEXT_BUTTON_PADDING, GeneralSettings.FONT_SIZE*GeneralSettings.FONT_SCALING_FACTOR + 2*GeneralSettings.TEXT_BUTTON_PADDING);

        for(TextButton button : dropDownButtons){
            button.initializePosition(buttonPosition, buttonSize);
            buttonPosition.y -= (GeneralSettings.FONT_SIZE*GeneralSettings.FONT_SCALING_FACTOR + 2*GeneralSettings.TEXT_BUTTON_PADDING);
        }
        close();
    }

    @Override
    public void onPress(){
        System.out.println("HeaderMenu clicked");
        for(TextButton button : dropDownButtons){
            InputManager.buttons.add(button);
            TextMaster.loadGuiText(button.getText());
        }
    }

    public void close(){
        for(TextButton button : dropDownButtons){
            InputManager.buttons.remove(button);
            TextMaster.removeGuiText(button.getText());
        }
    }

}
