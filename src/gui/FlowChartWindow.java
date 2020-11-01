package gui;

import main.GeneralSettings;
import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;

public class FlowChartWindow {
    private Vector2f position = new Vector2f(0, -1);
    private Vector2f size = new Vector2f(1, 2- GeneralSettings.TEXT_BUTTON_PADDING*2 - GeneralSettings.FONT_SIZE*GeneralSettings.FONT_SCALING_FACTOR);

    private List<TextBox> textBoxList;
    private List<FlowchartLine> flowchartLineList;

    public FlowChartWindow(List<TextBox> textBoxList, List<FlowchartLine> flowchartLineList){
        this.textBoxList = textBoxList;
        this.flowchartLineList = flowchartLineList;
    }


    public void maximize(){
        position.x = -1f;
        size.x = 2f;
    }

    public void goSplitScreen(){
        position.x = 0f;
        size.x = 1f;
    }

    public void minimize(){
        position.x = 0f;
        size.x = 0f;
    }

    public List<TextBox> getTextBoxList() {
        return textBoxList;
    }

    public List<FlowchartLine> getFlowchartLineList(){
        return flowchartLineList;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getSize() {
        return size;
    }
}
