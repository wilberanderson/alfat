package dataStructures;

import org.lwjgl.util.vector.Vector3f;
/**NOTE: This may be deprecated */
public class Color3f extends Vector3f {
    public Color3f(Color3f color){
        super(color);
    }

    public Color3f(float r, float g, float b){
        super(r, g, b);
    }

    public Color3f(){

    }

    /**
     * Undefined behavior if r g or b are outside the range of 0x00-0xFF(0-255)
     */
    public Color3f(int r, int g, int b){
        super((float)r/255f, (float)g/255f, (float)b/255f);
    }
}
