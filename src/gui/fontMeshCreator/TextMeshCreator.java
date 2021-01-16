package gui.fontMeshCreator;

import gui.texts.Text;
import utils.MyFile;

import java.util.ArrayList;
import java.util.List;

public class TextMeshCreator {

	protected static final double LINE_HEIGHT = 0.03f;
	protected static final int SPACE_ASCII = 32;
	protected static final int NEW_LINE = 10;

	private MetaFile metaData;

	protected TextMeshCreator(MyFile metaFile) {
		metaData = new MetaFile(metaFile);
	}

	protected TextMeshData createTextMesh(Text text, String textString) {
		Line line = createStructure(text, textString);
		return createQuadVertices(text, line);
	}

	private Line createStructure(Text text, String textString) {
		char[] chars = textString.toCharArray();
		Line line = new Line(metaData.getSpaceWidth(), text.getFontSize());
		Word currentWord = new Word(text.getFontSize());
		for (char c : chars) {
			if (c == SPACE_ASCII) {
				line.addWord(currentWord);
				currentWord = new Word(text.getFontSize());
				continue;
			}/*else if(c == '\t'){
				 line.addSpaces(GeneralSettings.DEFAULT_TAB_WIDTH);
			}*/
			Character character = metaData.getCharacter(c);
			currentWord.addCharacter(character);
		}
		line.addWord(currentWord);
		text.setLength(line.getLineLength());
		return line;
	}


	private TextMeshData createQuadVertices(Text text, Line line) {
		double curserX = 0f;
		double curserY = 0f;
		List<Float> vertices = new ArrayList<>();
		List<Float> textureCoords = new ArrayList<>();
		List<Float> characterEdges = new ArrayList<>();
		characterEdges.add(0f);

		for (Word word : line.getWords()) {
			for (Character letter : word.getCharacters()) {
				addVerticesForCharacter(curserX, curserY, letter, text.getFontSize(), vertices);
				addTexCoords(textureCoords, letter.getxTextureCoord(), letter.getyTextureCoord(),
						letter.getXMaxTextureCoord(), letter.getYMaxTextureCoord());
				curserX += letter.getxAdvance() * text.getFontSize();
				characterEdges.add((float) curserX);
			}
			curserX += metaData.getSpaceWidth() * text.getFontSize();
			characterEdges.add((float) curserX);
		}
		characterEdges.remove(characterEdges.size()-1);
		return new TextMeshData(listToArray(vertices), listToArray(textureCoords), listToArray(characterEdges));
	}

	private void addVerticesForCharacter(double curserX, double curserY, Character character, double fontSize,
			List<Float> vertices) {
		double x = curserX + (character.getxOffset() * fontSize);
		double y = curserY + (character.getyOffset() * fontSize);
		double maxX = x + (character.getSizeX() * fontSize);
		double maxY = y + (character.getSizeY() * fontSize);
		double properX = (2 * x) - 1;
		double properY = (-2 * y) + 1;
		double properMaxX = (2 * maxX) - 1;
		double properMaxY = (-2 * maxY) + 1;
		addVertices(vertices, properX, properY, properMaxX, properMaxY);
	}

	private static void addVertices(List<Float> vertices, double x, double y, double maxX, double maxY) {
		vertices.add((float) x);
		vertices.add((float) y);
		vertices.add((float) x);
		vertices.add((float) maxY);
		vertices.add((float) maxX);
		vertices.add((float) maxY);
		vertices.add((float) maxX);
		vertices.add((float) maxY);
		vertices.add((float) maxX);
		vertices.add((float) y);
		vertices.add((float) x);
		vertices.add((float) y);
	}

	private static void addTexCoords(List<Float> texCoords, double x, double y, double maxX, double maxY) {
		texCoords.add((float) x);
		texCoords.add((float) y);
		texCoords.add((float) x);
		texCoords.add((float) maxY);
		texCoords.add((float) maxX);
		texCoords.add((float) maxY);
		texCoords.add((float) maxX);
		texCoords.add((float) maxY);
		texCoords.add((float) maxX);
		texCoords.add((float) y);
		texCoords.add((float) x);
		texCoords.add((float) y);
	}

	
	private static float[] listToArray(List<Float> listOfFloats) {
		float[] array = new float[listOfFloats.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = listOfFloats.get(i);
		}
		return array;
	}

	public float getSpaceSize(){
		return (float) metaData.getSpaceWidth();
	}

}
