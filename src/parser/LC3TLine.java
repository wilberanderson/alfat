package parser;

import java.util.ArrayList;

/*
* A LC3TLine stands for LC-3 Text Line.
* It contains the line number, full text of a single line and..
* TODO: define the rest of the stuff it should contain...
*
* */

public class LC3TLine extends TLine {

    //Each line usually has a combination of these:
    public String command = null;
    public String label = null;
    public ArrayList<Integer> registers = new ArrayList<>();

    //general constructor:
    LC3TLine(int lineNumber,String lineText){
        super(lineNumber, lineText);
        //call lc3 parser
    }

    @Override
    //TODO: Change if want this to be something different. Remove comment and return super otherwise...
    public String toString() {
        return super.toString();
    }

}
