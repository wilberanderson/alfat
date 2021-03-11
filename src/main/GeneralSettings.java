package main;

import gui.UserPreferences;
import gui.fontMeshCreator.FontType;
import loaders.Loader;
import org.lwjgl.util.vector.Matrix2f;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import utils.MyFile;

/**
 * Contains general settings for various classes. Rather than hard coding a value a setting should be added to this file to make it easier to find the value and change it in the future.
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

	public static final Vector3f base3_light   = new Vector3f(0.0000000f, 0.1686275f, 0.2117647f); // #002b36
	public static final Vector3f base2_light   = new Vector3f(0.0274510f, 0.2117647f, 0.2588235f); // #073642
	public static final Vector3f base1_light   = new Vector3f(0.3450980f, 0.4313725f, 0.4588235f); // #586e75
	public static final Vector3f base0_light   = new Vector3f(0.3960784f, 0.4823529f, 0.5137255f); // #657b83
	public static final Vector3f base00_light  = new Vector3f(0.5137255f, 0.5803922f, 0.5882353f); // #839496
	public static final Vector3f base01_light  = new Vector3f(0.5764706f, 0.6313725f, 0.6313725f); // #93a1a1
	public static final Vector3f base02_light  = new Vector3f(0.9333333f, 0.9098039f, 0.8352941f); // #eee8d5
	public static final Vector3f base03_light  = new Vector3f(0.9921569f, 0.9647059f, 0.8901961f); // #fdf6e3
	public static final Vector3f yellow_light  = new Vector3f(0.7098039f, 0.5372549f, 0.0000000f); // #b58900
	public static final Vector3f orange_light  = new Vector3f(0.7960784f, 0.2941176f, 0.0862745f); // #cb4b16
	public static final Vector3f red_light     = new Vector3f(0.8627451f, 0.1960784f, 0.1843137f); // #dc322f
	public static final Vector3f magenta_light = new Vector3f(0.8274510f, 0.2117647f, 0.5098039f); // #d33682
	public static final Vector3f violet_light  = new Vector3f(0.4235294f, 0.4431373f, 0.7686275f); // #6c71c4
	public static final Vector3f blue_light    = new Vector3f(0.1490196f, 0.5450980f, 0.8235294f); // #268bd2
	public static final Vector3f cyan_light    = new Vector3f(0.1647059f, 0.6313725f, 0.5960784f); // #2aa198
	public static final Vector3f green_light   = new Vector3f(0.5215686f, 0.6000000f, 0.0000000f); // #859900





	//**************Icons*******************
	public static final String ICON_LOCATION = "/res/icon/alfatlogo2.png";                                            //Stores the file location of the icon which displays in the task bar and on the window
	public static final int ICON_WIDTH = 512;                                                                   //The width of the icon in pixels
	public static final int ICON_HEIGHT = ICON_WIDTH;                                                           //The height of the icon in pixels. The icon is currently set to be a square.

	//*********************Shaders****************************
	public static final MyFile GUI_VERTEX = new MyFile("/rendering/guis/guiVertexShader.glsl");
	public static final MyFile GUI_FRAGMENT = new MyFile("/rendering/guis/guiFragmentShader.glsl");
	public static final MyFile FONT_VERTEX = new MyFile("/rendering/text/fontVertex.glsl");
	public static final MyFile FONT_FRAGMENT = new MyFile("/rendering/text/fontFragment.glsl");
	public static final MyFile TEXT_LINE_VERTEX = new MyFile("/rendering/textLines/textLineVertex.glsl");
	public static final MyFile TEXT_LINE_FRAGMENT = new MyFile("/rendering/textLines/textLineFragment.glsl");
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

	//***********************************Window settings*****************************************
	public static final int DEFAULT_WIDTH = 1280;
	public static final int DEFAULT_HEIGHT = 720;
	public static final String WINDOW_TITLE = "ALFAT";


	//*****************************Text editor settings********************************************
	public static final int DEFAULT_TAB_WIDTH = 4;                                      //The number of spaces per tab
	public static final float TEXT_BOX_BORDER_WIDTH = 0.003f;                           //The amount of space between text and the border of the text box
	public static final Vector3f TEXT_BOX_BACKGROUND_COLOR = base03;                    //The color which displays behing the text in the text box
	public static final Vector3f TEXT_BOX_EMPHASIS_COLOR = base1;                       //TODO: Use this somewhere
	public static final Vector3f TEXT_BOX_BORDER_COLOR = new Vector3f(0, 0, 0);         //The color of the border displaying around a text box
	public static final Vector3f CURSOR_COLOR = new Vector3f(1, 1, 1);                  //The color in which the cursor displays
	public static final Vector3f LINE_NUMBER_BACKGROUND_COLOR = base00;                 //The color which displays behind the line numbers in a text box
	public static final Vector3f LINE_NUMBER_COLOR = base1;                             //The text color used for line numbers

	//*****************************Flowchart settings********************************************
	public static final Vector3f FLOWCHART_LINE_COLOR = new Vector3f(1, 1, 1);                              //The color for lines connecting boxes in a flowchart
	public static final float MIN_ZOOM = 0.1f;                                                              //The smallest amount of zoom that is allowed for the flowchart
	public static final float FLOWCHART_PAD_TOP = .2f;                                                      //The vertical spacing between flowchart boxes
	public static final float FLOWCHART_PAD_LEFT = FLOWCHART_PAD_TOP * (DEFAULT_WIDTH / DEFAULT_HEIGHT);    //The horizontal spacing between flowchart boxes
	public static final float LINE_OFFSET = .05f;                                                           //The minmum distance between lines pathing in the same direction in the flowchart
	public static final float FLOWCHART_TEXT_BOX_INTERNAL_PAD_RIGHT = 0.1f;
	public static final int ARROWHEAD_RIGHT = 3;
	public static final int ARROWHEAD_LEFT = 1;
	public static final int ARROWHEAD_UP = 0;
	public static final int ARROWHEAD_DOWN = 2;
	public static final float DEFAULT_LINE_WIDTH = 1f;
	public static final float HIGHLIGHTED_LINE_WIDTH = 3f;

	public static boolean OPEN_PARTIAL_FILE = false;
	public static String PARTIAL_FILE_TAG_TARGET = "";

	//************************************************Text settings***********************************
	public static final float FONT_SIZE = 1f;                                           //The default size for rendered text, individual texts may override this value
	public static final float FONT_SCALING_FACTOR = 0.06f;                              //The amount that texts increase in size based on, cannot be modified
	public static final float FONT_HEIGHT = FONT_SIZE * FONT_SCALING_FACTOR;
	public static final float FONT_WIDTH = 0.5f;                                        //The alpha value on the distance field where a text will start falling off
	public static final float FONT_EDGE = 0.1f;                                         //The alpha value greater than FONT_WIDTH where a text will finish falling off
	public static final Vector3f TEXT_COLOR = base2;                                    //The default text color
	public static FontType FONT;                                                        //The default font which is used by the majority of the text in the application
	public static String DEFAULT_FONT_LOCATION = "/res/fonts/consolas/consolas";

	//*****************************GUI settings**************************************************
	public static final Vector3f HIGHLIGHT_COLOR = base01;                                          		//The color which a standard TextButton will turn it's background when the mouse is hovering over it
	public static final Vector3f HEADER_COLOR = base03;                                             		//The color of the header at the top of the screen
	public static final float TEXT_BUTTON_PADDING = 0.005f;                                         		//The space between the text on a TextButton and the edge of the button
	public static final Vector3f TEXT_BUTTON_BACKGROUND_COLOR = new Vector3f(.05f, .05f, .05f);     //The starting background color of a TextButton
	public static final Vector3f SCROLL_BAR_COLOR = new Vector3f(0.3f, 0.3f, 0.3f);				//The color of the scroll bars for the code window

	//*****************************Image saving parameters*************************************
	public static Matrix3f IMAGE_TRANSLATION;                              //The required translation to save the currently open flowchart centered in the screen
	public static Vector2f IMAGE_SIZE;                                     //The size of the screenshot using the OpenGL coordinate system

	//******************************File input/output fields**********************************
	public static String FILE_PATH;                                              //A string used for holding the current file path of the file open in the text editor
	public static String SYNTAX_PATH = "CodeSyntax/LC3.json";                    //The location of the default syntax at compile time
	public static boolean IS_SYNTAX_PATH_CHANGED = true;                         //Toggle to tell whether the syntax file path has been changed and needs to be updated.
	public static boolean FIXED_OR_FREE_FORM_MODE = false; 					     //Boolean to start program in free form mode if on first launch until the mode is set in the settings menu
	public static String TEMP_DIR = "temp";                                      //The directory where temporary files are saved
	public static final int TEMP_FILE_LIMIT = 5;								 //The number of temporary files to be saved at any time
	public static final String PREF_FILE_TYPE = "asm;txt;asm,txt"; 				 //The default file extensions that the file dialog opens for

	//****************************Syntax highlighting colors************************
	public static final Vector3f labelColor = new Vector3f(0.8f, 0.8f, 0.8f);   //The color of a label either at the start of the line or as a destination for a jump statement
	public static final Vector3f commandColor = blue;                                   //The color for a standard recognized command such as ADD or AND
	public static final Vector3f branchColor = violet;                                  //The color for a branch statement such as a jump or a break
	public static final Vector3f errorColor = red;                                      //The color for any unrecognized commands or values such as AD or a broken label

	public static final Vector3f registerColor = green;                                 //The color for a register such as R0 or R1
	public static final Vector3f immediateColor = yellow;                               //The color for an immediate value such as #14 or 0xE
	public static final Vector3f commentColor = base01;                                 //The color for any comments
	public static final Vector3f separatorColor = new Vector3f(1, 1, 1);          //The color for any unparsed portions of a line such as the ',' between arguments

	//**************************Resizing parameters******************************
	public static int DISPLAY_WIDTH = DEFAULT_WIDTH;                            //The current width of the display in pixels
	public static int DISPLAY_HEIGHT = DEFAULT_HEIGHT;                          //The current height of the display in pixels
	public static Matrix2f ASPECT_RATIO = new Matrix2f();                       //A two by two matrix which can be used to scale program elements based on the size of the screen relative to the default width and height

	//*******************************************User Preferences ************************
	public static UserPreferences USERPREF;
	public static Boolean MasterRendererUserPrefToggle = false;

	//************************Global identity matrices************************
	public static final Matrix2f IDENTITY2 = new Matrix2f();
	public static final Matrix3f IDENTITY3 = new Matrix3f();

	//***********************Popup related*************************************
	public static final int popupWidth = 400;
	public static final int popupHeight = 200;

	//*************************Useful methods**********************************

	/**
	 * Calculates the current OpenGL coordinate system scaling factor for a certain width and height of the window. The result will be <1, 1> if the width and height are {@value #DEFAULT_WIDTH} and {@value #DEFAULT_HEIGHT} respectively. Larger screen values will result in a smaller scale factor in that axis.
	 * @param width the width of the screen in pixels.
	 * @param height the height of the screen in pixels.
	 */
	public static void updateAspectRatio(int width, int height) {
		ASPECT_RATIO.m00 = (float) DEFAULT_WIDTH / width;
		ASPECT_RATIO.m11 = (float) DEFAULT_HEIGHT / height;
		ASPECT_RATIO.m10 = 0;
		ASPECT_RATIO.m01 = 0;
		DISPLAY_WIDTH = width;
		DISPLAY_HEIGHT = height;
	}

	/**
	 * Retrieves the current OpenGL coordinate system scaling factor. The result will be <1, 1> if the #DISPLAY_WIDTH and #DISPLAY_HEIGHT are {@value #DEFAULT_WIDTH} and {@value #DEFAULT_HEIGHT} respectively. Larger screen values will result in a smaller scale factor in that axis.
	 * @return The 2x2 matrix which will scale an element in such a way to appear right with the current screen size.
	 */
	public Matrix2f getAspectRatio() {
		return ASPECT_RATIO;
	}

	/**
	 * Loads the default font for use in rendering texts.
	 */
	public static void initializeFonts() {
		FONT = new FontType(Loader.loadTexture(new MyFile(DEFAULT_FONT_LOCATION + ".png")), new MyFile(DEFAULT_FONT_LOCATION + ".fnt"));
	}
}
