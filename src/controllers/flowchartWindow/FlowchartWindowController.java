package controllers.flowchartWindow;

import controllers.TextLineController;
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
    private FlowchartTextBoxController flowchartTextBoxController;
    
    public FlowchartWindowController(TextLineController textLineController){
        flowchartWindow = new FlowchartWindow();
        flowchartTextBoxController = new FlowchartTextBoxController(textLineController);
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
        flowchartWindow.setAspectRatio(aspectRatio);
    }
    
    public void clear(){
//        for(FlowchartTextBox textBox: flowchartWindow.getFlowchartTextBoxList()){
//            textBox.clear();
//        }
//        flowchartWindow.getFlowchartTextBoxList().clear();
        flowchartWindow.getFlowchartLineList().clear();
        flowchartTextBoxController.clear();
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


    public void locateRegisters(String args) {
        flowchartTextBoxController.locateRegisters(args);
    }

    public void clearHighlighting(){
        flowchartTextBoxController.clearHighlighting();
    }

    public void locateAlert(String alert){
        flowchartTextBoxController.locateAlert(alert);
    }

    public void setFlowchartLineList(List<FlowchartLine> lines){
        flowchartWindow.getFlowchartLineList().addAll(lines);
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

    public float getZoom(){
        return flowchartWindow.getZoom();
    }

    public void setZoom(float zoom){
        flowchartWindow.setZoom(zoom);
    }

    public FlowchartTextBoxController getFlowchartTextBoxController() {
        return flowchartTextBoxController;
    }

    public void click(int key, int action){
        flowchartTextBoxController.click(key, action);
    }

    public void moveMouse(double xPos, double yPos){
        flowchartTextBoxController.moveMouse((xPos - flowchartWindow.getZoomTranslateMatrix().m20) / flowchartWindow.getZoom(), (yPos - flowchartWindow.getZoomTranslateMatrix().m21) / flowchartWindow.getZoom());
    }
}
