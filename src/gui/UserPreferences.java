package gui;

import dataStructures.Color3f;
import dataStructures.ColorSettings;
import main.GeneralSettings;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;
import java.util.prefs.Preferences;
/**
 * This class gets and sets java preferences for the ALFAT user preferences.
 * User preferences require a key name & a default fallback value
 * The default fallback should come from general settings
 *
 * */
public class UserPreferences {
    //*********************************************************************************************************************************************
    //Class set up variables and functions

    //Java Class Preferences
    private final Preferences userPref;

    //User Preferences node path
    private String userPreferencesPath = "com/alfat/userpref";


    /** Opens/creates user preferences node */
    public UserPreferences() {
        userPref = Preferences.userRoot().node(userPreferencesPath);
    }
    /** Open/create user preferences node with a provided path string*/
    public UserPreferences(String userPreferencesPath) {
        this.userPreferencesPath = userPreferencesPath;
        userPref = Preferences.userRoot().node(userPreferencesPath);
    }

    /** Removes a preferences attribute */
    private void removeID(String key) {
        userPref.remove(key);
    }

    /** Returns a string of the user preferences path node */
    public String getUserPreferencesPath() {
        return userPreferencesPath;
    }

    /** Set a string to the user preferences path node */
    public void setUserPreferencesPath(String userPreferencesPath) {
        this.userPreferencesPath = userPreferencesPath;
    }

    //*********************************************************************************************************************************************
    // Syntax Path Settings
    private final String keySyntaxPath = "syntaxpath";
    private final String fbValueSyntaxPath = GeneralSettings.SYNTAX_PATH;

    /**Set the code syntax path to be used*/
    public void setSyntaxPath(String dir) {
        userPref.put(keySyntaxPath, dir);
    }

    /**Get the code syntax path to be used*/
    public String getSyntaxPath() {
        return userPref.get(keySyntaxPath, fbValueSyntaxPath);
    }


    //*********************************************************************************************************************************************
    // Temporary File Path Settings
    private final String keyTempFilePath = "tempfilepath";
    private final String fbValueTempFilePath = GeneralSettings.TEMP_DIR;

    private final String keyTempFileLimit = "tempfilelimit";
    private final int fbValue = GeneralSettings.TEMP_FILE_LIMIT;

    private final String keyFixedOrFreeFromMode = "fixedorfreeformmode";
    private final boolean fbValueFixedOrFreeFromMode = GeneralSettings.FIXED_OR_FREE_FORM_MODE;

    /** Set the temp file directory path */
    public void setUserTempFileDirPath(String dir) {
        userPref.put(keyTempFilePath, dir);
    }

    /** Returns a string of the temp file directory path */
    public String getUserTempFileDirPath() { return userPref.get(keyTempFilePath, fbValueTempFilePath); }

    /** Set the the limit on number of temp files to be stored. */
    public void setTempFileLimit(int limit) {
        userPref.putInt(keyTempFileLimit,limit);
    }

    /** Return the int limit of number of temp files to be stored. */
    public int getTempFileLimit() {
        return userPref.getInt(keyTempFileLimit, fbValue);
    }

    /** Removes temp file directory path attribute */
    public void RemoveUserTempFileDirPath() {
        removeID(keyTempFilePath);
    }
    /**Set the mode for the parser to either be in fixed form (true) or free form (false) mode*/
    public void setFixedOrFreeFromMode(boolean tof) {
        userPref.putBoolean(keyFixedOrFreeFromMode, tof);
    }
    /**Get the mode for the parser to either be in fixed form (true) or free form (false) mode*/
    public boolean getFixedOrFreeFromMode() {
        return userPref.getBoolean(keyFixedOrFreeFromMode, fbValueFixedOrFreeFromMode);
    }

    //*********************************************************************************************************************************************
    // Default file extensions that file dialog opens with
    private final String keyPreferredFiletype = "preferredfiletype";
    private final String fbValuePreferredFiletype = GeneralSettings.PREF_FILE_TYPE;

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


    //*********************************************************************************************************************************************
    // Display settings
    private final String keyAutoGenFlowchart = "openautogenflowchart";
    private final Boolean fbValueAutoGenFlowchart = false;

    private final String keyOpenSplitScreen = "opensplitscreen";
    private final Boolean fbValueOpenSplitScreen = true;

    private final String keyOpenFullScreen = "openfullscreen";
    private final int fbValueOpenfullScreen = 0; //0 = false, 0 > open to editor, 0 < open to flowchart

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

    //*********************************************************************************************************************************************
    //Background color settings
    private final String keyBGColor_RED = "bgcolorred";
    private final float fbValueBGColor_RED = GeneralSettings.base02.x;

    private final String keyBGColor_GREEN = "bgcolorgreen";
    private final float fbValueBGColor_GREEN = GeneralSettings.base02.y;

    private final String keyBGColor_BLUE = "bgcolorblue";
    private final float fbValueBGColor_BLUE = GeneralSettings.base02.z;

    /**Sets the background color*/
    public void setBackgroundColor(Color bgColor) {
        userPref.putFloat(keyBGColor_RED, bgColor.getRed()/255f);
        userPref.putFloat(keyBGColor_GREEN, bgColor.getGreen()/255f);
        userPref.putFloat(keyBGColor_BLUE, bgColor.getBlue()/255f);

        System.out.println("userPref set:" + bgColor.getRed()/255f + " " + bgColor.getGreen()/255f + " " + bgColor.getBlue()/255f);

    }

    /**Sets the background color*/
    public void setBackgroundColor3f(Vector3f bgColor) {
        userPref.putFloat(keyBGColor_RED, bgColor.x);
        userPref.putFloat(keyBGColor_GREEN, bgColor.y);
        userPref.putFloat(keyBGColor_BLUE, bgColor.z);
    }

    /**Gets the background color*/
    public Vector3f getBackgroundColor3f() {
        Vector3f bgColor = new Vector3f();
        bgColor.setX(userPref.getFloat(keyBGColor_RED, fbValueBGColor_RED));
        bgColor.setY(userPref.getFloat(keyBGColor_GREEN, fbValueBGColor_GREEN));
        bgColor.setZ(userPref.getFloat(keyBGColor_BLUE, fbValueBGColor_BLUE));
        return bgColor;
    }

    /**Gets the background color*/
    public Color getBackgroundColor() {
        Color bgColor = new Color(
                userPref.getFloat(keyBGColor_RED, fbValueBGColor_RED),
                userPref.getFloat(keyBGColor_GREEN, fbValueBGColor_GREEN),
                userPref.getFloat(keyBGColor_BLUE, fbValueBGColor_BLUE)
        );
        return bgColor;
    }


