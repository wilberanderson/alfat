package UnitTests;


import static org.junit.jupiter.api.Assertions.*;
import gui.GUIText;
import gui.SaveToFile;
import main.GeneralSettings;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class SaveToFileTest {

    @Test
    public void testGetSaveFilepath() {
        SaveToFile test = new SaveToFile(GeneralSettings.TEMP_DIR);
        assertDoesNotThrow(() -> test.getSaveFilepath());

    }

    @Test
    public void testSave() {
        List<GUIText> textLines = new ArrayList<GUIText>();
        SaveToFile test = new SaveToFile(GeneralSettings.TEMP_DIR);

        assertDoesNotThrow(() -> test.save(textLines));
    }

    @Test
    public void testTestSave() {
        File file = new File(GeneralSettings.TEMP_DIR);
        List<GUIText> textLines = new ArrayList<GUIText>();

        SaveToFile test = new SaveToFile(GeneralSettings.TEMP_DIR);

        assertDoesNotThrow(() -> test.save(textLines, file));

    }

    @Test
    public void testTestSave1() {
        List<GUIText> textLines = new ArrayList<GUIText>();
        SaveToFile test = new SaveToFile(GeneralSettings.TEMP_DIR);

        assertDoesNotThrow(() -> test.save(textLines, GeneralSettings.TEMP_DIR));
    }
}