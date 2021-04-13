package parser.LogicScripter;

/**
 * This is a enum corresponding to the regex schema
 * of the keywords pattern type for a syntax json
 *
 * @see UtilityKeywordTypeMatcher
 * */
public enum KeywordTypes {
    procedurestart,
    procedureend,
    control,
    reserved,
    arithmetic,
    dataMovement,
    register,
    commentLine,
    constantHex,
    constantNumeric,
    constantBinary,
    constantCharacter,
    doubleQuotedString,
    emptySpace,
    label,
    comment,
    separator
}
