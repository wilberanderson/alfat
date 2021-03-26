package parser;

import controllers.ApplicationController;
import gui.Notifications.AppEvents;
import gui.texts.EditableFormattedTextLine;
import main.GeneralSettings;
import parser.LogicScripter.Ruler;
import utils.Printer;

import java.io.File;


public class ParserManager {
    private  Parser2 parser;
    private  CodeSyntax codeSyntax;
    private Ruler ruler;

    public ParserManager() {
        this.codeSyntax = null;
        parser = new Parser2();
        updateSyntaxIfNeeded();
    }

    /**
     * Updates the code Syntax if needed in the event of a syntax file path change
     * or on launch.
     * @see gui.Settings.SettingsMenu
     * @see GeneralSettings
     * */
    private void updateSyntaxIfNeeded() {
        //If this is set to true then we need to update the syntax for the parser
        //This is only ever set to true from startup in general settings or
        //the settings menu when the syntax path is changed
        if(GeneralSettings.IS_SYNTAX_PATH_CHANGED) {
            setCodeSyntax(new File(GeneralSettings.USERPREF.getSyntaxPath()));

            if(codeSyntax != null) {
                //Check if the columns Lengths is valid
                if(codeSyntax.isColumnLengthsValid() == false) {
                    codeSyntax.setColumnLengths(null); //Kill it no good can't use
                    //System.out.println(codeSyntax.getColumnLengths());
                    ApplicationController.notification.setEvent(AppEvents.FIXED_FORM_SYNTAX_NOT_SET);
                }

                if(codeSyntax.isParserTokenLogicValid() == false) {
                    codeSyntax.setParserTokenLogic(null); //Kill it's not good can't use!!!
                    ApplicationController.notification.setEvent(AppEvents.FIXED_FORM_SYNTAX_NOT_SET);
                }


                if(codeSyntax.isKeywordsPatternsValid() == false) {
                    codeSyntax = null; // kill it no good can't use
                    ApplicationController.notification.setEvent(AppEvents.INVALID_SYNTAX_FILE);
                } else {
                    parser.setCodeSyntax(codeSyntax);
                }

                if(codeSyntax.isRulerValid() == true) {
                    this.ruler = new Ruler(codeSyntax.getRuler().inner, true);
                } else {
                    this.ruler = null; //KILL it can't be used
                }


            } else {
                ApplicationController.notification.setEvent(AppEvents.INVALID_SYNTAX_FILE);
            }
            GeneralSettings.IS_SYNTAX_PATH_CHANGED = false;
        }
    }

    /**
     * Map the code syntax from the JsonReader. Needs a file path.
     * */
    private void setCodeSyntax(File filePathToJson) {
        this.codeSyntax = null;
        this.codeSyntax = JsonReader.mapJsonToCodeSyntax(filePathToJson);
        //System.out.println(this.codeSyntax); //To see to string of current syntax
    }

    /**
     * Attempts to parse a file using the syntax path provided.
     * If the syntax file path provided can be mapped to JSON then
     * the file is parsed. True is returned. Otherwise false is returned.
     * */
    public boolean attemptFileParse(String filePath) {
        boolean result = false;
        updateSyntaxIfNeeded();
        if(codeSyntax != null) {
            result = true;
            parser.clear();
            parser.ReadFile(filePath,false);
            parser.generateFlowObjects();
            //parse fixed
        }
        return result;
    }

    public boolean attemptPartialFileParse(String filePath, String targetFileTag) {
        boolean result = false;
        updateSyntaxIfNeeded();
        if(codeSyntax != null) {
            result = true;
            GeneralSettings.PARTIAL_FILE_TAG_TARGET = targetFileTag;
            parser.clear();
            parser.ReadFile(filePath,true);
            parser.generateFlowObjects();
        }
        return result;
    }

    /**
     * Returns the parser.
     * */
    public Parser2 getParser() {
        return parser;
    }


    /**
     * If there is a valid syntax file then the parser uses the normal get FormattedLine,
     * Other wise uses the default to white formatted line.
     * */
    public EditableFormattedTextLine getFormattedLine(String line) {
        //Update the syntax path if needed
        updateSyntaxIfNeeded();

        if(codeSyntax != null) {
            return parser.getFormattedLine(line);
        } else {
            return parser.getFormattedLineDefault(line);
        }
    }

    /**Returns true if syntax is valid, returns false if syntax is invalid*/
    public boolean isSyntaxValid() {
        return !(codeSyntax != null);
    }


    /**Returns true or false whether a ruler has been defined in the json
     * defined meaning not null.
     * TODO:Add check to make sure -# can't be provided in.
     * */
    public boolean isRulerValid() {
        boolean result = true;
        if(codeSyntax != null) {
            if(codeSyntax.getRuler() == null) {
                result = false;
            }
        } else {
            result = false;
        }
        return result;
    }


    /**
     * Returns the current ruler. If the ruler is not valid
     * then this will return null.
     * */
    public Ruler getRules() {
        return ruler;
    }





}