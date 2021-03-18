package controllers.flowchartWindow;

import controllers.TextLineController;
import dataStructures.RawModel;
import gui.FlowchartLine;
import gui.Mouse;
import gui.terminators.ArrowHead;
import gui.terminators.Junction;
import gui.textBoxes.FlowchartTextBox;
import gui.texts.FormattedTextLine;
import loaders.Loader;
import main.GeneralSettings;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.system.CallbackI;
import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector2f;
import utils.Printer;

import java.nio.FloatBuffer;
import java.util.List;

public class FlowchartWindowController {

    FlowchartWindow flowchartWindow;
    boolean verbose = false;
    private FlowchartTextBoxController flowchartTextBoxController;
    public RawModel lines;
    public int numberOfSegments;
    public int numberOfArrowHeads;
    public int numberOfJunctions;
    private static final float[] VERTICES = {
            0, 0,
            0, -1
    };
    Vector2f mousePosition;
    
    public FlowchartWindowController(TextLineController textLineController){
        flowchartWindow = new FlowchartWindow();
        flowchartTextBoxController = new FlowchartTextBoxController(textLineController,this);
        lines = Loader.loadToVAO(VERTICES, 2);
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
//        flowchartWindow.getZoomTranslateMatrix().m20 = flowchartWindow.getZoomTranslateMatrix().m20 * flowchartWindow.getZoom() / oldZoom;
//        flowchartWindow.getZoomTranslateMatrix().m21 = flowchartWindow.getZoomTranslateMatrix().m21 * flowchartWindow.getZoom() / oldZoom;

        Printer.print(oldZoom);
//        flowchartWindow.getZoomTranslateMatrix().m20 = flowchartWindow.getZoomTranslateMatrix().m20 + (mousePosition.x*oldZoom - (mousePosition.x * flowchartWindow.getZoom()));
//        flowchartWindow.getZoomTranslateMatrix().m20 = flowchartWindow.getZoomTranslateMatrix().m20 - (mousePosition.x / oldZoom);
//        flowchartWindow.getZoomTranslateMatrix().m20 = flowchartWindow.getZoomTranslateMatrix().m20 + mousePosition.x
//        flowchartWindow.getZoomTranslateMatrix().m20 = flowchartWindow.getZoomTranslateMatrix().m20 * flowchartWindow.getZoom() / flowchartWindow.getZoom();
//        flowchartWindow.getZoomTranslateMatrix().m20 = flowchartWindow.getZoomTranslateMatrix().m20 - (mousePosition.x / oldZoom);

//        flowchartWindow.getZoomTranslateMatrix().m21 = flowchartWindow.getZoomTranslateMatrix().m21 - (mousePosition.y / oldZoom);
//        flowchartWindow.getZoomTranslateMatrix().m21 = flowchartWindow.getZoomTranslateMatrix().m21 * flowchartWindow.getZoom() / oldZoom;
//        flowchartWindow.getZoomTranslateMatrix().m21 = flowchartWindow.getZoomTranslateMatrix().m21 - (mousePosition.y / flowchartWindow.getZoom());
//        Printer.print(mousePosition);
//        Printer.print(getZoomTranslateMatrix());
//        Vector2f relativeMousePosition = new Vector2f();
//        relativeMousePosition.x = (mousePosition.x+1) * oldZoom;
//        relativeMousePosition.y = (mousePosition.y+1) * oldZoom;
//        Printer.print(relativeMousePosition);
//        Vector2f absoluteMousePosition = new Vector2f();
//        absoluteMousePosition.x = relativeMousePosition.x - flowchartWindow.getZoomTranslateMatrix().m20;// + relativeMousePosition.x;
//        absoluteMousePosition.y = relativeMousePosition.y - flowchartWindow.getZoomTranslateMatrix().m21;// + relativeMousePosition.y;
        Vector2f absoluteMousePosition = new Vector2f(((mousePosition.x/flowchartWindow.getAspectRatio().m00 - flowchartWindow.getZoomTranslateMatrix().m20)/flowchartWindow.getZoomTranslateMatrix().m00), (mousePosition.y/flowchartWindow.getAspectRatio().m11 - flowchartWindow.getZoomTranslateMatrix().m21)/flowchartWindow.getZoomTranslateMatrix().m11);
        Printer.print(absoluteMousePosition);
//        flowchartWindow.getZoomTranslateMatrix().m20 = absoluteMousePosition.x - relativeMousePosition.x / oldZoom * flowchartWindow.getZoom();
//        flowchartWindow.getZoomTranslateMatrix().m21 = absoluteMousePosition.y - relativeMousePosition.y / oldZoom * flowchartWindow.getZoom();
        Printer.print(flowchartWindow.getZoomTranslateMatrix());
//        flowchartWindow.getZoomTranslateMatrix().m21 = flowchartWindow.getZoomTranslateMatrix().m21 + (mousePosition.y*oldZoom - (mousePosition.y * flowchartWindow.getZoom()));

//        flowchartWindow.getZoomTranslateMatrix().m20 = flowchartWindow.getZoomTranslateMatrix().m20 * flowchartWindow.getZoom() / oldZoom;
//        flowchartWindow.getZoomTranslateMatrix().m20 = (flowchartWindow.getZoomTranslateMatrix().m20 - mousePosition.x);
//        flowchartWindow.getZoomTranslateMatrix().m21 = (flowchartWindow.getZoomTranslateMatrix().m21 - mousePosition.y);
//        flowchartWindow.getZoomTranslateMatrix().m21 = flowchartWindow.getZoomTranslateMatrix().m21 * flowchartWindow.getZoom() / oldZoom;
//        flowchartWindow.getZoomTranslateMatrix().m20 = (flowchartWindow.getZoomTranslateMatrix().m20 + mousePosition.x/flowchartWindow.getZoom());
//        flowchartWindow.getZoomTranslateMatrix().m21 = (flowchartWindow.getZoomTranslateMatrix().m21 + mousePosition.y/flowchartWindow.getZoom());
        unloadFlowchartBoxes();
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

        unloadFlowchartBoxes();
    }

