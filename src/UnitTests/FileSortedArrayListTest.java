package UnitTests;

import gui.FileSortedArrayList;
import main.GeneralSettings;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class FileSortedArrayListTest {
    @Test
    @DisplayName("A special test case")
    public void Test(){
        FileSortedArrayList test = new FileSortedArrayList();

        File folder = new File(GeneralSettings.TEMP_DIR);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                test.addSort(listOfFiles[i]);
            } else if (listOfFiles[i].isDirectory()) {
                //Should ignore might need to dig by date...
            }
        }

        assertFalse(test.isEmpty(), "Should not be empty");
    }
}
