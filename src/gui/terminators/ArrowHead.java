package gui.terminators;

import dataStructures.RawModel;
import loaders.Loader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public class ArrowHead extends Terminator{

    private boolean upwards;
    private static RawModel model;
    private static final int renderMode = GL11.GL_TRIANGLES;
    private static final float[] VERTICES = {
            -0.01f, -0.02f,
            0.01f, -0.02f,
            0f, 0f
    };

    public ArrowHead(Vector2f position, boolean upwards){
        super(position);
        this.upwards = upwards;
        if(model == null){
            model = Loader.loadToVAO(VERTICES, 2);
        }
    }

    public boolean isUpwards() {
        return upwards;
    }

    @Override
    public RawModel getModel() {
        return model;
    }
}
