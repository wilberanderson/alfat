package gui;

import main.GeneralSettings;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;
import java.util.prefs.Preferences;
/**
 * This class gets and sets java preferences for the ALFAT user preferences.
 * TODO: Add more settings, create XML or JSON default loader...
 * */
public class UserPreferences {
    //Java Class Preferences
    private Preferences userPref;

    //User Preferences node path
    private String userPreferencesPath = "com/alfat/userpref";

    //------------------------------------------------------------------------------
    //User preferences key name & Default fallback values.
    //NOTE: Java Class preferences require a fallback value


    private String keySyntaxPath = "syntaxpath";
    private String fbValuesyntaxPath = GeneralSettings.SYNTAX_PATH;

    private String keyTempfilepath = "tempfilepath";
    private String fbValueTempfilepath = GeneralSettings.TEMP_DIR;

    private String keyTempfileLimit = "tempfilelimit";
    private int fbValue = 5;

    private String keyPreferredFiletype = "preferredfiletype";
    private String fbValuePreferredFiletype = "asm;txt;asm,txt";

    private String keyAutoGenFlowchart = "openautogenflowchart";
    private Boolean fbValueAutoGenFlowchart = false;

    private String keyOpenSplitScreen = "opensplitscreen";
    private Boolean fbValueOpenSplitScreen = true;

    private String keyOpenFullScreen = "openfullscreen";
    private int fbValueOpenfullScreen = 0; //0 = false, 0 > open to editor, 0 < open to flowchart

    //Background color
    private String keyBGColor_RED = "bgcolorred";
    private float fbValueBGColor_RED = GeneralSettings.base02.x;


    private String keyBGColor_GREEN = "bgcolorgreen";
    private float fbValueBGColor_GREEN = GeneralSettings.base02.y;


    private String keyBGColor_BLUE = "bgcolorblue";
    private float fbValueBGColor_BLUE = GeneralSettings.base02.z;

    //Flowchart background color
    private String keyFlowchartBoxBGColor_RED = "fcbbgcolorred";
    private float fbValueFlowchartBoxBGColor_RED = GeneralSettings.TEXT_BOX_BACKGROUND_COLOR.x;


    private String keyFlowchartBoxBGColor_GREEN = "fcbbgcolorgreen";
    private float fbValueFlowchartBoxBGColor_GREEN = GeneralSettings.TEXT_BOX_BACKGROUND_COLOR.y;


    private String keyFlowchartBoxBGColor_BLUE = "fcbbgcolorblue";
    private float fbValueFlowchartBoxBGColor_BLUE = GeneralSettings.TEXT_BOX_BACKGROUND_COLOR.z;

    //Flowchart highlight color
    private String keyFlowchartBoxHLColor_RED = "fcbhlcolorred";
    private float fbValueFlowchartBoxHLColor_RED = GeneralSettings.TEXT_COLOR.x;

    private String keyFlowchartBoxHLColor_GREEN = "fcbhlcolorgreen";
    private float fbValueFlowchartBoxHLColor_GREEN = GeneralSettings.TEXT_COLOR.y;

    private String keyFlowchartBoxHLColor_BLUE = "fcbhlcolorblue";
    private float fbValueFlowchartBoxHLColor_BLUE = GeneralSettings.TEXT_COLOR.z;


    //Flowchart line number background color
    private String keyFlowchartBoxLineNumberBGColor_RED = "fcblnbgcolorred";
    private float fbValueFlowchartBoxLineNumberBGColor_RED = GeneralSettings.LINE_NUMBER_BACKGROUND_COLOR.x;

    private String keyFlowchartBoxLineNumberBGColor_GREEN = "fcblnbgcolorgreen";
    private float fbValueFlowchartBoxLineNumberColor_GREEN = GeneralSettings.LINE_NUMBER_BACKGROUND_COLOR.y;

    private String keyFlowchartBoxLineNumberBGColor_BLUE = "fcblnbgcolorblue";
    private float fbValueFlowchartBoxLineNumberColor_BLUE = GeneralSettings.LINE_NUMBER_BACKGROUND_COLOR.z;


