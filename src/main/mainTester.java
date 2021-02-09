package main;

import gui.Settings.RegisterSearch;
import gui.Settings.SettingsMenu;
import parser.CodeSyntax;
import parser.JsonReader;

import java.io.File;
import java.text.ParseException;

public class mainTester {
    public static void main(String[] args) throws InterruptedException, ParseException {

//        System.out.println(RegisterSearch.search());
//        System.out.println(RegisterSearch.search2());
//        System.out.println("Can't see this until done!");

        JsonReader jr = new JsonReader(new File("CodeSyntax/LC3-New.json"));


        GenericSyntax syn = jr.mapJsonToGenericSyntax();
        if(syn == null) {

        }
        //System.out.println(syn);
        System.out.println(syn.getCommands());


    }

}
