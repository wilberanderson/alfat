package controllers.flowchartWindow;

import gui.GUIFilledBox;
import gui.fontMeshCreator.Line;
import gui.texts.*;
import gui.textBoxes.FlowchartTextBox;
import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class FlowchartTextBoxController {

    boolean verbose = false;
    private List<FlowchartTextBox> textBoxes = new ArrayList<>();
    private TextLineController textLineController = new TextLineController();

    /**
     *
     */
    public FlowchartTextBoxController() {

    }

    /**
     * @param position
     * @param textLines
     * @param lineNumber
     * @param registers
     * @param alert
     */
    public void add(Vector2f position, List<TextLine> textLines, int lineNumber, List<String> registers, String alert) {
        FlowchartTextBox textBox = new FlowchartTextBox(position, registers, alert);
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

            //TODO: Add line numbers to formatted lines
            LineNumberWord lineNumberText = new LineNumberWord(Integer.toString(lineNumber),new Vector2f(GeneralSettings.TEXT_BOX_BORDER_WIDTH + position.x, line.getPosition().y - lineHeight * textLines.size() - GeneralSettings.TEXT_BOX_BORDER_WIDTH), "");
            line.getWords().add(0, lineNumberText);
            if (lineNumberText.getLength() > longestLineNumber) {
                longestLineNumber = (float) lineNumberText.getLength();
            }

            textLineController.addFlowchartTextLine(line);

            minHeight += lineHeight;
            lineNumber++;

        }

        textBox.setTextNumberFilledBox(new GUIFilledBox(position, new Vector2f(longestLineNumber * 2 + 2 * GeneralSettings.TEXT_BOX_BORDER_WIDTH, minHeight), GeneralSettings.LINE_NUMBER_BACKGROUND_COLOR));
        textBox.setSize(new Vector2f((float) greatestLength * 2 + 4 * GeneralSettings.TEXT_BOX_BORDER_WIDTH + textBox.getTextNumberFilledBox().getSize().x + GeneralSettings.FLOWCHART_TEXT_BOX_INTERNAL_PAD_RIGHT, lineHeight * textLines.size() + GeneralSettings.TEXT_BOX_BORDER_WIDTH));
        textBox.setGuiFilledBox(new GUIFilledBox(position, textBox.getSize(), GeneralSettings.TEXT_BOX_BACKGROUND_COLOR));
        for (Text text : textBox.getTexts()) {
            text.setPosition(new Vector2f(textBox.getTextNumberFilledBox().getPosition().x + textBox.getTextNumberFilledBox().getSize().x, text.getPosition().y));
        }
        setPosition(new Vector2f(textBox.getPosition().x, textBox.getPosition().y - textBox.getSize().y), textBox);
        textBoxes.add(textBox);
    }

    /**
     *
     */
    public void clear() {
        for (FlowchartTextBox textBox : textBoxes) {
            textBox.clear();
        }
        textLineController.clear();
    }

    /**
     * @param position
     * @param textBox
     */
    public void setPosition(Vector2f position, FlowchartTextBox textBox) {
        textBox.changeHorizontalPosition(textBox.getPosition().x - position.x);
        textBox.changeVerticalPosition(textBox.getPosition().y - position.y);
        textBox.setPosition(position);
        textBox.getTextNumberFilledBox().setPosition(new Vector2f(position));
        textBox.getGuiFilledBox().setPosition(new Vector2f(position));
    }

    /**
     * @param register
     */
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

    /**
     * @param alert
     */
    public void locateAlert(String alert) {
        for (FlowchartTextBox box : textBoxes) {
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

    /**
     * @return
     */
    public TextLineController getTextLineController() {
        return textLineController;
    }

    /**
     * @return
     */
    public List<FlowchartTextBox> getTextBoxes() {
        return textBoxes;
    }
}