    //*********************************************************************************************************************************************
    //Flowchart background color
    private final String keyFlowchartBoxBGColor_RED = "fcbbgcolorred";
    private final float fbValueFlowchartBoxBGColor_RED = GeneralSettings.TEXT_BOX_BACKGROUND_COLOR.x;

    private final String keyFlowchartBoxBGColor_GREEN = "fcbbgcolorgreen";
    private final float fbValueFlowchartBoxBGColor_GREEN = GeneralSettings.TEXT_BOX_BACKGROUND_COLOR.y;


    private final String keyFlowchartBoxBGColor_BLUE = "fcbbgcolorblue";
    private final float fbValueFlowchartBoxBGColor_BLUE = GeneralSettings.TEXT_BOX_BACKGROUND_COLOR.z;

    /**Set the flowchart background color*/
    public void setFlowchartBoxbackgroundColor(Color bgColor) {
        userPref.putFloat(keyFlowchartBoxBGColor_RED, bgColor.getRed()/255f);
        userPref.putFloat(keyFlowchartBoxBGColor_GREEN, bgColor.getGreen()/255f);
        userPref.putFloat(keyFlowchartBoxBGColor_BLUE, bgColor.getBlue()/255f);
    }

    /**Set the flowchart background color*/
    public void setFlowchartBoxbackgroundColor3f(Vector3f bgColor) {
        userPref.putFloat(keyFlowchartBoxBGColor_RED, bgColor.x);
        userPref.putFloat(keyFlowchartBoxBGColor_GREEN, bgColor.y);
        userPref.putFloat(keyFlowchartBoxBGColor_BLUE, bgColor.z);
    }

    /**Get the flowchart background color*/
    public Vector3f getFlowchartBoxbackgroundColor3f() {
        Vector3f bgColor = new Vector3f();
        bgColor.setX(userPref.getFloat(keyFlowchartBoxBGColor_RED, fbValueFlowchartBoxBGColor_RED));
        bgColor.setY(userPref.getFloat(keyFlowchartBoxBGColor_GREEN, fbValueFlowchartBoxBGColor_GREEN));
        bgColor.setZ(userPref.getFloat(keyFlowchartBoxBGColor_BLUE, fbValueFlowchartBoxBGColor_BLUE));
        return bgColor;
    }

    /**Get the flowchart background color*/
    public Color getFlowchartBoxackgroundColor() {
        Color bgColor = new Color(
                userPref.getFloat(keyFlowchartBoxBGColor_RED, fbValueFlowchartBoxBGColor_RED),
                userPref.getFloat(keyFlowchartBoxBGColor_GREEN, fbValueFlowchartBoxBGColor_GREEN),
                userPref.getFloat(keyFlowchartBoxBGColor_BLUE, fbValueFlowchartBoxBGColor_BLUE)
        );
        return bgColor;
    }

    ///*********************************************************************************************************************************************
    //Flowchart highlight color
    private final String keyFlowchartBoxHLColor_RED = "fcbhlcolorred";
    private final float fbValueFlowchartBoxHLColor_RED = GeneralSettings.TEXT_COLOR.x;

    private final String keyFlowchartBoxHLColor_GREEN = "fcbhlcolorgreen";
    private final float fbValueFlowchartBoxHLColor_GREEN = GeneralSettings.TEXT_COLOR.y;

    private final String keyFlowchartBoxHLColor_BLUE = "fcbhlcolorblue";
    private final float fbValueFlowchartBoxHLColor_BLUE = GeneralSettings.TEXT_COLOR.z;

    /**Set the flowchart highlight color*/
    public void setFlowchartBoxHighlightColor(Color bgColor) {
        userPref.putFloat(keyFlowchartBoxHLColor_RED, bgColor.getRed()/255f);
        userPref.putFloat(keyFlowchartBoxHLColor_GREEN, bgColor.getGreen()/255f);
        userPref.putFloat(keyFlowchartBoxHLColor_BLUE, bgColor.getBlue()/255f);
    }

    /**Set the flowchart highlight color*/
    public void setFlowchartBoxHighlightColor3f(Vector3f bgColor) {
        userPref.putFloat(keyFlowchartBoxHLColor_RED, bgColor.x);
        userPref.putFloat(keyFlowchartBoxHLColor_GREEN, bgColor.y);
        userPref.putFloat(keyFlowchartBoxHLColor_BLUE, bgColor.z);
    }

    /**Get the flowchart highlight color*/
    public Vector3f getFlowchartBoxHighlightColor3f() {
        Vector3f bgColor = new Vector3f();
        bgColor.setX(userPref.getFloat(keyFlowchartBoxHLColor_RED, fbValueFlowchartBoxHLColor_RED));
        bgColor.setY(userPref.getFloat(keyFlowchartBoxHLColor_GREEN, fbValueFlowchartBoxHLColor_GREEN));
        bgColor.setZ(userPref.getFloat(keyFlowchartBoxHLColor_BLUE, fbValueFlowchartBoxHLColor_BLUE));
        return bgColor;
    }

    /**Get the flowchart highlight color*/
    public Color getFlowchartBoxHighlightColor() {
        Color bgColor = new Color(
                userPref.getFloat(keyFlowchartBoxHLColor_RED, fbValueFlowchartBoxHLColor_RED),
                userPref.getFloat(keyFlowchartBoxHLColor_GREEN, fbValueFlowchartBoxHLColor_GREEN),
                userPref.getFloat(keyFlowchartBoxHLColor_BLUE, fbValueFlowchartBoxHLColor_BLUE)
        );
        return bgColor;
    }

    //*********************************************************************************************************************************************
    //Flowchart line number background color
    private final String keyFlowchartBoxLineNumberBGColor_RED = "fcblnbgcolorred";
    private final float fbValueFlowchartBoxLineNumberBGColor_RED = GeneralSettings.LINE_NUMBER_BACKGROUND_COLOR.x;

    private final String keyFlowchartBoxLineNumberBGColor_GREEN = "fcblnbgcolorgreen";
    private final float fbValueFlowchartBoxLineNumberColor_GREEN = GeneralSettings.LINE_NUMBER_BACKGROUND_COLOR.y;

    private final String keyFlowchartBoxLineNumberBGColor_BLUE = "fcblnbgcolorblue";
    private final float fbValueFlowchartBoxLineNumberColor_BLUE = GeneralSettings.LINE_NUMBER_BACKGROUND_COLOR.z;

