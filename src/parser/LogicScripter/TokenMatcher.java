package parser.LogicScripter;

/**
 * This is a interface used to dynamically match tokens based
 * on the regexes provided from the JSON parserLogic.
 * @see parser.LogicScripter.ParserLogicScripter
 * */
public interface TokenMatcher {
    public boolean isMatch(String token);
}