    //Text editor background color
    private String keyTexteditorBGcolor_RED = "tebgcred";
    private float fbValueTexteditorBGcolor_RED = GeneralSettings.TEXT_BOX_BACKGROUND_COLOR.x;


    private String keyTexteditorBGcolor_GREEN = "tebgcgreen";
    private float fbValueTexteditorBGcolor_GREEN = GeneralSettings.TEXT_BOX_BACKGROUND_COLOR.y;


    private String keyTexteditorBGcolor_BLUE = "tebgcblue";
    private float fbValueTexteditorBGcolor_BLUE = GeneralSettings.TEXT_BOX_BACKGROUND_COLOR.z;


    //Text editor line number background color
    private String keyTexteditorLinenumberBGcolor_RED = "telnbgcred";
    private float fbValueTexteditorLinenumberBGcolor_RED = GeneralSettings.LINE_NUMBER_BACKGROUND_COLOR.x;


    private String keyTexteditorLinenumberBGcolor_GREEN = "telnbgcgreen";
    private float fbValueTexteditorLinenumberBGcolor_GREEN = GeneralSettings.LINE_NUMBER_BACKGROUND_COLOR.y;


    private String keyTexteditorLinenumberBGcolor_BLUE = "telnbgcblue";
    private float fbValueTexteditorLinenumberBGcolor_BLUE = GeneralSettings.LINE_NUMBER_BACKGROUND_COLOR.z;


    //Header color
    private String keyHeaderColor_RED = "headercolorred";
    private float fbValueHeadercolor_RED = GeneralSettings.HEADER_COLOR.x;


    private String keyHeaderColor_GREEN = "headercolorgreen";
    private float fbValueHeadercolor_GREEN = GeneralSettings.HEADER_COLOR.y;


    private String keyHeaderColor_BLUE = "headercolorblue";
    private float fbValueHeadercolor_BLUE = GeneralSettings.HEADER_COLOR.z;


    //Menu Button background colors
    private String keyMenuBtnBGcolor_RED = "menubtnbgcolorred";
    private float fbValueMenuBtnBGcolor_RED = GeneralSettings.TEXT_BUTTON_BACKGROUND_COLOR.x;


    private String keyMenuBtnBGcolor_GREEN = "menubtnbgcolorgreen";
    private float fbValueMenuBtnBGcolor_GREEN = GeneralSettings.TEXT_BUTTON_BACKGROUND_COLOR.y;


    private String keyMenuBtnBGcolor_BLUE = "";
    private float fbValueMenuBtnBGcolor_BLUE = GeneralSettings.TEXT_BUTTON_BACKGROUND_COLOR.z;

    //Menu button highlight colors
    private String KeyMenuBtnHLColor_RED = "menubtnhlcolorred";
    private float  fbValueMenuBtnHLColor_RED = GeneralSettings.HIGHLIGHT_COLOR.x;

    private String KeyMenuBtnHLColor_GREEN = "menubtnhlcolorgreen";
    private float  fbValueMenuBtnHLColor_GREEN = GeneralSettings.HIGHLIGHT_COLOR.y;

    private String KeyMenuBtnHLColor_BLUE = "menubtnhlcolorblue";
    private float  fbValueMenuBtnHLColor_BLUE = GeneralSettings.HIGHLIGHT_COLOR.z;








    /**Returns true if os is windows*/
    public Boolean isOSWindows() {
        Boolean result = false;
        String OS = System.getProperty("os.name").toLowerCase();
        if(OS.contains("win")) {
            result = true;
        }
        return result;
    }


    /**Returns true if os is GNU/Linux*/
    public Boolean isOSLinux() {
        Boolean result = false;
        String OS = System.getProperty("os.name").toLowerCase();
        if(OS.contains("nix") || OS.contains("nux") || OS.contains("aix")) {
            result = true;
        }
        return result;
    }

    /**Returns true if os is mac*/
    public Boolean isOSMac() {
        Boolean result = false;
        String OS = System.getProperty("os.name").toLowerCase();
        if(OS.contains("mac")) {
            result = true;
        }
        return result;
    }


    /**
     * Opens / creates user preferences node
     * */
    public UserPreferences() {
        userPref = Preferences.userRoot().node(userPreferencesPath);
    }
    /**
     * Opens / creates user preferences node based on provided string
     * */
    public UserPreferences(String userPreferencesPath) {
        this.userPreferencesPath = userPreferencesPath;
        userPref = Preferences.userRoot().node(userPreferencesPath);
    }


