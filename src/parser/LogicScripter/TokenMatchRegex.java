package parser.LogicScripter;

/**
 * This is a interface used to store a list of regexes provided
 * from the parser logic json parameter.
 * @see parser.LogicScripter.ParserLogicScripter
 * */
public interface TokenMatchRegex {
    public boolean regex(String token);
}
