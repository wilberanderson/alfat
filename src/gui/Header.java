package gui;

import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import utils.InputManager;

import java.util.ArrayList;
import java.util.List;

public class Header {
    List<HeaderMenu> menuList;
    GUIFilledBox guiFilledBox;
    Vector2f position;

    public Header(Vector2f position, Vector2f size){
        menuList = new ArrayList<>();
        guiFilledBox = new GUIFilledBox(position, size, GeneralSettings.HEADER_COLOR);
        this.position = position;



        List<TextButton> testMenuButtonList = new ArrayList<>();
        TextButton button = new TextButton("Text creation test0") {
            @Override
            public void onPress() {
                System.out.println("Test success Button 0");
                GeneralSettings.FILE_PATH = "Hello World";
            }
        };
        testMenuButtonList.add(button);
        button = new TextButton("Text creation test00") {
            @Override
            public void onPress() {
                System.out.println("Tes success Button 1");
            }
        };
        testMenuButtonList.add(button);
        button = new TextButton("Text creation test000") {
            @Override
            public void onPress() {
                System.out.println("Tesess Button 2");
            }
        };
        testMenuButtonList.add(button);
        button = new TextButton("Text creation test0000") {
            @Override
            public void onPress() {
                System.out.println("Testccess Button 3");
            }
        };
        testMenuButtonList.add(button);
        button = new TextButton("Text creation test000000") {
            @Override
            public void onPress() {
                System.out.println("Test scess Button 4");
            }
        };


        testMenuButtonList.add(button);
        HeaderMenu file = new HeaderMenu(new Vector2f(-1f, 1-GeneralSettings.FONT_SIZE*GeneralSettings.FONT_SCALING_FACTOR - 2*GeneralSettings.TEXT_BUTTON_PADDING), "File", new Vector3f(0, 0, 0), GeneralSettings.HIGHLIGHT_COLOR, GeneralSettings.CURSOR_COLOR, GeneralSettings.TACOMA, GeneralSettings.FONT_SIZE, GeneralSettings.FONT_WIDTH, GeneralSettings.FONT_EDGE, testMenuButtonList);
        menuList.add(file);
    }

    public GUIFilledBox getGuiFilledBox() {
        return guiFilledBox;
    }

    public Vector2f getPosition(){
        return position;
    }

    public List<HeaderMenu> getMenuList(){
        return menuList;
    }
}
