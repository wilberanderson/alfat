package parser;

import java.util.Iterator;

public class TLineIterator<LineType> implements Iterator<LineType> {
    TLineNode<LineType> current;

    public TLineIterator(TLinesManager<LineType> list) {
        this.current = list.HEAD; //Should be get head
    }

    @Override
    public boolean hasNext() {
        return current != null;
    }

    @Override
    public LineType next() {
        LineType data = current.in;
        current = current.next;
        return data;
    }

    @Override
    public void remove() {

    }


}