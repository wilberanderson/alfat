package controllers.codeWindow;

import controllers.ApplicationController;
import gui.Cursor;
import gui.texts.CodeWindowText;
import gui.texts.Text;
import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;

public class CursorController {
    Cursor cursor;
    CodeWindowController codeWindow;
    List<CodeWindowText> texts;
    CodeWindowText currentGUIText;
    private Vector2f aspectRatio;
    private int lineIndex;
    private int characterIndex;
    private boolean visible = false;
    private boolean toggle = true;

    public CursorController(Cursor cursor, CodeWindowController codeWindow){
        this.cursor = cursor;
        this.codeWindow = codeWindow;
    }

    public void moveCursor(Vector2f mousePosition, CodeWindowController codeWindow){
        visible = true;
        this.codeWindow = codeWindow;
        texts = codeWindow.getTexts();
        this.aspectRatio = new Vector2f(codeWindow.getAspectRatio());
        mousePosition.x /= aspectRatio.x;
        mousePosition.y /= aspectRatio.y;
        float testHeight = mousePosition.y;
        currentGUIText = null;
        for (int i = 0; i < texts.size(); i++){
            float newHeight = texts.get(i).getPosition().y;
            if(i+1 == texts.size()){
                mousePosition.y = newHeight;
                currentGUIText = texts.get(i);
                continue;
            }
            if (testHeight < newHeight  && testHeight > texts.get(i+1).getPosition().y){
                mousePosition.y = newHeight;
                currentGUIText = texts.get(i);
                break;
            }
        }
        if (currentGUIText != null) {
            lineIndex = texts.indexOf(currentGUIText);
            float[] characterEdges = currentGUIText.getCharacterEdges();
            float testWidth = mousePosition.x - currentGUIText.getPosition().x;
            int i;
            for (i = 0; i < characterEdges.length-1; i++){
                if (Math.abs(characterEdges[i+1]*2 - testWidth) > Math.abs(characterEdges[i]*2 - testWidth)){
                    mousePosition.x = (characterEdges[i]*2 + currentGUIText.getPosition().x);
                    break;
                }
            }
            if(i == characterEdges.length-1){
                mousePosition.x = (characterEdges[i]*2 + currentGUIText.getPosition().x);
            }
            characterIndex = i;
        }
        cursor.setPosition(mousePosition);
    }

    public void moveLeft(){
        characterIndex -= 1;
        if(characterIndex >= 0) {
            updateXPosition();
        }else{
            if(lineIndex > 0){
                lineIndex--;
                currentGUIText = texts.get(lineIndex);
                characterIndex = currentGUIText.getCharacterEdges().length-1;
                updatePosition();
            }else{
                characterIndex = 0;
            }
        }
    }

    public void moveRight(){
        characterIndex += 1;
        if(characterIndex < currentGUIText.getCharacterEdges().length) {
            updateXPosition();
        }else{
            if(lineIndex < texts.size()-1){
                lineIndex++;
                currentGUIText = texts.get(lineIndex);
                characterIndex = 0;
                updatePosition();
            }else{
                characterIndex = currentGUIText.getCharacterEdges().length-1;
            }
        }
    }

    public void moveDown(){
        if(lineIndex < texts.size()-1){
            lineIndex++;
            currentGUIText = texts.get(lineIndex);
            if(characterIndex > currentGUIText.getCharacterEdges().length-1){
                characterIndex = currentGUIText.getCharacterEdges().length-1;
            }
            updatePosition();
        }else{
            characterIndex = currentGUIText.getCharacterEdges().length-1;
            updateXPosition();
        }
    }

    public void moveUp(){
        if(lineIndex > 0){
            lineIndex--;
            currentGUIText = texts.get(lineIndex);
            if(characterIndex > currentGUIText.getCharacterEdges().length-1){
                characterIndex = currentGUIText.getCharacterEdges().length-1;
            }
            updatePosition();
        }else{
            characterIndex = 0;
            updateXPosition();
        }
    }

    public void backSpace(){
        if (characterIndex > 0){
            characterIndex--;
            String newContent = currentGUIText.getTextString().substring(0, characterIndex) + currentGUIText.getTextString().substring(characterIndex + 1);
            currentGUIText = new CodeWindowText(newContent, currentGUIText, true);
            codeWindow.getTexts().set(lineIndex, currentGUIText);
            updateXPosition();
        } else if (lineIndex > 0) {
            lineIndex--;
            characterIndex = texts.get(lineIndex).getCharacterEdges().length - 1;
            currentGUIText = codeWindow.mergeTexts(texts.get(lineIndex), currentGUIText);
            updatePosition();
        }
    }

    public void delete(){
        if (characterIndex < currentGUIText.getCharacterEdges().length - 1){
            String newContent = currentGUIText.getTextString().substring(0, characterIndex) + currentGUIText.getTextString().substring(characterIndex + 1);
            currentGUIText = new CodeWindowText(newContent, currentGUIText, true);
            codeWindow.getTexts().set(lineIndex, currentGUIText);
        } else if (lineIndex < texts.size() - 1) {
            currentGUIText = codeWindow.mergeTexts(currentGUIText, texts.get(lineIndex+1));
        }
    }

