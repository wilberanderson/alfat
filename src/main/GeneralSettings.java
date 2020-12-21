package main;

import gui.fontMeshCreator.FontType;
import loaders.Loader;
import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector2f;
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
	//**************Colors*******************
	// Solarized dark
	public static final Vector3f base03  = new Vector3f(0.0000000f, 0.1686275f, 0.2117647f); // #002b36
	public static final Vector3f base02  = new Vector3f(0.0274510f, 0.2117647f, 0.2588235f); // #073642
	public static final Vector3f base01  = new Vector3f(0.3450980f, 0.4313725f, 0.4588235f); // #586e75
	public static final Vector3f base00  = new Vector3f(0.3960784f, 0.4823529f, 0.5137255f); // #657b83
	public static final Vector3f base0   = new Vector3f(0.5137255f, 0.5803922f, 0.5882353f); // #839496
	public static final Vector3f base1   = new Vector3f(0.5764706f, 0.6313725f, 0.6313725f); // #93a1a1
	public static final Vector3f base2   = new Vector3f(0.9333333f, 0.9098039f, 0.8352941f); // #eee8d5
	public static final Vector3f base3   = new Vector3f(0.9921569f, 0.9647059f, 0.8901961f); // #fdf6e3
	public static final Vector3f yellow  = new Vector3f(0.7098039f, 0.5372549f, 0.0000000f); // #b58900
	public static final Vector3f orange  = new Vector3f(0.7960784f, 0.2941176f, 0.0862745f); // #cb4b16
	public static final Vector3f red     = new Vector3f(0.8627451f, 0.1960784f, 0.1843137f); // #dc322f
	public static final Vector3f magenta = new Vector3f(0.8274510f, 0.2117647f, 0.5098039f); // #d33682
	public static final Vector3f violet  = new Vector3f(0.4235294f, 0.4431373f, 0.7686275f); // #6c71c4
	public static final Vector3f blue    = new Vector3f(0.1490196f, 0.5450980f, 0.8235294f); // #268bd2
	public static final Vector3f cyan    = new Vector3f(0.1647059f, 0.6313725f, 0.5960784f); // #2aa198
	public static final Vector3f green   = new Vector3f(0.5215686f, 0.6000000f, 0.0000000f); // #859900

	// Solarized light
	/*
	public static final Vector3f base3   = new Vector3f(0.0000000f, 0.1686275f, 0.2117647f); // #002b36
	public static final Vector3f base2   = new Vector3f(0.0274510f, 0.2117647f, 0.2588235f); // #073642
	public static final Vector3f base1   = new Vector3f(0.3450980f, 0.4313725f, 0.4588235f); // #586e75
	public static final Vector3f base0   = new Vector3f(0.3960784f, 0.4823529f, 0.5137255f); // #657b83
	public static final Vector3f base00  = new Vector3f(0.5137255f, 0.5803922f, 0.5882353f); // #839496
	public static final Vector3f base01  = new Vector3f(0.5764706f, 0.6313725f, 0.6313725f); // #93a1a1
	public static final Vector3f base02  = new Vector3f(0.9333333f, 0.9098039f, 0.8352941f); // #eee8d5
	public static final Vector3f base03  = new Vector3f(0.9921569f, 0.9647059f, 0.8901961f); // #fdf6e3
	public static final Vector3f yellow  = new Vector3f(0.7098039f, 0.5372549f, 0.0000000f); // #b58900
	public static final Vector3f orange  = new Vector3f(0.7960784f, 0.2941176f, 0.0862745f); // #cb4b16
	public static final Vector3f red     = new Vector3f(0.8627451f, 0.1960784f, 0.1843137f); // #dc322f
	public static final Vector3f magenta = new Vector3f(0.8274510f, 0.2117647f, 0.5098039f); // #d33682
	public static final Vector3f violet  = new Vector3f(0.4235294f, 0.4431373f, 0.7686275f); // #6c71c4
	public static final Vector3f blue    = new Vector3f(0.1490196f, 0.5450980f, 0.8235294f); // #268bd2
	public static final Vector3f cyan    = new Vector3f(0.1647059f, 0.6313725f, 0.5960784f); // #2aa198
	public static final Vector3f green   = new Vector3f(0.5215686f, 0.6000000f, 0.0000000f); // #859900
	*/


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
	public static final MyFile TERMINATOR_VERTEX = new MyFile("/rendering/terminators/terminatorVertex.glsl");
	public static final MyFile TERMINATOR_FRAGMENT = new MyFile("/rendering/terminators/terminatorFragment.glsl");

    public static int DISPLAY_WIDTH = 1280;
	public static int DISPLAY_HEIGHT = 720;
	public static final int DEFAULT_WIDTH = 1280;
	public static final int DEFAULT_HEIGHT = 720;

	public static final int DEFAULT_TAB_WIDTH = 4;
	public static final Vector3f HIGHLIGHT_COLOR = base01;//new Vector3f(0, 0.35f, 0.465f);
	public static final float TEXT_BOX_BORDER_WIDTH = 0.003f;
	public static final Vector3f HEADER_COLOR = base03;

	public static final float FONT_SIZE = 1f;
	public static final float FONT_WIDTH = 0.25f;
	public static final float FONT_EDGE = 0.4f;
	public static final float FONT_SCALING_FACTOR = 0.06f;
	public static final Vector3f TEXT_COLOR = base2;
	public static final Vector3f TEXT_BOX_BACKGROUND_COLOR = base03;
	public static final Vector3f TEXT_BOX_EMPHASIS_COLOR = base1;
	public static final Vector3f TEXT_BOX_BORDER_COLOR = new Vector3f(0, 0, 0);
	public static FontType FONT;
	public static void initializeFonts() {
		//TACOMA = new FontType(Loader.loadTexture(new MyFile("/res/fonts/arial/arial.png")), new MyFile("/res/fonts/arial/arial.fnt"));
		//TACOMA = new FontType(Loader.loadTexture(new MyFile("/res/fonts/courierNew/courierNew.png")), new MyFile("/res/fonts/courierNew/courierNew.fnt"));
		FONT = new FontType(Loader.loadTexture(new MyFile("/res/fonts/consolas/consolas.png")), new MyFile("/res/fonts/consolas/consolas.fnt"));

	}

	public static final Vector3f CURSOR_COLOR = new Vector3f(1, 1, 1);

	public static final Vector3f FLOWCHART_LINE_COLOR = new Vector3f(1,1,1);

	public static final float TEXT_BUTTON_PADDING = 0.005f;
	public static final Vector3f TEXT_BUTTON_BACKGROUND_COLOR = new Vector3f(.05f,.05f,.05f);

	public static String FILE_PATH;

	public static final Vector3f LINE_NUMBER_BACKGROUND_COLOR = base00;
	public static final Vector3f LINE_NUMBER_COLOR = base1;
	public static String SYNTAX_PATH = "CodeSyntax/LC3.json";
	public static String TEMP_DIR = "temp";


	public static final float MIN_ZOOM = 0.1f;

	public static Matrix2f ASPECT_RATIO = new Matrix2f();

	public static void updateAspectRatio(int width, int height){
		ASPECT_RATIO.m00 = (float)DEFAULT_WIDTH/width;
		ASPECT_RATIO.m11 = (float)DEFAULT_HEIGHT/height;
		ASPECT_RATIO.m10 = 0;
		ASPECT_RATIO.m01 = 0;
		DISPLAY_WIDTH = width;
		DISPLAY_HEIGHT = height;
	}

	public Matrix2f getAspectRatio(){
		return ASPECT_RATIO;
	}

	public static final float FLOWCHART_PAD_TOP = .2f;
	public static final float FLOWCHART_PAD_LEFT = FLOWCHART_PAD_TOP*(DEFAULT_WIDTH/DEFAULT_HEIGHT);	//left padding

	public static final float LINE_OFFSET = .05f;
	public static Matrix3f SCREENSHOT_TRANSLATION;
	public static Vector2f SCREENSHOT_SIZE;
	public static boolean SCREENSHOT_IN_PROGRESS = false;

	//*******************************************Text line parameters************************
	public static final Vector3f labelColor = new Vector3f(0.8f, 0.8f, 0.8f);
	public static final Vector3f commandColor = blue;
	public static final Vector3f registerColor = green;
	public static final Vector3f immediateColor = yellow;
	public static final Vector3f commentColor = base01;
	public static final Vector3f errorColor = red;
	public static final Vector3f defaultColor = new Vector3f(1,1,1);
	public static final Vector3f branchColor = violet;
	public static final int CHARACTERS_PER_SPACE = 4;

}
