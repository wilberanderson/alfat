package parser;

public class LC3LinesManager extends TLinesManager<LC3TLine>  {


    //TODO: add a list of spacer nodes for quicker inserts and edits. Basically it would be just extra references every 25 lines or so.



    @Override
    public void addLine(LC3TLine newLine) {
        TLineNode<LC3TLine> newNode = new TLineNode<LC3TLine>(newLine);
        if(HEAD == null) {
            HEAD = newNode;
            TAIL = newNode;
        } else {
            TAIL.next = newNode;
            TAIL = newNode;
        }
    }


    @Override
    public void editLine(LC3TLine newLine) {
        CURR = HEAD;
        //Find and replace line in node.
        while(CURR != null) {
            if(CURR.in.getLineNumber() == newLine.getLineNumber()) {
                CURR.in = null;
                CURR.in = newLine;
                return;
            }
            CURR = CURR.next;
        }
        //Must be at end
        addLine(newLine);
    }

    @Override
    public void insertLine(LC3TLine newLine) {
        TLineNode<LC3TLine> newNode = new TLineNode<LC3TLine>(newLine);

        CURR = HEAD;

        //Check if in head
        if(CURR.in.getLineNumber() == newNode.in.getLineNumber()) {
            newNode.next = CURR;
            HEAD = newNode;
            inccLineNumber(CURR);
            return;
        }

        //If in middle
        while(CURR.next != null) {
            if(CURR.next.in.getLineNumber() == newNode.in.getLineNumber()) {
                TEMP = CURR.next;
                CURR.next = newNode;
                newNode.next = TEMP;
                inccLineNumber(TEMP);
                return;
            }
            CURR = CURR.next;
        }

        //Must be at end
        addLine(newLine);
    }

    //Prints out the current text lines to console
    public void print() {
        TEMP = HEAD;
        while(TEMP != null) {
            System.out.println(TEMP.in);
            TEMP = TEMP.next;
        }
    }

    //Increments line number count at a starting node until null is reached
    private void inccLineNumber(TLineNode<LC3TLine> start) {
        while(start != null) {
            start.in.setLineNumber(start.in.getLineNumber()+1);
            start = start.next;
        }
    }
}
