package controllers.flowchartWindow;

import controllers.TextLineController;
import gui.FlowchartLine;
import gui.Mouse;
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
        flowchartTextBoxController = new FlowchartTextBoxController(textLineController,this);
    }

    /**
     * Updates the zoom level of the flowchart
     * @param scrollChange
     */
    public void updateZoom(float scrollChange){
        //Save the old zoom and modify zoom
        float oldZoom = flowchartWindow.getZoom();
        flowchartWindow.setZoom(flowchartWindow.getZoom()+scrollChange);

        //Ensure that zoom does not fall below MIN_ZOOM
        if (flowchartWindow.getZoom() < GeneralSettings.MIN_ZOOM) {
            flowchartWindow.setZoom(GeneralSettings.MIN_ZOOM);
        }

        //Populate the zoom translate matrix with zoom
        flowchartWindow.getZoomTranslateMatrix().m00 = flowchartWindow.getZoom();
        flowchartWindow.getZoomTranslateMatrix().m11 = flowchartWindow.getZoom();

        //Modify the translations saved in the zoom translate matrix by zoom
        flowchartWindow.getZoomTranslateMatrix().m20 = flowchartWindow.getZoomTranslateMatrix().m20 * flowchartWindow.getZoom() / oldZoom;
        flowchartWindow.getZoomTranslateMatrix().m21 = flowchartWindow.getZoomTranslateMatrix().m21 * flowchartWindow.getZoom() / oldZoom;

    }

    /**
     * Moves the boundary between the code window and flowchart window
     * @param change
     */
    public void moveBoundary(float change){
        flowchartWindow.getPosition().x += change;
        flowchartWindow.getSize().x -= change;
    }

    /**
     * Updates the translation being applied to the flowchart based on mouse movements
     * @param translation
     */
    public void updateTranslation(Vector2f translation){
        flowchartWindow.getZoomTranslateMatrix().m20 += translation.x/flowchartWindow.getZoom();
        flowchartWindow.getZoomTranslateMatrix().m21 += translation.y/flowchartWindow.getZoom();
    }

    /**
     * Sets the translation being applied to the flowchart to a specific value
     * @param translation
     */
    public void setTranslation(Vector2f translation){
        flowchartWindow.getZoomTranslateMatrix().m20 = translation.x /flowchartWindow.getZoom();
        flowchartWindow.getZoomTranslateMatrix().m21 = translation.y / flowchartWindow.getZoom();
        flowchartWindow.setTranslation(translation);
    }

    public void updateAspectRatio(Matrix2f aspectRatio){
        flowchartWindow.setAspectRatio(aspectRatio);
    }

    public void clear(){
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
        flowchartTextBoxController.moveMouse(((xPos/flowchartWindow.getAspectRatio().m00 - flowchartWindow.getZoomTranslateMatrix().m20)/flowchartWindow.getZoomTranslateMatrix().m00), (yPos/flowchartWindow.getAspectRatio().m11 - flowchartWindow.getZoomTranslateMatrix().m21)/flowchartWindow.getZoomTranslateMatrix().m11);
        if(xPos > getPosition().x && yPos > getPosition().y && xPos < getPosition().x + getSize().x && yPos < getPosition().y + getSize().y){
            Mouse.setHand();
        }
    }
}
