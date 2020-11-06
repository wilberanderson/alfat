package parser;

import gui.TextBox;

import java.util.ArrayList;

public class FlowChartObject {
    public String label = "";
    public String target = "";
    public ArrayList<LC3TLine> lines = new ArrayList<>();
    public int lineCount = 0;   //number of lines in the box
    public int startLine;   //the starting line number of the box
    public String alert = "";

    public boolean jumps = false;
    public FlowChartObject connection = null;

    public String getFullText(boolean comments){
        String temp = "";
        for (LC3TLine line : lines){
            temp = (temp + line.getLineText(comments) + "\n");
        }
        temp = temp.substring(0,temp.length()-1); //remove extra newline
        return temp;
    }

    public void addLine(LC3TLine line){
        lines.add(line);
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
        for (LC3TLine line : lines){
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
}
