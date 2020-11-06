package rendering.renderEngine;

import gui.*;
import gui.textBoxes.CodeWindow;
import gui.textBoxes.TextBox;
import org.lwjgl.util.vector.Vector3f;
import rendering.cursor.CursorRenderer;
import rendering.flowchartLine.FlowchartLineRenderer;
import rendering.guis.GuiRenderer;
import rendering.filledBox.FilledBoxRenderer;
import rendering.text.TextMaster;

import java.util.List;

/**
 * This class is in charge of rendering everything in the scene to the screen.
 * @author Karl
 *
 */
public class MasterRenderer {

	private GuiRenderer guiRenderer;
	private FilledBoxRenderer filledBoxRenderer;
	private CursorRenderer cursorRenderer;
	private FlowchartLineRenderer flowchartLineRenderer;

	public MasterRenderer() {
		guiRenderer = new GuiRenderer();
		filledBoxRenderer = new FilledBoxRenderer();
		TextMaster.init();
		cursorRenderer = new CursorRenderer();
		flowchartLineRenderer = new FlowchartLineRenderer();
	}

	/**
	 * Renders the scene to the screen.
	 */
	public void renderScene(List<GuiTexture> guis, List<TextBox> textBoxes, Vector3f color, Cursor cursor, float fontSize, Header header, List<FlowchartLine> flowchartLines, FlowChartWindow flowChartWindow, CodeWindow codeWindow) {
		guiRenderer.render(guis);
		filledBoxRenderer.render(textBoxes, flowChartWindow, header);
		flowchartLineRenderer.render(flowchartLines, flowChartWindow);
		TextMaster.render(flowChartWindow, codeWindow);
		if(cursor != null) {
			cursorRenderer.render(cursor);
		}
		filledBoxRenderer.renderGuis(header);
		TextMaster.renderGuis(flowChartWindow, codeWindow);
	}

	/**
	 * Clean up when the game is closed.
	 */
	public void cleanUp() {
		guiRenderer.cleanUp();
		TextMaster.cleanUp();
		filledBoxRenderer.cleanUp();
		cursorRenderer.cleanUp();
		flowchartLineRenderer.cleanUp();
	}
}
