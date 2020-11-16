package UnitTests;

import main.EngineTester;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EngineTesterTest {
    @Before
    void create(){

    }
    @Test
    void run() {
        EngineTester foo = new EngineTester();
        assertDoesNotThrow(() -> foo.run());
    }



    @Test
    void crash() {

    }

    @Test
    void main() {

    }

    @Test
    void getWindow() {

    }
}