package main;

import gui.FileSortedArrayList;
import gui.TempFileManager;
import parser.LC3Parser;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class mainTester {
    public static void main(String[] args) throws InterruptedException, ParseException {
//        LC3Parser foo = new LC3Parser("tests/test2.txt", true);
//
//        foo.ReadFile("tests/test5.asm");
//
//        foo.getFlowObjects();
//
//        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH-mm-ss");
//
//        Date date = new Date();
//
//        System.out.println(formatter.format(date));
//        ArrayList<String> foo = new ArrayList<String>();
//
//        for (int i = 0; i < 10; i++) {
//            foo.add(new String(formatter.format(new Date())));
//            TimeUnit.SECONDS.sleep(1);
//        }
//
//        for (int i = 0; i < 10; i++) {
//            System.out.println(foo.get(i));
//        }
//
//        ArrayList<Date> d = new ArrayList<Date>();
//
//        for (int i = 0; i < 10; i++) {
//            Date s = new Date();
//            s = formatter.parse(foo.get(i));
//            d.add(s);
//        }
//        Date big = null;
//        for (int i = 0; i < 10; i++) {
//            if(big == null) {
//                big = d.get(i);
//            }
//
//            if(big.compareTo(d.get(i)) < 0) {
//                big = d.get(i);
//            }
//        }
//
//        System.out.println(big + "This is the last date!");


        FileSortedArrayList foo2 = new FileSortedArrayList();

        foo2.resort();


        File folder = new File(GeneralSettings.TEMP_DIR);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                //System.out.println("File " + listOfFiles[i].getName());
                foo2.addSort(listOfFiles[i]);
            } else if (listOfFiles[i].isDirectory()) {
                //System.out.println("Directory " + listOfFiles[i].getName());
            }
        }


//        for(int i = 0; i < foo2.size(); i++) {
//            System.out.println(foo2.get(i).getName());
//        }

        //foo2.resort();
//        for(int i = 0; i < foo2.size(); i++) {
//            System.out.println(foo2.get(i).getName());
//        }

        TempFileManager tfm = new TempFileManager(GeneralSettings.TEMP_DIR);




    }

}