    /**
     * Sets the translation being applied to the flowchart to a specific value
     * @param translation
     */
    public void setTranslation(Vector2f translation){
        flowchartWindow.getZoomTranslateMatrix().m20 = translation.x /flowchartWindow.getZoom();
        flowchartWindow.getZoomTranslateMatrix().m21 = translation.y / flowchartWindow.getZoom();
        flowchartWindow.setTranslation(translation);

        unloadFlowchartBoxes();
    }

    public void updateAspectRatio(Matrix2f aspectRatio){
        flowchartWindow.setAspectRatio(aspectRatio);

        unloadFlowchartBoxes();
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
        Junction temp = new Junction(new Vector2f(0,0));
        ArrowHead temp2 = new ArrowHead(new Vector2f(0,0),0);
        flowchartWindow.getFlowchartLineList().addAll(lines);
        numberOfSegments = populateLineVbo(lines, this.lines.getVaoID());
        populateArrowHeadVbo(lines, ArrowHead.getModel().getVaoID());
        populateJunctionVbo(lines, Junction.getModel().getVaoID());
    }

    public int populateLineVbo(List<FlowchartLine> lines, int vao){
        int instanceCount = 0;
        for(FlowchartLine line : lines){
            int i = line.getPositions().size();
            if(i > 1){
                instanceCount += i-1;
            }else{
                System.err.println("Line added without a full line segment");
            }
        }
        int vbo = Loader.createEmptyVbo(instanceCount*GeneralSettings.TEXT_LINE_INSTANCED_DATA_LENGTH, GL15.GL_STATIC_DRAW);
        Loader.addInstanceAttribute(vao, vbo, 1, 4, GeneralSettings.TEXT_LINE_INSTANCED_DATA_LENGTH, 0);
        Loader.addInstanceAttribute(vao, vbo, 2, 3, GeneralSettings.TEXT_LINE_INSTANCED_DATA_LENGTH, 4);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(instanceCount*GeneralSettings.TEXT_LINE_INSTANCED_DATA_LENGTH);
        float data[] = new float[instanceCount*GeneralSettings.TEXT_LINE_INSTANCED_DATA_LENGTH];
        int i = 0;
        for(FlowchartLine line : lines){
            for(int j = 0; j < line.getPositions().size()-1;){
                data[i] = line.getPositions().get(j).x;
                i++;
                data[i] = line.getPositions().get(j).y;
                i++;
                j++;
                data[i] = line.getPositions().get(j).x;
                i++;
                data[i] = line.getPositions().get(j).y;
                i++;
                data[i] = line.getColor().x;
                i++;
                data[i] = line.getColor().y;
                i++;
                data[i] = line.getColor().z;
                i++;
            }
        }
        Loader.updateVbo(vbo, data, buffer);
        return instanceCount;
    }

