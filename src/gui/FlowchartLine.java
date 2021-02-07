package gui;

import gui.terminators.Terminator;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

public class FlowchartLine {
    private List<Vector2f> positions;
    private Vector3f color = new Vector3f(1,1,1);
    private Terminator terminator;
    private boolean highlight = false;

    public FlowchartLine(List<Vector2f> positions, Terminator terminator){
        this.positions = positions;
        this.terminator = terminator;
    }

    public List<Vector2f> getPositions() {
        return positions;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public Vector3f getColor() {
        return color;
    }

    public Terminator getTerminator() {
        return terminator;
    }

    public void setTerminator(Terminator terminator) {
        this.terminator = terminator;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    public boolean isHighlighted() {
        return highlight;
    }
}
