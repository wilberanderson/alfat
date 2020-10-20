package parser;

import java.util.ArrayList;

public class FlowChartObject {
    public String codeBlock;
    public ArrayList<FlowChartObject> connections = new ArrayList<FlowChartObject>();
}
