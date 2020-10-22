package parser;
/*
* A text line node is a generic node object
* which contains a single LineType "in" it.
* */
public class TLineNode<LineType> {
    public TLineNode<LineType> next;
    public LineType in;
    public TLineNode() {
        // TODO Auto-generated constructor stub
        next = null;
    }

    public TLineNode(LineType in) {
        next = null;
        this.in = in;
    }

}
