package parser;

import gui.texts.EditableFormattedTextLine;
import main.GeneralSettings;

import java.io.File;


public class ParserManager {

    private  Parser parser;

    private  CodeSyntax codeSyntax;


    public ParserManager() {
        this.codeSyntax = null;
        parser = new Parser();
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
                parser.setCodeSyntax(codeSyntax);
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
    public Parser getParser() {
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
}