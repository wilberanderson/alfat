package parser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


public class CodeSyntax {

    private KeywordPatterns keywordPatterns;

    public InnerIntArray columnLengths;

    public InnerIntArray ruler;


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
        boolean result = false;
        result = checkNotNull(keywordPatterns.getControl());
        result = checkNotNull(keywordPatterns.getRegister());
        result = checkNotNull(keywordPatterns.getArithmetic());
        result = checkNotNull(keywordPatterns.getDataMovement());
        result = checkNotNull(keywordPatterns.getRegister());
        result = checkNotNull(keywordPatterns.getCommentLine());
        result = checkNotNull(keywordPatterns.getConstantHex());
        result = checkNotNull(keywordPatterns.getConstantNumeric());
        result = checkNotNull(keywordPatterns.getConstantBinary());
        result = checkNotNull(keywordPatterns.getConstantCharacter());
        result = checkNotNull(keywordPatterns.getDoubleQuotedString());
        result = checkNotNull(keywordPatterns.getEmptySpace());
        result = checkNotNull(keywordPatterns.getLabel());
        result = checkNotNull(keywordPatterns.getComment());
        return result;
   }

   private boolean checkNotNull(String in) {
        return in == null;
   }

   //Function to tell whether code has columns

    //Function to return max column len

    //Function to tell what each column size is


    @Override
    public String toString() {
        return "CodeSyntax{" +
                "keywordPatterns=" + keywordPatterns +
                ", columnLengths=" + columnLengths +
                ", ruler=" + ruler +
                '}';
    }
}
