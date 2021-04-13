package parser.LogicScripter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility KeywordPatterns Type Matcher is a helper class
 * that provides matchers to words from the keywords patterns
 * json schema.
 *
 * This contains a compiled list a keywords used in the JSON
 * and their corresponding regexes and matcher functions.
 * @see parser.GlobalParser
 * @see parser.LogicScripter.ParserLogicScripter
 * @see parser.CodeSyntax
 *
 * TODO: Find better location for this...
 * */
public class UtilityKeywordTypeMatcher {


    private Map<KeywordTypes, String> keywordTypesregexMap = new HashMap<>();
    private ArrayList<String> keywordTypesregexList = new ArrayList<>();

    public UtilityKeywordTypeMatcher() {
        init();
    }


    /**Initialize the keyword regexes map and arraylist*/
    private void init() {
        keywordTypesregexMap.put(KeywordTypes.procedurestart, "(?i)(procedurestart)");
        keywordTypesregexList.add("(?i)(procedurestart)");
        keywordTypesregexMap.put(KeywordTypes.procedureend, "(?i)(procedureend)");
        keywordTypesregexList.add("(?i)(procedureend)");
        keywordTypesregexMap.put(KeywordTypes.control, "(?i)(control)");
        keywordTypesregexList.add("(?i)(control)");
        keywordTypesregexMap.put(KeywordTypes.reserved, "(?i)(reserved)");
        keywordTypesregexList.add("(?i)(reserved)");
        keywordTypesregexMap.put(KeywordTypes.arithmetic, "(?i)(arithmetic)");
        keywordTypesregexList.add("(?i)(arithmetic)");
        keywordTypesregexMap.put(KeywordTypes.dataMovement, "(?i)(dataMovement)");
        keywordTypesregexList.add("(?i)(dataMovement)");
        keywordTypesregexMap.put(KeywordTypes.register, "(?i)(register)");
        keywordTypesregexList.add("(?i)(register)");
        keywordTypesregexMap.put(KeywordTypes.commentLine, "(?i)(commentLine)");
        keywordTypesregexList.add("(?i)(commentLine)");
        keywordTypesregexMap.put(KeywordTypes.constantHex, "(?i)(constantHex)");
        keywordTypesregexList.add("(?i)(constantHex)");
        keywordTypesregexMap.put(KeywordTypes.constantNumeric, "(?i)(constantNumeric)");
        keywordTypesregexList.add("(?i)(constantNumeric)");
        keywordTypesregexMap.put(KeywordTypes.constantBinary, "(?i)(constantBinary)");
        keywordTypesregexList.add("(?i)(constantBinary)");
        keywordTypesregexMap.put(KeywordTypes.constantCharacter, "(?i)(constantCharacter)");
        keywordTypesregexList.add("(?i)(constantCharacter)");
        keywordTypesregexMap.put(KeywordTypes.doubleQuotedString, "(?i)(doubleQuotedString)");
        keywordTypesregexList.add("(?i)(doubleQuotedString)");
        keywordTypesregexMap.put(KeywordTypes.emptySpace, "(?i)(emptySpace)");
        keywordTypesregexList.add("(?i)(emptySpace)");
        keywordTypesregexMap.put(KeywordTypes.label, "(?i)(label)");
        keywordTypesregexList.add("(?i)(label)");
        keywordTypesregexMap.put(KeywordTypes.comment, "(?i)(comment)");
        keywordTypesregexList.add("(?i)(comment)");
        keywordTypesregexMap.put(KeywordTypes.separator, "(?i)(separator)");
        keywordTypesregexList.add("(?i)(separator)");
    }


    /**
     * matches a string to a keyword type.
     * @param type the keyword type
     * @param in the string in
     * @return true or false
     */
    public boolean isMatch(KeywordTypes type, String in) {
        return in.matches(keywordTypesregexMap.get(type));
    }


    /**
     * This is used to tell whether a list of strings
     * matches the defined regex keyword patters from
     * the json schema
     * @param in a arraylist of strings
     * @return true or false
     */
    public boolean isMatchAll(ArrayList<String> in) {
        boolean result = true;
        int truth = 0;

        for (int i = 0; i < in.size(); i++) {
            for(String regex : keywordTypesregexList) {
                if(in.get(i).matches(regex)) {
                    truth += 1;
                }
            }
        }
        if(truth != in.size()) {
            result = false;
        }

        return result;
    }
}
