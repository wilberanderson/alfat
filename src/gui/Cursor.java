package gui;

import main.GeneralSettings;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector2f;
import utils.InputManager;

import java.util.List;

public class Cursor {

    private int lineIndex;
    private int characterIndex;
    private TextBox textBox;
    private GUIText text;
    private Vector2f position;
    private int rapidCounter = 0;
    private List<GUIText> texts;

    public Cursor(Vector2f newPosition, TextBox textBox){
        this.textBox = textBox;
        texts = textBox.getTexts();
        float testHeight = newPosition.y;
        text = null;
        for (int i = 0; i < texts.size(); i++){
            float newHeight = texts.get(i).getPosition().y;
            if(i+1 == texts.size()){
                newPosition.y = newHeight;
                text = texts.get(i);
                continue;
            }
            if (testHeight < newHeight  && testHeight > texts.get(i+1).getPosition().y){
                newPosition.y = newHeight;
                text = texts.get(i);
                break;
            }
        }
        if (text != null) {
            lineIndex = texts.indexOf(text);
            float[] characterEdges = text.getCharacterEdges();
            float testWidth = newPosition.x - text.getPosition().x;
            int i;
            for (i = 0; i < characterEdges.length-1; i++){
                if (Math.abs(characterEdges[i+1]*2 - testWidth) > Math.abs(characterEdges[i]*2 - testWidth)){
                    newPosition.x = (characterEdges[i]*2 + text.getPosition().x);
                    break;
                }
            }
            if(i == characterEdges.length-1){
                newPosition.x = (characterEdges[i]*2 + text.getPosition().x);
            }
            characterIndex = i;
        }
        position = newPosition;

        //Clear codepoints of any existing characters so only new inputs modify text
        InputManager.codepoints.clear();
    }

    public void processInputs(long window){
        rapidCounter = (rapidCounter + 1) % GeneralSettings.RAPID_MODE_FREQUENCY;
        if (InputManager.LEFT_PRESSED) {
            if(InputManager.LEFT_DURATION == GeneralSettings.getFrameTime()) {
                moveLeft();
            }else if(InputManager.LEFT_DURATION > GeneralSettings.RAPID_MODE_DURATION && rapidCounter == 0){
                moveLeft();
            }
        }else if (InputManager.RIGHT_PRESSED) {
            if(InputManager.RIGHT_DURATION == GeneralSettings.getFrameTime()) {
                moveRight();
            }else if(InputManager.RIGHT_DURATION > GeneralSettings.RAPID_MODE_DURATION && rapidCounter == 0){
                moveRight();
            }
        }else if (InputManager.UP_PRESSED) {
            if(InputManager.UP_DURATION == GeneralSettings.getFrameTime()) {
                moveUp();
            }else if(InputManager.UP_DURATION > GeneralSettings.RAPID_MODE_DURATION && rapidCounter == 0){
                moveUp();
            }
        }else if (InputManager.DOWN_PRESSED) {
            if(InputManager.DOWN_DURATION == GeneralSettings.getFrameTime()) {
                moveDown();
            }else if(InputManager.DOWN_DURATION > GeneralSettings.RAPID_MODE_DURATION && rapidCounter == 0){
                moveDown();
            }
        }else if (InputManager.BACKSPACE_PRESSED) {
            if(InputManager.BACKSPACE_DURATION == GeneralSettings.getFrameTime()) {
                backSpace();
            }else if(InputManager.BACKSPACE_DURATION > GeneralSettings.RAPID_MODE_DURATION && rapidCounter == 0){
                backSpace();
            }
        }else if (InputManager.DELETE_PRESSED) {
            if(InputManager.DELETE_DURATION == GeneralSettings.getFrameTime()) {
                delete();
            }else if(InputManager.DELETE_DURATION > GeneralSettings.RAPID_MODE_DURATION && rapidCounter == 0){
                delete();
            }
        }else if(InputManager.PASTE){
            paste(GLFW.glfwGetClipboardString(window));
            InputManager.PASTE = false;
        }
        if(!InputManager.codepoints.isEmpty()) {
            type();
        }

        position.y = position.y + (float)InputManager.SCROLL_CHANGE/10;
    }

    public Vector2f getPosition(){
        return position;
    }

    private void moveLeft(){
        characterIndex -= 1;
        if(characterIndex >= 0) {
            updateXPosition();
        }else{
            if(lineIndex > 0){
                lineIndex--;
                text = texts.get(lineIndex);
                characterIndex = text.getCharacterEdges().length-1;
                updatePosition();
            }else{
                characterIndex = 0;
            }
        }
    }

    private void moveRight(){
        characterIndex += 1;
        if(characterIndex < text.getCharacterEdges().length) {
            updateXPosition();
        }else{
            if(lineIndex < texts.size()-1){
                lineIndex++;
                text = texts.get(lineIndex);
                characterIndex = 0;
                updatePosition();
            }else{
                characterIndex = text.getCharacterEdges().length-1;
            }
        }
    }

