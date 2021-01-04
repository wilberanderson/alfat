package rendering.renderEngine;

import controllers.ApplicationController;
import controllers.flowchartWindow.FlowchartWindowController;
import controllers.flowchartWindow.TextLineController;
import gui.GuiTexture;
import main.EngineTester;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import rendering.cursor.CursorRenderer;
import rendering.filledBox.FilledBoxRenderer;
import rendering.flowchartLine.FlowchartLineRenderer;
//import rendering.guis.GuiRenderer;
import rendering.terminators.TerminatorRenderer;
import rendering.text.TextMaster;
import rendering.textLines.TextLineRenderer;

import java.util.List;

/**
 * The MasterRenderer controls rendering everything contained within the application. Anything that appears on the screen is put there by one of the Renderer's which the MasterRenderer controls. This contains a Renderer for each category of opject that may appear on the screen. Currently support objects to be rendered are graphical images, filled in rectangles, a cursor to display in the text box, a line which connects boxes in the flowchart, text for the gui, and lines of formatted text. The MasterRenderer also control general OpenGL state to ensure that all rendering is performed properly.
 */
public class MasterRenderer {

	//private static GuiRenderer guiRenderer;
	private static FilledBoxRenderer filledBoxRenderer;
	private static CursorRenderer cursorRenderer;
	private static FlowchartLineRenderer flowchartLineRenderer;
	private static TerminatorRenderer terminatorRenderer;
	private static TextLineRenderer textLineRenderer;

	private static TextLineController textLineController = new TextLineController();

	/**
	 * No instances of MasterRenderer should be made, set the constructor to private to ensure this.
	 */
	private MasterRenderer() {
	}

	/**
	 * Initializes the MasterRenderer to be able to perform all tasks that it needs to. Initializes the various renderers that are needed to be used.
	 * TODO: Remove the TextMaster
	 */
	public static void init() {
		//guiRenderer = new GuiRenderer();
		filledBoxRenderer = new FilledBoxRenderer();
		TextMaster.init();
		cursorRenderer = new CursorRenderer();
		flowchartLineRenderer = new FlowchartLineRenderer();
		terminatorRenderer = new TerminatorRenderer();
		textLineRenderer = new TextLineRenderer();
	}

	/**
	 * Renders all objects which are to be rendered to the screen.
	 * @param guis the list of {@link GuiTexture} which are to be rendered to the screen. These are images which will be rendered to the screen directly.
	 * @param controller the {@link ApplicationController} which controls all operations in the application. Each controller contains the models which will be rendered.
	 */
	public static void renderScene(List<GuiTexture> guis, ApplicationController controller) {
		//Clear the framebuffer
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		//Render the gui images
		//guiRenderer.render(guis);

		//Render the filled boxes.
		filledBoxRenderer.renderToScreen(controller.getFlowchartWindowController(), controller.getCodeWindowController());


		//Render flowchart lines and terminators
		flowchartLineRenderer.renderToScreen(controller.getFlowchartWindowController());
		terminatorRenderer.renderToScreen(controller.getFlowchartWindowController());

		//If the code window controller is not null then regular text needs to be rendered, render it
		//TODO: Turn TextMaster into a GUI Text Controller
		if (controller.getCodeWindowController() != null) {
			TextMaster.render(controller.getFlowchartWindowController(), controller.getCodeWindowController().getCodeWindow(), true, false);
		}

		//If the flowchart window controller is not null then formatted text needs to be rendered, render it
		if (controller.getFlowchartWindowController() != null) {
			textLineRenderer.renderToScreen(controller.getFlowchartWindowController().getFlowchartTextBoxController().getTextLineController(), controller.getFlowchartWindowController(), controller.getCodeWindowController().getCodeWindow());
		}

		//If the cursor controller is not null then the cursor is present. Render the cursor
		if (controller.getCodeWindowController() != null && controller.getCodeWindowController().getCursorController() != null) {
			cursorRenderer.render(controller.getCodeWindowController().getCursorController());
		}

		//Render gui elements
		filledBoxRenderer.renderGuis(controller.getHeader());

		//Render gui text
		TextMaster.renderGuis();

		//Swap the color buffers to update the screen
		GLFW.glfwSwapBuffers(EngineTester.getWindow());
	}

	/**
	 * Clean up the renderers when the game is closed. OpenGL does not have a garbage collector so various things need to be cleaned up as the game closes. Each renderer has a shader program saved in the GPU that needs to be deleted when the application is closed.
	 */
	public static void cleanUp() {
		//guiRenderer.cleanUp();
		TextMaster.cleanUp();
		filledBoxRenderer.cleanUp();
		cursorRenderer.cleanUp();
		flowchartLineRenderer.cleanUp();
		terminatorRenderer.cleanUp();
		textLineRenderer.cleanUp();
	}

	/**
	 * Renders the application to save the flowchart. GUI elements and portions of the code window should not be rendered, only the flowchart elements
	 * @param flowchartWindowController
	 */
	public static void renderScreenshot(FlowchartWindowController flowchartWindowController) {
		//Clear the image to ensure it behaves properly
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		//Render the flowchart and it's text
		filledBoxRenderer.renderToImage(flowchartWindowController);
		flowchartLineRenderer.renderToImage(flowchartWindowController);
		terminatorRenderer.renderToImage(flowchartWindowController);
		//TODO: Move the line numbers into the text lines to remove this render call
		TextMaster.render(flowchartWindowController, null, false, true);
		textLineRenderer.renderToImage(flowchartWindowController.getFlowchartTextBoxController().getTextLineController(), flowchartWindowController);
	}
}
