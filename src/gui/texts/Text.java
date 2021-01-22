package gui.texts;

import gui.fontMeshCreator.FontType;
import gui.fontMeshCreator.TextMeshData;
import org.lwjgl.util.vector.Vector2f;
import rendering.text.TextMaster;

public abstract class Text {
    private float fontSize;

    private int textMeshVao;
    private int vertexCount;

    private Vector2f position;

    private double length;

    private TextMeshData textMeshData;


    /**
     * Creates a new text, loads the text's quads into a VAO, and adds the text
     * to the screen.
     *
     * @param text
     *            - the text.
     * @param fontSize
     *            - the font size of the text, where a font size of 1 is the
     *            default size.

     * @param position
     *            - the position on the screen where the top left corner of the
     *            text should be rendered. The top left corner of the screen is
     *            (0, 0) and the bottom right is (1, 1).
     */
    public Text(String text, float fontSize, Vector2f position) {
        this.fontSize = fontSize;
        this.position = position;
        this.position.x = (position.x+1)/2;
        this.position.y = -(position.y - 1)/2;
        if(this instanceof GUIText){
            this.textMeshData = TextMaster.loadGuiText((GUIText) this, text);
        }else{
            this.textMeshData = TextMaster.loadText(this, text);
        }
        this.position.x = this.position.x*2-1;
        this.position.y = -this.position.y*2+1;
    }

    public Text(String textString, Text text, boolean deleteText) {
        this.fontSize = text.fontSize;
        this.position = new Vector2f(text.position);
        position.x = (position.x+1)/2;
        position.y = -(position.y - 1)/2;
        if(this instanceof GUIText){
            this.textMeshData = TextMaster.loadGuiText((GUIText) this, textString);
        }else{
            this.textMeshData = TextMaster.loadText(this, textString);
        }
        if(deleteText){
            if(this instanceof GUIText){
                TextMaster.removeGuiText((GUIText) text);
            }else {
                TextMaster.removeText(text);
            }
        }
        position.x = this.position.x*2-1;
        position.y = -this.position.y*2+1;
    }


    /**
     * Remove the text from the screen.
     */
    public void remove(Text text) {
        if(this instanceof GUIText){
            TextMaster.removeGuiText((GUIText) text);
        }else {
            TextMaster.removeText(text);
        }
    }

    /**
     * @return The position of the top-left corner of the text in screen-space.
     *         (0, 0) is the top left corner of the screen, (1, 1) is the bottom
     *         right.
     */
    public Vector2f getPosition() {
        return position;
    }

    /**
     * @return the ID of the text's VAO, which contains all the vertex data for
     *         the quads on which the text will be rendered.
     */
    public int getMesh() {
        return textMeshVao;
    }

    /**
     * Set the VAO and vertex count for this text.
     *
     * @param vao
     *            - the VAO containing all the vertex data for the quads on
     *            which the text will be rendered.
     * @param verticesCount
     *            - the total number of vertices in all of the quads.
     */
    public void setMeshInfo(int vao, int verticesCount) {
        this.textMeshVao = vao;
        this.vertexCount = verticesCount;
    }

    /**
     * @return The total number of vertices of all the text's quads.
     */
    public int getVertexCount() {
        return this.vertexCount;
    }

    /**
     * @return the font size of the text (a font size of 1 is normal).
     */
    public float getFontSize() {
        return fontSize;
    }

    public void setLength(double length){
        this.length = length;
    }

    public double getLength(){
        return length;
    }

    public void changeVerticalPosition(float change){
        this.position.y += change;
    }

    public void changeHorizontalPosition(float change){
        this.position.x += change;
    }

    public float[] getCharacterEdges(){
        return textMeshData.getCharacterEdges();
    }

    public void setPosition(Vector2f position){
        this.position = position;
    }

    public FontType getFont() {
        System.err.println("getFont in Text.java not overridden, override to prevent undefined behavior");
        return null;
    }
}
