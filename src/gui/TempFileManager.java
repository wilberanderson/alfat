package gui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



/**
 * Manages Temp Files in the following ways:
 * (1) Used to fave contents of code editor upon crash,
 * @author Thomas
 * */
public class TempFileManager extends SaveToFile {
    //Boolean to show console prints
    private Boolean verbose;


    /**
     * Must set the string literal file path to where the file is to be saved.
     * @param saveFilepath
     */
    public TempFileManager(String saveFilepath) {
        super(saveFilepath);
        verbose = false;
    }

    /**
     * Set the flag to see debug text...
     * @see private void printFolder()
     * @param verbose boolean ture to show print, set false in constructor.
    * */
    public void showPrints(Boolean verbose) {
        this.verbose = verbose;
    }


    /**
     * Saves the current the list of code editor contents to a temp file in a temp folder.
     * @see gui.SaveToFile
     * @see gui.GUIText
     * @param textLines A java LIST<GUIText>
     * @param currentFilePath java string literal
     * */
    public void exitSave(List<GUIText> textLines, String currentFilePath) {
        //Gets date from system
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");

        //Add date to string
        String timeStamp = formatter.format(date);
        File tempFile;

        //If the file loaded from the current file path then it saves it's and adds
        //a timestamp. Otherwise it just adds the timestamp and a tempSave
        if(currentFilePath != null && !currentFilePath.equals("null")) {
            tempFile = new File(currentFilePath);
            tempFile = new File(super.getSaveFilepath(),timeStamp+"_"+tempFile.getName());
        } else {
            tempFile = new File(super.getSaveFilepath(),timeStamp+"_tempSave");
        }
        super.save(textLines, tempFile);

        printFolder();
    }


    /**
     * Deletes the contents of a folder.
     * SHOULD NEVER BE ANYTHING but the temp folder if called.
     * You have been warned!!!
     * */
    public void clearTempFolder(String tempFolderPath) {
        File folder = new File(tempFolderPath);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                listOfFiles[i].delete();
            }
        }
    }

    /**
     * Only exist for debug...
     * */
    private void printFolder() {
        if(verbose == true) {
            File folder = new File(super.getSaveFilepath());
            File[] listOfFiles = folder.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    System.out.println("File " + listOfFiles[i].getName());
                } else if (listOfFiles[i].isDirectory()) {
                    System.out.println("Directory " + listOfFiles[i].getName());
                }
            }
        }
    }

}
