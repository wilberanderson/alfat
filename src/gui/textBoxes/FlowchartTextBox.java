package gui.textBoxes;

import gui.FlowchartLine;
import gui.texts.FormattedTextLine;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

public class FlowchartTextBox extends TextBox{
    private List<String> registers;
    private String alert;
    private boolean isHighlighted = false;
    private List<FormattedTextLine> textLines;
    private int boxNumber;

    public FlowchartTextBox(Vector2f position, List<String> registers, String alert, List<FormattedTextLine> textLines, int boxNumber){
        super();
        super.setPosition(position);
        this.registers = registers;
        this.alert = alert;
        this.textLines = textLines;
        this.boxNumber = boxNumber;
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

    public List<FormattedTextLine> getTextLines(){
        return textLines;
    }

    public int getBoxNumber() {
        return boxNumber;
    }
}
