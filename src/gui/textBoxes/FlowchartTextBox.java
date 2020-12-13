package gui.textBoxes;

import gui.GUIFilledBox;
import gui.GUIText;
import gui.TextLine;
import gui.TextWord;
import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;
import rendering.text.TextMaster;

import java.util.ArrayList;
import java.util.List;

public class FlowchartTextBox extends TextBox{
    private List<String> registers;
    private String alert;

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

}