    public void populateArrowHeadVbo(List<FlowchartLine> lines, int vao){
        numberOfArrowHeads = 0;
        for(FlowchartLine line : lines){
            if(line.getTerminator() instanceof ArrowHead){
                numberOfArrowHeads++;
            }
        }
        int vbo = Loader.createEmptyVbo(numberOfArrowHeads*GeneralSettings.ARROW_HEAD_INSTANCED_DATA_LENGTH, GL15.GL_STATIC_DRAW);
        Loader.addInstanceAttribute(vao, vbo, 1, 2, GeneralSettings.ARROW_HEAD_INSTANCED_DATA_LENGTH, 0);
        Loader.addInstanceAttribute(vao, vbo, 2, 3, GeneralSettings.ARROW_HEAD_INSTANCED_DATA_LENGTH, 2);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(numberOfArrowHeads*GeneralSettings.ARROW_HEAD_INSTANCED_DATA_LENGTH);
        float data[] = new float[numberOfArrowHeads*GeneralSettings.ARROW_HEAD_INSTANCED_DATA_LENGTH];
        int i = 0;
        for(FlowchartLine line : lines){
            if(line.getTerminator() instanceof ArrowHead) {
                data[i] = line.getTerminator().getPosition().x;
                i++;
                data[i] = line.getTerminator().getPosition().y;
                i++;
                data[i] = line.getColor().x;
                i++;
                data[i] = line.getColor().y;
                i++;
                data[i] = line.getColor().z;
                i++;
            }
        }
        Loader.updateVbo(vbo, data, buffer);
    }

    public void populateJunctionVbo(List<FlowchartLine> lines, int vao){
        numberOfJunctions = 0;
        for(FlowchartLine line : lines){
            if(line.getTerminator() instanceof Junction){
                numberOfJunctions++;
            }
        }
        int vbo = Loader.createEmptyVbo(numberOfJunctions*GeneralSettings.JUNCTION_INSTANCED_DATA_LENGTH, GL15.GL_STATIC_DRAW);
        Loader.addInstanceAttribute(vao, vbo, 1, 2, GeneralSettings.JUNCTION_INSTANCED_DATA_LENGTH, 0);
        Loader.addInstanceAttribute(vao, vbo, 2, 3, GeneralSettings.JUNCTION_INSTANCED_DATA_LENGTH, 2);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(numberOfJunctions*GeneralSettings.JUNCTION_INSTANCED_DATA_LENGTH);
        float data[] = new float[numberOfJunctions*GeneralSettings.JUNCTION_INSTANCED_DATA_LENGTH];
        int i = 0;
        for(FlowchartLine line : lines){
            if(line.getTerminator() instanceof Junction) {
                data[i] = line.getTerminator().getPosition().x;
                i++;
                data[i] = line.getTerminator().getPosition().y;
                i++;
                data[i] = line.getColor().x;
                i++;
                data[i] = line.getColor().y;
                i++;
                data[i] = line.getColor().z;
                i++;
            }
        }
        Loader.updateVbo(vbo, data, buffer);
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
        mousePosition = new Vector2f((float)xPos, (float)yPos);
    }

    public void unloadFlowchartBoxes(){
        for(FlowchartTextBox textBox : flowchartTextBoxController.getTextBoxes()){
            if(((textBox.getPosition().y)*getZoomTranslateMatrix().m11 + getZoomTranslateMatrix().m21)* getAspectRatio().m11 > 1f || ((textBox.getPosition().y + textBox.getSize().y)*getZoomTranslateMatrix().m11 + getZoomTranslateMatrix().m21)* getAspectRatio().m11 < -1f){
                flowchartTextBoxController.unload(textBox);
            }else{
                flowchartTextBoxController.load(textBox);
            }
        }
    }
}