    /**Sets the background color of linenumber bar for a flowchart box*/
    public void setFlowchartBoxlinenumberBGColor(Color bgColor) {
        userPref.putFloat(keyFlowchartBoxLineNumberBGColor_RED, bgColor.getRed()/255f);
        userPref.putFloat(keyFlowchartBoxLineNumberBGColor_GREEN, bgColor.getGreen()/255f);
        userPref.putFloat(keyFlowchartBoxLineNumberBGColor_BLUE, bgColor.getBlue()/255f);
    }
    /**Sets the background color of linenumber bar for a flowchart box*/
    public void setFlowchartBoxlinenumberBGColor3f(Vector3f bgColor) {
        userPref.putFloat(keyFlowchartBoxLineNumberBGColor_RED, bgColor.x);
        userPref.putFloat(keyFlowchartBoxLineNumberBGColor_GREEN, bgColor.y);
        userPref.putFloat(keyFlowchartBoxLineNumberBGColor_BLUE, bgColor.z);
    }
    /**Gets the background color of linenumber bar for a flowchart box*/
    public Vector3f getFlowchartBoxlinenumberBGColor3f() {
        Vector3f bgColor = new Vector3f();
        bgColor.setX(userPref.getFloat(keyFlowchartBoxLineNumberBGColor_RED, fbValueFlowchartBoxLineNumberBGColor_RED));
        bgColor.setY(userPref.getFloat(keyFlowchartBoxLineNumberBGColor_GREEN, fbValueFlowchartBoxLineNumberColor_GREEN));
        bgColor.setZ(userPref.getFloat(keyFlowchartBoxLineNumberBGColor_BLUE, fbValueFlowchartBoxLineNumberColor_BLUE));
        return bgColor;
    }

    /**Gets the background color of linenumber bar for a flowchart box*/
    public Color getFlowchartBoxlinenumberBGColor() {
        Color bgColor = new Color(
                userPref.getFloat(keyFlowchartBoxLineNumberBGColor_RED, fbValueFlowchartBoxLineNumberBGColor_RED),
                userPref.getFloat(keyFlowchartBoxLineNumberBGColor_GREEN, fbValueFlowchartBoxLineNumberColor_GREEN),
                userPref.getFloat(keyFlowchartBoxLineNumberBGColor_BLUE, fbValueFlowchartBoxLineNumberColor_BLUE)
        );
        return bgColor;
    }


    //*********************************************************************************************************************************************
    //Text editor background color
    private final String keyTexteditorBGcolor_RED = "tebgcred";
    private final float fbValueTexteditorBGcolor_RED = GeneralSettings.TEXT_BOX_BACKGROUND_COLOR.x;


    private final String keyTexteditorBGcolor_GREEN = "tebgcgreen";
    private final float fbValueTexteditorBGcolor_GREEN = GeneralSettings.TEXT_BOX_BACKGROUND_COLOR.y;


    private final String keyTexteditorBGcolor_BLUE = "tebgcblue";
    private final float fbValueTexteditorBGcolor_BLUE = GeneralSettings.TEXT_BOX_BACKGROUND_COLOR.z;
    /**Sets the background color of a codewindow*/
    public void setTextEditorBGColor(Color bgColor) {
        userPref.putFloat(keyTexteditorBGcolor_RED, bgColor.getRed()/255f);
        userPref.putFloat(keyTexteditorBGcolor_GREEN, bgColor.getGreen()/255f);
        userPref.putFloat(keyTexteditorBGcolor_BLUE, bgColor.getBlue()/255f);
    }
    /**Sets the background color of a codewindow*/
    public void setTexteditorBGColor3f(Vector3f bgColor) {
        userPref.putFloat(keyTexteditorBGcolor_RED, bgColor.x);
        userPref.putFloat(keyTexteditorBGcolor_GREEN, bgColor.y);
        userPref.putFloat(keyTexteditorBGcolor_BLUE, bgColor.z);
    }
    /**Gets the background color of a codewindow*/
    public Vector3f getTexteditorBGColor3f() {
        Vector3f bgColor = new Vector3f();
        bgColor.setX(userPref.getFloat(keyTexteditorBGcolor_RED, fbValueTexteditorBGcolor_RED));
        bgColor.setY(userPref.getFloat(keyTexteditorBGcolor_GREEN, fbValueTexteditorBGcolor_GREEN));
        bgColor.setZ(userPref.getFloat(keyTexteditorBGcolor_BLUE, fbValueTexteditorBGcolor_BLUE));
        return bgColor;
    }
    /**Gets the background color of a codewindow*/
    public Color getTexteditorBGColor() {
        Color bgColor = new Color(
                userPref.getFloat(keyTexteditorBGcolor_RED, fbValueTexteditorBGcolor_RED),
                userPref.getFloat(keyTexteditorBGcolor_GREEN, fbValueTexteditorBGcolor_GREEN),
                userPref.getFloat(keyTexteditorBGcolor_BLUE, fbValueTexteditorBGcolor_BLUE)
        );
        return bgColor;
    }

    //*********************************************************************************************************************************************
    //Text editor line number background color
    private final String keyTexteditorLinenumberBGcolor_RED = "telnbgcred";
    private final float fbValueTexteditorLinenumberBGcolor_RED = GeneralSettings.LINE_NUMBER_BACKGROUND_COLOR.x;


    private final String keyTexteditorLinenumberBGcolor_GREEN = "telnbgcgreen";
    private final float fbValueTexteditorLinenumberBGcolor_GREEN = GeneralSettings.LINE_NUMBER_BACKGROUND_COLOR.y;


    private final String keyTexteditorLinenumberBGcolor_BLUE = "telnbgcblue";
    private final float fbValueTexteditorLinenumberBGcolor_BLUE = GeneralSettings.LINE_NUMBER_BACKGROUND_COLOR.z;

    /**Sets the linenumber background color of code window*/
    public void setTexteditorLinenumberBGColor(Color bgColor) {
        userPref.putFloat(keyTexteditorLinenumberBGcolor_RED, bgColor.getRed()/255f);
        userPref.putFloat(keyTexteditorLinenumberBGcolor_GREEN, bgColor.getGreen()/255f);
        userPref.putFloat(keyTexteditorLinenumberBGcolor_BLUE, bgColor.getBlue()/255f);
    }

