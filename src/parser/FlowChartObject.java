package parser;

import gui.texts.FormattedTextLine;

import java.util.ArrayList;
import java.util.List;

public class FlowChartObject {
    public String label = "";
    public String target = "";
    public ArrayList<CodeLine> lines = new ArrayList<>();
    public int lineCount = 0;   //number of lines in the box
    public int startLine;   //the starting line number of the box
    public String alert = "";
    public int boxNumber;

    public boolean jumps = false;
    public boolean returns = false;
    public FlowChartObject connection = null;

    private List<FormattedTextLine> formattedTextLines = new ArrayList<>();

    public String getFullText(boolean comments){
        String temp = "";
        for (CodeLine line : lines){
            temp = (temp + line.getLineText(comments) + "\n");
        }
        temp = temp.substring(0,temp.length()-1); //remove extra newline
        return temp;
    }

    public void addLine(CodeLine line){
        lines.add(line);
        formattedTextLines.add(line.getTextLine());
    }

    public void setLineCount(int lineCount) {
        this.lineCount = lineCount;
    }

    public int getLineCount() {
        return lineCount;
    }

    public int getStartLine() {
        return startLine;
    }

    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    public boolean isInRange(int index){
        return (index >= startLine && index <= startLine + lineCount);
    }

    public boolean isJumps() {
        return jumps;
    }

    public void setLabel(String label){
        this.label = label;
    }

    public ArrayList<String> getRegisters(){
        ArrayList<String> registers = new ArrayList<>();
        for (CodeLine line : lines){
            if (!line.getRegisters().isEmpty()){
                for (String register : line.getRegisters()){
                    if (!registers.contains(register)) {
                        registers.add(register);
                    }
                }
            }
        }
        return registers;
    }

    public void setBoxNumber(int boxNumber) {
        this.boxNumber = boxNumber;
    }

    public int getBoxNumber() {
        return boxNumber;
    }

    public List<FormattedTextLine> getTextLines(){
        return formattedTextLines;
    }

    public boolean isReturns() {
        return returns;
    }

    public void setReturns(boolean ret){
        this.returns = ret;
    }
}
