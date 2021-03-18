package parser;

import utils.Printer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Chops up a single string then reconstructs:
 * (1) Strings based on double quotes
 * (2) Chars based on single quotes
 * (3) Slash single characters e.g. [\,"] -> [\"]
 * (4) reconstructs a comment into a single line
 * */

public class SimpleTokenizer {

    //The default tokenizer regex
    //private String split = "((?<=\\s)|(?=\\s+))|((?<=,))|(?=\\.)|((?<=\\\")|(?=\\\"))|((?<=\\\\)|(?=\\\\))|((?<=')|(?='))";
    private String splitRegex = "((?<=\\s)|(?=\\s+))|((?<=,))|(?=\\.)|((?<=\\\")|(?=\\\"))|((?<=\\\\)|(?=\\\\))|((?<=')|(?='))|((?<=;)|(?=;))"; //Left to this has default
    private String commentRegex = ";"; //Left to this has default


    private boolean verbose = true;

    public SimpleTokenizer () {
        //DO NOTHING
    }
    public SimpleTokenizer (String splitRegex) {
        this.splitRegex = splitRegex;
    }


    /**
     * Returns a tokenized string in the form of a array of strings.
     * Empty string returned as a empty string.
     * How this works:
     * (1) First string is split up based on split regex
     * (2) argument line is converted into a array list
     * (3) Single quotes are reconstructed
     * (4) Slash characters are reconstructed
     * (5) Double quotes are reconstructed
     * (6) Array primitive is returned
     * */
    public String[] tokenizeString(String arrLineIn) {
        String arrLineOut[] = null;

        arrLineOut = arrLineIn.split(splitRegex);

        List arrayList = new ArrayList<String>(Arrays.asList(arrLineOut));

        if(verbose)
            verbosePrint(arrayList, "After tokenized");

        //Reconstruct single quotes [',a,'] -> ['a']
        reconstructSingleQuotes(arrayList);
        if(verbose)
            verbosePrint(arrayList, "reconstructSingleQuotes");

        //Reconstruct slash chars [\,"] -> [\"]
        reconstructSlashChars(arrayList);
        if(verbose)
           //verbosePrint(arrayList, "reconstructSlashChars");


        //Reconstruct double quotes [",XXXXX,"] -> ["XXXXX"]
        reconstructDoublequotes2(arrayList);
        if(verbose)
            //verbosePrint(arrayList, "reconstructDoublequotes2 DONE");

        //Reconstruct comment line ["something", ";", "something"] -> ["something", ";something"]
        yeetComment(arrayList);
        if(verbose)
            //verbosePrint(arrayList, "yeet comment");

        if(verbose)
            verbosePrint(arrayList, "Final");

        arrLineOut = new String[arrayList.size()];
        arrayList.toArray(arrLineOut);

        return arrLineOut;
    }


    /**
     * Reconstruct single quotes:
     * (1) [',a.'] -> ['a']
     * (2) [',\,"'] -> ['\"']
     * */
    private void reconstructSingleQuotes(List al) {
        String charStr = "";
        //Iterate through tokens
        for (int i = 0; i < al.size(); i++) {
            charStr = "";
            if (al.get(i).equals("'")) {
                //If a single quote is found check 2 in front and if it's a match reconstruct
                //Case [',a,'] -> ['a']
                if(CheckNAhead(2,i,al)) {
                    charStr = getSingleQuote(2,i,al);
                    al.subList(i,i+3).clear();
                    al.add(i,charStr);
                }
                //If a single quote is found and its not 2 in front check 3 and it's a match reconstruct
                //Case [',\,"'] -> ['\"']
               if(CheckNAheadForSlash(3,i,al)) {
                    charStr = getSingleQuote(3,i,al);
                    al.subList(i,i+4).clear();
                    al.add(i,charStr);
                }
            }
        }
    }

    /**
     * Return string after reading between i the start of the index, and n the look ahead amount.
     * */
    private String getSingleQuote(int n, int i, List al) {
        String charStr = "";
        for(int k = i;k <= (i+n);k++) {
            charStr += al.get(k);
        }
        return charStr;
    }

    /**
     * Check if the string after the index i based on the n look ahead amount. If each string is 1 in length then return true.
     * */
    private boolean CheckNAhead(int n, int i,List al) {
        String  check = "";
        if(i+n < al.size()) {
            if(al.get(i+n).equals("'")) {
                //Need to check if only 1 char in string or if \+char else false
                if(((String)al.get(i+1)).length() == 1) {
                    //Example reason: ['\''] we know that [{'}\''] and ['\{'}'] was found
                    //so if \ is in the middle and we have a extra ' at the end
                    //This means that this should be a ['\''] NOT ['\'],[']
                    if (((String)al.get(i+1)).equals("\\")) {
                        if(i+3 < al.size()) {
                            if(((String)al.get(i+3)).equals("'")) {
                                return false;
                            }
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Check if the string after the index i based on the n look ahead amount. If each string is 1 in length and the
     * first one is a \ then return true.
     * */
    private boolean CheckNAheadForSlash(int n, int i,List al) {
        String  check = "";
        if(i+n < al.size()) {
            if(al.get(i+n).equals("'")) {
                //Need to check if only 1 char in string or if \+char else false
                if(((String)al.get(i+1)).equals("\\") && ((String)al.get(i+2)).length() == 1 ) {
                    return true;
                }
            }
        }
        return false;
    }



    /**
     * Reconstructs slash chars into a single string.
     * E.g., [\,"] -> [\"]
     * */
    private void reconstructSlashChars(List al) {
        String temp;
        for(int i = 0; i < al.size(); i++) {
            if(al.get(i).equals("\\")) {
                temp = "";
                if(i+1 < al.size()) {
                    temp += al.get(i);
                    temp +=  al.get(i+1);
                    al.subList(i,i+2).clear();
                    al.add(i, temp);
                    i++;
                }
            }
        }
    }


    /**
     * Reconstructs double quotes into single strings.
     * NOTE: The string policy is:
     * (1) [",XXXXX,"][][",YYYYY,"] -> ["XXXXX"][]["YYYYY"]
     * (2) [;][",XXX,"]["YYY"] -> [;][",XXX,"]["YYY"]  | Ignores the comment
     * (3) [",X,;,Y"][][;,X,Y] -> ["X;Y"][][;,",X,",Y]  | creates string if comment is withing strings but ignores a string at start of comment
     * */
    private void reconstructDoublequotes2(List al) {
        //The end of the double quote index
        int endIndex = 0;
        //Temp string to reconstruct with
        String temp = "";
        //Toggle whether at least one string was found before disregarding the comment character
        boolean oneFound = false;
        //Handles special edge case for when a string is read in like \" something something \"
        //1 is for ", 2 is for \"
        int look4Toggle[] = {1,1,2};

        //i is set to findStartIndex and if the start of a line contains a comment character
        //The whole line is ignored
        for(int i = findStartIndex(al, true, look4Toggle); i < al.size(); i++) {

            //Case implies there is NO double quotes OR a comment exist before any double quotes
            if(i < 0) {
                //STOP
                return;
            }

            //Search for the first double quote
            endIndex = findEndIndex(i,al,look4Toggle);

            //Case to prevent infinite loop if the start index and end index are the same
            if(endIndex == i) {
                //STOP
                return;
            }

            //Case broken string "...... grab everything to end of the string
            if(endIndex < 0) {
                temp = "";
                temp = getDobuleQuoteString(i, al.size()-1, al);
                al.subList(i,al.size()).clear();
                al.add(i,temp);
                //STOP
                i = al.size();
            } else {
                //Case there must be at least 1 more double quote

                //reset str to be empty
                temp = "";
                temp = getDobuleQuoteString(i, endIndex, al);
                al.subList(i,endIndex+1).clear();
                al.add(i,temp);

                //adjust i index for next spot
                i = findStartIndex(al,oneFound, look4Toggle)-1;
                if(i < -1) {
                    //STOP there is no more single double quotes
                    i = al.size();
                }
                //once this is true stop if we find a comment character
                if(!oneFound)
                    oneFound = true;
            }
        }
    }

    /**
     * Returns a string based on a range between the start index and end index of a double quote found
     * */
    private String getDobuleQuoteString(int startIndex, int endIndex, List al) {
        String result = "";
        for(int i = startIndex; i <= endIndex;i++) {
            result += al.get(i);
        }
        return result;
    }

    /**
     * Finds the index of the first double quote
     * Returns -1 if there is not double quote found or a comment character is found.
     * */
    private int findStartIndex(List al, boolean stopIF, int []look4Toggle) {
        for(int i = 0; i < al.size(); i++) {
            //stop if checking for comment character and it's found
            if(stopIF) {
                if(isCommentRegexMatch((String) al.get(i))) {
                    return -1; //STOP
                }
            }
            //Return the start of the quote

            //Return the start of the quote
            if(al.get(i).equals("\"")) {
                look4Toggle[0] = look4Toggle[1];
                return i;
            } else if (al.get(i).equals("\\\"")) {
                look4Toggle[0] = look4Toggle[2];
                //Check if the quote starts with a \" case and return that index
                return i;
            }
        }
        //Start not found
        return -1;
    }

    /**
     * Finds the index of the next double quote.
     * Returns -1 if there is no double quote found.
     * */
    private int findEndIndex(int startIndex,List al, int []look4Toggle) {
        int endIndex = -1;
        //Start one ahead
        if(startIndex+1 < al.size()) {
            startIndex += 1;
        }
        for(int i = startIndex; i < al.size(); i++) {
            if(look4Toggle[0] == look4Toggle[1]) {
                if(al.get(i).equals("\"")) {
                    endIndex = i;
                    //STOP  String
                    i = al.size();
                }
            } else {
                if(al.get(i).equals("\\\"")) {
                    endIndex = i;
                    //STOP  String
                    i = al.size();
                }
            }
        }
        return endIndex;
    }


    /**
     * Reconstructs everything at the start of a comment to the end of line.
     * i.e. yeets the comment
     * */
    public void yeetComment(List al) {
        int indexStart = -1;
        String temp = "";
        for(int i = 0; i < al.size(); i++) {
            //if(al.get(i).equals(";") && indexStart == -1) {
            if(isCommentRegexMatch((String) al.get(i)) && indexStart == -1) {
                indexStart = i;
                temp += al.get(i);
            } else if (indexStart > -1) {
                temp += al.get(i);
            }
        }

        if(indexStart != -1) {
            al.subList(indexStart, al.size()).clear();
            al.add(indexStart, temp);
        }

    }

    /**
     * Returns true if a string matches a comment regex
     * */
    private boolean isCommentRegexMatch(String string) {
        return string.matches(commentRegex);
    }


    /**
     * Set the comment regex
     * @see Parser
     * @see ParserManager
     **/
    public void setCommentRegex(String commentRegex) {
        this.commentRegex = commentRegex;
    }


    /**
     * Prints the array list to see how it looks while being split up and reconstructed...
     * */
    private void verbosePrint(List al, String msg) {
        for (int i = 0; i < al.size(); i++) {
            System.out.print("["+ al.get(i) + "]");
        }
        System.out.println("^ " + msg);
    }

    /**
     * Set to true to see debug in console...
     * */
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    /**
     * Set the split regex...
     * @see Parser
     * @see ParserManager
     * */
    public void setSplitRegex(String splitRegex) {
        this.splitRegex = splitRegex;
    }



}