    /**Sets the linenumber background color of code window*/
    public void setTexteditorLinenumberBGColor3f(Vector3f bgColor) {
        userPref.putFloat(keyTexteditorLinenumberBGcolor_RED, bgColor.x);
        userPref.putFloat(keyTexteditorLinenumberBGcolor_GREEN, bgColor.y);
        userPref.putFloat(keyTexteditorLinenumberBGcolor_BLUE, bgColor.z);
    }
    /**Gets the linenumber background color of code window*/
    public Vector3f getTexteditorLinenumberBGColor3f() {
        Vector3f bgColor = new Vector3f();
        bgColor.setX(userPref.getFloat(keyTexteditorLinenumberBGcolor_RED, fbValueTexteditorLinenumberBGcolor_RED));
        bgColor.setY(userPref.getFloat(keyTexteditorLinenumberBGcolor_GREEN, fbValueTexteditorLinenumberBGcolor_GREEN));
        bgColor.setZ(userPref.getFloat(keyTexteditorLinenumberBGcolor_BLUE, fbValueTexteditorLinenumberBGcolor_BLUE));
        return bgColor;
    }
    /**Gets the linenumber background color of code window*/
    public Color getTexteditorLinenumberBGColor() {
        Color bgColor = new Color(
                userPref.getFloat(keyTexteditorLinenumberBGcolor_RED, fbValueTexteditorLinenumberBGcolor_RED),
                userPref.getFloat(keyTexteditorLinenumberBGcolor_GREEN, fbValueTexteditorLinenumberBGcolor_GREEN),
                userPref.getFloat(keyTexteditorLinenumberBGcolor_BLUE, fbValueTexteditorLinenumberBGcolor_BLUE)
        );
        return bgColor;
    }

    //*********************************************************************************************************************************************
    //The color for the scroll bar
    private final String keyScrollBarColor_RED = "scrollbarred";
    private final float fbValueScrollBarColor_RED = GeneralSettings.SCROLL_BAR_COLOR.x;

    private final String keyScrollBarColor_GREEN = "scrollbargreen";
    private final float fbValueScrollBarColor_GREEN = GeneralSettings.SCROLL_BAR_COLOR.y;

    private final String keyTextScrollBarColor_BLUE = "scrollbarblue";
    private final float fbValueScrollBarColor_BLUE = GeneralSettings.SCROLL_BAR_COLOR.z;

    /**Sets the scroll bar color*/
    public void setScrollBarColor(Color bgColor) {
        userPref.putFloat(keyScrollBarColor_RED, bgColor.getRed()/255f);
        userPref.putFloat(keyScrollBarColor_GREEN, bgColor.getGreen()/255f);
        userPref.putFloat(keyTextScrollBarColor_BLUE, bgColor.getBlue()/255f);
    }
    /**Sets the scroll bar color*/
    public void setScrollBarColor3f(Vector3f bgColor) {
        userPref.putFloat(keyScrollBarColor_RED, bgColor.x);
        userPref.putFloat(keyScrollBarColor_GREEN, bgColor.y);
        userPref.putFloat(keyTextScrollBarColor_BLUE, bgColor.z);
    }

    /**Gets the scroll bar color*/
    public Vector3f getScrollBarColor3f() {
        Vector3f bgColor = new Vector3f();
        bgColor.setX(userPref.getFloat(keyScrollBarColor_RED, fbValueScrollBarColor_RED));
        bgColor.setY(userPref.getFloat(keyScrollBarColor_GREEN, fbValueScrollBarColor_GREEN));
        bgColor.setZ(userPref.getFloat(keyTextScrollBarColor_BLUE, fbValueScrollBarColor_BLUE));
        return bgColor;
    }

    /**Gets the scroll bar color*/
    public Color getScrollBarColor() {
        Color bgColor = new Color(
                userPref.getFloat(keyScrollBarColor_RED, fbValueScrollBarColor_RED),
                userPref.getFloat(keyScrollBarColor_GREEN, fbValueScrollBarColor_GREEN),
                userPref.getFloat(keyTextScrollBarColor_BLUE, fbValueScrollBarColor_BLUE)
        );
        return bgColor;
    }





    //*********************************************************************************************************************************************
    //Header color
    private final String keyHeaderColor_RED = "headercolorred";
    private final float fbValueHeadercolor_RED = GeneralSettings.HEADER_COLOR.x;

    private final String keyHeaderColor_GREEN = "headercolorgreen";
    private final float fbValueHeadercolor_GREEN = GeneralSettings.HEADER_COLOR.y;

    private final String keyHeaderColor_BLUE = "headercolorblue";
    private final float fbValueHeadercolor_BLUE = GeneralSettings.HEADER_COLOR.z;

    /**Sets the color background color of the header menu bar*/
    public void setHeaderColor(Color bgColor) {
        userPref.putFloat(keyHeaderColor_RED, bgColor.getRed()/255f);
        userPref.putFloat(keyHeaderColor_GREEN, bgColor.getGreen()/255f);
        userPref.putFloat(keyHeaderColor_BLUE, bgColor.getBlue()/255f);
    }

    /**Sets the color background color of the header menu bar*/
    public void setHeaderColor3f(Vector3f bgColor) {
        userPref.putFloat(keyHeaderColor_RED, bgColor.x);
        userPref.putFloat(keyHeaderColor_GREEN, bgColor.y);
        userPref.putFloat(keyHeaderColor_BLUE, bgColor.z);
    }

    /**Gets the color background color of the header menu bar*/
    public Vector3f getHeaderColor3f() {
        Vector3f bgColor = new Vector3f();
        bgColor.setX(userPref.getFloat(keyHeaderColor_RED, fbValueHeadercolor_RED));
        bgColor.setY(userPref.getFloat(keyHeaderColor_GREEN, fbValueHeadercolor_GREEN));
        bgColor.setZ(userPref.getFloat(keyHeaderColor_BLUE, fbValueHeadercolor_BLUE));
        return bgColor;
    }

    /**Gets the color background color of the header menu bar*/
    public Color getHeaderColor() {
        Color bgColor = new Color(
                userPref.getFloat(keyHeaderColor_RED, fbValueHeadercolor_RED),
                userPref.getFloat(keyHeaderColor_GREEN, fbValueHeadercolor_GREEN),
                userPref.getFloat(keyHeaderColor_BLUE, fbValueHeadercolor_BLUE)
        );
        return bgColor;
    }

    //*********************************************************************************************************************************************
    //Menu Button background colors
    private final String keyMenuBtnBGcolor_RED = "menubtnbgcolorred";
    private final float fbValueMenuBtnBGcolor_RED = GeneralSettings.TEXT_BUTTON_BACKGROUND_COLOR.x;


