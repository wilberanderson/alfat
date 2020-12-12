package rendering.renderEngine;

import controllers.ApplicationController;
import controllers.codeWindow.CursorController;
import controllers.flowchartWindow.FlowchartWindow;
import controllers.flowchartWindow.FlowchartWindowController;
import gui.*;
import gui.textBoxes.CodeWindow;
import gui.textBoxes.FlowchartTextBox;
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

	/**
	 * Renders the scene to the screen.
	 */
	public static void renderScene(List<GuiTexture> guis, CursorController cursor, Header header, ApplicationController controller) {
		guiRenderer.render(guis);
		if(controller.getCodeWindowController() != null) {
			if (controller.getFlowChartWindowController() == null) {
				filledBoxRenderer.render(null, controller.getFlowChartWindowController(), header);
			} else {
				filledBoxRenderer.render(controller.getFlowChartWindowController().getFlowchartTextBoxList(), controller.getFlowChartWindowController(), header);
			}
		}
		if(controller.getFlowChartWindowController() != null) {
			flowchartLineRenderer.render(controller.getFlowChartWindowController().getFlowchartLineList(), controller.getFlowChartWindowController(), true, false);
			terminatorRenderer.render(controller.getFlowChartWindowController().getFlowchartLineList(), controller.getFlowChartWindowController(), true, false);
		}
		if(controller.getCodeWindowController() != null){
			TextMaster.render(controller.getFlowChartWindowController(), controller.getCodeWindowController().getCodeWindow(), true);
		}else{

		}
		if(cursor != null) {
			cursorRenderer.render(cursor);
		}
		filledBoxRenderer.renderGuis(header);
		TextMaster.renderGuis();
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

	public static void renderScreenshot(FlowchartWindowController flowchartWindowController){
		FilledBoxRenderer filledBoxRenderer = new FilledBoxRenderer();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		filledBoxRenderer.renderScreenshot(flowchartWindowController.getFlowchartTextBoxList(), flowchartWindowController);
		flowchartLineRenderer.render(flowchartWindowController.getFlowchartLineList(), flowchartWindowController, false, true);
		terminatorRenderer.render(flowchartWindowController.getFlowchartLineList(), flowchartWindowController, false, true);
		TextMaster.render(flowchartWindowController, null, false);
		filledBoxRenderer.cleanUp();
	}
}
