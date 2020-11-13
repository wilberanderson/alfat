package rendering.renderEngine;

import gui.*;
import gui.textBoxes.CodeWindow;
import gui.textBoxes.FlowChartTextBox;
import gui.textBoxes.TextBox;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import rendering.cursor.CursorRenderer;
import rendering.flowchartLine.FlowchartLineRenderer;
import rendering.guis.GuiRenderer;
import rendering.filledBox.FilledBoxRenderer;
import rendering.terminators.TerminatorRenderer;
import rendering.text.TextMaster;

import java.util.List;

/**
 * This class is in charge of rendering everything in the scene to the screen.
 * @author Karl
 *
 */
public class MasterRenderer {

	private static GuiRenderer guiRenderer;
	private static FilledBoxRenderer filledBoxRenderer;
	private static CursorRenderer cursorRenderer;
	private static FlowchartLineRenderer flowchartLineRenderer;
	private static TerminatorRenderer terminatorRenderer;
	private static FlowChartWindow flowChartWindow;

	private MasterRenderer(){

	}

	public static void init() {
		guiRenderer = new GuiRenderer();
		filledBoxRenderer = new FilledBoxRenderer();
		TextMaster.init();
		cursorRenderer = new CursorRenderer();
		flowchartLineRenderer = new FlowchartLineRenderer();
		terminatorRenderer = new TerminatorRenderer();
	}

	public static void setFlowChartWindow(FlowChartWindow flowChartWindow){
		MasterRenderer.flowChartWindow = flowChartWindow;
	}

	/**
	 * Renders the scene to the screen.
	 */
	public static void renderScene(List<GuiTexture> guis, List<FlowChartTextBox> textBoxes, Vector3f color, Cursor cursor, float fontSize, Header header, List<FlowchartLine> flowchartLines, FlowChartWindow flowChartWindow, CodeWindow codeWindow) {
		guiRenderer.render(guis);
		filledBoxRenderer.render(textBoxes, flowChartWindow, header);
		if(flowchartLines != null) {
			flowchartLineRenderer.render(flowchartLines, flowChartWindow, true);
			terminatorRenderer.render(flowchartLines, flowChartWindow, true);
		}
		TextMaster.render(flowChartWindow, codeWindow, true);
		if(cursor != null) {
			cursorRenderer.render(cursor);
		}
		filledBoxRenderer.renderGuis(header);
		TextMaster.renderGuis(flowChartWindow, codeWindow);
	}

	/**
	 * Clean up when the game is closed.
	 */
	public static void cleanUp() {
		guiRenderer.cleanUp();
		TextMaster.cleanUp();
		filledBoxRenderer.cleanUp();
		cursorRenderer.cleanUp();
		flowchartLineRenderer.cleanUp();
		terminatorRenderer.cleanUp();
	}

	public static void renderScreenshot(){
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		filledBoxRenderer.renderScreenshot(FlowChartWindow.getFlowChartTextBoxList(), flowChartWindow);
		flowchartLineRenderer.render(FlowChartWindow.getFlowchartLineList(), flowChartWindow, false);
		terminatorRenderer.render(FlowChartWindow.getFlowchartLineList(), flowChartWindow, false);
		TextMaster.render(flowChartWindow, null, false);
	}
}
