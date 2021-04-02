package parser;

import gui.texts.FormattedTextLine;
import gui.texts.ImmediateWord;
import gui.texts.LabelWord;
import gui.texts.TextWord;
import org.lwjgl.system.CallbackI;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FlowChartObject {
    public String label = "";
    public String target = "";
    public ArrayList<CodeLine> lines = new ArrayList<>();
    public int lineCount = 0;   //number of lines in the box
    public int startLine;   //the starting line number of the box
    public String alert = "";
    public int boxNumber;
    public boolean minimized = false;
    public int codeBlock = 0;

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
        if (!minimized || (lineCount <=3) ) {return formattedTextLines;}
        List<FormattedTextLine> temp = new ArrayList<>();
        temp.add(formattedTextLines.get(0));
        ArrayList<TextWord> dottedLine = new ArrayList<>();
        dottedLine.add(new LabelWord("... (code folded - ", new Vector2f(0,0)));
        dottedLine.add(new ImmediateWord(""+(lineCount-2), new Vector2f(0,0)));
        dottedLine.add(new LabelWord(" lines hidden)", new Vector2f(0,0)));
        temp.add(new FormattedTextLine(dottedLine));
        temp.add(formattedTextLines.get(formattedTextLines.size()-1));
        return temp;
    }

    public boolean isReturns() {
        return returns;
    }

    public void setReturns(boolean ret){
        this.returns = ret;
    }

    public void setMinimized(boolean minimized){
        this.minimized = minimized;
    }
}
