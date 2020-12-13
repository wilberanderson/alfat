package rendering.renderEngine;

import controllers.ApplicationController;
import controllers.codeWindow.CursorController;
import controllers.flowchartWindow.FlowchartWindowController;
import controllers.flowchartWindow.TextLineController;
import gui.*;
import main.GeneralSettings;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import rendering.cursor.CursorRenderer;
import rendering.flowchartLine.FlowchartLineRenderer;
import rendering.guis.GuiRenderer;
import rendering.filledBox.FilledBoxRenderer;
import rendering.terminators.TerminatorRenderer;
import rendering.text.TextMaster;
import rendering.textLines.TextLineRenderer;

import java.util.ArrayList;
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
	private static TextLineRenderer textLineRenderer;

	private static TextLineController textLineController = new TextLineController();

	private MasterRenderer(){
	}

	public static void init() {
		guiRenderer = new GuiRenderer();
		filledBoxRenderer = new FilledBoxRenderer();
		TextMaster.init();
		cursorRenderer = new CursorRenderer();
		flowchartLineRenderer = new FlowchartLineRenderer();
		terminatorRenderer = new TerminatorRenderer();
		textLineRenderer = new TextLineRenderer();
	}

	/**
	 * Renders the scene to the screen.
	 */
	public static void renderScene(List<GuiTexture> guis, ApplicationController controller, long window) {
		//Clear the framebuffer
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		guiRenderer.render(guis);
		if(controller.getCodeWindowController() != null) {
			if (controller.getFlowchartWindowController() == null) {
				filledBoxRenderer.render(null, controller.getFlowchartWindowController(), controller.getCodeWindowController().getCodeWindow());
			} else {
				filledBoxRenderer.render(controller.getFlowchartWindowController().getFlowchartTextBoxList(), controller.getFlowchartWindowController(), controller.getCodeWindowController().getCodeWindow());
			}
		}
		if(controller.getFlowchartWindowController() != null) {
			flowchartLineRenderer.render(controller.getFlowchartWindowController().getFlowchartLineList(), controller.getFlowchartWindowController(), true, false);
			terminatorRenderer.render(controller.getFlowchartWindowController().getFlowchartLineList(), controller.getFlowchartWindowController(), true, false);
		}
		if(controller.getCodeWindowController() != null){
			TextMaster.render(controller.getFlowchartWindowController(), controller.getCodeWindowController().getCodeWindow(), true, false);
		}
		if(controller.getFlowchartWindowController() != null) {
			textLineRenderer.render(controller.getFlowchartWindowController().getTextLineController(), controller.getFlowchartWindowController(), controller.getCodeWindowController().getCodeWindow(), true, false);
		}
		if(controller.getCodeWindowController() != null && controller.getCodeWindowController().getCursorController() != null) {
			cursorRenderer.render(controller.getCodeWindowController().getCursorController());
		}
		filledBoxRenderer.renderGuis(controller.getHeader());
		TextMaster.renderGuis();

		//Swap the color buffers to update the screen
		GLFW.glfwSwapBuffers(window);
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
		textLineRenderer.cleanUp();
	}

	public static void renderScreenshot(FlowchartWindowController flowchartWindowController){
		FilledBoxRenderer filledBoxRenderer = new FilledBoxRenderer();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		filledBoxRenderer.renderScreenshot(flowchartWindowController.getFlowchartTextBoxList(), flowchartWindowController);
		flowchartLineRenderer.render(flowchartWindowController.getFlowchartLineList(), flowchartWindowController, false, true);
		terminatorRenderer.render(flowchartWindowController.getFlowchartLineList(), flowchartWindowController, false, true);
		TextMaster.render(flowchartWindowController, null, false, true);
		textLineRenderer.render(flowchartWindowController.getTextLineController(), flowchartWindowController, null, false, true);
		filledBoxRenderer.cleanUp();
	}
}
