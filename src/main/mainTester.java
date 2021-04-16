package main;

import parser.SimpleTokenizer;

import java.text.ParseException;

public class mainTester {
    public static void main(String[] args) throws InterruptedException, ParseException {

        String foo = "          ";
        //if (foo.matches("(^([\t]+)([\t])$)|([\t])"))
        if (foo.matches("(^([ \t\\s]+)([ \t\\s])$)|([ \t\\s])"))
            System.out.println("True");

    }
}
