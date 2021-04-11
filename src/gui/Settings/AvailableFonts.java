package gui.Settings;

import java.io.File;
import java.util.ArrayList;

/**This is a class that stores the paths from the res folder of the available fonts for ALFAT
 * and is used to maintain their paths and names.
 * @see SettingsMenu
 * */
public class AvailableFonts {
    //NOTE: These must be defined in one-to-one order!
    public ArrayList<String> FONT_NAMES = new ArrayList<String>();
    public ArrayList<String> FONT_PATHS = new ArrayList<String>();
    /**
     * Initialize the font paths and font names from the res folder.
     * */
    public AvailableFonts() {
        FONT_NAMES.add("consolas");
        FONT_PATHS.add("/res/fonts/consolas/consolas");
        FONT_NAMES.add("courierNew");
        FONT_PATHS.add("/res/fonts/courierNew/courierNew");
        FONT_NAMES.add("firacode");
        FONT_PATHS.add("/res/fonts/firacode/firacode");
        FONT_NAMES.add("iawritermonos");
        FONT_PATHS.add("/res/fonts/iawritermonos/iawritermonos");
        FONT_NAMES.add("monoid");
        FONT_PATHS.add("/res/fonts/monoid/monoid");
        FONT_NAMES.add("robotomono");
        FONT_PATHS.add("/res/fonts/robotomono/robotomono");
        FONT_NAMES.add("anonymouspro");
        FONT_PATHS.add("/res/fonts/anonymouspro/anonymouspro");
    }

    /**Returns a string array list of font names*/
    public String[] getFontNamesStrings() {
        String[] fontNamesStrings = new String[FONT_NAMES.size()];
        for(int i = 0; i < FONT_NAMES.size(); i++) {
            fontNamesStrings[i] = FONT_NAMES.get(i);
        }
        return fontNamesStrings;
    }


    /**This returns the font path associated with the font name.
     * NULL is returned if no file is found...
     * NOTE: FONT_NAMES and FONT_PATHS must be defined one-to-one*/
    public String getFontPathFromName(String name) {
        String result = null;
        for(int i = 0; i < FONT_NAMES.size(); i++) {
            if(FONT_NAMES.get(i).equals(name)) {
                result = FONT_PATHS.get(i);
                continue;
            }
        }
        return result;
    }


    /**This returns the font name Index
     * 0 is returned if no file is found...
     * NOTE: FONT_NAMES and FONT_PATHS must be defined one-to-one*/
    public int getNameIndexFromPath(String path) {
        int result = 0;

        for(int i = 0; i < FONT_PATHS.size(); i++) {
            if(FONT_PATHS.get(i).equals(path)) {
                result = i;
            }
        }
        return result;
    }



    /**This returns the font name Index from a font name
     * 0 is returned if no file is found...
     * NOTE: FONT_NAMES and FONT_PATHS must be defined one-to-one*/
    public int getNameIndexFromName(String name) {
        int result = 0;

        for(int i = 0; i < FONT_NAMES.size(); i++) {
            if(FONT_NAMES.get(i).equals(name)) {
                result = i;
            }
        }
        return result;
    }










}
