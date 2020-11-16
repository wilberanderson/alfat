package UnitTests;

import gui.TempFileManager;
import main.GeneralSettings;
import org.junit.jupiter.api.*;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class TempFileManagerTest {
    @Test
    @DisplayName("Consructor test")
    public void Test(){
        TempFileManager test = new TempFileManager("foobar");
        assertNotEquals("null", test.getMostRecent());
    }

    @Test
    @DisplayName("Consructor test2")
    public void Test2(){

    }


    @Test
    @DisplayName("Out of bounds test: Rollback")
    public void Test3() {
        TempFileManager test = new TempFileManager(GeneralSettings.TEMP_DIR);

        int bounds = 100;

        for(int i = 0; i < bounds; i++) {
            assertNotEquals("null", test.rollback());
        }


    }

    @Test
    @DisplayName("Out of bounds test: foreword")
    public void Test4() {
        TempFileManager test = new TempFileManager(GeneralSettings.TEMP_DIR);

        int bounds = 100;

        for(int i = 0; i < bounds; i++) {
            assertNotEquals("null", test.moveForeword());
        }


    }

}
