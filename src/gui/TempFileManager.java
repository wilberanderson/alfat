package gui;

import main.GeneralSettings;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Manages the paths of the current files in the temp folder.
 * @author Thomas
 * TODO: Get deletes to work
 */
public class TempFileManager {
    private String directoryPath;
    private FileSortedArrayList tempFiles;
    private int current;
    private String timePattern = "(MM-dd-yyyy HH-mm-ss)_";
    private Boolean verbose;
    private int fileLimit;

    /**
     * Must set the directory of the temp folder
     * */
    public TempFileManager(String dirPath) {
        initializeDirectory(dirPath);
        tempFiles = new FileSortedArrayList();
        current = 0;
        fileLimit = 1000;
        verbose = false;
        update();


    }


    /**
     * Set the number of temp files to keep track of
     * */
    public void setFileLimit(int fileLimit) {
        this.fileLimit = fileLimit;
    }





    /**
     * If the directory path doesn't exist create it.
     * */
    private void initializeDirectory (String dirPath) {
        File directory = new File(dirPath);
        if(!directory.exists()){
            directory.mkdirs();
        }
        this.directoryPath = dirPath;
    }

    //                                        Load Temp Files                                 //

    /**
     * Returns the most recently modified file of the temp list
     * @return String, a string literal filepath if it exist otherwise returns
     * string literal "null"
     * */
    public String getMostRecent() {
        String result = "null";

        if(!tempFiles.isEmpty()) {
            tempFiles.resort();
            result = getFilePath(0);
        }
        return result;
    }


    /**
     * Returns the current index to the filePath
     * @return String, a string literal filepath if it exist otherwise returns
     * string literal "null"
     * */
    public String getCurrent() {
        String result = "null";
        if(!tempFiles.isEmpty()) {
            result = getFilePath(current);
        }
        return result;
    }

    /**
     * Moves the current index of the tempFiles back one place
     * and returns the current index to the filePath
     * @return String, a string literal filepath if it exist otherwise returns
     * string literal "null"
     * */
    public String rollback() {
        String result = "null";
        if(!tempFiles.isEmpty()) {
            currBack();
            result = getFilePath(current);
        }
        return result;
    }


    /**
     * move current back by 1 if possible
     * */
    private void currBack() {
        if(current < tempFiles.size()-1) {
            current++;
        }
    }


    /**
     * Moves the current index of the tempFiles back one place
     * and returns the current index to the filePath
     * @return String, a string literal filepath if it exist otherwise returns
     * string literal "null"
     * */
    public String moveForeword() {
        String result = "null";
        if(!tempFiles.isEmpty()) {
            currForeword();
            result = getFilePath(current);
        }
        return result;
    }

    /**
     * move current foreword
     * */
    private void currForeword() {
        if(current > 0) {
            current--;
        }
    }

    /**
     * returns the string file path of tempFiles based on a provided index.
     * @see public String getMostRecent()
     * @see public String getCurrent()
     * @see public String rollback()
     * @see public String moveForeword()
     * @return String, a string literal filepath
     * */
    private String getFilePath(int index) {
        return tempFiles.get(index).getAbsolutePath();
    }


    //                          Manage Temp Files                           //


    /**
     * Updates list of tempFiles from the temp files folder path.
     */
    public void update() {
        //Clears currently stored files
        if(!tempFiles.isEmpty()) {
            tempFiles.removeAll(tempFiles);
        }
        try {
            File folder = new File(this.directoryPath);
            File[] listOfFiles = folder.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    tempFiles.addSort(listOfFiles[i]);
                } else if (listOfFiles[i].isDirectory()) {
                    //Should ignore might need to dig by date...
                }
            }

            //remove any temp files over limit
            removeTempFilesOverLimit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //                          Delete Temp Files                           //

    /**
     * Remove oldest files not within set temp file limit
     * */
    public void removeTempFilesOverLimit() {
        if(!tempFiles.isEmpty()) {
            for(int i = tempFiles.size()-1; tempFiles.size() > this.fileLimit; i--) {
                tempFiles.get(i).delete();
                tempFiles.remove(i);
            }
        }
    }


    /**
     * A hard delete of temp files. Removes from tempFiles and the temp folder.
     * Careful!!! MUST ENSURE that ONLY the tempfolder is set.
     * */
    public void deleteCurrentTempFile() {
        if(!tempFiles.isEmpty()) {
            deleteCurrent(current);
        }
    }


    //TODO: Add a soft remove that does not remove file but excludes it from tempFiles during run time

    /**
     * Deletes the contents of a folder.
     * SHOULD NEVER BE ANYTHING but the temp folder if called.
     * You have been warned!!!
     * @param FolderPath string literal
     * */
    public void clearTempFolder(String FolderPath) {
        File folder = new File(FolderPath);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                listOfFiles[i].delete();
            }
        }
    }


    /**
     * Deletes the contents of a folder.
     * SHOULD NEVER BE ANYTHING but the temp folder if called.
     * Uses the directoryPath set in the constructor
     * You have been warned!!!
     * */
    public void clearTempFolder() {
        File folder = new File(directoryPath);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                listOfFiles[i].delete();
            }
        }
    }



    /**
     * Deletes the current file from tempFiles and temp folder
     * and move current foreword.
     * */
    private void deleteCurrent(int index) {
        tempFiles.get(index).delete();
        tempFiles.remove(index);
        currForeword();
    }


 //                                    Copy File Management                                                     //

    /**
     * Copy a file from source into temp folder with a timeStamp appended to the name of the file.
     * @param filePath string literal
     * */
    public void copyFiletoTempFile(String filePath, String destinationPath) {
        //Add date to string
        String timeStamp = getTimeStamp(timePattern);

        try {
            File src = new File(filePath);
            File dest = new File(destinationPath);
            Files.copy(src.toPath(), new File(dest.getAbsolutePath() + File.separator + timeStamp + src.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //                                    Save File Management                                                     //


    /**
     * Saves the current the list of code editor contents to a temp file in a temp folder.
     * @see gui.SaveToFile
     * @see gui.GUIText
     * @param textLines A java LIST<GUIText>
     * @param currentFilePath java string literal
     * */
    public void saveCodeEditorTextToFile(List<GUIText> textLines, String currentFilePath, String destinationPath) {
        SaveToFile stf = new SaveToFile(destinationPath);
        String timeStamp = getTimeStamp(timePattern);
        File tempFile;
        //If the file loaded from the current file path then it saves it's and adds
        //a timestamp. Otherwise it just adds the timestamp and a tempSave
        if(currentFilePath != null && !currentFilePath.equals("null")) {
            tempFile = new File(currentFilePath);
            tempFile = new File(stf.getSaveFilepath(),timeStamp+tempFile.getName());
        } else {
            tempFile = new File(stf.getSaveFilepath(),timeStamp+"tempSave");
        }
        stf.save(textLines, tempFile);
    }


    private String getTimeStamp(String pattern) {
        //Gets date from system
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        //Add date to string
        return formatter.format(date);
    }


    /**
     * Exist only for debug...
     * */
    public void printFiles() {
        for (int i = 0; i < tempFiles.size(); i++) {
            System.out.println(tempFiles.get(i).getName());
        }
    }


    /**
     * Only exist for debug...
     * */
    private void printFolder() {
        if(verbose == true) {
            File folder = new File(this.directoryPath);
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
