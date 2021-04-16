package parser.LogicScripter;


import parser.CodeSyntax;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ParserLogicScripter {
    //Needs the syntax to assign matching
    private CodeSyntax syn;
    public boolean procedureMatching = false;

    public ParserLogicScripter(CodeSyntax codeSyntax) {
        this.syn = codeSyntax;
        buildMatchers();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Default Token Matchers MUST be provided in the event that the JSON parser logic is not set
    /**
     * <pre>
     *     Anything that is a command word
     * </pre>
     * */
    public TokenMatcher commandMatcher = new TokenMatcher() {
        @Override
        public boolean isMatch(String token,int column) {
            return token.matches(syn.getKeywordPatterns().getReserved()) ||
                    token.matches(syn.getKeywordPatterns().getArithmetic()) ||
                    token.matches(syn.getKeywordPatterns().getDataMovement());
        }
    };

    /**
     * <pre>
     *     Anything that is control word
     * </pre>
     * */
    public TokenMatcher controlMatcher = new TokenMatcher() {
        @Override
        public boolean isMatch(String token,int column) {
            return token.matches(syn.getKeywordPatterns().getControl());
        }
    };

    /**
     * <pre>
     *     Anything that is a register
     * </pre>
     * */
    public TokenMatcher registerMatcher = new TokenMatcher() {
        @Override
        public boolean isMatch(String token,int column) {
            return token.matches(syn.getKeywordPatterns().getRegister());
        }
    };

    /**
     * <pre>
     *     Anything that is a immediate value
     *     e,g. a number
     * </pre>
     * */
    public TokenMatcher immediateMatcher = new TokenMatcher() {
        @Override
        public boolean isMatch(String token,int column) {
            return token.matches(syn.getKeywordPatterns().getConstantNumeric())
                    || token.matches(syn.getKeywordPatterns().getConstantHex())
                    || token.matches(syn.getKeywordPatterns().getConstantNumeric());
        }
    };

    /**
     * <pre>
     *    anything that is a label
     * </pre>
     * */
    public TokenMatcher labelMatcher = new TokenMatcher() {
        @Override
        public boolean isMatch(String token,int column) {
            return token.matches(syn.getKeywordPatterns().getLabel());
        }
    };

    /**
     * <pre>
     *    Anything that is a start of a procedure
     * </pre>
     * */
    public TokenMatcher procedureStartmatcher = new TokenMatcher() {
        @Override
        public boolean isMatch(String token,int column) {
            return token.matches(syn.getKeywordPatterns().getProcedurestart());
        }
    };

    /**
     * <pre>
     *    Anything that is a end  of a procedure
     * </pre>
     * */
    public TokenMatcher procedureEndmatcher = new TokenMatcher() {
        @Override
        public boolean isMatch(String token,int column) {
            return token.matches(syn.getKeywordPatterns().getProcedureend());
        }
    };

    /**
     * <pre>
     *    Any none keyword that is not a immediate.
     *    Could be a string or some user defined word.
     * </pre>
     * */
    public TokenMatcher userDefinedMatcher = new TokenMatcher() {
        @Override
        public boolean isMatch(String token,int column) {
            return token.matches(syn.getKeywordPatterns().getDoubleQuotedString()) ||
                    token.matches(syn.getKeywordPatterns().getLabel());
        }
    };
    /**
     * <pre>
     *    Anything that could be a comment
     * </pre>
     * */
    public TokenMatcher commentMatcher = new TokenMatcher() {
        @Override
        public boolean isMatch(String token,int column) {
            return token.matches(syn.getKeywordPatterns().getCommentLine());
        }
    };

    /**
     * <pre>
     *    Anything that could be a separator token
     *    e.g., commas or anything that is a non-white space non tab character.
     * </pre>
     * */
    public TokenMatcher separatorMatcher = new TokenMatcher() {
        @Override
        public boolean isMatch(String token,int column) {
            return token.matches(syn.getKeywordPatterns().getSeparator());
        }
    };


    /**
     * This is not defined from the CodeSyntax
     * <pre>
     *     Any white space character(s)
     *     e.g., "   ", " ",
     * </pre>
     * */
    public TokenMatcher whitespaceMatcher = new TokenMatcher() {
        @Override
        public boolean isMatch(String token, int column) {
            return token.matches("(^([ \t\\s]+)([ \t\\s])$)|([ \t\\s])");
        }
    };


    /**
     * This is not defined from the CodeSyntax
     * <pre>
     *     Any tab character e.g.,\t"
     * </pre>
     * */
    public TokenMatcher tabMatcher = new TokenMatcher() {
        @Override
        public boolean isMatch(String token, int column) {
            return token.matches("(^([\t]+)([\t])$)|([\t])");
        }
    };


    /**
     * This generates the matchers on the fly based on the json regexes defined
     * */
    private void buildMatchers() {
        //If the JSON provides a parser token logic we build them over using the default ones that are hard coded
        if(syn.getParserTokenLogic() != null) {
            // Procedure start/end boolean
            if (syn.getKeywordPatterns().getProcedurestart().isBlank() || syn.getKeywordPatterns().getProcedureend().isBlank()){
                procedureMatching = false;
            }


            //CommandMatcher
            if(syn.getParserTokenLogic().getCommand() != null) {
                //Get the regexes from jsons
                ArrayList<TokenMatchRegex> regexTempList = new ArrayList<TokenMatchRegex>();
                for(String parameterName : syn.getParserTokenLogic().getCommand().getRegexes().inner) {
                    addRegexes(regexTempList,parameterName);
                }
                commandMatcher = new TokenMatcher() {
                    ArrayList<TokenMatchRegex> regexList = regexTempList;
                    //Get the columns to check from json
                    ArrayList<Integer> columnstoCheck = syn.getParserTokenLogic().getCommand().getColumns().inner;
                    //Check whether the columns is only 1 and contains 0 meaning we want to check regex every time
                    boolean alwaysRun = syn.getParserTokenLogic().getCommand().getColumns().inner.size() == 1
                            && syn.getParserTokenLogic().getCommand().getColumns().inner.get(0) == 0;


                    @Override
                    public boolean isMatch(String token,int column) {
                        boolean result = false;

                        if(columnstoCheck.contains(column) || alwaysRun) {
                            //System.out.println("token" + "[" + token + "]" + " match: " + result + " column index: " +  column);
                            //Do the regex check
                            for(TokenMatchRegex matcher : regexList) {
                                result = result || matcher.regex(token);
                            }
                        }



                        return result;
                    }
                };
            }


            //__________________________________________________________________________________________________
            //Control Matcher
            if(syn.getParserTokenLogic().getControl() != null) {
                ArrayList<TokenMatchRegex> regexTempList = new ArrayList<TokenMatchRegex>();
                for(String parameterName : syn.getParserTokenLogic().getControl().getRegexes().inner) {
                    addRegexes(regexTempList,parameterName);
                }
                controlMatcher = new TokenMatcher() {
                    ArrayList<TokenMatchRegex> regexList = regexTempList;

                    ArrayList<Integer> columnstoCheck = syn.getParserTokenLogic().getControl().getColumns().inner;
                    //Check whether the columns is only 1 and contains 0 meaning we want to check regex every time
                    boolean alwaysRun = syn.getParserTokenLogic().getControl().getColumns().inner.size() == 1
                            && syn.getParserTokenLogic().getControl().getColumns().inner.get(0) == 0;

                    @Override
                    public boolean isMatch(String token,int column) {
                        boolean result = false;
                        if(columnstoCheck.contains(column) || alwaysRun) {
                            //Do the regex check
                            for(TokenMatchRegex matcher : regexList) {
                                result = result || matcher.regex(token);
                            }
                        }
                        return result;
                    }
                };
            }
            //__________________________________________________________________________________________________
            //Register Matcher
            if(syn.getParserTokenLogic().getRegister() != null) {
                ArrayList<TokenMatchRegex> regexTempList = new ArrayList<TokenMatchRegex>();
                for(String parameterName : syn.getParserTokenLogic().getRegister().getRegexes().inner) {
                    addRegexes(regexTempList,parameterName);
                }
                registerMatcher = new TokenMatcher() {
                    ArrayList<TokenMatchRegex> regexList = regexTempList;

                    ArrayList<Integer> columnstoCheck = syn.getParserTokenLogic().getRegister().getColumns().inner;
                    //Check whether the columns is only 1 and contains 0 meaning we want to check regex every time
                    boolean alwaysRun = syn.getParserTokenLogic().getRegister().getColumns().inner.size() == 1
                            && syn.getParserTokenLogic().getRegister().getColumns().inner.get(0) == 0;


                    @Override
                    public boolean isMatch(String token,int column) {
                        boolean result = false;

                        if(columnstoCheck.contains(column) || alwaysRun) {
                            //Do the regex check
                            for(TokenMatchRegex matcher : regexList) {
                                result = result || matcher.regex(token);
                            }
                        }

                        return result;
                    }
                };
            }
            //__________________________________________________________________________________________________
            //Immediate Matcher
            if(syn.getParserTokenLogic().getImmediate() != null) {
                ArrayList<TokenMatchRegex> regexTempList = new ArrayList<TokenMatchRegex>();
                for(String parameterName : syn.getParserTokenLogic().getImmediate().getRegexes().inner) {
                    addRegexes(regexTempList,parameterName);
                }
                immediateMatcher = new TokenMatcher() {
                    ArrayList<TokenMatchRegex> regexList = regexTempList;

                    ArrayList<Integer> columnstoCheck = syn.getParserTokenLogic().getImmediate().getColumns().inner;
                    //Check whether the columns is only 1 and contains 0 meaning we want to check regex every time
                    boolean alwaysRun = syn.getParserTokenLogic().getImmediate().getColumns().inner.size() == 1
                            && syn.getParserTokenLogic().getImmediate().getColumns().inner.get(0) == 0;

                    @Override
                    public boolean isMatch(String token,int column) {
                        boolean result = false;
                        if(columnstoCheck.contains(column) || alwaysRun) {
                            //Do the regex check
                            for(TokenMatchRegex matcher : regexList) {
                                result = result || matcher.regex(token);
                            }
                        }
                        return result;
                    }
                };
            }
            //__________________________________________________________________________________________________
            //Label Matcher
            if(syn.getParserTokenLogic().getLabel() != null) {
                ArrayList<TokenMatchRegex> regexTempList = new ArrayList<TokenMatchRegex>();
                for(String parameterName : syn.getParserTokenLogic().getLabel().getRegexes().inner) {
                    addRegexes(regexTempList,parameterName);
                }
                labelMatcher = new TokenMatcher() {
                    ArrayList<TokenMatchRegex> regexList = regexTempList;
                    //Get the columns to check from json
                    ArrayList<Integer> columnstoCheck = syn.getParserTokenLogic().getLabel().getColumns().inner;
                    //Check whether the columns is only 1 and contains 0 meaning we want to check regex every time
                    boolean alwaysRun = syn.getParserTokenLogic().getLabel().getColumns().inner.size() == 1
                            && syn.getParserTokenLogic().getLabel().getColumns().inner.get(0) == 0;
                    @Override
                    public boolean isMatch(String token,int column) {
                        boolean result = false;


                        if(columnstoCheck.contains(column) || alwaysRun) {
                            //System.out.println("token" + "[" + token + "]" + " match: " + result + " column index: " +  column);
                            for (TokenMatchRegex matcher : regexList) {
                                result = result || matcher.regex(token);
                            }
                        }


                        return result;
                    }
                };
            }
            //__________________________________________________________________________________________________
            //Procedure Start Matcher
            if(syn.getParserTokenLogic().getProcedurestart() != null) {
                ArrayList<TokenMatchRegex> regexTempList = new ArrayList<TokenMatchRegex>();
                for(String parameterName : syn.getParserTokenLogic().getProcedurestart().getRegexes().inner) {
                    addRegexes(regexTempList,parameterName);
                }
                procedureStartmatcher = new TokenMatcher() {
                    ArrayList<TokenMatchRegex> regexList = regexTempList;

                    ArrayList<Integer> columnstoCheck = syn.getParserTokenLogic().getProcedurestart().getColumns().inner;
                    //Check whether the columns is only 1 and contains 0 meaning we want to check regex every time
                    boolean alwaysRun = syn.getParserTokenLogic().getProcedurestart().getColumns().inner.size() == 1
                            && syn.getParserTokenLogic().getProcedurestart().getColumns().inner.get(0) == 0;

                    @Override
                    public boolean isMatch(String token,int column) {
                        boolean result = false;
                        if(columnstoCheck.contains(column) || alwaysRun) {
                            //Do the regex check
                            for(TokenMatchRegex matcher : regexList) {
                                result = result || matcher.regex(token);
                            }
                        }
                        return result;
                    }
                };
            }
            //__________________________________________________________________________________________________
            //Procedure End Matcher
            if(syn.getParserTokenLogic().getProcedureend() != null) {
                ArrayList<TokenMatchRegex> regexTempList = new ArrayList<TokenMatchRegex>();
                for(String parameterName : syn.getParserTokenLogic().getProcedureend().getRegexes().inner) {
                    addRegexes(regexTempList,parameterName);
                }
                procedureEndmatcher = new TokenMatcher() {
                    ArrayList<TokenMatchRegex> regexList = regexTempList;

                    ArrayList<Integer> columnstoCheck = syn.getParserTokenLogic().getProcedureend().getColumns().inner;
                    //Check whether the columns is only 1 and contains 0 meaning we want to check regex every time
                    boolean alwaysRun = syn.getParserTokenLogic().getProcedureend().getColumns().inner.size() == 1
                            && syn.getParserTokenLogic().getProcedureend().getColumns().inner.get(0) == 0;

                    @Override
                    public boolean isMatch(String token,int column) {
                        boolean result = false;
                        if(columnstoCheck.contains(column) || alwaysRun) {
                            //Do the regex check
                            for(TokenMatchRegex matcher : regexList) {
                                result = result || matcher.regex(token);
                            }
                        }
                        return result;
                    }
                };
            }
            //__________________________________________________________________________________________________
            //User Defined Matcher
            if(syn.getParserTokenLogic().getUserdefined() != null) {
                ArrayList<TokenMatchRegex> regexTempList = new ArrayList<TokenMatchRegex>();
                for(String parameterName : syn.getParserTokenLogic().getUserdefined().getRegexes().inner) {
                    addRegexes(regexTempList,parameterName);
                }
                userDefinedMatcher = new TokenMatcher() {
                    ArrayList<TokenMatchRegex> regexList = regexTempList;

                    ArrayList<Integer> columnstoCheck = syn.getParserTokenLogic().getUserdefined().getColumns().inner;
                    //Check whether the columns is only 1 and contains 0 meaning we want to check regex every time
                    boolean alwaysRun = syn.getParserTokenLogic().getUserdefined().getColumns().inner.size() == 1
                            && syn.getParserTokenLogic().getUserdefined().getColumns().inner.get(0) == 0;


                    @Override
                    public boolean isMatch(String token,int column) {
                        boolean result = false;
                        if(columnstoCheck.contains(column) || alwaysRun) {
                            //Do the regex check
                            for(TokenMatchRegex matcher : regexList) {
                                result = result || matcher.regex(token);
                            }
                        }
                        return result;
                    }
                };
            }
            //__________________________________________________________________________________________________
            //Comment Matcher
            if(syn.getParserTokenLogic().getComment() != null) {
                ArrayList<TokenMatchRegex> regexTempList = new ArrayList<TokenMatchRegex>();
                for(String parameterName : syn.getParserTokenLogic().getComment().getRegexes().inner) {
                    addRegexes(regexTempList,parameterName);
                }
                commentMatcher = new TokenMatcher() {
                    ArrayList<TokenMatchRegex> regexList = regexTempList;

                    ArrayList<Integer> columnstoCheck = syn.getParserTokenLogic().getComment().getColumns().inner;
                    //Check whether the columns is only 1 and contains 0 meaning we want to check regex every time
                    boolean alwaysRun = syn.getParserTokenLogic().getComment().getColumns().inner.size() == 1
                            && syn.getParserTokenLogic().getComment().getColumns().inner.get(0) == 0;

                    @Override
                    public boolean isMatch(String token,int column) {
                        boolean result = false;
                        if(columnstoCheck.contains(column) || alwaysRun) {
                            //Do the regex check
                            for(TokenMatchRegex matcher : regexList) {
                                result = result || matcher.regex(token);
                            }
                        }
                        return result;
                    }
                };
            }
            //__________________________________________________________________________________________________
            //Separator Matcher
            if(syn.getParserTokenLogic().getSeparator() != null) {
                ArrayList<TokenMatchRegex> regexTempList = new ArrayList<TokenMatchRegex>();
                for(String parameterName : syn.getParserTokenLogic().getSeparator().getRegexes().inner) {
                    addRegexes(regexTempList,parameterName);
                }
                separatorMatcher = new TokenMatcher() {
                    ArrayList<TokenMatchRegex> regexList = regexTempList;

                    ArrayList<Integer> columnstoCheck = syn.getParserTokenLogic().getSeparator().getColumns().inner;
                    //Check whether the columns is only 1 and contains 0 meaning we want to check regex every time
                    boolean alwaysRun = syn.getParserTokenLogic().getSeparator().getColumns().inner.size() == 1
                            && syn.getParserTokenLogic().getSeparator().getColumns().inner.get(0) == 0;

                    @Override
                    public boolean isMatch(String token,int column) {
                        boolean result = false;
                        if(columnstoCheck.contains(column) || alwaysRun) {
                            //Do the regex check
                            for(TokenMatchRegex matcher : regexList) {
                                result = result || matcher.regex(token);
                            }
                        }
                        return result;
                    }
                };
            }
        }

    }

    /**
     * Adds the the list of regexes from the parser to the list of Token Matches.
     * NOTE: This list of matches must be identical to the keywordsPatterns from the JSON
     * there is NO validation here.
     * TODO: Add validation to parser manager.
     * */
    private void addRegexes(ArrayList<TokenMatchRegex> regexTempList, String parameterName) {
        //Control
        if(parameterName.matches("(?i)(control)")) {
           regexTempList.add(new TokenMatchRegex() {
               @Override
               public boolean regex(String token) {
                   return token.matches(syn.getKeywordPatterns().getControl());
               }
           });
        } else if(parameterName.matches("(?i)(reserved)")) {
            regexTempList.add(new TokenMatchRegex() {
                @Override
                public boolean regex(String token) {
                    return token.matches(syn.getKeywordPatterns().getReserved());
                }
            });
        } else if(parameterName.matches("(?i)(arithmetic)")) {
            regexTempList.add(new TokenMatchRegex() {
                @Override
                public boolean regex(String token) {
                    return token.matches(syn.getKeywordPatterns().getArithmetic());
                }
            });
        } else if(parameterName.matches("(?i)(dataMovement)")) {
            regexTempList.add(new TokenMatchRegex() {
                @Override
                public boolean regex(String token) {
                    return token.matches(syn.getKeywordPatterns().getDataMovement());
                }
            });
        } else if(parameterName.matches("(?i)(register)")) {
            regexTempList.add(new TokenMatchRegex() {
                @Override
                public boolean regex(String token) {
                    return token.matches(syn.getKeywordPatterns().getRegister());
                }
            });
        } else if(parameterName.matches("(?i)(commentLine)")) {
            regexTempList.add(new TokenMatchRegex() {
                @Override
                public boolean regex(String token) {
                    return token.matches(syn.getKeywordPatterns().getCommentLine());
                }
            });
        } else if(parameterName.matches("(?i)(constantHex)")) {
            regexTempList.add(new TokenMatchRegex() {
                @Override
                public boolean regex(String token) {
                    return token.matches(syn.getKeywordPatterns().getConstantHex());
                }
            });
        } else if(parameterName.matches("(?i)(constantNumeric)")) {
            regexTempList.add(new TokenMatchRegex() {
                @Override
                public boolean regex(String token) {
                    return token.matches(syn.getKeywordPatterns().getConstantNumeric());
                }
            });
        }else if(parameterName.matches("(?i)(constantBinary)")) {
            regexTempList.add(new TokenMatchRegex() {
                @Override
                public boolean regex(String token) {
                    return token.matches(syn.getKeywordPatterns().getConstantBinary());
                }
            });
        }else if(parameterName.matches("(?i)(doubleQuotedString)")) {
            regexTempList.add(new TokenMatchRegex() {
                @Override
                public boolean regex(String token) {
                    return token.matches(syn.getKeywordPatterns().getDoubleQuotedString());
                }
            });
        }else if(parameterName.matches("(?i)(emptySpace)")) {
            regexTempList.add(new TokenMatchRegex() {
                @Override
                public boolean regex(String token) {
                    return token.matches(syn.getKeywordPatterns().getEmptySpace());
                }
            });
        }else if(parameterName.matches("(?i)(label)")) {
            regexTempList.add(new TokenMatchRegex() {
                @Override
                public boolean regex(String token) {
                    return token.matches(syn.getKeywordPatterns().getLabel());
                }
            });
        }else if(parameterName.matches("(?i)(comment)")) {
            regexTempList.add(new TokenMatchRegex() {
                @Override
                public boolean regex(String token) {
                    return token.matches(syn.getKeywordPatterns().getComment());
                }
            });
        }else if(parameterName.matches("(?i)(procedurestart)")) {
            regexTempList.add(new TokenMatchRegex() {
                @Override
                public boolean regex(String token) {
                    return token.matches(syn.getKeywordPatterns().getProcedurestart());
                }
            });
        }else if(parameterName.matches("(?i)(procedureend)")) {
            regexTempList.add(new TokenMatchRegex() {
                @Override
                public boolean regex(String token) {
                    return token.matches(syn.getKeywordPatterns().getProcedureend());
                }
            });
        }else if(parameterName.matches("(?i)(separator)")) {
            regexTempList.add(new TokenMatchRegex() {
                @Override
                public boolean regex(String token) {
                    return token.matches(syn.getKeywordPatterns().getSeparator());
                }
            });
        }
    }

    /*
    //Removed for now might be used to completely script the parser order
    String parserOrderString = "command,control,register, immediate, label, procedure_start, procedure_end, user_defined, comment, separator, error";
    ArrayList<ParserTokenTypes> parserOrder = new ArrayList<ParserTokenTypes>();
    public void buildParserOrder() {
        String [] list = parserOrderString.split("[, ]");
        for(String str: list) {
            if(str.matches("(?i)(command)\\b")) {
                parserOrder.add(ParserTokenTypes.COMMAND);
            } else if(str.matches("(?i)(control)\\b")) {
                parserOrder.add(ParserTokenTypes.CONTROL);
            }else if(str.matches("(?i)(register)\\b")) {
                parserOrder.add(ParserTokenTypes.REGISTER);
            }else if(str.matches("(?i)(IMMEDIATE)\\b")) {
                parserOrder.add(ParserTokenTypes.IMMEDIATE);
            }else if(str.matches("(?i)(LABEL)\\b")) {
                parserOrder.add(ParserTokenTypes.LABEL);
            }else if(str.matches("(?i)(PROCEDURE_START)\\b")) {
                parserOrder.add(ParserTokenTypes.PROCEDURE_START);
            }else if(str.matches("(?i)(PROCEDURE_END)\\b")) {
                parserOrder.add(ParserTokenTypes.PROCEDURE_END);
            }else if(str.matches("(?i)(USER_DEFINED)\\b")) {
                parserOrder.add(ParserTokenTypes.USER_DEFINED);
            }else if(str.matches("(?i)(COMMENT)\\b")) {
                parserOrder.add(ParserTokenTypes.COMMENT);
            }else if(str.matches("(?i)(SEPARATOR)\\b")) {
                parserOrder.add(ParserTokenTypes.SEPARATOR);
            }else if(str.matches("(?i)(ERROR)\\b")) {
                parserOrder.add(ParserTokenTypes.ERROR);
            }
            //System.out.println(str);
        }
        System.out.println(parserOrder);
    }
*/
}
