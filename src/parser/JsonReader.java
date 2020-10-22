package parser;


import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;
/*
*
* Takes a file as an input and creates and object mapper based on the defined syntax in LC3Syntax using the mapJson method.
*
* */
public class JsonReader {
    File input;

    public JsonReader () {

    }
    public JsonReader (File input) {
        this.input = input;
    }

    public void setInput(File input) {
        this.input = input;
    }

    //Then create object
    //TODO: find a more dynamic way to return a mapped class
    public LC3Syntax mapJson(File input) {

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






}
