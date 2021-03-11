package controllers.flowchartWindow;

import controllers.TextLineController;
import gui.FlowchartLine;
import gui.GUIFilledBox;
import gui.texts.*;
import gui.textBoxes.FlowchartTextBox;
import main.GeneralSettings;
import org.lwjgl.system.CallbackI;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import utils.Printer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class FlowchartTextBoxController {

    private List<FlowchartTextBox> textBoxes = new ArrayList<>();
    private List<FlowchartTextBox> loadedTextBoxes = new ArrayList<>();
    private TextLineController textLineController;
    boolean verbose = false;
    private FlowchartTextBox selectedTextBox;

    private static Vector3f backgroundColor;
    private static Vector3f highlightedColor;
    private static Vector3f textNumberBackgroundColor;
    private Vector2f mousePosition = new Vector2f();
    private FlowchartWindowController parent;

    public FlowchartTextBoxController(TextLineController textLineController, FlowchartWindowController parent){
        this.textLineController = textLineController;
        this.parent = parent;
        backgroundColor = GeneralSettings.USERPREF.getFlowchartBoxbackgroundColor3f();
        highlightedColor = GeneralSettings.USERPREF.getFlowchartBoxHighlightColor3f();
        textNumberBackgroundColor = GeneralSettings.USERPREF.getFlowchartBoxlinenumberBGColor3f();
    }

    public void add(Vector2f position, List<FormattedTextLine> formattedTextLines, int lineNumber, List<String> registers, String alert){
        FlowchartTextBox textBox = new FlowchartTextBox(position, registers, alert, formattedTextLines);
        //background color unhighlighted
        textBox.setBackgroundColor(GeneralSettings.TEXT_BOX_BACKGROUND_COLOR);
        textBox.setBorderColor(GeneralSettings.TEXT_BOX_BORDER_COLOR);
//        textBox.setTextColor(GeneralSettings.TEXT_COLOR);

        float minHeight = GeneralSettings.TEXT_BOX_BORDER_WIDTH;
        double greatestLength = 0;
        float longestLineNumber = 0;
        float lineHeight = GeneralSettings.FONT_HEIGHT;

        boolean lineNumberChanged = false;

        for (FormattedTextLine line : formattedTextLines) {
            if (line.getLength() > greatestLength) {
                greatestLength = line.getLength();
            }

            line.getPosition().setX(GeneralSettings.TEXT_BOX_BORDER_WIDTH * 2 + position.x);
            line.getPosition().setY(position.y - minHeight);

            LineNumberWord lineNumberText = new LineNumberWord(Integer.toString(lineNumber),new Vector2f(GeneralSettings.TEXT_BOX_BORDER_WIDTH + position.x, line.getPosition().y - lineHeight * formattedTextLines.size() - GeneralSettings.TEXT_BOX_BORDER_WIDTH));
            line.getWords()[0] = lineNumberText;
            if (lineNumberText.getLength() > longestLineNumber) {
                if(longestLineNumber > 0){
                    lineNumberChanged = true;
                }
                longestLineNumber = (float) lineNumberText.getLength();
            }

            textLineController.addFlowchartTextLine(line);

            minHeight += lineHeight;
            lineNumber++;

        }
        if(lineNumberChanged){
            for(FormattedTextLine line: formattedTextLines){
                line.changeContentsHorizontalPosition(longestLineNumber*2 - (float)formattedTextLines.get(0).getWords()[0].getLength()*2, false);
            }
        }

        textBox.setTextNumberFilledBox(new GUIFilledBox(position, new Vector2f(longestLineNumber * 2 + 2 * GeneralSettings.TEXT_BOX_BORDER_WIDTH, minHeight), GeneralSettings.LINE_NUMBER_BACKGROUND_COLOR));
        textBox.setSize(new Vector2f((float) greatestLength * 2 + 4 * GeneralSettings.TEXT_BOX_BORDER_WIDTH + textBox.getTextNumberFilledBox().getSize().x + GeneralSettings.FLOWCHART_TEXT_BOX_INTERNAL_PAD_RIGHT, lineHeight * formattedTextLines.size() + GeneralSettings.TEXT_BOX_BORDER_WIDTH));
        textBox.setGuiFilledBox(new GUIFilledBox(position, textBox.getSize(), GeneralSettings.TEXT_BOX_BACKGROUND_COLOR));
//        for (Text text : textBox.getTexts()) {
//            text.setPosition(new Vector2f(textBox.getTextNumberFilledBox().getPosition().x + textBox.getTextNumberFilledBox().getSize().x, text.getPosition().y));
//        }
        setPosition(new Vector2f(textBox.getPosition().x, textBox.getPosition().y - textBox.getSize().y), textBox);
        textBoxes.add(textBox);
    }

    public void clear(){
        for(FlowchartTextBox textBox : textBoxes){
//            textBox.clear();

        }
        textBoxes.clear();
        textLineController.clear();
    }

    public void setPosition(Vector2f position, FlowchartTextBox textBox){
//        textBox.changeHorizontalPosition(textBox.getPosition().x - position.x);
//        textBox.changeVerticalPosition(textBox.getPosition().y - position.y);
        textBox.setPosition(position);
        textBox.getTextNumberFilledBox().setPosition(new Vector2f(position));
        textBox.getGuiFilledBox().setPosition(new Vector2f(position));
    }

    public void clearHighlighting(){
        for (FlowchartTextBox box : textBoxes){
            box.setBackgroundColor(GeneralSettings.TEXT_BOX_BACKGROUND_COLOR);
            //box.setTextColor(GeneralSettings.TEXT_COLOR);
        }
        for (FlowchartLine line : parent.getFlowchartLineList()){
            line.setHighlight(false);
            line.getTerminator().setHighlighted(false);
        }
    }

    public void locateRegisters(String args) {
        // catch empty argument string
        if (args.isEmpty()) {
            //highlight nothing
            for (FlowchartTextBox box : textBoxes){
                box.setHighlighted(false);
            }
            return;
        }

        String operation = "and";
        boolean not = false;
        boolean[] candidate = new boolean[textBoxes.size()];
        Arrays.fill(candidate, true);

        List<String> argv = new LinkedList<String>(Arrays.asList(args.split(" ")));


        // method: create a map of all boxes and whether they match the string.
        // Then use boolean comparisons to compare to the candidate solution.

        while (argv.size() >= 1){
            if (argv.get(0).charAt(0)=='!' && argv.get(0).length() > 1){
                not = true;
                argv.set(0,argv.get(0).substring(1,argv.get(0).length()));
                System.out.println("not " + argv.get(0));
            }
            switch (argv.get(0).toUpperCase()) {
                case "NOT":
                case "!":
                    not = true;
                    break;
                case "&":
                case "&&":
                case "AND":
                    operation = "and";
                    break;
                case "|":
                case "||":
                case "OR":
                    operation = "or";
                    break;
                default:
                    if (not) {
                        if (operation.equals("and")) {
                            //System.out.println();
                            for (int i = 0; i < textBoxes.size(); i++) {
                                //System.out.println(candidate[i] + " && " + textBoxes.get(i).getRegisters().contains(argv.get(0)) + " -> " + (candidate[i] && textBoxes.get(i).getRegisters().contains(argv.get(0))));
                                candidate[i] = candidate[i] && !textBoxes.get(i).getRegisters().contains(argv.get(0));
                            }
                        } else {
                            for (int i = 0; i < textBoxes.size(); i++) {
                                candidate[i] = candidate[i] || !textBoxes.get(i).getRegisters().contains(argv.get(0));
                            }
                        }
                        not = false;
                    } else {
                        if (operation.equals("and")) {
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
            }
            argv.remove(0);
        }
        for (int i = 0; i < textBoxes.size(); i++){
            if (candidate[i]){
                textBoxes.get(i).setHighlighted(true);
                //textBoxes.get(i).setBackgroundColor(GeneralSettings.TEXT_COLOR);
                //textBoxes.get(i).setTextColor(GeneralSettings.TEXT_BOX_BACKGROUND_COLOR);
            } else {
                textBoxes.get(i).setHighlighted(false);
                //textBoxes.get(i).setBackgroundColor(GeneralSettings.TEXT_BOX_BACKGROUND_COLOR);
                //textBoxes.get(i).setTextColor(GeneralSettings.TEXT_COLOR);
            }
        }
    }

    void locateAlert(String alert){
        for (FlowchartTextBox box : textBoxes){
            if (verbose) System.out.println("Checking box " + box + " for alert " + alert);
            if (verbose) System.out.println("Contains registers: " + box.getRegisters());
            if (alert != null && box.getAlert().equals(alert)) {
                if (verbose) System.out.println("Match found");
                box.setHighlighted(true);
                //box.setBackgroundColor(GeneralSettings.TEXT_COLOR);
                //box.setTextColor(GeneralSettings.TEXT_BOX_BACKGROUND_COLOR);
            } else {
                box.setHighlighted(false);
                //box.setBackgroundColor(GeneralSettings.TEXT_BOX_BACKGROUND_COLOR);
                //box.setTextColor(GeneralSettings.TEXT_COLOR);
            }
        }
        for (FlowchartLine line : parent.getFlowchartLineList()){
            line.setHighlight(false);
            line.getTerminator().setHighlighted(false);
        }
    }

    public TextLineController getTextLineController(){
        return textLineController;
    }

    public List<FlowchartTextBox> getTextBoxes() {
        return textBoxes;
    }

    public static Vector3f getBackgroundColor() {
        return backgroundColor;
    }

    public static void setBackgroundColor(Vector3f backgroundColor) {
        FlowchartTextBoxController.backgroundColor = backgroundColor;
    }

    public static Vector3f getHighlightedColor() {
        return highlightedColor;
    }

    public static void setHighlightedColor(Vector3f highlightedColor) {
        FlowchartTextBoxController.highlightedColor = highlightedColor;
    }

    public static Vector3f getTextNumberBackgroundColor() {
        return textNumberBackgroundColor;
    }

    public static void setTextNumberBackgroundColor(Vector3f textNumberBackgroundColor) {
        FlowchartTextBoxController.textNumberBackgroundColor = textNumberBackgroundColor;
    }

    public void click(int key, int action){
        if(key == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE){
            // selectedTextBox = null;
            for(FlowchartTextBox textBox : textBoxes){
                if(mousePosition.x > textBox.getPosition().x && mousePosition.y > textBox.getPosition().y && mousePosition.x < textBox.getPosition().x + textBox.getSize().x && mousePosition.y < textBox.getPosition().y + textBox.getSize().y){
                    selectedTextBox = textBox;
                    textBox.setHighlighted(true);
                    for(FlowchartTextBox textBox_2 : textBoxes) {
                        if (selectedTextBox != textBox_2) textBox_2.setHighlighted(false);
                    }
                    // highlight lines
                    for (FlowchartLine line : parent.getFlowchartLineList()){
                        if     (line.getPositions().get(0).x                            == selectedTextBox.getPosition().x ||
                                line.getPositions().get(line.getPositions().size()-1).x == selectedTextBox.getPosition().x ||
                                line.getPositions().get(0).x                            == selectedTextBox.getPosition().x + selectedTextBox.getSize().x ||
                                line.getPositions().get(line.getPositions().size()-1).x == selectedTextBox.getPosition().x + selectedTextBox.getSize().x ||
                                line.getPositions().get(0).y                            == selectedTextBox.getPosition().y ||
                                line.getPositions().get(line.getPositions().size()-1).y == selectedTextBox.getPosition().y ||
                                line.getPositions().get(0).y                            == selectedTextBox.getPosition().y + selectedTextBox.getSize().y ||
                                line.getPositions().get(line.getPositions().size()-1).y == selectedTextBox.getPosition().y + selectedTextBox.getSize().y){
                            // ^ if line touches current box
                            line.setHighlight(true);
                            line.getTerminator().setHighlighted(true);
                        } else {
                            line.setHighlight(false);
                            line.getTerminator().setHighlighted(false);
                        }
                    }
                    return;
                }
            }
/*            for (FlowchartLine line : parent.getFlowchartLineList()){
                line.setHighlight(false);
                line.getTerminator().setHighlighted(false);
            }*/
        }
    }

    public void moveMouse(double xPos, double yPos){
        mousePosition.x = (float)xPos;
        mousePosition.y = (float)yPos;
    }

    public FlowchartTextBox getSelectedTextBox(){
        return selectedTextBox;
    }

    public void unload(FlowchartTextBox textBox){
        if(loadedTextBoxes.contains(textBox)){
            loadedTextBoxes.remove(textBox);
            for(FormattedTextLine line : textBox.getTextLines()){
                textLineController.unloadText(line, 0);
            }
        }
    }

    public void load(FlowchartTextBox textBox){
        if(!loadedTextBoxes.contains(textBox)){
            loadedTextBoxes.add(textBox);
            for(FormattedTextLine line : textBox.getTextLines()){
                textLineController.loadText(line, 0);
            }
        }
    }

    public List<FlowchartTextBox> getLoadedTextBoxes(){
        return loadedTextBoxes;
    }
}
