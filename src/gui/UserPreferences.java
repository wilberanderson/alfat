package gui;

import main.GeneralSettings;

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
    //TODO: Figure out better way to handle default fallback value if a attribute is not set

    private String keyTempfilepath = "tempfilepath";
    private String fbValueTempfilepath;

    private String keyPreferredFiletype = "preferredfiletype";
    private String fbValuePreferredFiletype = "asm;txt;asm,txt";

    private String keyAutoGenFlowchart = "openautogenflowchart";
    private Boolean fbValueAutoGenFlowchart = false;

    private String keyOpenSplitScreen = "opensplitscreen";
    private Boolean fbValueOpenSplitScreen = true;

    private String keyOpenFullScreen = "openfullscreen";
    private int fbValueOpenfullScreen = 0; //0 = false, 0 > open to editor, 0 < open to flowchart

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




