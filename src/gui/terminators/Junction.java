package gui.terminators;

import dataStructures.RawModel;
import loaders.Loader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public class Junction extends Terminator{

    private static RawModel model;
    private static final int renderMode = GL11.GL_TRIANGLES;
    private static final float[] VERTICES = {
            0f, 0f
    };

    public Junction(Vector2f position){
        super(position);
        if(model == null){
            model = Loader.loadToVAO(VERTICES, 2);
        }
    }

    public static RawModel getModel() {
        return model;
    }
}
