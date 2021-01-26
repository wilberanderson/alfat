package gui.textBoxes;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

public class FlowchartTextBox extends TextBox{
    private List<String> registers;
    private String alert;
    private boolean isHighlighted = false;

    private static Vector3f unhighlightedColor;
    private static Vector3f highlightColor;

    public FlowchartTextBox(Vector2f position, List<String> registers, String alert){
        super();
        super.setPosition(position);
        this.registers = registers;
        this.alert = alert;
    }

    @Override
    public void setPosition(Vector2f position){
        super.setPosition(position);
    }

    public List<String> getRegisters(){
        return registers;
    }

    public String getAlert(){
        return alert;
    }



    public static Vector3f getunhighlightedColor() {
        return unhighlightedColor;
    }


    public static void setunhighlightedColor(Vector3f backgroundColor) {
        unhighlightedColor = backgroundColor;
    }

    public static Vector3f getHighlightColor() {
        return highlightColor;
    }

    public static void setHighlightColor(Vector3f highlightColor) {
        highlightColor = highlightColor;
    }

}
