package controllers.flowchartWindow;

import gui.GUIFilledBox;
import gui.GUIText;
import gui.TextLine;
import gui.textBoxes.FlowchartTextBox;
import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class FlowchartTextBoxController {

    private List<FlowchartTextBox> textBoxes = new ArrayList<>();
    private TextLineController textLineController = new TextLineController();
    boolean verbose = false;

    public FlowchartTextBoxController(){

    }

    /**
     * Changes the background color of the flowchart boxes
     * */
    public void changeTextBoxBackgroundcolor3f(Vector3f newBackgroundColor) {
        if(textBoxes != null || !textBoxes.isEmpty()) {
            for(int i = 0; i < textBoxes.size(); i++) {
                textBoxes.get(i).setBackgroundColor(newBackgroundColor);
            }
        }
    }

    /**
     * Changes the background color of the flowcharts number line background color
     * */
    public void changeTextBoxNumberLineBGColor3f(Vector3f newBackgroundColor) {
        if(textBoxes != null || !textBoxes.isEmpty()) {
            for(int i = 0; i < textBoxes.size(); i++) {
                textBoxes.get(i).setTextNumberFilledBoxBackgroundColor(newBackgroundColor);
            }
        }
    }



    public void add(Vector2f position, List<TextLine> textLines, int lineNumber, List<String> registers, String alert){
        FlowchartTextBox textBox = new FlowchartTextBox(position, registers, alert);
        //TODO: WHY IS THIS SET THIS COLOR!?!?!?!?
        textBox.setBackgroundColor(GeneralSettings.TEXT_BOX_BACKGROUND_COLOR);
        textBox.setBorderColor(GeneralSettings.TEXT_BOX_BORDER_COLOR);
        textBox.setTextColor(GeneralSettings.TEXT_COLOR);

        float minHeight = GeneralSettings.TEXT_BOX_BORDER_WIDTH;
        double greatestLength = 0;
        float longestLineNumber = 0;
        float lineHeight = GeneralSettings.FONT_SIZE*GeneralSettings.FONT_SCALING_FACTOR;

        List<TextLine> addedLines = new ArrayList<>();
        for(TextLine line : textLines){
            if(line.getLength() > greatestLength){
                greatestLength = line.getLength();
            }

            line.getPosition().setX(GeneralSettings.TEXT_BOX_BORDER_WIDTH*2 + position.x-1);
            line.getPosition().setY(position.y-minHeight - 1);
            addedLines.add(line);

            GUIText lineNumberText = new GUIText(Integer.toString(lineNumber), GeneralSettings.FONT_SIZE, GeneralSettings.FONT, new Vector2f(GeneralSettings.TEXT_BOX_BORDER_WIDTH + position.x-1, line.getPosition().y-lineHeight*textLines.size() - GeneralSettings.TEXT_BOX_BORDER_WIDTH/*+GeneralSettings.TEXT_BOX_BORDER_WIDTH + lineHeight*textLines.size()*/), GeneralSettings.FONT_WIDTH, GeneralSettings.FONT_EDGE, GeneralSettings.LINE_NUMBER_COLOR, null, false, true, false);
            textBox.getLineNumbers().add(lineNumberText);
            if(lineNumberText.getLength() > longestLineNumber){
                longestLineNumber = (float) lineNumberText.getLength();
            }

            minHeight+=lineHeight;
            lineNumber++;

        }
        if(textBox.getLineNumbers().size() > 0) {
            float offset = longestLineNumber*2;
            System.out.println(offset);
            for(TextLine line : addedLines){
                line.getPosition().setX(line.getPosition().x + offset);
                textLineController.add(line);
            }
        }


        textBox.setTextNumberFilledBox(new GUIFilledBox(position, new Vector2f(longestLineNumber*2 + 2*GeneralSettings.TEXT_BOX_BORDER_WIDTH, minHeight), GeneralSettings.USERPREF.getFlowchartBoxlinenumberBGColor3f()));
        textBox.setSize(new Vector2f((float)greatestLength*2 + 4*GeneralSettings.TEXT_BOX_BORDER_WIDTH + textBox.getTextNumberFilledBox().getSize().x,lineHeight*textLines.size() + GeneralSettings.TEXT_BOX_BORDER_WIDTH));
        textBox.setGuiFilledBox(new GUIFilledBox(position, textBox.getSize(), GeneralSettings.USERPREF.getFlowchartBoxbackgroundColor3f()));
        for(GUIText text : textBox.getTexts()){
            text.setPosition(new Vector2f(textBox.getTextNumberFilledBox().getPosition().x+textBox.getTextNumberFilledBox().getSize().x-1, text.getPosition().y));
        }
        for(GUIText text : textBox.getTexts()){
            if(text.getPositionBounds() == null){
                text.setPositionBounds(new Vector4f(position.x, position.y, position.x+textBox.getSize().x, position.y+textBox.getSize().y));
            }
        }
        setPosition(new Vector2f(textBox.getPosition().x, textBox.getPosition().y-textBox.getSize().y), textBox);
        textBoxes.add(textBox);
    }

    public void clear(){
        for(FlowchartTextBox textBox : textBoxes){
            textBox.clear();
        }
        textLineController.clear();
    }

    public void setPosition(Vector2f position, FlowchartTextBox textBox){
        textBox.changeHorizontalPosition(textBox.getPosition().x - position.x);
        textBox.changeVerticalPosition(textBox.getPosition().y - position.y);
        textBox.setPosition(position);
        textBox.getTextNumberFilledBox().setPosition(position);
        textBox.getGuiFilledBox().setPosition(new Vector2f(position.x, position.y));
    }

    public void locateRegister(String register) {
        for (FlowchartTextBox box : textBoxes) {
            if (verbose) System.out.println("Checking box " + box + " for register " + register);
            if (verbose) System.out.println("Contains registers: " + box.getRegisters());
            if (register != null && box.getRegisters().contains(register)) {
                if (verbose) System.out.println("Match found");
                box.setBackgroundColor(GeneralSettings.TEXT_COLOR);
                box.setTextColor(GeneralSettings.TEXT_BOX_BACKGROUND_COLOR);
            } else {
                box.setBackgroundColor(GeneralSettings.TEXT_BOX_BACKGROUND_COLOR);
                box.setTextColor(GeneralSettings.TEXT_COLOR);
            }
        }
    }

    public void locateAlert(String alert){
        for (FlowchartTextBox box : textBoxes){
            if (verbose) System.out.println("Checking box " + box + " for alert " + alert);
            if (verbose) System.out.println("Contains registers: " + box.getRegisters());
            if (alert != null && box.getAlert().equals(alert)){
                if (verbose) System.out.println("Match found");
                box.setBackgroundColor(GeneralSettings.TEXT_COLOR);
                box.setTextColor(GeneralSettings.TEXT_BOX_BACKGROUND_COLOR);
            } else {
                box.setBackgroundColor(GeneralSettings.TEXT_BOX_BACKGROUND_COLOR);
                box.setTextColor(GeneralSettings.TEXT_COLOR);
            }
        }
    }

    public TextLineController getTextLineController(){
        return textLineController;
    }

    public List<FlowchartTextBox> getTextBoxes() {
        return textBoxes;
    }
}
