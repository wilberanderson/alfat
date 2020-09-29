package main;

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
	public static final MyFile TEXT_BOX_VERTEX = new MyFile("/rendering/text/boxVertex.glsl");
	public static final MyFile TEXT_BOX_FRAGMENT = new MyFile("/rendering/text/boxFragment.glsl");


    public static final int DISPLAY_WIDTH = 1280;
	public static final int DISPLAY_HEIGHT = 720;


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

}
