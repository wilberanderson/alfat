package gui;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

public class FlowchartLine {
    private List<Vector2f> positions;
    private Vector3f color = new Vector3f(1,1,1);

    public FlowchartLine(List<Vector2f> positions){
        this.positions = positions;
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
}