    private final String keyMenuBtnBGcolor_GREEN = "menubtnbgcolorgreen";
    private final float fbValueMenuBtnBGcolor_GREEN = GeneralSettings.TEXT_BUTTON_BACKGROUND_COLOR.y;


    private final String keyMenuBtnBGcolor_BLUE = "menubtnbgcolorblue";
    private final float fbValueMenuBtnBGcolor_BLUE = GeneralSettings.TEXT_BUTTON_BACKGROUND_COLOR.z;

    /**Sets the background color of all menu buttons*/
    public void setMenuBtnBGColor(Color bgColor) {
        userPref.putFloat(keyMenuBtnBGcolor_RED, bgColor.getRed()/255f);
        userPref.putFloat(keyMenuBtnBGcolor_GREEN, bgColor.getGreen()/255f);
        userPref.putFloat(keyMenuBtnBGcolor_BLUE, bgColor.getBlue()/255f);
    }

    /**Sets the background color of all menu buttons*/
    public void setMenuBtnBGColor3f(Vector3f bgColor) {
        userPref.putFloat(keyMenuBtnBGcolor_RED, bgColor.x);
        userPref.putFloat(keyMenuBtnBGcolor_GREEN, bgColor.y);
        userPref.putFloat(keyMenuBtnBGcolor_BLUE, bgColor.z);
    }

    /**Gets the background color of all menu buttons*/
    public Vector3f getMenuBtnBGColor3f() {
        Vector3f bgColor = new Vector3f();
        bgColor.setX(userPref.getFloat(keyMenuBtnBGcolor_RED, fbValueMenuBtnBGcolor_RED));
        bgColor.setY(userPref.getFloat(keyMenuBtnBGcolor_GREEN, fbValueMenuBtnBGcolor_GREEN));
        bgColor.setZ(userPref.getFloat(keyMenuBtnBGcolor_BLUE, fbValueMenuBtnBGcolor_BLUE));
        return bgColor;
    }

    /**Gets the background color of all menu buttons*/
    public Color getMenuBtnBGColor() {
        Color bgColor = new Color(
                userPref.getFloat(keyMenuBtnBGcolor_RED, fbValueMenuBtnBGcolor_RED),
                userPref.getFloat(keyMenuBtnBGcolor_GREEN, fbValueMenuBtnBGcolor_GREEN),
                userPref.getFloat(keyMenuBtnBGcolor_BLUE, fbValueMenuBtnBGcolor_BLUE)
        );
        return bgColor;
    }

    //*********************************************************************************************************************************************
    //Menu button highlight colors
    private final String KeyMenuBtnHLColor_RED = "menubtnhlcolorred";
    private final float  fbValueMenuBtnHLColor_RED = GeneralSettings.HIGHLIGHT_COLOR.x;

    private final String KeyMenuBtnHLColor_GREEN = "menubtnhlcolorgreen";
    private final float  fbValueMenuBtnHLColor_GREEN = GeneralSettings.HIGHLIGHT_COLOR.y;

    private final String KeyMenuBtnHLColor_BLUE = "menubtnhlcolorblue";
    private final float  fbValueMenuBtnHLColor_BLUE = GeneralSettings.HIGHLIGHT_COLOR.z;

    /**Sets the highlight color of all menu buttons*/
    public void setMenuBtnHLColor(Color bgColor) {
        userPref.putFloat(KeyMenuBtnHLColor_RED, bgColor.getRed()/255f);
        userPref.putFloat(KeyMenuBtnHLColor_GREEN, bgColor.getGreen()/255f);
        userPref.putFloat(KeyMenuBtnHLColor_BLUE, bgColor.getBlue()/255f);
    }
    /**Sets the highlight color of all menu buttons*/
    public void setMenuBtnHLColor3f(Vector3f bgColor) {
        userPref.putFloat(KeyMenuBtnHLColor_RED, bgColor.x);
        userPref.putFloat(KeyMenuBtnHLColor_GREEN, bgColor.y);
        userPref.putFloat(KeyMenuBtnHLColor_BLUE, bgColor.z);
    }
    /**Gets the highlight color of all menu buttons*/
    public Vector3f getMenuBtnHLColor3f() {
        Vector3f bgColor = new Vector3f();
        bgColor.setX(userPref.getFloat(KeyMenuBtnHLColor_RED, fbValueMenuBtnHLColor_RED));
        bgColor.setY(userPref.getFloat(KeyMenuBtnHLColor_GREEN, fbValueMenuBtnHLColor_GREEN));
        bgColor.setZ(userPref.getFloat(KeyMenuBtnHLColor_BLUE, fbValueMenuBtnHLColor_BLUE));
        return bgColor;
    }

    /**Gets the highlight color of all menu buttons*/
    public Color getMenuBtnHLColor() {
        Color bgColor = new Color(
                userPref.getFloat(KeyMenuBtnHLColor_RED, fbValueMenuBtnHLColor_RED),
                userPref.getFloat(KeyMenuBtnHLColor_GREEN, fbValueMenuBtnHLColor_GREEN),
                userPref.getFloat(KeyMenuBtnHLColor_BLUE, fbValueMenuBtnHLColor_BLUE)
        );
        return bgColor;
    }

    //*********************************************************************************************************************************************
    //Flowchart line width settings
    private final String keyMenuDefaultLineWidth = "defaultlinewidth";
    private final float fbValueDefaultLineWidth = GeneralSettings.DEFAULT_LINE_WIDTH;

    private final String keyMenuHighlightedLineWidth = "highlightedlinewidth";
    private final float fbValueHighlightedLineWidth = GeneralSettings.HIGHLIGHTED_LINE_WIDTH;

    /**Sets the default line width of flowchart lines*/
    public void setDefaultLineWidth(float lineWidth){
        userPref.putFloat(keyMenuDefaultLineWidth,lineWidth);
    }
    /**Sets the default line width of highlighted flowchart lines*/
    public void setHighlightedLineWidth(float lineWidth){
        userPref.putFloat(keyMenuHighlightedLineWidth,lineWidth);
    }
    /**Gets the default line width of flowchart lines*/
    public float getDefaultLineWidth(){
        return userPref.getFloat(keyMenuDefaultLineWidth, fbValueDefaultLineWidth);
    }
    /**Gets the default line width of highlighted flowchart lines*/
    public float getHighlightedLineWidth(){
        return userPref.getFloat(keyMenuHighlightedLineWidth, fbValueHighlightedLineWidth);
    }