    public void setSyntaxPath(String dir) {
        userPref.put(keySyntaxPath, dir);
    }

    public String getSyntaxPath() {
        return userPref.get(keySyntaxPath, GeneralSettings.SYNTAX_PATH);
    }

    /**
     * Set the temp file directory path
     * */
    public void setUserTempFileDirPath(String dir) {
        userPref.put(keyTempfilepath, dir);
    }

    /**
     * Returns a string of the temp file directory path
     * */
    public String getUserTempFileDirPath() {
        //Return the user set path. If it's not set it defaults the the temp dir
        return userPref.get(keyTempfilepath, GeneralSettings.TEMP_DIR);
    }

    /**
     * Set the the limit on number of temp files to be stored.
     * */
    public void setTempFileLimit(int limit) {
        userPref.putInt(keyTempfileLimit,limit);
    }

    /**
     * Return the int limit of number of temp files to be stored.
     * */
    public int getTempFileLimit() {
        return userPref.getInt(keyTempfileLimit, fbValue);
    }


    /**
     * Removes temp file directory path attribute
     * */
    public void RemoveUserTempFileDirPath() {
        removeID(keyTempfilepath);
    }

    /**
     * Set the preferred file type
     * file type must match the file filter format e.g. "type1,type2;type1"
     * or "type1;type1,type2". This function DOES not check.
     * */
    public void setPreferredFileType(String fileType) {
        userPref.put(keyPreferredFiletype, fileType);
    }

    /**
     * Returns a string for the current preferred file type
     * */
    public String getPreferredFiletype() {
        return userPref.get(keyPreferredFiletype, fbValuePreferredFiletype);
    }

    /**Remove preferred file type attribute*/
    public void removePreferredFiletype() {
        userPref.remove(keyPreferredFiletype);
    }

    /**Set whether flowchart is auto generated when file is opened*/
    public void setAutoGenFlowchart(Boolean tof) {
        userPref.putBoolean(keyAutoGenFlowchart,tof);
    }

    /**Return boolean true or false whether is auto generated when file is opened*/
    public boolean getAutoGenFlowchart() {
        return userPref.getBoolean(keyAutoGenFlowchart, fbValueAutoGenFlowchart);
    }

    /**Removes auto generate flowchart when file is opened attribute*/
    public void removeAutoGenFlowchart() {
        userPref.remove(keyAutoGenFlowchart);
    }

    /**Set whether split screen is started when file is opened*/
    public void setSplitScreen(Boolean tof) {
        userPref.putBoolean(keyOpenSplitScreen, tof);
    }

    /**Return boolean true or false whether to set split when file is opened*/
    public Boolean getSplitScreen() {
        return userPref.getBoolean(keyOpenSplitScreen, fbValueOpenSplitScreen);
    }
    /**Remove split screen attribute*/
    public void removeSplitScreen() {
        userPref.remove(keyOpenSplitScreen);
    }

    /**Set whether to open in full screen
     * NOTE: 0 = false, 0 > to editor, 0 < to flowchart */
    public void setFullscreen(int value) {
        userPref.putInt(keyOpenFullScreen, value);
    }
    /**Return whether to open in full screen.
     * NOTE: 0 = false, 0 > to editor, 0 < to flowchart*/
    public int getFullscreen() {
        return userPref.getInt(keyOpenFullScreen, fbValueOpenfullScreen);
    }

    /**Remove full screen attribute*/
    public void removeFullScreen() {
        userPref.remove(keyOpenFullScreen);
    }


    //Color Settings

    //Background color
    public void setBackgroundColor(Color bgColor) {
        userPref.putFloat(keyBGColor_RED, bgColor.getRed()/255f);
        userPref.putFloat(keyBGColor_GREEN, bgColor.getGreen()/255f);
        userPref.putFloat(keyBGColor_BLUE, bgColor.getBlue()/255f);
    }

