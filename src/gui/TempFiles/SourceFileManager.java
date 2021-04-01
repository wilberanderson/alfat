package gui.TempFiles;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 *
 * This class is responsible for breaking up a source file into chunks
 * and maintaining the order of them so they can be read into the text editor
 * through the parser.
 *<pre>
 * On usage:
 * You must set the initializeDirectory()
 * Then clean() the directory from start and when a new file is opened
 *
 * Think of a file being broken into chunks. You can addChunks() 1-n.
 *
 * And the current chunk is the one you have immediate access to.
 * You can getCurrent(), and replaceCurrent()
 * file chunks (start/up)[0][1][2][3]...(end/down)
 *                        |
 *                    (current)
 * You can moveDown current
 * file chunks (start/up)[0][1][2][3]...(end/down)
 *                           |
 *                        (current)
 *You can moveUp current
 * file chunks (start/up)[0][1][2][3]...(end/down)
 *                        |
 *                    (current)
 * You can also insert below and above current.
 *</pre>
 */
public class SourceFileManager {
    private String directoryPath = null;
    private final String FOLDER_NAME = ".chunks" + File.separator;
    public FileSortedArrayList files = new FileSortedArrayList();
    private int chunkIndexCount = 0;
    private String timePattern = "(HH.mm.ss.SSSSSS.";
    private int current = 0;
    private final int UP = -1;
    private final int DOWN = 1;


    /**
     * Opens the folder and attaches the files in order of
     * creation date to the FileSortedArrayList
     * Should set path to getdirectoryPath()
     */
    public void attachFilesFromDir(String path) {
        if (isPathExist(path)) {
            files.clear(); //Lazy I know TO BAD!
            File folder = new File(directoryPath);
            File[] listOfFiles = folder.listFiles();

            for (File in : listOfFiles) {
                files.add(in);
            }
            files.bubbleSort();
        }
    }


    /**
     * Reads a file form files sorted list and returns by reference
     * a arraylist of strings
     * */
    private void readFileToArrayList(ArrayList<String> list, int index) {
        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(files.get(index).getAbsoluteFile()));
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a array list of strings of the current chuck selected
     * */
    public ArrayList<String> getCurrent() {
        ArrayList<String> result = new ArrayList<String>();
        readFileToArrayList(result,current);
        return result;
    }

