package parser.LogicScripter;

import main.GeneralSettings;

import java.util.ArrayList;

/**
 * Ruler is a array list of floats that represent where to place ruler liens.
 * A single ruler line is a in between character computed for used in LWJGL / OpenGL
 * coordinates as it with respects to the text system of ALFAT.
 * Each ruler line is calculated in the following way:
 * rulerLine = fontSpaceSize * multiplier * (# of char in column)
 * The (# of char in column) is a array list of integers that represent a single line
 * column demarcation from 1 to n.
 * i.e. three columns each 5 long.
 * XXXXX|XXXXX|XXXXX|
 * integers of rulers array [5,10,15]
 * */
public class Ruler extends ArrayList<Float> {
    private final float MULTIPLIER = 2.f; // The default multiplier value
    private final float FONTSIZESAPCE = GeneralSettings.FONT.getSpaceSize(); // The default font size space
    private ArrayList<Integer> jsonRulerColumns = new ArrayList<Integer>(); //The list of ruler columns from the json
    private float fontSizeSpace = FONTSIZESAPCE; //The font size
    private float multiplier = MULTIPLIER; //The multiplier

    public Ruler() {
        //Do nothing
    }

    /**
     * Ruler constructor
     * @param jsonRulerColumns integer list of ruler columns
     * @param fontSizeSpace float font size space
     * @param multiplier float multiplier
     * @param build boolean true or false whether to build ruler values
     */
    public Ruler(ArrayList<Integer> jsonRulerColumns, float fontSizeSpace, float multiplier, boolean build) {
        if(build) {
            setAllAndBuild(jsonRulerColumns, fontSizeSpace, multiplier);
        } else {
            setAll(jsonRulerColumns, fontSizeSpace, multiplier);
        }
    }


    /**
     * Ruler constructor using the default multiplier value
     * @param jsonRulerColumns  integer list of ruler columns
     * @param fontSizeSpace float font size space
     * @param build boolean true or false whether to build ruler values
     */
    public Ruler(ArrayList<Integer> jsonRulerColumns, float fontSizeSpace, boolean build) {
        if(build) {
            setAllAndBuild(jsonRulerColumns, fontSizeSpace, multiplier);
        } else {
            setAll(jsonRulerColumns, fontSizeSpace, multiplier);
        }
    }

    /**
     * Ruler constructor using the default multiplier value and fontSize space
     * @param jsonRulerColumns  integer list of ruler columns
     */
    public Ruler(ArrayList<Integer> jsonRulerColumns, boolean build) {
        if(build) {
            setAllAndBuild(jsonRulerColumns, fontSizeSpace, multiplier);
        } else {
            setAll(jsonRulerColumns, fontSizeSpace, multiplier);
        }
    }



    /**
     * This builds the set of rulers based on the already provided
     * jsonRulerColumns, fontSizeSpace, and multiplier
     * and fills the array list of floats with the computed values.
     * */
    public void buildRulers() {
        //Make sure that a we have a valid json integer list of columns added
        if(jsonRulerColumns != null) {
            //If the float list was already full with values previously clear it
            if(!this.isEmpty()) {
                this.clear();
            }
            //Compute the values and add it to the array of floats
            for (Integer in : jsonRulerColumns) {
                this.add(fontSizeSpace * multiplier * (float)in);
            }
        }
    }


    /**
     * This builds the set of rulers based on provided
     * @param fontSizeSpace float
     * @param multiplier float
     */
    public void buildRulers(float fontSizeSpace, float multiplier) {
        setMultiplier(multiplier);
        setFontSizeSpace(fontSizeSpace);
        buildRulers();
    }

    /**
     * Set all the values for this class
     * @param jsonRulerColumns integer array list
     * @param fontSizeSpace float of font size space
     * @param multiplier float multiplier value
     */
    public void setAll(ArrayList<Integer> jsonRulerColumns, float fontSizeSpace, float multiplier) {
        setFontSizeSpace(fontSizeSpace);
        setMultiplier(multiplier);
        setJsonRulerColumns(jsonRulerColumns);

    }
    /**
     * Set all the values for this class and build (compute) the ruler positions while adding them to the
     * array list of floats
     * @param jsonRulerColumns integer array list
     * @param fontSizeSpace float of font size space
     * @param multiplier float multiplier value
     */
    public void setAllAndBuild(ArrayList<Integer> jsonRulerColumns, float fontSizeSpace, float multiplier) {
        setFontSizeSpace(fontSizeSpace);
        setMultiplier(multiplier);
        setJsonRulerColumnsAndBuild(jsonRulerColumns);

    }

    /**
     * @return float of font size space
     */
    public float getFontSizeSpace() {
        return fontSizeSpace;
    }

    /**
     * @param fontSizeSpace set the float font size space
     */
    public void setFontSizeSpace(float fontSizeSpace) {
        this.fontSizeSpace = fontSizeSpace;
    }

    /**
     * @return return the multiplier value
     */
    public float getMultiplier() {
        return multiplier;
    }

    /**
     * @param multiplier float multiplier value
     */
    public void setMultiplier(float multiplier) {
        this.multiplier = multiplier;
    }


    /**
     * @return a array list of integer ruler columns
     */
    public ArrayList<Integer> getJsonRulerColumns() {
        return jsonRulerColumns;
    }

    public void setJsonRulerColumnsAndBuild(ArrayList<Integer> jsonRulerColumns) {
        if(jsonRulerColumns != null) {
            this.jsonRulerColumns = jsonRulerColumns;
            buildRulers();
        }
    }

    /**
     * @param jsonRulerColumns add a array list of integer ruler columns
     */
    public void setJsonRulerColumns(ArrayList<Integer> jsonRulerColumns) {
        if(jsonRulerColumns != null) {
            this.jsonRulerColumns = jsonRulerColumns;
        }
    }


}
