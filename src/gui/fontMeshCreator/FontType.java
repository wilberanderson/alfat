package gui.fontMeshCreator;

import gui.texts.GUIText;
import gui.texts.Text;
import utils.MyFile;
import utils.Printer;

/**
 * Represents a font. It holds the font's texture atlas as well as having the
 * ability to create the quad vertices for any text using this font.
 * 
 * @author Karl
 *
 */
public class FontType {

	private int textureAtlas;
	private TextMeshCreator loader;

	/**
	 * Creates a new font and loads up the data about each character from the
	 * font file.
	 *
	 * @param textureAtlas
	 *            - the ID of the font atlas texture.
	 * @param fontFile
	 *            - the font file containing information about each character in
	 *            the texture atlas.
	 */
	public FontType(int textureAtlas, MyFile fontFile) {
		this.textureAtlas = textureAtlas;
		this.loader = new TextMeshCreator(fontFile);
	}

	/**
	 * Creates a new font and loads up the data about each character from the
	 * font file.
	 *
	 * @param textureAtlas
	 *            - the ID of the font atlas texture.
	 * @param fontFile
	 *            - the font file containing information about each character in
	 *            the texture atlas.
	 */
	public FontType(int textureAtlas, MyFile fontFile, int width, int height) {
		this.textureAtlas = textureAtlas;
		this.loader = new TextMeshCreator(fontFile, width, height);
	}

	/**
	 * @return The font texture atlas.
	 */
	public int getTextureAtlas() {
		return textureAtlas;
	}

	/**
	 * Takes in an unloaded text and calculate all of the vertices for the quads
	 * on which this text will be rendered. The vertex positions and texture
	 * coords and calculated based on the information from the font file.
	 * 
	 * @param text
	 *            - the unloaded text.
	 * @return Information about the vertices of all the quads.
	 */
	public TextMeshData loadText(Text text, String textString) {
		if(text instanceof GUIText) {
			Printer.print(((GUIText) text).getMaxLineSize());
		}
		if(text instanceof GUIText && ((GUIText) text).getMaxLineSize() == -1){
			return loader.createMultilineTextMesh((GUIText) text, textString, -1);
		}
		return loader.createTextMesh(text, textString);
	}

	public TextMeshData loadText(Text text, String textString, float maxSize) {
		if(text instanceof GUIText) {
			Printer.print(((GUIText) text).getMaxLineSize());
		}
		Printer.print("Making a multiline text with length " + maxSize);
		if(text instanceof GUIText && maxSize != -1){
			return loader.createMultilineTextMesh((GUIText) text, textString, maxSize);
		}
		return loader.createTextMesh(text, textString);
	}

	public float getSpaceSize(){
		return loader.getSpaceSize();
	}
}
