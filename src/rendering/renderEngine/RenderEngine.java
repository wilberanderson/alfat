package rendering.renderEngine;

import gui.Cursor;
import gui.GuiTexture;
import gui.TextBox;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

/**
 * This class represents the entire render engine.
 * 
 * @author Karl
 *
 */
public class RenderEngine {

	public static MasterRenderer renderer;

	public MasterRenderer getRenderer(){
		return renderer;
	}

	public RenderEngine() {
		init();
	}


	/**
	 * Renders the scene to the screen.
	 *
	 */
	public void renderScene(List<GuiTexture> guis, List<TextBox> textBoxes, Vector3f cursorColor, Cursor cursor, float fontSize) {
		renderer.renderScene(guis, textBoxes, cursorColor, cursor, fontSize);
	}

	/**
	 * Cleans up the renderers and closes the display.
	 */
	public void close() {
		renderer.cleanUp();
//		DisplayManager.closeDisplay();
	}

	/**
	 * Initializes a new render engine. Creates the display and inits the
	 * renderers
	 */
	public void init() {
//		DisplayManager.createDisplay();
		renderer = new MasterRenderer();
	}


}
