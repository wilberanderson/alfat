package rendering.text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fontMeshCreator.FontType;
import gui.CodeWindow;
import gui.FlowChartWindow;
import gui.GUIText;
import fontMeshCreator.TextMeshData;
import loaders.Loader;

public class TextMaster {

	private static Map<FontType, List<GUIText>> texts = new HashMap<FontType, List<GUIText>>();
	private static Map<FontType, List<GUIText>> guiTexts = new HashMap<FontType, List<GUIText>>();
	private static FontRenderer renderer;
	
	public static void init(){
		renderer = new FontRenderer();
	}
	
	public static void render(FlowChartWindow flowChartWindow, CodeWindow codeWindow){
		renderer.render(texts, flowChartWindow, codeWindow);
	}
	public static void renderGuis(FlowChartWindow flowChartWindow, CodeWindow codeWindow){
		renderer.render(guiTexts, flowChartWindow, codeWindow);

	}
	
	public static TextMeshData loadText(GUIText text){
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		int vao = Loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(vao, data.getVertexCount());
		List<GUIText> textBatch = texts.get(font);
		if(textBatch == null){
			textBatch = new ArrayList<>();
			texts.put(font, textBatch);
		}
		textBatch.add(text);
		return data;
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public static void removeText(GUIText text){
		List<GUIText> textBatch = texts.get(text.getFont());
		textBatch.remove(text);
		if(textBatch.isEmpty()){
			texts.remove(texts.get(text.getFont()));
		}
	}

	public static TextMeshData loadGuiText(GUIText text){
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		int vao = Loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(vao, data.getVertexCount());
		List<GUIText> textBatch = guiTexts.get(font);
		if(textBatch == null){
			textBatch = new ArrayList<>();
			guiTexts.put(font, textBatch);
		}
		textBatch.add(text);
		return data;
	}

	@SuppressWarnings("unlikely-arg-type")
	public static void removeGuiText(GUIText text){
		List<GUIText> textBatch = guiTexts.get(text.getFont());
		textBatch.remove(text);
		if (textBatch.isEmpty()) {
			guiTexts.remove(guiTexts.get(text.getFont()));
		}

	}

	public static void cleanUp(){
		renderer.cleanUp();
	}

}
