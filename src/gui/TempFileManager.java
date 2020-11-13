package gui;

import main.GeneralSettings;

import java.io.File;
import java.util.ArrayList;


/**
 * Manages the paths of the current files in the temp folder.
 * @author Thomas
 */
public class TempFileManager {
    private String folderPath;
    private FileSortedArrayList tempFiles;
    private int current;


    /**
     * Must set the directory of the temp folder
     * */
    public TempFileManager(String tempDir) {
        folderPath = tempDir;
        tempFiles = new FileSortedArrayList();
        current = -1; //Does not exist
        update();
        //printFiles();
    }


    /**
     * A hard delete of temple. Removes from tempFiles and the temp folder.
     * Careful!!! MUST ENSURE that ONLY the tempfolder is set.
     * */
    public void deleteCurrentTempFile() {
        if(!tempFiles.isEmpty()) {
            deleteCurrent(current);
        }
    }



    //TODO: Add a soft remove that does not remove file but excludes it from tempFiles during run time

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
     * Updates list of tempFiles from the temp files folder path.
     */
    public void update() {
        try {
            File folder = new File(this.folderPath);
            File[] listOfFiles = folder.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    tempFiles.addSort(listOfFiles[i]);
                } else if (listOfFiles[i].isDirectory()) {
                    //Should ignore might need to dig by date...
                }
            }
            current = 0;
        } catch (Exception e) {
            e.printStackTrace();
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


    /**
    * move current back by 1 if possible
    * */
    private void currBack() {
        if(current < tempFiles.size()-1) {
            current++;
        }
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
     * Exist only for debug...
     * */
    private void printFiles() {
        for (int i = 0; i < tempFiles.size(); i++) {
            System.out.println(tempFiles.get(i).getName());
        }
    }


}
