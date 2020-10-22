package parser;

/*
* A TLine stands for Text line.
* A text line contains the line number, and full text of a single line of code.
* */
public abstract class TLine {
    private String lineText;
    private  int lineNumber;

    TLine() {
        //TODO: Should prob have something in here... ?
    }

    TLine(int lineNumber, String lineText) {
        this.lineNumber = lineNumber;
        this.lineText = lineText;
    }


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


    @Override
    public String toString() {
        return this.lineNumber + " " + this.lineText;
    }
}
