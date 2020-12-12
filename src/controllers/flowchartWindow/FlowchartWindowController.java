package controllers.flowchartWindow;

import gui.FlowchartLine;
import gui.textBoxes.FlowchartTextBox;
import main.GeneralSettings;
import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;

public class FlowchartWindowController {

    FlowchartWindow flowchartWindow;
    boolean verbose = false;
    
    public FlowchartWindowController(){
        flowchartWindow = new FlowchartWindow();   
    }
    
    public void updateZoom(float scrollChange){
        float oldZoom = flowchartWindow.getZoom();
        flowchartWindow.setZoom(flowchartWindow.getZoom()+scrollChange);
        if (flowchartWindow.getZoom() < GeneralSettings.MIN_ZOOM) {
            flowchartWindow.setZoom(GeneralSettings.MIN_ZOOM);
        }
        flowchartWindow.getZoomTranslateMatrix().m00 = flowchartWindow.getZoom();
        flowchartWindow.getZoomTranslateMatrix().m11 = flowchartWindow.getZoom();


        flowchartWindow.getZoomTranslateMatrix().m20 = flowchartWindow.getZoomTranslateMatrix().m20 * flowchartWindow.getZoom() / oldZoom;
        flowchartWindow.getZoomTranslateMatrix().m21 = flowchartWindow.getZoomTranslateMatrix().m21 * flowchartWindow.getZoom() / oldZoom;
        //Logic for a zoom focus point of 0, 1, does not work currently
//        if(scaleChange > 0){
//            flowchartWindow.getZoomTranslateMatrix().m21 = flowchartWindow.getZoomTranslateMatrix().m21 - 1*oldZoom + 1/zoom;
//        }
    }

    public void updateTranslation(Vector2f translation){
        flowchartWindow.getZoomTranslateMatrix().m20 += translation.x/flowchartWindow.getZoom();
        flowchartWindow.getZoomTranslateMatrix().m21 += translation.y/flowchartWindow.getZoom();
    }
    
    public void setTranslation(Vector2f translation){
        flowchartWindow.getZoomTranslateMatrix().m20 = translation.x /flowchartWindow.getZoom();
        flowchartWindow.getZoomTranslateMatrix().m21 = translation.y / flowchartWindow.getZoom();
        flowchartWindow.setTranslation(translation);
    }
    
    public void updateAspectRatio(Matrix2f aspectRatio){
        
    }
    
    public void clear(){
        for(FlowchartTextBox textBox: flowchartWindow.getFlowchartTextBoxList()){
            textBox.clear();
        }
        flowchartWindow.getFlowchartTextBoxList().clear();
        flowchartWindow.getFlowchartLineList().clear();
    }

    public void maximize(){
        flowchartWindow.getPosition().setX(-1);
        flowchartWindow.getSize().setX(2);
    }

    public void goSplitScreen(){
        flowchartWindow.getPosition().setX(0);
        flowchartWindow.getSize().setX(1);
    }

    public void minimize(){
        flowchartWindow.getPosition().setX(0);
        flowchartWindow.getSize().setX(0);
    }


    public void locateRegister(String register) {
        for (FlowchartTextBox box : flowchartWindow.getFlowchartTextBoxList()) {
            if (verbose) System.out.println("Checking box " + box + " for register " + register);
            if (verbose) System.out.println("Contains registers: " + box.getRegisters());
            if (register != null && box.getRegisters().contains(register)) {
                if (verbose) System.out.println("Match found");
                box.setBackgroundColor(GeneralSettings.TEXT_COLOR);
                box.setTextColor(GeneralSettings.TEXT_BOX_BACKGROUND_COLOR);
            } else {
                box.setBackgroundColor(GeneralSettings.TEXT_BOX_BACKGROUND_COLOR);
                box.setTextColor(GeneralSettings.TEXT_COLOR);
            }
        }
    }

    public void locateAlert(String alert){
        for (FlowchartTextBox box : flowchartWindow.getFlowchartTextBoxList()){
            if (verbose) System.out.println("Checking box " + box + " for alert " + alert);
            if (verbose) System.out.println("Contains registers: " + box.getRegisters());
            if (alert != null && box.getAlert().equals(alert)){
                if (verbose) System.out.println("Match found");
                box.setBackgroundColor(GeneralSettings.TEXT_COLOR);
                box.setTextColor(GeneralSettings.TEXT_BOX_BACKGROUND_COLOR);
            } else {
                box.setBackgroundColor(GeneralSettings.TEXT_BOX_BACKGROUND_COLOR);
                box.setTextColor(GeneralSettings.TEXT_COLOR);
            }
        }
    }


    public void setFlowChartTextBoxList(List<FlowchartTextBox> textBoxes){
        flowchartWindow.getFlowchartTextBoxList().addAll(textBoxes);
    }

    public void setFlowchartLineList(List<FlowchartLine> lines){
        flowchartWindow.getFlowchartLineList().addAll(lines);
    }

    public List<FlowchartTextBox> getFlowchartTextBoxList(){
        return flowchartWindow.getFlowchartTextBoxList();
    }

    public List<FlowchartLine> getFlowchartLineList(){
        return flowchartWindow.getFlowchartLineList();
    }

    public Matrix3f getZoomTranslateMatrix(){
        return flowchartWindow.getZoomTranslateMatrix();
    }

    public Matrix2f getAspectRatio(){
        return flowchartWindow.getAspectRatio();
    }

    public Vector2f getPosition(){
        return flowchartWindow.getPosition();
    }

    public Vector2f getSize(){
        return flowchartWindow.getSize();
    }

    public void resetZoom(){
        flowchartWindow.setTranslation(new Vector2f(0, 0.9f));
        flowchartWindow.setZoom(1f);
    }
}
