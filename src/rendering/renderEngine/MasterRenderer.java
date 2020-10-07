package rendering.renderEngine;

import gui.Cursor;
import gui.TextBox;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import rendering.cursor.CursorRenderer;
import rendering.guis.GuiRenderer;
import gui.GuiTexture;
import rendering.textBox.TextBoxRenderer;
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
	private CursorRenderer cursorRenderer;

	public MasterRenderer() {
		guiRenderer = new GuiRenderer();
		textBoxRenderer = new TextBoxRenderer();
		TextMaster.init();
		cursorRenderer = new CursorRenderer();
	}

	/**
	 * Renders the scene to the screen.
	 */
	protected void renderScene(List<GuiTexture> guis, List<TextBox> textBoxes, Vector3f color, Cursor cursor, float fontSize) {
		guiRenderer.render(guis);
		textBoxRenderer.render(textBoxes);
		TextMaster.render();
		if(cursor != null) {
			cursorRenderer.render(color, cursor.getPosition(), fontSize);
		}
	}

	/**
	 * Clean up when the game is closed.
	 */
	protected void cleanUp() {
		guiRenderer.cleanUp();
		TextMaster.cleanUp();
		textBoxRenderer.cleanUp();
		cursorRenderer.cleanUp();
	}
}
