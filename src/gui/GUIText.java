package gui;

import gui.fontMeshCreator.FontType;
import gui.fontMeshCreator.TextMeshData;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import org.lwjgl.util.vector.Vector4f;
import rendering.text.TextMaster;

/**
 * Represents a piece of text in the game.
 * 
 * @author Karl
 *
 */
public class GUIText {

	public String textString;
	private float fontSize;

	private int textMeshVao;
	private int vertexCount;
	private Vector3f color;

	private Vector2f position;

	private float width;
	private float edge;
	private float borderWidth = 0.0f;
	private float borderEdge;
	private Vector2f offset = new Vector2f(0.0f, 0.0f);
	private Vector3f outlineColor = new Vector3f(0.0f, 0.0f, 0.0f);

	private FontType font;
	private double length;

	private Vector4f positionBounds;

	private TextMeshData textMeshData;

	private boolean isGuiText;

	private boolean isInFlowchart;
	private boolean isCodeWindowText;

	private double spaceSize;

	/**
	 * Creates a new text, loads the text's quads into a VAO, and adds the text
	 * to the screen.
	 * 
	 * @param text
	 *            - the text.
	 * @param fontSize
	 *            - the font size of the text, where a font size of 1 is the
	 *            default size.
	 * @param font
	 *            - the font that this text should use.
	 * @param position
	 *            - the position on the screen where the top left corner of the
	 *            text should be rendered. The top left corner of the screen is
	 *            (0, 0) and the bottom right is (1, 1).
	 */
	public GUIText(String text, float fontSize, FontType font, Vector2f position, float width, float edge, Vector3f color, Vector4f positionBounds, boolean isGuiText, boolean isInFlowchart, boolean isCodeWindowText) {
		this.textString = text;
		this.fontSize = fontSize;
		this.font = font;
		this.position = position;
		this.position.x = (position.x+1)/2;
		this.position.y = -(position.y - 1)/2;
		this.width = width;
		this.edge = edge;
		this.borderEdge = edge;
		this.color = new Vector3f(color);
		this.positionBounds = positionBounds;
		this.isGuiText = isGuiText;
		this.isInFlowchart = isInFlowchart;
		this.isGuiText = isGuiText;
		this.isCodeWindowText = isCodeWindowText;
		if(isGuiText){
			this.textMeshData = TextMaster.loadGuiText(this);
		}else{
			this.textMeshData = TextMaster.loadText(this);
		}
		this.position.x = this.position.x*2-1;
		this.position.y = -this.position.y*2+1;
	}

	public GUIText(String text, GUIText guiText, boolean deleteText) {
		this.textString = text;
		this.fontSize = guiText.fontSize;
		this.font = guiText.font;
		this.position = new Vector2f(guiText.position);
		position.x = (position.x+1)/2;
		position.y = -(position.y - 1)/2;
		this.width = guiText.width;
		this.edge = guiText.edge;
		this.borderEdge = edge;
		this.color = guiText.color;
		this.positionBounds = guiText.positionBounds;
		this.isGuiText = guiText.isGuiText;
		this.isInFlowchart = guiText.isInFlowchart;
		this.isCodeWindowText = guiText.isCodeWindowText;
		if(isGuiText){
			this.textMeshData = TextMaster.loadGuiText(this);
		}else{
			this.textMeshData = TextMaster.loadText(this);
		}
		if(deleteText){
			if(isGuiText){
				TextMaster.removeGuiText(guiText);
			}else {
				TextMaster.removeText(guiText);
			}
		}
		position.x = this.position.x*2-1;
		position.y = -this.position.y*2+1;
	}


	/**
	 * Remove the text from the screen.
	 */
	public void remove(GUIText text) {
		if(isGuiText){
			TextMaster.removeGuiText(text);
		}else {
			TextMaster.removeText(text);
		}
	}

	/**
	 * @return The font used by this text.
	 */
	public FontType getFont() {
		return font;
	}

	/**
	 * Set the color of the text.
	 * 
	 * @param r
	 *            - red value, between 0 and 1.
	 * @param g
	 *            - green value, between 0 and 1.
	 * @param b
	 *            - blue value, between 0 and 1.
	 */
	public void setColor(float r, float g, float b) {
		color.set(r, g, b);
	}

	/**
	 * @return the color of the text.
	 */
	public Vector3f getColor() {
		return color;
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

	/**
	 * @return The string of text.
	 */
	public String getTextString() {
		return textString;
	}

	/**
	 *@return The width the letters should have in the distance field map
	 */
	public float getWidth(){
		return width;
	}

	/**
	 *@return The width of the transition of the edge of the letters in the distance field map
	 */
	public float getEdge(){
		return edge;
	}

	/**
	 *@return The width of the border around the letters in the distance field map
	 */
	public float getBorderWidth(){
		return borderWidth;
	}

	/**
	 *@return The width of transition of the outside edge of the border around the letters in the distance field map
	 */
	public float getBorderEdge(){
		return borderEdge;
	}

	/**
	 *@return The offset between the border around the text and the text
	 */
	public Vector2f getOffset(){
		return offset;
	}

	/**
	 *@return The color of the border around the letters
	 */
	public Vector3f getOutlineColor(){
		return outlineColor;
	}

	/**
	 *@param borderWidth The desired width of the border around the letters in the distance field map
	 */
	public void setBorderWidth(float borderWidth){
		this.borderWidth = borderWidth;
	}

	/**
	 *@param borderEdge The desired width of transition of the outside edge of the border around the letters in the distance field map
	 */
	public void setBorderEdge(float borderEdge){
		this.borderEdge = borderEdge;
	}

	/**
	 *@param offset The desired offset between the border around the text and the text
	 */
	public void setOffset(Vector2f offset){
		this.offset = offset;
	}

	/**
	 *@param outlineColor The desired color of the border around the letters
	 */
	public void setOutlineColor(Vector3f outlineColor){
		this.outlineColor = outlineColor;
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

	public Vector4f getPositionBounds() {
		return positionBounds;
	}

	public void setPositionBounds(Vector4f positionBounds){
		this.positionBounds = positionBounds;
	}

	public float[] getCharacterEdges(){
		return textMeshData.getCharacterEdges();
	}

	public void setPosition(Vector2f position){
		this.position = position;
	}

	public boolean isInFlowchart() {
		return isInFlowchart;
	}

	public boolean isGuiText(){
		return isGuiText;
	}

	public boolean isCodeWindowText(){
		return isCodeWindowText;
	}

	public double getSpaceSize() {
		return spaceSize;
	}

	public void setSpaceSize(double spaceSize) {
		this.spaceSize = spaceSize;
	}
}
