package parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
* A TLine stands for Text line.
* A text line contains the line number, and full text of a single line of code.
* */
public abstract class TLine {
    private String lineText;
    private int lineNumber = 0;
    private String command;
    private String label;
    private String target;
    private List<String> registers = new ArrayList<>();
    private boolean jump;

    private TLine() {
        /* this space intentionally left blank
        *  please do not call the default constructor */
    }

    //getters and setters:

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getLineText() {
        return lineText;
    }

    public void setLineText(String lineText) {
        this.lineText = lineText;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command){
        this.command = command;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label){
        this.label = label;
    }

    public String getTarget(){
        return target;
    }

    public void setTarget(String target){
        this.target = target;
    }

    public List<String> getRegisters() {
        return registers;
    }

    public void setRegisters(List<String> registers) {
        this.registers = registers;
    }

    public boolean isJumps() {
        return jump;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    @Override
    public String toString() {
        return this.lineNumber + " " + this.lineText;
    }
}