    public void paste(String clipboardContents){
        String originalText = currentGUIText.getTextString();
        String textString = originalText.substring(0, characterIndex);
        String endText = originalText.substring(characterIndex);
        StringBuilder stringBuilder = new StringBuilder(textString);
        char[] pastedChars = new char[clipboardContents.length()];
        for(int i = 0; i < clipboardContents.length(); i++) {
            pastedChars[i] = clipboardContents.charAt(i);
        }

        for(char c : pastedChars){
            if(c == '\n'){
                CodeWindowText newText = new CodeWindowText(stringBuilder.toString(), currentGUIText, true);
                texts.set(lineIndex, newText);
                currentGUIText = new CodeWindowText(endText, newText, false);
                currentGUIText.setPosition(new Vector2f(currentGUIText.getPosition().x, currentGUIText.getPosition().y - currentGUIText.getFontSize()*0.06f));
                lineIndex++;
                codeWindow.addText(currentGUIText, lineIndex);
                stringBuilder = new StringBuilder();
                characterIndex = 0;
                updateYPosition();
            }else{
                stringBuilder.append(c);
                characterIndex++;
            }
        }
        currentGUIText = new CodeWindowText(stringBuilder.toString() + endText, currentGUIText, true);
        texts.set(lineIndex, currentGUIText);
        updateXPosition();
        ApplicationController.PASTE = false;
    }

    public void type(char c){
        String originalText = currentGUIText.getTextString();
        String textString = originalText.substring(0, characterIndex);
        String endText = originalText.substring(characterIndex);
        StringBuilder stringBuilder = new StringBuilder(textString);

        if(c == '\n'){
            CodeWindowText newText = new CodeWindowText(stringBuilder.toString(), currentGUIText, true);
            texts.set(lineIndex, newText);
            currentGUIText = new CodeWindowText(endText, newText, false);
            currentGUIText.setPosition(new Vector2f(currentGUIText.getPosition().x, currentGUIText.getPosition().y - currentGUIText.getFontSize()* GeneralSettings.FONT_SCALING_FACTOR));
            lineIndex++;
            codeWindow.addText(currentGUIText, lineIndex);
            stringBuilder = new StringBuilder();
            characterIndex = 0;
            updateYPosition();
        }else{
            stringBuilder.append(c);
            characterIndex++;
        }

        currentGUIText = new CodeWindowText(stringBuilder.toString() + endText, currentGUIText, true);
        texts.set(lineIndex, currentGUIText);
        updateXPosition();
    }

    public void scroll(float scrollChange){
        cursor.getPosition().y += scrollChange;
    }

    private void updateXPosition(){
        cursor.getPosition().x = currentGUIText.getCharacterEdges()[characterIndex]*2 + currentGUIText.getPosition().x;
        if ((codeWindow.getCodeWindow().getCodeWindowPosition().x)/aspectRatio.x > cursor.getPosition().x){
            codeWindow.changeContentsHorizontalPosition((codeWindow.getCodeWindow().getCodeWindowPosition().x)/aspectRatio.x - (currentGUIText.getPosition().x + currentGUIText.getCharacterEdges()[characterIndex]*2));
            cursor.getPosition().x = (codeWindow.getCodeWindow().getCodeWindowPosition().x)/aspectRatio.x;
        }else if(cursor.getPosition().x/aspectRatio.x > (codeWindow.getCodeWindow().getCodeWindowPosition().x + codeWindow.getCodeWindow().getCodeWindowSize().x)){
            codeWindow.changeContentsHorizontalPosition(-(currentGUIText.getPosition().x +currentGUIText.getCharacterEdges()[characterIndex]*2 - (codeWindow.getCodeWindow().getCodeWindowPosition().x + codeWindow.getCodeWindow().getCodeWindowSize().x)/aspectRatio.x));
            cursor.getPosition().x = (codeWindow.getCodeWindow().getCodeWindowPosition().x + codeWindow.getCodeWindow().getCodeWindowSize().x)/aspectRatio.x;
        }
    }

    private void updateYPosition(){
        cursor.getPosition().y = currentGUIText.getPosition().y;
        if(cursor.getPosition().y* aspectRatio.y > (codeWindow.getCodeWindow().getCodeWindowPosition().y + codeWindow.getCodeWindow().getCodeWindowSize().y)){
            float change = (codeWindow.getCodeWindow().getCodeWindowPosition().y + codeWindow.getCodeWindow().getCodeWindowSize().y)/aspectRatio.y-currentGUIText.getPosition().y;
            codeWindow.changeContentsVerticalPosition(change);
            codeWindow.changeLineNumberVerticalPosition(change);
            cursor.getPosition().y = (codeWindow.getCodeWindow().getCodeWindowPosition().y + codeWindow.getCodeWindow().getCodeWindowSize().y)/aspectRatio.y;
        }else if(cursor.getPosition().y*aspectRatio.y < codeWindow.getCodeWindow().getCodeWindowPosition().y+0.06*currentGUIText.getFontSize()){
            float change = (codeWindow.getCodeWindow().getCodeWindowPosition().y)/aspectRatio.y-(currentGUIText.getPosition().y-0.06f*currentGUIText.getFontSize());
            codeWindow.changeContentsVerticalPosition(change);
            codeWindow.changeLineNumberVerticalPosition(change);
            cursor.getPosition().y = (codeWindow.getCodeWindow().getCodeWindowPosition().y + 0.06f*currentGUIText.getFontSize());
        }
        cursor.getPosition().y /= aspectRatio.y;
    }

    private void updatePosition(){
        updateXPosition();
        updateYPosition();
    }

    public void updateAspectRatio(){
        this.aspectRatio = new Vector2f(codeWindow.getAspectRatio());
        if(currentGUIText != null) {
            updatePosition();
        }
    }

    public CodeWindowController getCodeWindow(){
        return codeWindow;
    }

    public Cursor getCursor(){
        return cursor;
    }

    public Vector2f getAspectRatio(){
        return aspectRatio;
    }

    public void setVisible(boolean visible){
        this.visible = visible;
    }

    public boolean isVisible(){
        return visible && toggle;
    }

    public void toggleVisible(){
        if(visible){
            toggle = !toggle;
        }
    }
}
