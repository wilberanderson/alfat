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
    private final Preferences userPref;

    //User Preferences node path
    private String userPreferencesPath = "com/alfat/userpref";

    //------------------------------------------------------------------------------
    //User preferences key name & Default fallback values.
    //NOTE: Java Class preferences require a fallback value


    private final String keySyntaxPath = "syntaxpath";
    private final String fbValuesyntaxPath = GeneralSettings.SYNTAX_PATH;

    private final String keyTempfilepath = "tempfilepath";
    private final String fbValueTempfilepath = GeneralSettings.TEMP_DIR;

    private final String keyTempfileLimit = "tempfilelimit";
    private final int fbValue = 5;

    private final String keyPreferredFiletype = "preferredfiletype";
    private final String fbValuePreferredFiletype = "asm;txt;asm,txt";

    private final String keyAutoGenFlowchart = "openautogenflowchart";
    private final Boolean fbValueAutoGenFlowchart = false;

    private final String keyOpenSplitScreen = "opensplitscreen";
    private final Boolean fbValueOpenSplitScreen = true;

    private final String keyOpenFullScreen = "openfullscreen";
    private final int fbValueOpenfullScreen = 0; //0 = false, 0 > open to editor, 0 < open to flowchart

    //Background color
    private final String keyBGColor_RED = "bgcolorred";
    private final float fbValueBGColor_RED = GeneralSettings.base02.x;


    private final String keyBGColor_GREEN = "bgcolorgreen";
    private final float fbValueBGColor_GREEN = GeneralSettings.base02.y;


    private final String keyBGColor_BLUE = "bgcolorblue";
    private final float fbValueBGColor_BLUE = GeneralSettings.base02.z;

    //Flowchart background color
    private final String keyFlowchartBoxBGColor_RED = "fcbbgcolorred";
    private final float fbValueFlowchartBoxBGColor_RED = GeneralSettings.TEXT_BOX_BACKGROUND_COLOR.x;


    private final String keyFlowchartBoxBGColor_GREEN = "fcbbgcolorgreen";
    private final float fbValueFlowchartBoxBGColor_GREEN = GeneralSettings.TEXT_BOX_BACKGROUND_COLOR.y;


    private final String keyFlowchartBoxBGColor_BLUE = "fcbbgcolorblue";
    private final float fbValueFlowchartBoxBGColor_BLUE = GeneralSettings.TEXT_BOX_BACKGROUND_COLOR.z;

    //Flowchart highlight color
    private final String keyFlowchartBoxHLColor_RED = "fcbhlcolorred";
    private final float fbValueFlowchartBoxHLColor_RED = GeneralSettings.TEXT_COLOR.x;

    private final String keyFlowchartBoxHLColor_GREEN = "fcbhlcolorgreen";
    private final float fbValueFlowchartBoxHLColor_GREEN = GeneralSettings.TEXT_COLOR.y;

    private final String keyFlowchartBoxHLColor_BLUE = "fcbhlcolorblue";
    private final float fbValueFlowchartBoxHLColor_BLUE = GeneralSettings.TEXT_COLOR.z;


    //Flowchart line number background color
    private final String keyFlowchartBoxLineNumberBGColor_RED = "fcblnbgcolorred";
    private final float fbValueFlowchartBoxLineNumberBGColor_RED = GeneralSettings.LINE_NUMBER_BACKGROUND_COLOR.x;

    private final String keyFlowchartBoxLineNumberBGColor_GREEN = "fcblnbgcolorgreen";
    private final float fbValueFlowchartBoxLineNumberColor_GREEN = GeneralSettings.LINE_NUMBER_BACKGROUND_COLOR.y;

    private final String keyFlowchartBoxLineNumberBGColor_BLUE = "fcblnbgcolorblue";
    private final float fbValueFlowchartBoxLineNumberColor_BLUE = GeneralSettings.LINE_NUMBER_BACKGROUND_COLOR.z;


    //Text editor background color
    private final String keyTexteditorBGcolor_RED = "tebgcred";
    private final float fbValueTexteditorBGcolor_RED = GeneralSettings.TEXT_BOX_BACKGROUND_COLOR.x;


    private final String keyTexteditorBGcolor_GREEN = "tebgcgreen";
    private final float fbValueTexteditorBGcolor_GREEN = GeneralSettings.TEXT_BOX_BACKGROUND_COLOR.y;


    private final String keyTexteditorBGcolor_BLUE = "tebgcblue";
    private final float fbValueTexteditorBGcolor_BLUE = GeneralSettings.TEXT_BOX_BACKGROUND_COLOR.z;


    //Text editor line number background color
    private final String keyTexteditorLinenumberBGcolor_RED = "telnbgcred";
    private final float fbValueTexteditorLinenumberBGcolor_RED = GeneralSettings.LINE_NUMBER_BACKGROUND_COLOR.x;


    private final String keyTexteditorLinenumberBGcolor_GREEN = "telnbgcgreen";
    private final float fbValueTexteditorLinenumberBGcolor_GREEN = GeneralSettings.LINE_NUMBER_BACKGROUND_COLOR.y;


    private final String keyTexteditorLinenumberBGcolor_BLUE = "telnbgcblue";
    private final float fbValueTexteditorLinenumberBGcolor_BLUE = GeneralSettings.LINE_NUMBER_BACKGROUND_COLOR.z;


    //Header color
    private final String keyHeaderColor_RED = "headercolorred";
    private final float fbValueHeadercolor_RED = GeneralSettings.HEADER_COLOR.x;


    private final String keyHeaderColor_GREEN = "headercolorgreen";
    private final float fbValueHeadercolor_GREEN = GeneralSettings.HEADER_COLOR.y;


    private final String keyHeaderColor_BLUE = "headercolorblue";
    private final float fbValueHeadercolor_BLUE = GeneralSettings.HEADER_COLOR.z;


    //Menu Button background colors
    private final String keyMenuBtnBGcolor_RED = "menubtnbgcolorred";
    private final float fbValueMenuBtnBGcolor_RED = GeneralSettings.TEXT_BUTTON_BACKGROUND_COLOR.x;


    private final String keyMenuBtnBGcolor_GREEN = "menubtnbgcolorgreen";
    private final float fbValueMenuBtnBGcolor_GREEN = GeneralSettings.TEXT_BUTTON_BACKGROUND_COLOR.y;


    private final String keyMenuBtnBGcolor_BLUE = "";
    private final float fbValueMenuBtnBGcolor_BLUE = GeneralSettings.TEXT_BUTTON_BACKGROUND_COLOR.z;

    //Menu button highlight colors
    private final String KeyMenuBtnHLColor_RED = "menubtnhlcolorred";
    private final float  fbValueMenuBtnHLColor_RED = GeneralSettings.HIGHLIGHT_COLOR.x;

    private final String KeyMenuBtnHLColor_GREEN = "menubtnhlcolorgreen";
    private final float  fbValueMenuBtnHLColor_GREEN = GeneralSettings.HIGHLIGHT_COLOR.y;

    private final String KeyMenuBtnHLColor_BLUE = "menubtnhlcolorblue";
    private final float  fbValueMenuBtnHLColor_BLUE = GeneralSettings.HIGHLIGHT_COLOR.z;

    //Line width settings
    private final String keyMenuDefaultLineWidth = "defaultlinewidth";
    private final float fbValueDefaultLineWidth = GeneralSettings.DEFAULT_LINE_WIDTH;

    private final String keyMenuHighlightedLineWidth = "highlightedlinewidth";
    private final float fbValueHighlightedLineWidth = GeneralSettings.HIGHLIGHTED_LINE_WIDTH;

    //Label color
    private final String keyLabelColor_RED = "labelcolorred";
    private final float fbValueLabelColor_RED = GeneralSettings.labelColor.x;

    private final String keyLabelColor_BLUE = "labelcolorblue";
    private final float fbValueLabelColor_BLUE = GeneralSettings.labelColor.y;

    private final String keyLabelColor_GREEN = "labelcolorgreen";
    private final float fbValueLabelColor_GREEN = GeneralSettings.labelColor.z;

    //Command color
    private final String keyCommandColor_RED = "commandcolorred";
    private final float fbValueCommandColor_RED = GeneralSettings.commandColor.x;

    private final String keyCommandColor_BLUE = "commandcolorblue";
    private final float fbValueCommandColor_BLUE = GeneralSettings.commandColor.y;

    private final String keyCommandColor_GREEN = "commandcolorgreen";
    private final float fbValueCommandColor_GREEN = GeneralSettings.commandColor.z;

    //Branch color
    private final String keyBranchColor_RED = "branchcolorred";
    private final float fbValueBranchColor_RED = GeneralSettings.branchColor.x;

    private final String keyBranchColor_BLUE = "commandcolorblue";
    private final float fbValueBranchColor_BLUE = GeneralSettings.branchColor.y;

    private final String keyBranchColor_GREEN = "branchcolorgreen";
    private final float fbValueBranchColor_GREEN = GeneralSettings.branchColor.z;

    //Error color
    private final String keyErrorColor_RED = "errorcolorred";
    private final float fbValueErrorColor_RED = GeneralSettings.errorColor.x;

    private final String keyErrorColor_BLUE = "errorcolorblue";
    private final float fbValueErrorColor_BLUE = GeneralSettings.errorColor.y;

    private final String keyErrorColor_GREEN = "errorcolorgreen";
    private final float fbValueErrorColor_GREEN = GeneralSettings.errorColor.z;

    //Register color
    private final String keyRegisterColor_RED = "registercolorred";
    private final float fbValueRegisterColor_RED = GeneralSettings.registerColor.x;

    private final String keyRegisterColor_BLUE = "registercolorblue";
    private final float fbValueRegisterColor_BLUE = GeneralSettings.registerColor.y;

    private final String keyRegisterColor_GREEN = "registercolorgreen";
    private final float fbValueRegisterColor_GREEN = GeneralSettings.registerColor.z;

    //Immediate color
    private final String keyImmediateColor_RED = "immediatecolorred";
    private final float fbValueImmediateColor_RED = GeneralSettings.immediateColor.x;

    private final String keyImmediateColor_BLUE = "immediatecolorblue";
    private final float fbValueImmediateColor_BLUE = GeneralSettings.immediateColor.y;

    private final String keyImmediateColor_GREEN = "immediatecolorgreen";
    private final float fbValueImmediateColor_GREEN = GeneralSettings.immediateColor.z;

    //Comment color
    private final String keyCommentColor_RED = "commentcolorred";
    private final float fbValueCommentColor_RED = GeneralSettings.commentColor.x;

    private final String keyCommentColor_BLUE = "commentcolorblue";
    private final float fbValueCommentColor_BLUE = GeneralSettings.commentColor.y;

    private final String keyCommentColor_GREEN = "commentcolorgreen";
    private final float fbValueCommentColor_GREEN = GeneralSettings.commentColor.z;

    //Default color
    private final String keyDefaultColor_RED = "defaultcolorred";
    private final float fbValueDefaultColor_RED = GeneralSettings.defaultColor.x;

    private final String keyDefaultColor_BLUE = "defaultcolorblue";
    private final float fbValueDefaultColor_BLUE = GeneralSettings.defaultColor.y;

    private final String keyDefaultColor_GREEN = "defaultcolorgreen";
    private final float fbValueDefaultColor_GREEN = GeneralSettings.defaultColor.z;

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
    
    //Label Color
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

    //Command Color
    public void setCommandColor(Color bgColor) {
        userPref.putFloat(keyCommandColor_RED, bgColor.getRed()/255f);
        userPref.putFloat(keyCommandColor_GREEN, bgColor.getGreen()/255f);
        userPref.putFloat(keyCommandColor_BLUE, bgColor.getBlue()/255f);
    }

    public void setCommandColor3f(Vector3f bgColor) {
        userPref.putFloat(keyCommandColor_RED, bgColor.x);
        userPref.putFloat(keyCommandColor_GREEN, bgColor.y);
        userPref.putFloat(keyCommandColor_BLUE, bgColor.z);
    }

    public Vector3f getCommandColor3f() {
        Vector3f bgColor = new Vector3f();
        bgColor.setX(userPref.getFloat(keyCommandColor_RED, fbValueCommandColor_RED));
        bgColor.setY(userPref.getFloat(keyCommandColor_GREEN, fbValueCommandColor_GREEN));
        bgColor.setZ(userPref.getFloat(keyCommandColor_BLUE, fbValueCommandColor_BLUE));
        return bgColor;
    }

    public Color getCommandColor() {
        Color bgColor = new Color(
                userPref.getFloat(keyCommandColor_RED, fbValueCommandColor_RED),
                userPref.getFloat(keyCommandColor_GREEN, fbValueCommandColor_GREEN),
                userPref.getFloat(keyCommandColor_BLUE, fbValueCommandColor_BLUE)
        );
        return bgColor;
    }

    //Branch Color
    public void setBranchColor(Color bgColor) {
        userPref.putFloat(keyBranchColor_RED, bgColor.getRed()/255f);
        userPref.putFloat(keyBranchColor_GREEN, bgColor.getGreen()/255f);
        userPref.putFloat(keyBranchColor_BLUE, bgColor.getBlue()/255f);
    }

    public void setBranchColor3f(Vector3f bgColor) {
        userPref.putFloat(keyBranchColor_RED, bgColor.x);
        userPref.putFloat(keyBranchColor_GREEN, bgColor.y);
        userPref.putFloat(keyBranchColor_BLUE, bgColor.z);
    }

    public Vector3f getBranchColor3f() {
        Vector3f bgColor = new Vector3f();
        bgColor.setX(userPref.getFloat(keyBranchColor_RED, fbValueBranchColor_RED));
        bgColor.setY(userPref.getFloat(keyBranchColor_GREEN, fbValueBranchColor_GREEN));
        bgColor.setZ(userPref.getFloat(keyBranchColor_BLUE, fbValueBranchColor_BLUE));
        return bgColor;
    }

    public Color getBranchColor() {
        Color bgColor = new Color(
                userPref.getFloat(keyBranchColor_RED, fbValueBranchColor_RED),
                userPref.getFloat(keyBranchColor_GREEN, fbValueBranchColor_GREEN),
                userPref.getFloat(keyBranchColor_BLUE, fbValueBranchColor_BLUE)
        );
        return bgColor;
    }

    //Error Color
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

    //Register Color
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

    //Immediate Color
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

    //Comment Color
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

    //Default Color
    public void setDefaultColor(Color bgColor) {
        userPref.putFloat(keyDefaultColor_RED, bgColor.getRed()/255f);
        userPref.putFloat(keyDefaultColor_GREEN, bgColor.getGreen()/255f);
        userPref.putFloat(keyDefaultColor_BLUE, bgColor.getBlue()/255f);
    }

    public void setDefaultColor3f(Vector3f bgColor) {
        userPref.putFloat(keyDefaultColor_RED, bgColor.x);
        userPref.putFloat(keyDefaultColor_GREEN, bgColor.y);
        userPref.putFloat(keyDefaultColor_BLUE, bgColor.z);
    }

    public Vector3f getDefaultColor3f() {
        Vector3f bgColor = new Vector3f();
        bgColor.setX(userPref.getFloat(keyDefaultColor_RED, fbValueDefaultColor_RED));
        bgColor.setY(userPref.getFloat(keyDefaultColor_GREEN, fbValueDefaultColor_GREEN));
        bgColor.setZ(userPref.getFloat(keyDefaultColor_BLUE, fbValueDefaultColor_BLUE));
        return bgColor;
    }

    public Color getDefaultColor() {
        Color bgColor = new Color(
                userPref.getFloat(keyDefaultColor_RED, fbValueDefaultColor_RED),
                userPref.getFloat(keyDefaultColor_GREEN, fbValueDefaultColor_GREEN),
                userPref.getFloat(keyDefaultColor_BLUE, fbValueDefaultColor_BLUE)
        );
        return bgColor;
    }
    
    // flowchart connector line widths
    public void setDefaultLineWidth(float lineWidth){
        userPref.putFloat(keyMenuDefaultLineWidth,lineWidth);
    }

    public void setHighlightedLineWidth(float lineWidth){
        userPref.putFloat(keyMenuHighlightedLineWidth,lineWidth);
    }

    public float getDefaultLineWidth(){
        return userPref.getFloat(keyMenuDefaultLineWidth, fbValueDefaultLineWidth);
    }

    public float getHighlightedLineWidth(){
        return userPref.getFloat(keyMenuHighlightedLineWidth, fbValueHighlightedLineWidth);
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