    public void setBackgroundColor3f(Vector3f bgColor) {
        userPref.putFloat(keyBGColor_RED, bgColor.x);
        userPref.putFloat(keyBGColor_GREEN, bgColor.y);
        userPref.putFloat(keyBGColor_BLUE, bgColor.z);
    }

    public Vector3f getBackgroundColor3f() {
        Vector3f bgColor = new Vector3f();
        bgColor.setX(userPref.getFloat(keyBGColor_RED, fbValueBGColor_RED));
        bgColor.setY(userPref.getFloat(keyBGColor_GREEN, fbValueBGColor_GREEN));
        bgColor.setZ(userPref.getFloat(keyBGColor_BLUE, fbValueBGColor_BLUE));
        return bgColor;
    }

    public Color getBackgroundColor() {
        Color bgColor = new Color(
                userPref.getFloat(keyBGColor_RED, fbValueBGColor_RED),
                userPref.getFloat(keyBGColor_GREEN, fbValueBGColor_GREEN),
                userPref.getFloat(keyBGColor_BLUE, fbValueBGColor_BLUE)
                );
        return bgColor;
    }


    //Flowchart box color background
    public void setFlowchartBoxbackgroundColor(Color bgColor) {
        userPref.putFloat(keyFlowchartBoxBGColor_RED, bgColor.getRed()/255f);
        userPref.putFloat(keyFlowchartBoxBGColor_GREEN, bgColor.getGreen()/255f);
        userPref.putFloat(keyFlowchartBoxBGColor_BLUE, bgColor.getBlue()/255f);
    }

    public void setFlowchartBoxbackgroundColor3f(Vector3f bgColor) {
        userPref.putFloat(keyFlowchartBoxBGColor_RED, bgColor.x);
        userPref.putFloat(keyFlowchartBoxBGColor_GREEN, bgColor.y);
        userPref.putFloat(keyFlowchartBoxBGColor_BLUE, bgColor.z);
    }

    public Vector3f getFlowchartBoxbackgroundColor3f() {
        Vector3f bgColor = new Vector3f();
        bgColor.setX(userPref.getFloat(keyFlowchartBoxBGColor_RED, fbValueFlowchartBoxBGColor_RED));
        bgColor.setY(userPref.getFloat(keyFlowchartBoxBGColor_GREEN, fbValueFlowchartBoxBGColor_GREEN));
        bgColor.setZ(userPref.getFloat(keyFlowchartBoxBGColor_BLUE, fbValueFlowchartBoxBGColor_BLUE));
        return bgColor;
    }

    public Color getFlowchartBoxackgroundColor() {
        Color bgColor = new Color(
                userPref.getFloat(keyFlowchartBoxBGColor_RED, fbValueFlowchartBoxBGColor_RED),
                userPref.getFloat(keyFlowchartBoxBGColor_GREEN, fbValueFlowchartBoxBGColor_GREEN),
                userPref.getFloat(keyFlowchartBoxBGColor_BLUE, fbValueFlowchartBoxBGColor_BLUE)
        );
        return bgColor;
    }

    //Flowchart highlight background color
    public void setFlowchartBoxHighlightColor(Color bgColor) {
        userPref.putFloat(keyFlowchartBoxHLColor_RED, bgColor.getRed()/255f);
        userPref.putFloat(keyFlowchartBoxHLColor_GREEN, bgColor.getGreen()/255f);
        userPref.putFloat(keyFlowchartBoxHLColor_BLUE, bgColor.getBlue()/255f);
    }

    public void setFlowchartBoxHighlightColor3f(Vector3f bgColor) {
        userPref.putFloat(keyFlowchartBoxHLColor_RED, bgColor.x);
        userPref.putFloat(keyFlowchartBoxHLColor_GREEN, bgColor.y);
        userPref.putFloat(keyFlowchartBoxHLColor_BLUE, bgColor.z);
    }

    public Vector3f getFlowchartBoxHighlightColor3f() {
        Vector3f bgColor = new Vector3f();
        bgColor.setX(userPref.getFloat(keyFlowchartBoxHLColor_RED, fbValueFlowchartBoxHLColor_RED));
        bgColor.setY(userPref.getFloat(keyFlowchartBoxHLColor_GREEN, fbValueFlowchartBoxHLColor_GREEN));
        bgColor.setZ(userPref.getFloat(keyFlowchartBoxHLColor_BLUE, fbValueFlowchartBoxHLColor_BLUE));
        return bgColor;
    }

