package rendering.renderEngine;

import gui.TextBox;
import rendering.guis.GuiRenderer;
import gui.GuiTexture;
import rendering.text.TextBoxRenderer;
import rendering.text.TextMaster;

import java.util.List;

/**
 * This class is in charge of rendering everything in the scene to the screen.
 * @author Karl
 *
 */
public class MasterRenderer {

	private GuiRenderer guiRenderer;
	private TextBoxRenderer textBoxRenderer;

	public void init() {
		guiRenderer = new GuiRenderer();
		textBoxRenderer = new TextBoxRenderer();
		TextMaster.init();
	}

	/**
	 * Renders the scene to the screen.
	 */
	protected void renderScene(List<GuiTexture> guis, List<TextBox> textBoxes) {
		guiRenderer.render(guis);
		textBoxRenderer.render(textBoxes);
		TextMaster.render();
	}

	/**
	 * Clean up when the game is closed.
	 */
	protected void cleanUp() {
		guiRenderer.cleanUp();
		TextMaster.cleanUp();
	}
}
