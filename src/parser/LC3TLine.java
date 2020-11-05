package parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
* A LC3TLine stands for LC-3 Text Line.
* It contains the line number, full text of a single line and..
* TODO: define the rest of the stuff it should contain...
*
* */

public class LC3TLine extends TLine {
    private String lineText;
    private int lineNumber;
    private String command;
    private String label;
    private String target;
    private List<String> registers = new ArrayList<>();
    private boolean jump;

    //general constructor:
    LC3TLine(){
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
    public LC3TLine(String fullText, Optional<String> comm, String label, String targetLabel, boolean jump, List<String> registers, int line) {
        comm.ifPresent(s -> this.command = s);
        if (!label.isEmpty())       this.label = label;
        if (!targetLabel.isEmpty()) this.target = targetLabel;
        if (!registers.isEmpty())   this.registers.addAll(registers);
        this.jump = jump;
        this.lineNumber = line;
        this.lineText = fullText;
    }

    @Override
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

}
