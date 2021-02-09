package parser;


import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * Takes a file as an input JSON and creates and object mapper based on the defined syntax in JSON to a Syntax object.
 * TODO: Find a more dynamic way to return a mapped class object...
 * @see parser.LC3Syntax
 * @author Thomas
 * @author Brandon
*/
public class JsonReader {
    //Holds the input file
    private File input;

    /**
     * Constructor must be set with a json file path to read from
     * @param input
    */
    public JsonReader (File input) {
        this.input = input;
    }

    /**
     * Returns a json mapped LC3Syntax object
     * @return LC3Syntax
     * */
    public LC3Syntax mapJsonLC3Syntax() {
        ObjectMapper objectMapper = new ObjectMapper();
        LC3Syntax outputClass;
        try {
            outputClass = objectMapper.readValue(input, LC3Syntax.class);
            return outputClass;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Generic Syntax
    public CodeSyntax mapJsonToSyntax() {
        ObjectMapper objectMapper = new ObjectMapper();
        CodeSyntax outputClass;
        try {
            outputClass = objectMapper.readValue(input, CodeSyntax.class);
            return outputClass;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public GenericSyntax mapJsonToGenericSyntax() {
        ObjectMapper objectMapper = new ObjectMapper();
        GenericSyntax outputClass;
        try {
            outputClass = objectMapper.readValue(input, GenericSyntax.class);
            return outputClass;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