    public Color getFlowchartBoxHighlightColor() {
        Color bgColor = new Color(
                userPref.getFloat(keyFlowchartBoxHLColor_RED, fbValueFlowchartBoxHLColor_RED),
                userPref.getFloat(keyFlowchartBoxHLColor_GREEN, fbValueFlowchartBoxHLColor_GREEN),
                userPref.getFloat(keyFlowchartBoxHLColor_BLUE, fbValueFlowchartBoxHLColor_BLUE)
        );
        return bgColor;
    }




    //Flowchart box line number background color
    public void setFlowchartBoxlinenumberBGColor(Color bgColor) {
        userPref.putFloat(keyFlowchartBoxLineNumberBGColor_RED, bgColor.getRed()/255f);
        userPref.putFloat(keyFlowchartBoxLineNumberBGColor_GREEN, bgColor.getGreen()/255f);
        userPref.putFloat(keyFlowchartBoxLineNumberBGColor_BLUE, bgColor.getBlue()/255f);
    }

    public void setFlowchartBoxlinenumberBGColor3f(Vector3f bgColor) {
        userPref.putFloat(keyFlowchartBoxLineNumberBGColor_RED, bgColor.x);
        userPref.putFloat(keyFlowchartBoxLineNumberBGColor_GREEN, bgColor.y);
        userPref.putFloat(keyFlowchartBoxLineNumberBGColor_BLUE, bgColor.z);
    }

    public Vector3f getFlowchartBoxlinenumberBGColor3f() {
        Vector3f bgColor = new Vector3f();
        bgColor.setX(userPref.getFloat(keyFlowchartBoxLineNumberBGColor_RED, fbValueFlowchartBoxLineNumberBGColor_RED));
        bgColor.setY(userPref.getFloat(keyFlowchartBoxLineNumberBGColor_GREEN, fbValueFlowchartBoxLineNumberColor_GREEN));
        bgColor.setZ(userPref.getFloat(keyFlowchartBoxLineNumberBGColor_BLUE, fbValueFlowchartBoxLineNumberColor_BLUE));
        return bgColor;
    }

    public Color getFlowchartBoxlinenumberBGColor() {
        Color bgColor = new Color(
                userPref.getFloat(keyFlowchartBoxLineNumberBGColor_RED, fbValueFlowchartBoxLineNumberBGColor_RED),
                userPref.getFloat(keyFlowchartBoxLineNumberBGColor_GREEN, fbValueFlowchartBoxLineNumberColor_GREEN),
                userPref.getFloat(keyFlowchartBoxLineNumberBGColor_BLUE, fbValueFlowchartBoxLineNumberColor_BLUE)
        );
        return bgColor;
    }


    //Text editor background color
    public void setTexteditorBGColor(Color bgColor) {
        userPref.putFloat(keyTexteditorBGcolor_RED, bgColor.getRed()/255f);
        userPref.putFloat(keyTexteditorBGcolor_GREEN, bgColor.getGreen()/255f);
        userPref.putFloat(keyTexteditorBGcolor_BLUE, bgColor.getBlue()/255f);
    }

    public void setTexteditorBGColor3f(Vector3f bgColor) {
        userPref.putFloat(keyTexteditorBGcolor_RED, bgColor.x);
        userPref.putFloat(keyTexteditorBGcolor_GREEN, bgColor.y);
        userPref.putFloat(keyTexteditorBGcolor_BLUE, bgColor.z);
    }

    public Vector3f getTexteditorBGColor3f() {
        Vector3f bgColor = new Vector3f();
        bgColor.setX(userPref.getFloat(keyTexteditorBGcolor_RED, fbValueTexteditorBGcolor_RED));
        bgColor.setY(userPref.getFloat(keyTexteditorBGcolor_GREEN, fbValueTexteditorBGcolor_GREEN));
        bgColor.setZ(userPref.getFloat(keyTexteditorBGcolor_BLUE, fbValueTexteditorBGcolor_BLUE));
        return bgColor;
    }

