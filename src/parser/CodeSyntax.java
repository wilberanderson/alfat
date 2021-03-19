package parser;

import parser.JsonObjects.*;


public class CodeSyntax {

    private KeywordPatterns keywordPatterns;

    public ParserTokenLogic parserTokenLogic;

    public InnerIntArray columnLengths;

    public InnerIntArray ruler;

    public InnerBooleanArray fullSplitColumn;


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

   /**
    * This checks whether the keywords patterns in the json is valid.
    * Each one of these must not be null! If any of them are null
    * then this returns false.
    * */
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

    /**A simple helper function that returns true or false whether a string is null*/
    private boolean checkNotNull(String in) {
        return (in != null);
    }

    /**
    * This checks whether columns from the json is null meaning it is not defined.
    * And checks whether 0 or -1 is in the list. If it is then this means
    * this is a invalid list and should be considered null. And will return false.
    * */
   public boolean isColumnLengthsValid() {
        boolean result = true;
        if (this.columnLengths == null) {
            result = false;
        } else {
          for(Integer inty : columnLengths.inner) {
              if (inty <= 0) {
                  return false;
              }
          }
        }
        return result;
   }

   /**This checks whether the parser token logic is set and whether it is valid.
    * A valid parser token logic must contain each part and each part must not be
    * null. In the columns there must not be any negative numbers and if there is a
    * 0 then it must be the only number in the columns list. If these conditions are not
    * meet then this will return false.
    * TODO:Allow a empty string to be valid meaning don't use at all... ???
    * */
   public boolean isParserTokenLogicValid() {
       boolean result = true;

       if(this.parserTokenLogic != null) {
           result = isMatcherValid(parserTokenLogic.getCommand())
                   &&  isMatcherValid(parserTokenLogic.getControl())
                   && isMatcherValid(parserTokenLogic.getRegister())
                   && isMatcherValid(parserTokenLogic.getImmediate())
                   && isMatcherValid(parserTokenLogic.getLabel())
                   && isMatcherValid(parserTokenLogic.getProcedurestart())
                   && isMatcherValid(parserTokenLogic.getProcedureend())
                   && isMatcherValid(parserTokenLogic.getUserdefined())
                   && isMatcherValid(parserTokenLogic.getComment())
                   && isMatcherValid(parserTokenLogic.getSeparator());

       }else {
           result = false;
       }
       return result;
   }

   private boolean isMatcherValid(OuterIntStrArray check) {
       boolean result = true;
       if(check != null) {
            if(check.getRegexes() != null) {
                //IF regexes are valid
                if(GlobalParser.KEYWORD_PATTERN_MATCHER.isMatchAll(check.getRegexes().inner) == false) {
                    result = false;
                }

            }   else {
                result = false;
            }
            if(check.getColumns() != null) {
                //check if column has a 0 in it and if it is of size 1. Meaning it would be
                //a valid column list implying to check everywhere
                if(check.getColumns().inner.get(0) != 0 || check.getColumns().inner.size() != 1) {
                    //Check that there are no negative numbers and no 0 anywhere
                    for(Integer in : check.getColumns().inner) {
                        if(in <= 0) {
                            result = false;
                        }
                    }
                }
                //TODO:Should we check to make sure that the user can't add a number that is greater than the index????


            } else {
                result = false;
            }
       } else {
           result = false;
       }
       return result;
   }


   /**Checks whether fill split is not null meaning is is not defined in the json.
    * If it is not defined in the json it returns false.*/
   public boolean isfullSplitColumnValid() {
       boolean result = true;
       if(this.fullSplitColumn == null) {
           result = false;
       }
       return result;
   }

   /**
    * checks whether full splitcolumn and columnlengths are the same size
    * which is required. If they are not the same size then this returns false.
    * */
   public boolean isfullSplitColumnAndcolumnLengthsSameSize() {
       boolean result = false;
       if(this.columnLengths != null && this.fullSplitColumn != null) {
           if(this.columnLengths.inner.size() == this.fullSplitColumn.inner.size()) {
               result = true;
           }
       }
       return result;
   }


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
