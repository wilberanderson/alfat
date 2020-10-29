package gui;

import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Header {
    List<HeaderMenu> menuList;
    GUIFilledBox guiFilledBox;

    public Header(Vector2f position, Vector2f size){
        menuList = new ArrayList<>();
        guiFilledBox = new GUIFilledBox(position, size, GeneralSettings.HEADER_COLOR);
    }

    public GUIFilledBox getGuiFilledBox() {
        return guiFilledBox;
    }
}