    public Color getTexteditorBGColor() {
        Color bgColor = new Color(
                userPref.getFloat(keyTexteditorBGcolor_RED, fbValueTexteditorBGcolor_RED),
                userPref.getFloat(keyTexteditorBGcolor_GREEN, fbValueTexteditorBGcolor_GREEN),
                userPref.getFloat(keyTexteditorBGcolor_BLUE, fbValueTexteditorBGcolor_BLUE)
        );
        return bgColor;
    }

    //Text editor line number background color
    public void setTexteditorLinenumberBGColor(Color bgColor) {
        userPref.putFloat(keyTexteditorLinenumberBGcolor_RED, bgColor.getRed()/255f);
        userPref.putFloat(keyTexteditorLinenumberBGcolor_GREEN, bgColor.getGreen()/255f);
        userPref.putFloat(keyTexteditorLinenumberBGcolor_BLUE, bgColor.getBlue()/255f);
    }

    public void setTexteditorLinenumberBGColor3f(Vector3f bgColor) {
        userPref.putFloat(keyTexteditorLinenumberBGcolor_RED, bgColor.x);
        userPref.putFloat(keyTexteditorLinenumberBGcolor_GREEN, bgColor.y);
        userPref.putFloat(keyTexteditorLinenumberBGcolor_BLUE, bgColor.z);
    }

    public Vector3f getTexteditorLinenumberBGColor3f() {
        Vector3f bgColor = new Vector3f();
        bgColor.setX(userPref.getFloat(keyTexteditorLinenumberBGcolor_RED, fbValueTexteditorLinenumberBGcolor_RED));
        bgColor.setY(userPref.getFloat(keyTexteditorLinenumberBGcolor_GREEN, fbValueTexteditorLinenumberBGcolor_GREEN));
        bgColor.setZ(userPref.getFloat(keyTexteditorLinenumberBGcolor_BLUE, fbValueTexteditorLinenumberBGcolor_BLUE));
        return bgColor;
    }

    public Color getTexteditorLinenumberBGColor() {
        Color bgColor = new Color(
                userPref.getFloat(keyTexteditorLinenumberBGcolor_RED, fbValueTexteditorLinenumberBGcolor_RED),
                userPref.getFloat(keyTexteditorLinenumberBGcolor_GREEN, fbValueTexteditorLinenumberBGcolor_GREEN),
                userPref.getFloat(keyTexteditorLinenumberBGcolor_BLUE, fbValueTexteditorLinenumberBGcolor_BLUE)
        );
        return bgColor;
    }


    //Header Color

    public void setHeaderColor(Color bgColor) {
        userPref.putFloat(keyHeaderColor_RED, bgColor.getRed()/255f);
        userPref.putFloat(keyHeaderColor_GREEN, bgColor.getGreen()/255f);
        userPref.putFloat(keyHeaderColor_BLUE, bgColor.getBlue()/255f);
    }

    public void setHeaderColor3f(Vector3f bgColor) {
        userPref.putFloat(keyHeaderColor_RED, bgColor.x);
        userPref.putFloat(keyHeaderColor_GREEN, bgColor.y);
        userPref.putFloat(keyHeaderColor_BLUE, bgColor.z);
    }

    public Vector3f getHeaderColor3f() {
        Vector3f bgColor = new Vector3f();
        bgColor.setX(userPref.getFloat(keyHeaderColor_RED, fbValueHeadercolor_RED));
        bgColor.setY(userPref.getFloat(keyHeaderColor_GREEN, fbValueHeadercolor_GREEN));
        bgColor.setZ(userPref.getFloat(keyHeaderColor_BLUE, fbValueHeadercolor_BLUE));
        return bgColor;
    }

    public Color getHeaderColor() {
        Color bgColor = new Color(
                userPref.getFloat(keyHeaderColor_RED, fbValueHeadercolor_RED),
                userPref.getFloat(keyHeaderColor_GREEN, fbValueHeadercolor_GREEN),
                userPref.getFloat(keyHeaderColor_BLUE, fbValueHeadercolor_BLUE)
        );
        return bgColor;
    }



