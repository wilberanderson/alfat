package controllers.flowchartWindow;

import controllers.TextLineController;
import gui.GUIFilledBox;
import gui.texts.*;
import gui.textBoxes.FlowchartTextBox;
import main.GeneralSettings;
import org.lwjgl.system.CallbackI;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class FlowchartTextBoxController {

    private List<FlowchartTextBox> textBoxes = new ArrayList<>();
    private TextLineController textLineController;
    boolean verbose = false;

    public FlowchartTextBoxController(TextLineController textLineController){
        this.textLineController = textLineController;
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
        //background color unhighlighted
        textBox.setBackgroundColor(GeneralSettings.TEXT_BOX_BACKGROUND_COLOR);
        textBox.setBorderColor(GeneralSettings.TEXT_BOX_BORDER_COLOR);
        textBox.setTextColor(GeneralSettings.TEXT_COLOR);

        float minHeight = GeneralSettings.TEXT_BOX_BORDER_WIDTH;
        double greatestLength = 0;
        float longestLineNumber = 0;
        float lineHeight = GeneralSettings.FONT_SIZE * GeneralSettings.FONT_SCALING_FACTOR;

        for (TextLine line : textLines) {
            if (line.getLength() > greatestLength) {
                greatestLength = line.getLength();
            }

            line.getPosition().setX(GeneralSettings.TEXT_BOX_BORDER_WIDTH * 2 + position.x);
            line.getPosition().setY(position.y - minHeight);

            LineNumberWord lineNumberText = new LineNumberWord(Integer.toString(lineNumber),new Vector2f(GeneralSettings.TEXT_BOX_BORDER_WIDTH + position.x, line.getPosition().y - lineHeight * textLines.size() - GeneralSettings.TEXT_BOX_BORDER_WIDTH), "");
            line.getWords()[0] = lineNumberText;
            if (lineNumberText.getLength() > longestLineNumber) {
                longestLineNumber = (float) lineNumberText.getLength();
            }

            textLineController.addFlowchartTextLine(line);

            minHeight += lineHeight;
            lineNumber++;

        }

        textBox.setTextNumberFilledBox(new GUIFilledBox(position, new Vector2f(longestLineNumber * 2 + 2 * GeneralSettings.TEXT_BOX_BORDER_WIDTH, minHeight), GeneralSettings.USERPREF.getFlowchartBoxlinenumberBGColor3f()));
        textBox.setSize(new Vector2f((float) greatestLength * 2 + 4 * GeneralSettings.TEXT_BOX_BORDER_WIDTH + textBox.getTextNumberFilledBox().getSize().x + GeneralSettings.FLOWCHART_TEXT_BOX_INTERNAL_PAD_RIGHT, lineHeight * textLines.size() + GeneralSettings.TEXT_BOX_BORDER_WIDTH));
        //Background color of text box
        textBox.setGuiFilledBox(new GUIFilledBox(position, textBox.getSize(), GeneralSettings.USERPREF.getFlowchartBoxbackgroundColor3f()));
        for (Text text : textBox.getTexts()) {
            text.setPosition(new Vector2f(textBox.getTextNumberFilledBox().getPosition().x + textBox.getTextNumberFilledBox().getSize().x, text.getPosition().y));
        }
        setPosition(new Vector2f(textBox.getPosition().x, textBox.getPosition().y - textBox.getSize().y), textBox);
        textBoxes.add(textBox);
    }

    public void clear(){
        for(FlowchartTextBox textBox : textBoxes){
            textBox.clear();

        }
        textBoxes.clear();
        textLineController.clear();
    }

    public void setPosition(Vector2f position, FlowchartTextBox textBox){
        textBox.changeHorizontalPosition(textBox.getPosition().x - position.x);
        textBox.changeVerticalPosition(textBox.getPosition().y - position.y);
        textBox.setPosition(position);
        textBox.getTextNumberFilledBox().setPosition(new Vector2f(position));
        textBox.getGuiFilledBox().setPosition(new Vector2f(position));
    }

    public void clearHighlighting(){
        for (FlowchartTextBox box : textBoxes){
            box.setBackgroundColor(GeneralSettings.TEXT_BOX_BACKGROUND_COLOR);
            box.setTextColor(GeneralSettings.TEXT_COLOR);
        }
    }

    public void locateRegisters(String args) {
        String operation = "and";
        boolean[] candidate = new boolean[textBoxes.size()];
        Arrays.fill(candidate, true);

        List<String> argv = new LinkedList<String>(Arrays.asList(args.split(" ")));

        while (argv.size() >= 1){
            switch (argv.get(0)){
                case "&":
                case "&&":
                case "AND":
                case "and":
                    operation = "and";
                    break;
                case "|":
                case "||":
                case "OR":
                case "or":
                    operation = "or";
                    break;
                default:
                    if (operation.equals("and")) {
                        //System.out.println();
                        for (int i = 0; i < textBoxes.size(); i++) {
                            //System.out.println(candidate[i] + " && " + textBoxes.get(i).getRegisters().contains(argv.get(0)) + " -> " + (candidate[i] && textBoxes.get(i).getRegisters().contains(argv.get(0))));
                            candidate[i] = candidate[i] && textBoxes.get(i).getRegisters().contains(argv.get(0));
                        }
                    } else {
                        for (int i = 0; i < textBoxes.size(); i++) {
                            candidate[i] = candidate[i] || textBoxes.get(i).getRegisters().contains(argv.get(0));
                        }
                    }
            }
            argv.remove(0);
        }
        for (int i = 0; i < textBoxes.size(); i++){
            if (candidate[i]){
                textBoxes.get(i).setBackgroundColor(GeneralSettings.TEXT_COLOR);
                textBoxes.get(i).setTextColor(GeneralSettings.TEXT_BOX_BACKGROUND_COLOR);
            } else {
                textBoxes.get(i).setBackgroundColor(GeneralSettings.TEXT_BOX_BACKGROUND_COLOR);
                textBoxes.get(i).setTextColor(GeneralSettings.TEXT_COLOR);
            }
        }
    }

    public void locateAlert(String alert){
        for (FlowchartTextBox box : textBoxes){
            if (verbose) System.out.println("Checking box " + box + " for alert " + alert);
            if (verbose) System.out.println("Contains registers: " + box.getRegisters());
            if (alert != null && box.getAlert().equals(alert)) {
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