    /**
     * Clears the contents of a file then writes the contents of a list to
     * the file.
     * */
    private void writeArrayListToFile(ArrayList<String> list, int index) {
        try {
            FileWriter outFile = null;
            BufferedWriter bw = null;
            //Clear the file contents
            outFile = new FileWriter(files.get(index).getAbsolutePath());
            outFile.write("");
            outFile.close();
            //Fill with current stuff
            outFile = new FileWriter(files.get(index).getAbsolutePath());
            bw = new BufferedWriter(outFile);
            for(int i = 0; i < list.size(); i++) {
                bw.write(list.get(i));
                if(i < list.size()-1) {
                    bw.newLine();
                }
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Replaces the contents of the current file with the
     * contents of the arraylist of strings
     * */
    public void replaceCurrent(ArrayList<String> in) {
        writeArrayListToFile(in,current);
    }

    /**
     * Insert a arraylist of strings below the current
     * */
    public void insertBelowCurrent(ArrayList<String> in) {
        if(getDirectoryPath() != null) {
            addChunkAtIndex(in,getDirectoryPath() + getTimeStamp(timePattern) + "chunk" + (chunkIndexCount++) + ".txt",current+DOWN);
        }
    }

    /**
     * Insert a arraylist of strings above the current
     * */
    public void insertAboveCurrent(ArrayList<String> in) {
        if(getDirectoryPath() != null) {
            addChunkAtIndex(in,getDirectoryPath() + getTimeStamp(timePattern) + "chunk" + (chunkIndexCount++) + ".txt",current+UP);
        }
    }




    /**
     * Moves current
     * */
    public void moveDown() {
       if (currentRangeCheck(DOWN)) {
           current += DOWN;
       }
    }

    /**
     * Moves current down
     * */
    public void moveUP() {
        if (currentRangeCheck(UP)) {
            current += UP;
        }
    }


    /**
     * Returns true whether the current can be moved up or down.
     * */
    private boolean currentRangeCheck(int move) {
        boolean result = true;

        switch(move) {
            case UP:
                if(current+UP < 0 ) {
                    result = false;
                }
                break;
            case DOWN:
                if(current+DOWN > chunkIndexCount-1) {
                    result = false;
                }
                break;
                default:
                    result = false;
                    break;
         }
         return result;
    }



    /**
     * Gets a time stamp to append to a file name with
     * a pseudo unique enough tag
     * */
    private String getTimeStamp(String pattern) {
        //Gets date from system
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        //Add date to string
        String temp = formatter.format(date)  +  (Math.random() * (1000 - 1 + 1) + 1) + ")_";
        return temp;
    }



    /**
     * Add a chunk (ArrayList of strings) to be saved in a file.
     * And the file path is saved in the Files sorted array.
     * Inserted into the end.
     */
    public void addChunk(ArrayList<String> inList) {
        if(getDirectoryPath() != null) {
            addChunkAtIndex(inList,getDirectoryPath() + getTimeStamp(timePattern) + "chunk" + (chunkIndexCount++) + ".txt",-2);
        }
    }


    /**
     * @param inList arrayList of strings to add to a file chunk
     * @param fileOutPath the file path
     * @param index the actual index the file path is stored at -2 means add at end
     */
    private void addChunkAtIndex(ArrayList<String> inList, String fileOutPath, int index) {
        if (getDirectoryPath() != null) {
            try {
                FileWriter outFile = new FileWriter(new File(fileOutPath));
                BufferedWriter bw = new BufferedWriter(outFile);
                for (int i = 0; i < inList.size(); i++) {
                    bw.write(inList.get(i));
                    if (i != inList.size() - 1) {
                        bw.newLine();
                    }
                }
                bw.close();

                //Add to end
                if(index == -2) {
                    files.add(new File(fileOutPath));
                } else {
                    //Adjust current if insert was after it
                    if(index > current) {
                        //current += UP;
                        files.add(index,new File(fileOutPath));
                    }else {
                        //adjust current if insert was before it
                        if (index <= 0) {
                            files.add(0,new File(fileOutPath));
                        } else {
                            files.add(index,new File(fileOutPath));
                        }
                        current +=DOWN;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns the directory path,
     * if it does not exist returns null
     */
    public String getDirectoryPath() {
        String result = null;
        if (this.directoryPath != null) {
            if (isPathExist(this.directoryPath)) {
                result = this.directoryPath;
            }
        }
        return result;
    }


    /**
     * Returns true or false whether a path exist
     */
    private boolean isPathExist(String path) {
        boolean result = false;
        File dir = new File(path);
        if (dir.exists()) {
            result = true;
        }
        return result;
    }


    /**
     * This initializes the temp file directory
     * Should set to GeneralSettings.USERPREF.getUserTempFileDirPath()
     */
    public void initializeDirectory(String dirPath) {
        dirPath = dirPath + File.separator + FOLDER_NAME;
        File directory = new File(dirPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        this.directoryPath = dirPath;
    }


    /**
     * Remove all files in the temp directory
     * SHOULD NEVER BE ANYTHING but the temp folder if
     * called!
     * This WILL REMOVE ALL FILES IN A FOLDER!
     * You have been warned!!!
     * Use getDirectoryPath()
     */
    public void clean(String path) {
        if (isPathExist(path)) {
            chunkIndexCount = 0;
            current = 0;
            File folder = new File(directoryPath);
            File[] listOfFiles = folder.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    listOfFiles[i].delete();
                }
            }
        }
    }


    /**
     * Reads entire file an breaks it into approximately 10 parts.
     */
    public void chunker2(String path) throws IOException {
        //Count the number of lines
        long numberOflines = 0;
        long numberOfLinesPerSplit = 0;
        String line;
        boolean bwIsClosed = false; //Toggle for the new line
        //  line count index, toggle to make a new file, the file number index
        long i = 0, newFile = 0;

        //Count the number of line in the file
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            while (reader.readLine() != null) numberOflines++;
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //TODO: find a better way to split the file into small chunks
        numberOfLinesPerSplit = numberOflines / 10; //roughly 10 parts last one will have overlap

        //Split the file into small chunks
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            FileWriter outFile = null;
            BufferedWriter bw = null;
            while ((line = reader.readLine()) != null) {
                //Create file chunk
                if (newFile == 0) {
                    outFile = new FileWriter(new File(getDirectoryPath() + getTimeStamp(timePattern) + "chunk" + (chunkIndexCount++) + ".txt"));
                    bw = new BufferedWriter(outFile);
                    newFile = 1;
                    bwIsClosed = false;
                }
                //Add line into new file chunk and inc line number
                i++;
                bw.write(line);

                //If we reached the limit close the file reader and
                //set toggles
                if (i == numberOfLinesPerSplit) {
                    newFile = 0;
                    i = 0;
                    //outFile.close();
                    bw.close();
                    bwIsClosed = true;
                }
                //If the file was not closed we add a new line
                //Prevents adding extra new liens
                if (!bwIsClosed) {
                    bw.newLine();
                }
            }
            reader.close();
            //outFile.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Weird stack overflow way based on buffer size
     * ISSUE: Extra new lines...
     */
    public void chunker(String path) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(path, "r");
        long numSplits = 10; //from user input, extract it from args
        long sourceSize = raf.length();
        long bytesPerSplit = sourceSize / numSplits;
        long remainingBytes = sourceSize % numSplits;

        int maxReadBufferSize = 8 * 1024; //8KB
        for (int destIx = 1; destIx <= numSplits; destIx++) {
            BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(getDirectoryPath() + destIx));
            if (bytesPerSplit > maxReadBufferSize) {
                long numReads = bytesPerSplit / maxReadBufferSize;
                long numRemainingRead = bytesPerSplit % maxReadBufferSize;
                for (int i = 0; i < numReads; i++) {
                    readWrite(raf, bw, maxReadBufferSize);
                }
                if (numRemainingRead > 0) {
                    readWrite(raf, bw, numRemainingRead);
                }
            } else {
                readWrite(raf, bw, bytesPerSplit);
            }
            bw.close();
        }
        if (remainingBytes > 0) {
            BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(getDirectoryPath() + (numSplits + 1)));
            readWrite(raf, bw, remainingBytes);
            bw.close();
        }
        raf.close();

    }

    void readWrite(RandomAccessFile raf, BufferedOutputStream bw, long numBytes) throws IOException {
        byte[] buf = new byte[(int) numBytes];
        int val = raf.read(buf);
        if (val != -1) {
            bw.write(buf);
        }
    }


}
