package controllers.flowchartWindow;

import gui.FlowchartLine;
import gui.textBoxes.FlowchartTextBox;
import main.GeneralSettings;
import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class FlowchartWindow {
    private Vector2f position = new Vector2f(0, -1);
    private Vector2f size = new Vector2f(1, 2- GeneralSettings.TEXT_BUTTON_PADDING*2 - GeneralSettings.FONT_SIZE*GeneralSettings.FONT_SCALING_FACTOR);

    private static List<FlowchartLine> flowchartLineList = new ArrayList<>();
    private Matrix3f zoomTranslateMatrix = new Matrix3f();
    private Vector2f translation = new Vector2f(0, 0.9f);
    private float zoom = 1;
    private Matrix2f aspectRatio = new Matrix2f();
    private boolean isZoomable;

    FlowchartWindow(){
        zoomTranslateMatrix.setIdentity();
        zoomTranslateMatrix.m20 = translation.x;
        zoomTranslateMatrix.m21 = translation.y;
        aspectRatio = GeneralSettings.ASPECT_RATIO;
    }

    List<FlowchartLine> getFlowchartLineList(){
        return flowchartLineList;
    }
    void setFlowchartLineList(List<FlowchartLine> lines){
        flowchartLineList.addAll(lines);
    }

    Vector2f getPosition() {
        return position;
    }
    void setPosition(Vector2f position) {
        this.position = position;
    }

    Vector2f getSize() {
        return size;
    }
    void setSize(Vector2f size){
        this.size = size;
    }

    Matrix3f getZoomTranslateMatrix(){
        return zoomTranslateMatrix;
    }
    void setZoomTranslateMatrix(Matrix3f zoomTranslateMatrix) {
        this.zoomTranslateMatrix = zoomTranslateMatrix;
    }

    Matrix2f getAspectRatio() {
        return aspectRatio;
    }
    void setAspectRatio(Matrix2f aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    float getZoom(){
        return zoom;
    }
    void setZoom(float zoom){
        zoomTranslateMatrix.m00 /= this.zoom;
        zoomTranslateMatrix.m11 /= this.zoom;
        zoomTranslateMatrix.m00 *= zoom;
        zoomTranslateMatrix.m11 *= zoom;
        this.zoom = zoom;
    }

    void setTranslation(Vector2f translation){
        zoomTranslateMatrix.m20 = translation.x;
        zoomTranslateMatrix.m21 = translation.y;
    }
}
