package parser;

import java.util.Iterator;

/*
* A text lines manager, manages text lines through a linked list.
* */
public abstract class TLinesManager<LineType> implements Iterable<LineType> {
    protected TLineNode<LineType> HEAD;
    protected TLineNode<LineType> TAIL;
    protected TLineNode<LineType> CURR;
    protected TLineNode<LineType> TEMP;

    //TODO: add a list of spacer nodes for quicker inserts and edits. Basically it would be just extra references every 25 lines or so.

    public TLinesManager() {
        this.HEAD = null;
        this.TAIL = null;
        this.CURR = null;
        this.TEMP = null;
    }

    public void addLine(LineType newLine) {
        TLineNode<LineType> newNode = new TLineNode<LineType>(newLine);
        if(HEAD == null) {
            HEAD = newNode;
            TAIL = newNode;
        } else {
            TAIL.next = newNode;
            TAIL = newNode;
        }
    }

    public abstract void editLine(LineType newLine);

    //Inserts a new line then increments line number count afterwords
    public abstract void insertLine(LineType newLine);


    @Override
    public Iterator<LineType> iterator() {
        return new TLineIterator<LineType>(this);
    }

}
