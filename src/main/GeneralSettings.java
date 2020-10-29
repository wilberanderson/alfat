package main;

import fontMeshCreator.FontType;
import loaders.Loader;
import org.lwjgl.util.vector.Vector3f;
import utils.MyFile;

//import utils.MyFile;

/**
 * Just some configs. File locations mostly.
 * 
 * @author Karl
 *
 */
public class GeneralSettings {
	//**************Icons*******************
	public static final MyFile ICON_LOCATION = new MyFile("/res/icon/");

	//*********************Shaders****************************
	public static final MyFile GUI_VERTEX = new MyFile("/rendering/guis/guiVertexShader.glsl");
	public static final MyFile GUI_FRAGMENT = new MyFile("/rendering/guis/guiFragmentShader.glsl");
	public static final MyFile FONT_VERTEX = new MyFile("/rendering/text/fontVertex.glsl");
	public static final MyFile FONT_FRAGMENT = new MyFile("/rendering/text/fontFragment.glsl");
	public static final MyFile FILLED_BOX_VERTEX = new MyFile("/rendering/filledBox/filledBoxVertexShader.glsl");
	public static final MyFile FILLED_BOX_FRAGMENT = new MyFile("/rendering/filledBox/filledBoxFragmentShader.glsl");
	public static final MyFile CURSOR_VERTEX = new MyFile("/rendering/cursor/cursorVertex.glsl");
	public static final MyFile CURSOR_FRAGMENT = new MyFile("/rendering/cursor/cursorFragment.glsl");
	public static final MyFile SELECTION_VERTEX = new MyFile("/rendering/selection/vertex.glsl");
	public static final MyFile SELECTION_FRAGMENT = new MyFile("/rendering/selection/fragment.glsl");
	public static final MyFile FLOWCHART_VERTEX = new MyFile("/rendering/flowchartLine/flowchartLineVertex.glsl");
	public static final MyFile FLOWCHART_FRAGMENT = new MyFile("/rendering/flowchartLine/flowchartLineFragment.glsl");


    public static int DISPLAY_WIDTH = 1280;
	public static int DISPLAY_HEIGHT = 720;


    public static float delta;
	public static long lastFrameTime = System.currentTimeMillis();

	public static void update() {
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime) / 1000f;
		lastFrameTime = currentFrameTime;
	}

	private static long getCurrentTime() {
		return System.currentTimeMillis();
	}

	public static float getFrameTime(){
		return delta;
	}

	public static final float RAPID_MODE_DURATION = 1.0f;
	public static final int RAPID_MODE_FREQUENCY = 3;
	public static final int DEFAULT_TAB_WIDTH = 4;
	public static final Vector3f HIGHLIGHT_COLOR = new Vector3f(0, 0.35f, 0.465f);
	public static final float TEXT_BOX_BORDER_WIDTH = 0.003f;
	public static final Vector3f HEADER_COLOR = new Vector3f(0.25f, 0.25f, 0.25f);

	public static final float FONT_SIZE = 1f;
	public static final float FONT_WIDTH = 0.25f;
	public static final float FONT_EDGE = 0.4f;
	public static final float FONT_SCALING_FACTOR = 0.06f;
	public static FontType TACOMA;
	public static void initializeFonts() {
		TACOMA = new FontType(Loader.loadTexture(new MyFile("/res/fonts/arial/arial.png")), new MyFile("/res/fonts/arial/arial.fnt"));
	}

	public static final Vector3f CURSOR_COLOR = new Vector3f(1, 1, 1);

	public static final Vector3f FLOWCHART_LINE_COLOR = new Vector3f(1,1,1);

	public static final float TEXT_BUTTON_PADDING = 0.005f;
}