    private void moveDown(){
        if(lineIndex < texts.size()-1){
            lineIndex++;
            text = texts.get(lineIndex);
            if(characterIndex > text.getCharacterEdges().length-1){
                characterIndex = text.getCharacterEdges().length-1;
            }
            updatePosition();
        }else{
            characterIndex = text.getCharacterEdges().length-1;
            updateXPosition();
        }
    }

    private void moveUp(){
        if(lineIndex > 0){
            lineIndex--;
            text = texts.get(lineIndex);
            if(characterIndex > text.getCharacterEdges().length-1){
                characterIndex = text.getCharacterEdges().length-1;
            }
            updatePosition();
        }else{
            characterIndex = 0;
            updateXPosition();
        }
    }

    private void backSpace(){
        if (characterIndex > 0){
            characterIndex--;
            String newContent = text.getTextString().substring(0, characterIndex) + text.getTextString().substring(characterIndex + 1);
            text = new GUIText(newContent, text, true);
            texts.set(lineIndex, text);
            updateXPosition();
        } else if (lineIndex > 0) {
            lineIndex--;
            characterIndex = texts.get(lineIndex).getCharacterEdges().length - 1;
            text = textBox.mergeTexts(texts.get(lineIndex), text);
            updatePosition();
        }
    }

    private void delete(){
        if (characterIndex < text.getCharacterEdges().length - 1){
            String newContent = text.getTextString().substring(0, characterIndex) + text.getTextString().substring(characterIndex + 1);
            text = new GUIText(newContent, text, true);
            texts.set(lineIndex, text);
        } else if (lineIndex < texts.size() - 1) {
            lineIndex++;
            text = textBox.mergeTexts(text, texts.get(lineIndex));
        }
    }

    private void paste(String clipboardContents){
        String originalText = text.getTextString();
        String textString = originalText.substring(0, characterIndex);
        String endText = originalText.substring(characterIndex);
        char[] pastedChars = new char[clipboardContents.length()];
        for(int i = 0; i < clipboardContents.length(); i++) {
            pastedChars[i] = clipboardContents.charAt(i);
        }

        for(char c : pastedChars){
            if(c == '\n'){
                GUIText newText = new GUIText(textString, text, true);
                texts.set(lineIndex, newText);
                text = new GUIText(endText, newText, false);
                text.setPosition(new Vector2f(text.getPosition().x, text.getPosition().y - text.getFontSize()*0.06f));
                lineIndex++;
                textBox.addText(text, lineIndex);
                textString = "";
                characterIndex = 0;
                updateYPosition();
            }else{
                textString += c;
                characterIndex++;
            }
        }
        text = new GUIText(textString + endText, text, true);
        texts.set(lineIndex, text);
        updateXPosition();
        InputManager.PASTE = false;
    }

    private void type(){
        String originalText = text.getTextString();
        String textString = originalText.substring(0, characterIndex);
        String endText = originalText.substring(characterIndex);
        StringBuilder stringBuilder = new StringBuilder();
        for(char c : InputManager.codepoints){
            if(c == '\n'){
                GUIText newText = new GUIText(textString, text, true);
                texts.set(lineIndex, newText);
                text = new GUIText(endText, newText, false);
                text.setPosition(new Vector2f(text.getPosition().x, text.getPosition().y - text.getFontSize()*0.06f));
                lineIndex++;
                textBox.addText(text, lineIndex);
                textString = "";
                characterIndex = 0;
                updateYPosition();
            }else{
                textString += c;
                characterIndex++;
            }
        }
        text = new GUIText(textString + endText, text, true);
        texts.set(lineIndex, text);
        updateXPosition();

        InputManager.codepoints.clear();
    }

    private void updateXPosition(){
        position.x = text.getCharacterEdges()[characterIndex]*2 + text.getPosition().x;
        if (position.x < textBox.getPosition().x-1){
            textBox.changeContentsHorizontalPosition((textBox.getPosition().x-1) - (text.getPosition().x + text.getCharacterEdges()[characterIndex]*2));
            position.x = textBox.getPosition().x-1;
        }else if(position.x > textBox.getPosition().x + textBox.getSize().x - 1){
            textBox.changeContentsHorizontalPosition(-(text.getPosition().x +text.getCharacterEdges()[characterIndex]*2 - (textBox.getPosition().x + textBox.getSize().x-1)));
            position.x = textBox.getPosition().x + textBox.getSize().x - 1;
        }
    }

    private void updateYPosition(){
        position.y = text.getPosition().y;
        if(position.y > (textBox.getPosition().y + textBox.getSize().y) - 1){
            textBox.changeContentsVerticalPosition((textBox.getPosition().y - 1 + textBox.getSize().y)-text.getPosition().y);
            position.y = textBox.getPosition().y + textBox.getSize().y - 1;
        }else if(position.y < textBox.getPosition().y-1+0.06*text.getFontSize()){
            textBox.changeContentsVerticalPosition((textBox.getPosition().y-1)-(text.getPosition().y-0.06f*text.getFontSize()));
            position.y = textBox.getPosition().y-1 + 0.06f*text.getFontSize();
        }
    }

    private void updatePosition(){
        updateXPosition();
        updateYPosition();
    }
}
