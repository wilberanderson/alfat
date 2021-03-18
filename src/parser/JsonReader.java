package parser;


import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;
import parser.JsonObjects.KeywordPatterns;

/**
 * Takes a file as an input JSON and creates and object mapper based on the defined syntax in JSON to a Syntax object.
*/
public class JsonReader {

    /**
     * Maps a provided syntax file to the CodeSyntax class.
     * If the provided file is not the correct JSON schema
     * then this will return a NULL for the code syntax class.
     * @see parser.CodeSyntax
     * @see KeywordPatterns
     * */
    public static CodeSyntax mapJsonToCodeSyntax(File input) {
        ObjectMapper objectMapper = new ObjectMapper();
        CodeSyntax outputClass = null;
        try {
            outputClass = objectMapper.readValue(input, CodeSyntax.class);
        } catch (Exception e) {
            e.printStackTrace();
            outputClass = null;
        }
        return outputClass;
    }






}
