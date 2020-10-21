package parser;

import java.util.ArrayList;

public class TLine {
    //every line has these:
    public String lineText;
    public int lineNumber;

    //Each line usually has a combination of these:
    public String command = null;
    public String label = null;
    public ArrayList<Integer> registers = new ArrayList<>();

    //general constructor:
    public void parse(String inputText,int lineNumber){
        this.lineNumber = lineNumber;
        //call lc3 parser
    }
}
