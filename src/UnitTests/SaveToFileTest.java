package UnitTests;


import static org.junit.jupiter.api.Assertions.*;

import gui.SaveToFile;
import gui.texts.CodeWindowText;
import gui.texts.Text;
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
        List<CodeWindowText> textLines = new ArrayList<>();
        SaveToFile test = new SaveToFile(GeneralSettings.TEMP_DIR);

        assertDoesNotThrow(() -> test.save(textLines));
    }

    @Test
    public void testTestSave() {
        File file = new File(GeneralSettings.TEMP_DIR);
        List<CodeWindowText> textLines = new ArrayList<>();

        SaveToFile test = new SaveToFile(GeneralSettings.TEMP_DIR);

        assertDoesNotThrow(() -> test.save(textLines, file));

    }

    @Test
    public void testTestSave1() {
        List<CodeWindowText> textLines = new ArrayList<>();
        SaveToFile test = new SaveToFile(GeneralSettings.TEMP_DIR);

        assertDoesNotThrow(() -> test.save(textLines, GeneralSettings.TEMP_DIR));
    }
}