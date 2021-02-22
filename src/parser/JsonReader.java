package parser;


import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * Takes a file as an input JSON and creates and object mapper based on the defined syntax in JSON to a Syntax object.
*/
public class JsonReader {
    private File input;

    /**
     * Constructor must be set with a json file path to read from
     * @param input
    */
    public JsonReader (File input) {
        this.input = input;
    }

    public CodeSyntax mapJsonToCodeSyntax() {
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
