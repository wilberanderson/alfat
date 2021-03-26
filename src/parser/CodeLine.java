package parser;

import gui.texts.EditableFormattedTextLine;
import gui.texts.FormattedTextLine;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** A CodeLine stands for LC-3 Text Line.
 *
 * It contains the line number, full text of a single line and other attributes of the parsed line.
 *
 * @author wilber
 * 
 *
*
* */

public class CodeLine {
    private String lineText;
    private int lineNumber;
    private String command;
    private String label;
    private String target;
    private List<String> registers = new ArrayList<>();
    private boolean jump;
    private boolean returns;
    private int codeBlock; // 0: none, 1:start, 2:end
    private FormattedTextLine textLine;
    //general constructor:
    private CodeLine(){
        //left blank
    }

    /** Constructor.
     *
     * @param fullText Full string text of the line.
     * @param comm String command of the line or null
     * @param label Line's label (as in "LABEL: COMMAND")
     * @param targetLabel Label the line points to
     * @param jump Whether the command is a jump/break command
     * @param registers Registers used by this line
     * @param line Line index
     */
    public CodeLine(String fullText, Optional<String> comm, String label, String targetLabel, boolean jump, List<String> registers, int line, boolean returns, int codeBlock) {
        comm.ifPresent(s -> this.command = s);
        if (!label.isEmpty())       this.label = label;
        if (!targetLabel.isEmpty()) this.target = targetLabel;
        if (!registers.isEmpty())   this.registers.addAll(registers);
        this.jump = jump;
        this.lineNumber = line;
        this.lineText = fullText;
        if (this.label == null){
            this.label = "";
        }
        this.returns = returns;
        this.codeBlock = codeBlock;
    }

    public String getLabel() {
        return label;
    }

    public boolean isJumps() {
        return this.jump;
    }

    public String toString() {
        return super.toString();
    }

    /** Returns the text of the line, with or without any comments included.
     *
     * @param comments True: print with comments. False: truncate after semicolon
     * @return Line text
     */
    public String getLineText(boolean comments) {
        if (comments) return lineText;
        int location = lineText.indexOf(";");
        if (location == -1) return lineText;
        else return lineText.substring(0,location);
    }

    /** Increment the line number of this object.
     *
     * @param n Amount to increment
     */
    public void incrementLineNumber(int n){
        this.lineNumber += n;
    }

    public String getLineText() {
        return lineText;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void setRegisters(List<String> registers) {
        this.registers = registers;
    }

    public List<String> getRegisters() {
        return registers;
    }

    public boolean isJump() {
        return jump;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getCommand() {
        return command;
    }

    public String getTarget() {
        return target;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public void setLineText(String lineText) {
        this.lineText = lineText;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public FormattedTextLine getTextLine() {
        return textLine;
    }

    public void setTextLine(FormattedTextLine textLine) {
        this.textLine = textLine;
    }

    public boolean isReturns() {
        return returns;
    }

    public void setReturns(boolean returns){
        this.returns = returns;
    }

    public int getCodeBlock(){
        return codeBlock;
    }
}
