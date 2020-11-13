package gui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



/**
 * Operations to manages temp files in the following ways:
 * (1) Used to save contents of code editor upon crash,
 * (2) Copy source file to temp file location
 * @author Thomas
 * */
public class TempFileOperations extends SaveToFile {
    //Boolean to show console prints
    private Boolean verbose;
    private String tempTag;


    /**
     * Must set the string literal file path to where the file is to be saved.
     * @param saveFilepath string literal file path
     */
    public TempFileOperations(String saveFilepath) {
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
     * Copy a file from source into temp folder.
     * @param filePath string literal
     * */
    public void saveTempFile(String filePath) {
        try {
            File src = new File(filePath);
            File dest = new File(super.getSaveFilepath());
            Files.copy(src.toPath(), new File(dest.getAbsolutePath() + File.separator + src.getName()).toPath(),StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the current the list of code editor contents to a temp file in a temp folder.
     * @see gui.SaveToFile
     * @see gui.GUIText
     * @param textLines A java LIST<GUIText>
     * @param currentFilePath java string literal
     * */
    public void tempSave(List<GUIText> textLines, String currentFilePath) {
        //Gets date from system
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("(MM-dd-yyyy HH-mm-ss)");

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
     * @param tempFolderPath string literal
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
                    printFileDate(listOfFiles[i]);
                } else if (listOfFiles[i].isDirectory()) {
                    System.out.println("Directory " + listOfFiles[i].getName());
                }
            }
        }
    }


    /**
     * Only exist for debug...
     * */
    private void printFileDate(File file) {
        BasicFileAttributes attrs;
        try {
            attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            FileTime time = attrs.creationTime();

            String pattern = "MM-dd-yyyy HH-mm-ss";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

            String formatted = simpleDateFormat.format( new Date( time.toMillis() ) );

            System.out.println( "The file creation date and time is: " + formatted );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
