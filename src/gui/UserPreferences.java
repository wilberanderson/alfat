package gui;

import main.GeneralSettings;

import java.util.prefs.Preferences;
/**
 * This class gets and sets user preferences for ALFAT
 * TODO: Add more settings, create XML or JSON default loader...
 * */
public class UserPreferences {
    private Preferences userPref;

    public UserPreferences() {
        userPref = Preferences.userRoot().node("com/alfat/userprep");
    }


    //Set the default temporary directory name
    //Set the default specify
    public void setUserTempFileDirPath(String dir) {
        userPref.put("tempfilepath", dir);
    }

    public String getUserTempFileDirPath() {
        //Return the user set path. If it's not set it defaults the the temp dir
        return userPref.get("tempfilepath", GeneralSettings.TEMP_DIR);
    }

    //TODO: add better way to remove / reset preferences
    public void removeID() {
        userPref.remove("tempfilepath");
    }
}
