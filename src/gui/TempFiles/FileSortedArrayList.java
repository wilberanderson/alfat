package gui.TempFiles;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


/**
 * This is a ArrayList that allows for files to be added and sorted by the last modified date of the file
 * such that the first in the index is the most recent file modified.
 * */
public class FileSortedArrayList extends ArrayList<File> {

    /**
     * Add a file and sorts by last modified date
     * @param file java File type
     * */
    @SuppressWarnings("unchecked")
    public void addSort(File file) {
        add(file);
        for (int i = size()-1; i > 0 && cmp(file,get(i-1)) > 0; i--)
            Collections.swap(this, i, i-1);
    }

    /**
     * Resorts files by last modified date
     * */
    public void resort() {
        for (int i = size()-1; i > 0 && cmp(get(i),get(i-1)) > 0; i--)
            Collections.swap(this, i, i-1);
    }

    /**
     * Bubble sorts the files from first created to last created.
     * */
    public void bubbleSort() {
        for (int i = 0; i < this.size(); i++) {
            for(int j = 0; j < this.size()-i-1; j++){
                if(cmp(get(j),get(j+1)) > 0) {
                    Collections.swap(this, j, j+1);
                }
            }
        }
    }

    /**
     * Comparator method. int > 0 cur is after stored,
     * int < 0 cur is before stored, int == 0, cur and stored are equal.
     * @see public void addSort(File file)
     * @param current java File type
     * @param stored java File type
     * @return int value based on comparison
     * */
    private int cmp(File current, File stored) {
        int result = 0;

        try{
            BasicFileAttributes attrsCurr = Files.readAttributes(current.toPath(), BasicFileAttributes.class);
            BasicFileAttributes attrsStored = Files.readAttributes(stored.toPath(), BasicFileAttributes.class);
            FileTime timeCurr = attrsCurr.creationTime();
            FileTime timeStored = attrsStored.creationTime();


            //String pattern = "MM-dd-yyyy HH-mm-ss";
            //SimpleDateFormat formatter = new SimpleDateFormat(pattern);

            Date dateCurr = new Date(timeCurr.toMillis());
            //System.out.println("File -> " + current.toPath());
            //System.out.println("TIME" + formatter.format(dateCurr));

            Date dateStored = new Date(timeStored.toMillis());

            //System.out.println("File -> " + stored.toPath());
            //System.out.println("TIME" + formatter.format(dateStored));

            result = dateCurr.compareTo(dateStored);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(result);
        return result;
    }


}