    //*********************************************************************************************************************************************
    //Branch text color
    public final String keyBranchTextColor_Red = "branchtextcolorred";
    public final float fbValueBranchTextColor_Red = GeneralSettings.branchColor.x;
    public final String getKeyBranchTextColor_Green = "branchtextcolorgreen";
    public final float fbValueBranchTextColor_Green = GeneralSettings.branchColor.y;
    public final String keyBranchTextColor_Blue = "branchtextcolorblue";
    public final float fbValueBranchTextColor_Blue = GeneralSettings.branchColor.z;


    /**Sets the text color of branch syntax*/
    public void setBranchTextColor(Color bgColor) {
        userPref.putFloat(keyBranchTextColor_Red, bgColor.getRed()/255f);
        userPref.putFloat(getKeyBranchTextColor_Green, bgColor.getGreen()/255f);
        userPref.putFloat(keyBranchTextColor_Blue, bgColor.getBlue()/255f);
    }
    /**Sets the text color of branch syntax*/
    public void setBranchTextColor3f(Vector3f bgColor) {
        userPref.putFloat(keyBranchTextColor_Red, bgColor.x);
        userPref.putFloat(getKeyBranchTextColor_Green, bgColor.y);
        userPref.putFloat(keyBranchTextColor_Blue, bgColor.z);
    }
    /**Gets the text color of branch syntax*/
    public Vector3f getBranchTextColor3f() {
        Vector3f bgColor = new Vector3f();
        bgColor.setX(userPref.getFloat(keyBranchTextColor_Red, fbValueBranchTextColor_Red));
        bgColor.setY(userPref.getFloat(getKeyBranchTextColor_Green, fbValueBranchTextColor_Green));
        bgColor.setZ(userPref.getFloat(keyBranchTextColor_Blue, fbValueBranchTextColor_Blue));
        return bgColor;
    }

    /**Gets the text color of branch syntax*/
    public Color getBranchTextColor() {
        Color bgColor = new Color(
                userPref.getFloat(keyBranchTextColor_Red, fbValueBranchTextColor_Red),
                userPref.getFloat(getKeyBranchTextColor_Green, fbValueBranchTextColor_Green),
                userPref.getFloat(keyBranchTextColor_Blue, fbValueBranchTextColor_Blue)
        );
        return bgColor;
    }

    //*********************************************************************************************************************************************
    //Command text color

    public final String keyCommandTextColor_Red = "Commandtextcolorred";
    public final float fbValueCommandTextColor_Red = GeneralSettings.commandColor.x;
    public final String getKeyCommandTextColor_Green = "Commandtextcolorgreen";
    public final float fbValueCommandTextColor_Green = GeneralSettings.commandColor.y;
    public final String keyCommandTextColor_Blue = "Commandtextcolorblue";
    public final float fbValueCommandTextColor_Blue = GeneralSettings.commandColor.z;


    /**Sets the text color of command syntax*/
    public void setCommandTextColor(Color bgColor) {
        userPref.putFloat(keyCommandTextColor_Red, bgColor.getRed()/255f);
        userPref.putFloat(getKeyCommandTextColor_Green, bgColor.getGreen()/255f);
        userPref.putFloat(keyCommandTextColor_Blue, bgColor.getBlue()/255f);
    }
    /**Sets the text color of command  syntax*/
    public void setCommandTextColor3f(Vector3f bgColor) {
        userPref.putFloat(keyCommandTextColor_Red, bgColor.x);
        userPref.putFloat(getKeyCommandTextColor_Green, bgColor.y);
        userPref.putFloat(keyCommandTextColor_Blue, bgColor.z);
    }
    /**Gets the text color of command  syntax*/
    public Vector3f getCommandTextColor3f() {
        Vector3f bgColor = new Vector3f();
        bgColor.setX(userPref.getFloat(keyCommandTextColor_Red, fbValueCommandTextColor_Red));
        bgColor.setY(userPref.getFloat(getKeyCommandTextColor_Green, fbValueCommandTextColor_Green));
        bgColor.setZ(userPref.getFloat(keyCommandTextColor_Blue, fbValueCommandTextColor_Blue));
        return bgColor;
    }

    /**Gets the text color of command  syntax*/
    public Color getCommandTextColor() {
        Color bgColor = new Color(
                userPref.getFloat(keyCommandTextColor_Red, fbValueCommandTextColor_Red),
                userPref.getFloat(getKeyCommandTextColor_Green, fbValueCommandTextColor_Green),
                userPref.getFloat(keyCommandTextColor_Blue, fbValueCommandTextColor_Blue)
        );
        return bgColor;
    }

    //*********************************************************************************************************************************************
    //Comment text color
    private final String keyCommentColor_RED = "commentcolorred";
    private final float fbValueCommentColor_RED = GeneralSettings.commentColor.x;

    private final String keyCommentColor_GREEN = "commentcolorgreen";
    private final float fbValueCommentColor_GREEN = GeneralSettings.commentColor.y;

    private final String keyCommentColor_BLUE = "commentcolorblue";
    private final float fbValueCommentColor_BLUE = GeneralSettings.commentColor.z;



    public void setCommentColor(Color bgColor) {
        userPref.putFloat(keyCommentColor_RED, bgColor.getRed()/255f);
        userPref.putFloat(keyCommentColor_GREEN, bgColor.getGreen()/255f);
        userPref.putFloat(keyCommentColor_BLUE, bgColor.getBlue()/255f);
    }

    public void setCommentColor3f(Vector3f bgColor) {
        userPref.putFloat(keyCommentColor_RED, bgColor.x);
        userPref.putFloat(keyCommentColor_GREEN, bgColor.y);
        userPref.putFloat(keyCommentColor_BLUE, bgColor.z);
    }

    public Vector3f getCommentColor3f() {
        Vector3f bgColor = new Vector3f();
        bgColor.setX(userPref.getFloat(keyCommentColor_RED, fbValueCommentColor_RED));
        bgColor.setY(userPref.getFloat(keyCommentColor_GREEN, fbValueCommentColor_GREEN));
        bgColor.setZ(userPref.getFloat(keyCommentColor_BLUE, fbValueCommentColor_BLUE));
        return bgColor;
    }

    public Color getCommentColor() {
        Color bgColor = new Color(
                userPref.getFloat(keyCommentColor_RED, fbValueCommentColor_RED),
                userPref.getFloat(keyCommentColor_GREEN, fbValueCommentColor_GREEN),
                userPref.getFloat(keyCommentColor_BLUE, fbValueCommentColor_BLUE)
        );
        return bgColor;
    }


