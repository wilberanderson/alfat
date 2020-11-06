package gui.fontMeshCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a line of text during the loading of a text.
 * 
 * @author Karl
 *
 */
public class Line {

	private double maxLength;
	private double spaceSize;

	private List<Word> words = new ArrayList<>();
	private double currentLineLength = 0;

	/**
	 * Creates an empty line.
	 * 
	 * @param spaceWidth
	 *            - the screen-space width of a space character.
	 * @param fontSize
	 *            - the size of font being used.
	 */
	protected Line(double spaceWidth, double fontSize) {
		this.spaceSize = spaceWidth * fontSize;
	}

	/**
	 * Attempt to add a word to the line. If the line can fit the word in
	 * without reaching the maximum line length then the word is added and the
	 * line length increased.
	 * 
	 * @param word
	 *            - the word to try to add.
	 */
	protected void addWord(Word word) {
		currentLineLength += word.getWordWidth();
		currentLineLength += !words.isEmpty() ? spaceSize : 0;
		words.add(word);
	}

	protected void addSpaces(int numberOfSpaces){
		currentLineLength += numberOfSpaces * spaceSize;
	}

	/**
	 * @return The max length of the line.
	 */
	protected double getMaxLength() {
		return maxLength;
	}

	/**
	 * @return The current screen-space length of the line.
	 */
	protected double getLineLength() {
		return currentLineLength;
	}

	/**
	 * @return The list of words in the line.
	 */
	protected List<Word> getWords() {
		return words;
	}

}
