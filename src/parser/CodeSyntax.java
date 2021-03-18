package parser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


public class CodeSyntax {

    private KeywordPatterns keywordPatterns;

    public ParserTokenLogic parserTokenLogic;

    public InnerIntArray columnLengths;

    public InnerIntArray ruler;


    public ParserTokenLogic getParserTokenLogic() {
        return parserTokenLogic;
    }

    public void setParserTokenLogic(ParserTokenLogic parserTokenLogic) {
        this.parserTokenLogic = parserTokenLogic;
    }

    public KeywordPatterns getKeywordPatterns() {
        return keywordPatterns;
    }

    public void setKeywordPatterns(KeywordPatterns keywordPatterns) {
        this.keywordPatterns = keywordPatterns;
    }

    public InnerIntArray getColumnLengths() {
        return columnLengths;
    }

    public void setColumnLengths(InnerIntArray columnLengths) {
        this.columnLengths = columnLengths;
    }

    public InnerIntArray getRuler() {
        return ruler;
    }

    public void setRuler(InnerIntArray ruler) {
        this.ruler = ruler;
    }

    public String getCommands(){
        String combined = "";
        combined += keywordPatterns.getReserved().substring(0,keywordPatterns.getReserved().length()-3)+"|";
        combined += keywordPatterns.getArithmetic().substring(1,keywordPatterns.getArithmetic().length()-3)+"|";
        combined += keywordPatterns.getDataMovement().substring(1,keywordPatterns.getDataMovement().length());
        return combined;
   }

   public boolean isKeywordsPatternsValid() {
        return  checkNotNull(keywordPatterns.getProcedurestart())
        && checkNotNull(keywordPatterns.getProcedureend())
        && checkNotNull(keywordPatterns.getControl())
        && checkNotNull(keywordPatterns.getRegister())
        && checkNotNull(keywordPatterns.getArithmetic())
        && checkNotNull(keywordPatterns.getDataMovement())
        && checkNotNull(keywordPatterns.getRegister())
        && checkNotNull(keywordPatterns.getCommentLine())
        && checkNotNull(keywordPatterns.getConstantHex())
        && checkNotNull(keywordPatterns.getConstantNumeric())
        && checkNotNull(keywordPatterns.getConstantBinary())
        && checkNotNull(keywordPatterns.getConstantCharacter())
        && checkNotNull(keywordPatterns.getDoubleQuotedString())
        && checkNotNull(keywordPatterns.getEmptySpace())
        && checkNotNull(keywordPatterns.getLabel())
        && checkNotNull(keywordPatterns.getComment())
        && checkNotNull(keywordPatterns.getSeparator());


   }

   private boolean checkNotNull(String in) {
        return (in != null);
   }

   //Function to tell whether code has columns

    //Function to return max column len

    //Function to tell what each column size is


    @Override
    public String toString() {
        return "CodeSyntax{" +
                "keywordPatterns=" + keywordPatterns +
                ", parserTokenLogic=" + parserTokenLogic +
                ", columnLengths=" + columnLengths +
                ", ruler=" + ruler +
                '}';
    }
}
