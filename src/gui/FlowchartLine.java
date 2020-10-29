package gui;

import org.lwjgl.util.vector.Vector2f;

import java.util.List;

public class FlowchartLine {
    private List<Vector2f> positions;

    public FlowchartLine(List<Vector2f> positions){
        this.positions = positions;
    }

    public List<Vector2f> getPositions() {
        return positions;
    }
}
