package rendering.renderEngine;

import gui.Cursor;
import gui.Header;
import gui.TextBox;
import org.lwjgl.util.vector.Vector3f;
import rendering.cursor.CursorRenderer;
import rendering.guis.GuiRenderer;
import gui.GuiTexture;
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

	public MasterRenderer() {
		guiRenderer = new GuiRenderer();
		filledBoxRenderer = new FilledBoxRenderer();
		TextMaster.init();
		cursorRenderer = new CursorRenderer();
	}

	/**
	 * Renders the scene to the screen.
	 */
	public void renderScene(List<GuiTexture> guis, List<TextBox> textBoxes, Vector3f color, Cursor cursor, float fontSize, Header header) {
		guiRenderer.render(guis);
		filledBoxRenderer.render(textBoxes, header);
		TextMaster.render();
		if(cursor != null) {
			cursorRenderer.render(color, cursor.getPosition(), fontSize);
		}
		filledBoxRenderer.renderGuis();
		TextMaster.renderGuis();
	}

	/**
	 * Clean up when the game is closed.
	 */
	public void cleanUp() {
		guiRenderer.cleanUp();
		TextMaster.cleanUp();
		filledBoxRenderer.cleanUp();
		cursorRenderer.cleanUp();
	}
}
