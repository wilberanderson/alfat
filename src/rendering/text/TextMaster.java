package rendering.text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controllers.flowchartWindow.FlowchartWindowController;
import gui.fontMeshCreator.FontType;
import gui.textBoxes.CodeWindow;
import gui.texts.CodeWindowText;
import gui.texts.GUIText;
import gui.texts.Text;
import gui.fontMeshCreator.TextMeshData;
import loaders.Loader;

public class TextMaster {

	private static Map<FontType, List<Text>> texts = new HashMap<FontType, List<Text>>();
	private static Map<FontType, List<Text>> guiTexts = new HashMap<FontType, List<Text>>();
	private static FontRenderer renderer;
	
	public static void init(){
		renderer = new FontRenderer();
	}
	
	public static void render(FlowchartWindowController flowChartWindow, CodeWindow codeWindow, boolean doClipping, boolean isScreenshot){
		renderer.render(texts, flowChartWindow, codeWindow, doClipping, isScreenshot);
	}
	public static void renderGuis(){
		renderer.render(guiTexts, null, null, false, false);
	}
	
	public static TextMeshData loadText(Text text, String textString){
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text, textString);
		int vao = Loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(vao, data.getVertexCount());
		List<Text> textBatch = texts.get(font);
		if(textBatch == null){
			textBatch = new ArrayList<>();
			texts.put(font, textBatch);
		}
		textBatch.add(text);
		return data;
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public static void removeText(Text text){
		List<Text> textBatch = texts.get(text.getFont());
		textBatch.remove(text);
		if(textBatch.isEmpty()){
			texts.remove(texts.get(text.getFont()));
		}
	}

	public static TextMeshData loadGuiText(GUIText text){
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text, text.getTextString());
		int vao = Loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(vao, data.getVertexCount());
		List<Text> textBatch = guiTexts.get(font);
		if(textBatch == null){
			textBatch = new ArrayList<>();
			guiTexts.put(font, textBatch);
		}
		textBatch.add(text);
		return data;
	}

	public static TextMeshData loadGuiText(GUIText text, String textString){
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text, textString);
		int vao = Loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(vao, data.getVertexCount());
		List<Text> textBatch = guiTexts.get(font);
		if(textBatch == null){
			textBatch = new ArrayList<>();
			guiTexts.put(font, textBatch);
		}
		textBatch.add(text);
		return data;
	}

	@SuppressWarnings("unlikely-arg-type")
	public static void removeGuiText(GUIText text){
		List<Text> textBatch = guiTexts.get(text.getFont());
		textBatch.remove(text);
		if (textBatch.isEmpty()) {
			guiTexts.remove(guiTexts.get(text.getFont()));
		}

	}

	public static void cleanUp(){
		renderer.cleanUp();
	}

}
