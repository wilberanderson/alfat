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

    public GenericSyntax mapJsonToGenericSyntax() {
        ObjectMapper objectMapper = new ObjectMapper();
        GenericSyntax outputClass = null;
        try {
            outputClass = objectMapper.readValue(input, GenericSyntax.class);
        } catch (Exception e) {
            e.printStackTrace();
            outputClass = null;
        }
        return outputClass;
    }


}