    //Menu Item background color
    public void setMenuBtnBGColor(Color bgColor) {
        userPref.putFloat(keyMenuBtnBGcolor_RED, bgColor.getRed()/255f);
        userPref.putFloat(keyMenuBtnBGcolor_GREEN, bgColor.getGreen()/255f);
        userPref.putFloat(keyMenuBtnBGcolor_BLUE, bgColor.getBlue()/255f);
    }

    public void setMenuBtnBGColor3f(Vector3f bgColor) {
        userPref.putFloat(keyMenuBtnBGcolor_RED, bgColor.x);
        userPref.putFloat(keyMenuBtnBGcolor_GREEN, bgColor.y);
        userPref.putFloat(keyMenuBtnBGcolor_BLUE, bgColor.z);
    }
    public Vector3f getMenuBtnBGColor3f() {
        Vector3f bgColor = new Vector3f();
        bgColor.setX(userPref.getFloat(keyMenuBtnBGcolor_RED, fbValueMenuBtnBGcolor_RED));
        bgColor.setY(userPref.getFloat(keyMenuBtnBGcolor_GREEN, fbValueMenuBtnBGcolor_GREEN));
        bgColor.setZ(userPref.getFloat(keyMenuBtnBGcolor_BLUE, fbValueMenuBtnBGcolor_BLUE));
        return bgColor;
    }

    public Color getMenuBtnBGColor() {
        Color bgColor = new Color(
                userPref.getFloat(keyMenuBtnBGcolor_RED, fbValueMenuBtnBGcolor_RED),
                userPref.getFloat(keyMenuBtnBGcolor_GREEN, fbValueMenuBtnBGcolor_GREEN),
                userPref.getFloat(keyMenuBtnBGcolor_BLUE, fbValueMenuBtnBGcolor_BLUE)
        );
        return bgColor;
    }

    //Menu Item background color highlight
    public void setMenuBtnHLColor(Color bgColor) {
        userPref.putFloat(KeyMenuBtnHLColor_RED, bgColor.getRed()/255f);
        userPref.putFloat(KeyMenuBtnHLColor_GREEN, bgColor.getGreen()/255f);
        userPref.putFloat(KeyMenuBtnHLColor_BLUE, bgColor.getBlue()/255f);
    }

    public void setMenuBtnHLColor3f(Vector3f bgColor) {
        userPref.putFloat(KeyMenuBtnHLColor_RED, bgColor.x);
        userPref.putFloat(KeyMenuBtnHLColor_GREEN, bgColor.y);
        userPref.putFloat(KeyMenuBtnHLColor_BLUE, bgColor.z);
    }
    public Vector3f getMenuBtnHLColor3f() {
        Vector3f bgColor = new Vector3f();
        bgColor.setX(userPref.getFloat(KeyMenuBtnHLColor_RED, fbValueMenuBtnHLColor_RED));
        bgColor.setY(userPref.getFloat(KeyMenuBtnHLColor_GREEN, fbValueMenuBtnHLColor_GREEN));
        bgColor.setZ(userPref.getFloat(KeyMenuBtnHLColor_BLUE, fbValueMenuBtnHLColor_BLUE));
        return bgColor;
    }

    public Color getMenuBtnHLColor() {
        Color bgColor = new Color(
                userPref.getFloat(KeyMenuBtnHLColor_RED, fbValueMenuBtnHLColor_RED),
                userPref.getFloat(KeyMenuBtnHLColor_GREEN, fbValueMenuBtnHLColor_GREEN),
                userPref.getFloat(KeyMenuBtnHLColor_BLUE, fbValueMenuBtnHLColor_BLUE)
        );
        return bgColor;
    }


    /**Removes ALL attributes (hard reset). Next time any get is called is will return fall back values*/
    public void resetAll() {
        removeAutoGenFlowchart();
        removeFullScreen();
        removeSplitScreen();
        removePreferredFiletype();
        RemoveUserTempFileDirPath();
    }

    /**
     * Removes a preferences attribute
     * */
    private void removeID(String key) {
        userPref.remove(key);
    }

    /**
     * Returns a string of the user preferences path node
     * */
    public String getUserPreferencesPath() {
        return userPreferencesPath;
    }

    /**
     * Set a string to the user preferences path node
     * */
    public void setUserPreferencesPath(String userPreferencesPath) {
        this.userPreferencesPath = userPreferencesPath;
    }

}




