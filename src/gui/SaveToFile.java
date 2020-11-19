package gui;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Save TextEditor content to a file.
 * @author Thomas
 * */
public class SaveToFile {


    //File Path
    private String saveFilepath;



    public String getSaveFilepath() {
        return saveFilepath;
    }


    /**
     * Must set the string literal file path to where the file is to be saved.
     * */
    public SaveToFile(String saveFilepath) {
        this.saveFilepath = saveFilepath;
    }

    /**
     * Saves GUI text to file a file. File path is set in constructor.
     * @param textLines List<GUIText>
     * */
    public void save(List<GUIText> textLines)  {
        try {
            File file = new File(saveFilepath);
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(int i = 0; i < textLines.size(); i++) {
                bw.write(textLines.get(i).textString + "\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves GUI text to file a file.
     * @param textLines List<GUIText>
     * @param filePath string literal to file path to be saved
     * */
    public void save(List<GUIText> textLines, String filePath)  {
        try {
            File file = new File(filePath);
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(int i = 0; i < textLines.size(); i++) {

                bw.write(textLines.get(i).textString + "\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Saves GUI text to file a file.
     * @param textLines List<GUIText>
     * @param file file path to be saved to
     * */
    public void save(List<GUIText> textLines, File file)  {
        try {
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(int i = 0; i < textLines.size(); i++) {
                bw.write(textLines.get(i).textString + "\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }





}
