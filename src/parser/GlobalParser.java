package parser;

import parser.LogicScripter.UtilityKeywordTypeMatcher;

/**
 * A global scope manager for parser...
 * @see gui.Header
 * @see controllers.codeWindow.CodeWindowController
 * @see controllers.TextLineController
 * @see ParserManager
 * */
public class GlobalParser {
    public static UtilityKeywordTypeMatcher KEYWORD_PATTERN_MATCHER;
    public static ParserManager PARSER_MANAGER;

    public static void init() {
        KEYWORD_PATTERN_MATCHER = new UtilityKeywordTypeMatcher();
        PARSER_MANAGER = new ParserManager();
    }

}
