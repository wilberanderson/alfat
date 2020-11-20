package gui;

import gui.textBoxes.FlowChartTextBox;
import main.GeneralSettings;
import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class FlowChartWindow {
    private Vector2f position = new Vector2f(0, -1);
    private Vector2f size = new Vector2f(1, 2- GeneralSettings.TEXT_BUTTON_PADDING*2 - GeneralSettings.FONT_SIZE*GeneralSettings.FONT_SCALING_FACTOR);

    private static List<FlowChartTextBox> flowChartTextBoxList = new ArrayList<>();
    private static List<FlowchartLine> flowchartLineList = new ArrayList<>();
    private Matrix3f zoomTranslateMatrix = new Matrix3f();
    private Vector2f translation = new Vector2f(0, 0.9f);
    private float zoom = 1;
    private Matrix2f aspectRatio = new Matrix2f();
    private boolean isZoomable;

    public FlowChartWindow(){
        zoomTranslateMatrix.setIdentity();
        zoomTranslateMatrix.m20 = translation.x;
        zoomTranslateMatrix.m21 = translation.y;
        aspectRatio.setIdentity();
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
        setZoomable(false);
    }

    public static List<FlowChartTextBox> getFlowChartTextBoxList() {
        return flowChartTextBoxList;
    }

    public static List<FlowchartLine> getFlowchartLineList(){
        return flowchartLineList;
    }


    public static void setFlowChartTextBoxList(List<FlowChartTextBox> textBoxes){
        flowChartTextBoxList.clear();
        flowChartTextBoxList.addAll(textBoxes);
    }

    public static void setFlowchartLineList(List<FlowchartLine> lines){
        flowchartLineList.clear();
        flowchartLineList.addAll(lines);
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getSize() {
        return size;
    }

    public Matrix3f getZoomTranslateMatrix(){
        return zoomTranslateMatrix;
    }

    public void updateTranslation(Vector2f translationChange){
        zoomTranslateMatrix.m20 += translationChange.x/zoom;
        zoomTranslateMatrix.m21 += translationChange.y/zoom;

    }

    public void updateZoom(float scaleChange){
        if(isZoomable) {
            float oldZoom = zoom;
            zoom += scaleChange;
            if (zoom < GeneralSettings.MIN_ZOOM) {
                zoom = GeneralSettings.MIN_ZOOM;
            }
            zoomTranslateMatrix.m00 = zoom;
            zoomTranslateMatrix.m11 = zoom;


            zoomTranslateMatrix.m20 = zoomTranslateMatrix.m20 * zoom / oldZoom;
            zoomTranslateMatrix.m21 = zoomTranslateMatrix.m21 * zoom / oldZoom;
            //Logic for a zoom focus point of 0, 1, does not work currently
//        if(scaleChange > 0){
//            zoomTranslateMatrix.m21 = zoomTranslateMatrix.m21 - 1*oldZoom + 1/zoom;
//        }
        }
    }

    public Matrix2f getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(Matrix2f aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public float getZoom(){
        return zoom;
    }

    public void setZoomable(boolean zoomable){
        this.isZoomable = zoomable;
        System.out.println("Zoomable is now " + zoomable);
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public void setZoom(float zoom){
        updateZoom(zoom - this.zoom);
    }

    public void setPanning(Vector2f panning){
        this.translation = panning;
        zoomTranslateMatrix.m20 = translation.x /zoom;
        zoomTranslateMatrix.m21 = translation.y / zoom;
    }
}