    //*********************************************************************************************************************************************
    //Error text color
    private final String keyErrorColor_RED = "errorcolorred";
    private final float fbValueErrorColor_RED = GeneralSettings.errorColor.x;


    private final String keyErrorColor_GREEN = "errorcolorgreen";
    private final float fbValueErrorColor_GREEN = GeneralSettings.errorColor.y;


    private final String keyErrorColor_BLUE = "errorcolorblue";
    private final float fbValueErrorColor_BLUE = GeneralSettings.errorColor.z;


    public void setErrorColor(Color bgColor) {
        userPref.putFloat(keyErrorColor_RED, bgColor.getRed()/255f);
        userPref.putFloat(keyErrorColor_GREEN, bgColor.getGreen()/255f);
        userPref.putFloat(keyErrorColor_BLUE, bgColor.getBlue()/255f);
    }

    public void setErrorColor3f(Vector3f bgColor) {
        userPref.putFloat(keyErrorColor_RED, bgColor.x);
        userPref.putFloat(keyErrorColor_GREEN, bgColor.y);
        userPref.putFloat(keyErrorColor_BLUE, bgColor.z);
    }

    public Vector3f getErrorColor3f() {
        Vector3f bgColor = new Vector3f();
        bgColor.setX(userPref.getFloat(keyErrorColor_RED, fbValueErrorColor_RED));
        bgColor.setY(userPref.getFloat(keyErrorColor_GREEN, fbValueErrorColor_GREEN));
        bgColor.setZ(userPref.getFloat(keyErrorColor_BLUE, fbValueErrorColor_BLUE));
        return bgColor;
    }

    public Color getErrorColor() {
        Color bgColor = new Color(
                userPref.getFloat(keyErrorColor_RED, fbValueErrorColor_RED),
                userPref.getFloat(keyErrorColor_GREEN, fbValueErrorColor_GREEN),
                userPref.getFloat(keyErrorColor_BLUE, fbValueErrorColor_BLUE)
        );
        return bgColor;
    }


    //*********************************************************************************************************************************************
    //Immediate text color
    private final String keyImmediateColor_RED = "immediatecolorred";
    private final float fbValueImmediateColor_RED = GeneralSettings.immediateColor.x;

    private final String keyImmediateColor_GREEN = "immediatecolorgreen";
    private final float fbValueImmediateColor_GREEN = GeneralSettings.immediateColor.y;

    private final String keyImmediateColor_BLUE = "immediatecolorblue";
    private final float fbValueImmediateColor_BLUE = GeneralSettings.immediateColor.z;

    public void setImmediateColor(Color bgColor) {
        userPref.putFloat(keyImmediateColor_RED, bgColor.getRed()/255f);
        userPref.putFloat(keyImmediateColor_GREEN, bgColor.getGreen()/255f);
        userPref.putFloat(keyImmediateColor_BLUE, bgColor.getBlue()/255f);
    }

    public void setImmediateColor3f(Vector3f bgColor) {
        userPref.putFloat(keyImmediateColor_RED, bgColor.x);
        userPref.putFloat(keyImmediateColor_GREEN, bgColor.y);
        userPref.putFloat(keyImmediateColor_BLUE, bgColor.z);
    }

    public Vector3f getImmediateColor3f() {
        Vector3f bgColor = new Vector3f();
        bgColor.setX(userPref.getFloat(keyImmediateColor_RED, fbValueImmediateColor_RED));
        bgColor.setY(userPref.getFloat(keyImmediateColor_GREEN, fbValueImmediateColor_GREEN));
        bgColor.setZ(userPref.getFloat(keyImmediateColor_BLUE, fbValueImmediateColor_BLUE));
        return bgColor;
    }

    public Color getImmediateColor() {
        Color bgColor = new Color(
                userPref.getFloat(keyImmediateColor_RED, fbValueImmediateColor_RED),
                userPref.getFloat(keyImmediateColor_GREEN, fbValueImmediateColor_GREEN),
                userPref.getFloat(keyImmediateColor_BLUE, fbValueImmediateColor_BLUE)
        );
        return bgColor;
    }


    //*********************************************************************************************************************************************
    //Label text color
    private final String keyLabelColor_RED = "labelcolorred";
    private final float fbValueLabelColor_RED = GeneralSettings.labelColor.x;

    private final String keyLabelColor_GREEN = "labelcolorgreen";
    private final float fbValueLabelColor_GREEN = GeneralSettings.labelColor.y;

    private final String keyLabelColor_BLUE = "labelcolorblue";
    private final float fbValueLabelColor_BLUE = GeneralSettings.labelColor.z;


    public void setLabelColor(Color bgColor) {
        userPref.putFloat(keyLabelColor_RED, bgColor.getRed()/255f);
        userPref.putFloat(keyLabelColor_GREEN, bgColor.getGreen()/255f);
        userPref.putFloat(keyLabelColor_BLUE, bgColor.getBlue()/255f);
    }

    public void setLabelColor3f(Vector3f bgColor) {
        userPref.putFloat(keyLabelColor_RED, bgColor.x);
        userPref.putFloat(keyLabelColor_GREEN, bgColor.y);
        userPref.putFloat(keyLabelColor_BLUE, bgColor.z);
    }

    public Vector3f getLabelColor3f() {
        Vector3f bgColor = new Vector3f();
        bgColor.setX(userPref.getFloat(keyLabelColor_RED, fbValueLabelColor_RED));
        bgColor.setY(userPref.getFloat(keyLabelColor_GREEN, fbValueLabelColor_GREEN));
        bgColor.setZ(userPref.getFloat(keyLabelColor_BLUE, fbValueLabelColor_BLUE));
        return bgColor;
    }

    public Color getLabelColor() {
        Color bgColor = new Color(
                userPref.getFloat(keyLabelColor_RED, fbValueLabelColor_RED),
                userPref.getFloat(keyLabelColor_GREEN, fbValueLabelColor_GREEN),
                userPref.getFloat(keyLabelColor_BLUE, fbValueLabelColor_BLUE)
        );
        return bgColor;
    }

    //*********************************************************************************************************************************************
    //LineNumber text color

    private final String keyLineNumberColor_RED = "linenumbercolorred";
    private final float fbValueLineNumberColor_RED = GeneralSettings.TEXT_COLOR.x;

