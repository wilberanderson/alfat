package gui;

import org.lwjgl.PointerBuffer;
import static org.lwjgl.system.MemoryUtil.memAllocPointer;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.util.nfd.NativeFileDialog.*;

/**
 * Opens the native file dialog and supports choosing a single file to return its file path has a string literal,
 * and saving a file using the file dialog.
 * There are additional methods to see the source path that the file dialog will open from, set the filter list
 * for files, and a toggle for displaying console logs of actions.
 * @author Thomas
*/
public class OpenFileDialog {
    //The file path string if not set it always returns a "null" literal string.
    private String filePath;
    //The default filter list for the file dialog window
    private String filterList;
    //A file path to open the file dialog from
    private String openFromPath;
    //Boolean to open from default file dialog
    private Boolean isOpenFromDefault;
    //Boolean to display console prints
    private Boolean showPrint;
    /**
     * Constructor to sets the initial file dialog settings to be:
     * (1) filter list is set for .asm and .txt files,
     * (2) opens from the default OS path,
     * (3) console prints statements are not shown.
    */
    public OpenFileDialog() {
        filePath = "null";
        filterList = "asm,txt";
        openFromPath  = "null";
        isOpenFromDefault = true;
        showPrint = false;
    }

    /**
     * Set the default file path that the file dialog will open from.
     * NOTE: Does not check for path validation...
     * @param fpath a string literal
    */
    public void setDefaultFilePath(String fpath) {
        openFromPath = fpath;
        isOpenFromDefault = false;
    }

    /**
     *Set the filter list for the file dialog.
     * i.e. "asm,txt,c,png" ...
     * @param flist a string literal
     */
    public void setFilterList(String flist){
        filterList = flist;
    }

    /**
     * Returns the file path of a selected file from the file dialog.
     * @return <code> string literal </code>of file path or if no file path selected<code> string literal = "null" </code>
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Set whether to display console prints. By default no console prints are shown.
     * @param option <code>true</code> to display console prints and <code>false</code> to not.
     */
    public void displayConsole(boolean option) {
        showPrint = option;
    }

    /**
     * Opens the file dialog from either the default os path or a defined path to open a file.
     * @see public setDefaultFilePath(String fpath)
     * @see private void checkResult(int result, PointerBuffer path)
     */
    public void openFileWindow() {
        PointerBuffer outPath = memAllocPointer(1);
        try {
            if (isOpenFromDefault == true) {
                checkResult(NFD_OpenDialog(filterList, null, outPath), outPath);
            } else {
                checkResult(NFD_OpenDialog(filterList, openFromPath, outPath), outPath);
            }
        } finally {
            memFree(outPath);
        }
    }

    /**
     * Opens the file dialog from either the default os path or a defined path to save a file.
     * @see public setDefaultFilePath(String fpath)
     * @see private void checkResult(int result, PointerBuffer path)
     */
    public void saveFileWindow() {
        PointerBuffer outPath = memAllocPointer(1);
        try {
            if (isOpenFromDefault == true) {
                checkResult(NFD_SaveDialog(filterList, null, outPath), outPath);
            } else {
                checkResult(NFD_SaveDialog(filterList, openFromPath, outPath), outPath);
            }
        } finally {
            memFree(outPath);
        }
    }

    /**
     * Handles the file dialog options. A single file path is set to <code>filePath<code/>.
     * Whether prints to console display depends if <code>showPrint</code> was set to <code>true<code/> or <code>false<code/>
     * @see public OpenFileDialog()
     * @see public saveFileWindow()
     * @see public void displayConsole(boolean option)
     */
    private void checkResult(int result, PointerBuffer path) {
        switch (result) {
            case NFD_OKAY:
                if(showPrint == true) {
                    System.out.println("Success!");
                    System.out.println(path.getStringUTF8(0));
                }
                filePath = path.getStringUTF8(0);
                nNFD_Free(path.get(0));
                break;
            case NFD_CANCEL:
                if(showPrint == true) {
                    System.out.println("User pressed cancel.");
                }
                break;
            default: // NFD_ERROR
                System.err.format("Error: %s\n", NFD_GetError());
        }
    }
}