    private final String keyLineNumberColor_GREEN = "linenumbercolorgreen";
    private final float fbValueLineNumberColor_GREEN = GeneralSettings.TEXT_COLOR.y;

    private final String keyLineNumberColor_BLUE = "linenumbercolorblue";
    private final float fbValueLineNumberColor_BLUE = GeneralSettings.TEXT_COLOR.z;



    public void setLineNumberColor(Color bgColor) {
        userPref.putFloat(keyLineNumberColor_RED, bgColor.getRed()/255f);
        userPref.putFloat(keyLineNumberColor_GREEN, bgColor.getGreen()/255f);
        userPref.putFloat(keyLineNumberColor_BLUE, bgColor.getBlue()/255f);
    }

    public void setLineNumberColor3f(Vector3f bgColor) {
        userPref.putFloat(keyLineNumberColor_RED, bgColor.x);
        userPref.putFloat(keyLineNumberColor_GREEN, bgColor.y);
        userPref.putFloat(keyLineNumberColor_BLUE, bgColor.z);
    }

    public Vector3f getLineNumberColor3f() {
        Vector3f bgColor = new Vector3f();
        bgColor.setX(userPref.getFloat(keyLineNumberColor_RED, fbValueLineNumberColor_RED));
        bgColor.setY(userPref.getFloat(keyLineNumberColor_GREEN, fbValueLineNumberColor_GREEN));
        bgColor.setZ(userPref.getFloat(keyLineNumberColor_BLUE, fbValueLineNumberColor_BLUE));
        return bgColor;
    }

    public Color getLineNumberColor() {
        Color bgColor = new Color(
                userPref.getFloat(keyLineNumberColor_RED, fbValueLineNumberColor_RED),
                userPref.getFloat(keyLineNumberColor_GREEN, fbValueLineNumberColor_GREEN),
                userPref.getFloat(keyLineNumberColor_BLUE, fbValueLineNumberColor_BLUE)
        );
        return bgColor;
    }


    //*********************************************************************************************************************************************
    //Register text color

    private final String keyRegisterColor_RED = "registercolorred";
    private final float fbValueRegisterColor_RED = GeneralSettings.registerColor.x;

    private final String keyRegisterColor_GREEN = "registercolorgreen";
    private final float fbValueRegisterColor_GREEN = GeneralSettings.registerColor.y;

    private final String keyRegisterColor_BLUE = "registercolorblue";
    private final float fbValueRegisterColor_BLUE = GeneralSettings.registerColor.z;



    public void setRegisterColor(Color bgColor) {
        userPref.putFloat(keyRegisterColor_RED, bgColor.getRed()/255f);
        userPref.putFloat(keyRegisterColor_GREEN, bgColor.getGreen()/255f);
        userPref.putFloat(keyRegisterColor_BLUE, bgColor.getBlue()/255f);
    }

    public void setRegisterColor3f(Vector3f bgColor) {
        userPref.putFloat(keyRegisterColor_RED, bgColor.x);
        userPref.putFloat(keyRegisterColor_GREEN, bgColor.y);
        userPref.putFloat(keyRegisterColor_BLUE, bgColor.z);
    }

    public Vector3f getRegisterColor3f() {
        Vector3f bgColor = new Vector3f();
        bgColor.setX(userPref.getFloat(keyRegisterColor_RED, fbValueRegisterColor_RED));
        bgColor.setY(userPref.getFloat(keyRegisterColor_GREEN, fbValueRegisterColor_GREEN));
        bgColor.setZ(userPref.getFloat(keyRegisterColor_BLUE, fbValueRegisterColor_BLUE));
        return bgColor;
    }

    public Color getRegisterColor() {
        Color bgColor = new Color(
                userPref.getFloat(keyRegisterColor_RED, fbValueRegisterColor_RED),
                userPref.getFloat(keyRegisterColor_GREEN, fbValueRegisterColor_GREEN),
                userPref.getFloat(keyRegisterColor_BLUE, fbValueRegisterColor_BLUE)
        );
        return bgColor;
    }


    //*********************************************************************************************************************************************
    //Separator text color
    private final String keySeparatorColor_RED = "separatorcolorred";
    private final float fbValueSeparatorColor_RED = GeneralSettings.separatorColor.x;



    private final String keySeparatorColor_GREEN = "separatorcolorgreen";
    private final float fbValueSeparatorColor_GREEN = GeneralSettings.separatorColor.y;

    private final String keySeparatorColor_BLUE = "separatorcolorblue";
    private final float fbValueSeparatorColor_BLUE = GeneralSettings.separatorColor.z;

    public void setSeparatorColor(Color bgColor) {
        userPref.putFloat(keySeparatorColor_RED, bgColor.getRed()/255f);
        userPref.putFloat(keySeparatorColor_GREEN, bgColor.getGreen()/255f);
        userPref.putFloat(keySeparatorColor_BLUE, bgColor.getBlue()/255f);
    }

    public void setSeparatorColor3f(Vector3f bgColor) {
        userPref.putFloat(keySeparatorColor_RED, bgColor.x);
        userPref.putFloat(keySeparatorColor_GREEN, bgColor.y);
        userPref.putFloat(keySeparatorColor_BLUE, bgColor.z);
    }

    public Vector3f getSeparatorColor3f() {
        Vector3f bgColor = new Vector3f();
        bgColor.setX(userPref.getFloat(keySeparatorColor_RED, fbValueSeparatorColor_RED));
        bgColor.setY(userPref.getFloat(keySeparatorColor_GREEN, fbValueSeparatorColor_GREEN));
        bgColor.setZ(userPref.getFloat(keySeparatorColor_BLUE, fbValueSeparatorColor_BLUE));
        return bgColor;
    }

    public Color getSeparatorColor() {
        Color bgColor = new Color(
                userPref.getFloat(keySeparatorColor_RED, fbValueSeparatorColor_RED),
                userPref.getFloat(keySeparatorColor_GREEN, fbValueSeparatorColor_GREEN),
                userPref.getFloat(keySeparatorColor_BLUE, fbValueSeparatorColor_BLUE)
        );
        return bgColor;
    }


    //*********************************************************************************************************************************************
    //OS Settings calls TODO: Consider removing if we have no need for this
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

    //TODO: Create better reset methods
    /**Removes ALL attributes (hard reset). Next time any get is called is will return fall back values*/
    public void resetAll() {
        removeAutoGenFlowchart();
        removeFullScreen();
        removeSplitScreen();
        removePreferredFiletype();
        RemoveUserTempFileDirPath();
    }
}




