package gui;

import gui.textBoxes.CodeWindow;
import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import parser.LC3Parser;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import gui.buttons.HeaderMenu;
import gui.buttons.TextButton;

public class Header {
    private List<HeaderMenu> menuList;
    private GUIFilledBox guiFilledBox;
    private Vector2f position;
    private FlowChartWindow flowChartWindow;
    private CodeWindow codeWindow;
    private Cursor cursor;
    private Vector2f aspectRatio = new Vector2f(1, 1);
    //Manages the temp file paths
    private TempFileManager tfm;


    public Header(Vector2f position, Vector2f size){
        menuList = new ArrayList<>();
        guiFilledBox = new GUIFilledBox(position, size, GeneralSettings.HEADER_COLOR);
        this.position = position;

        //Set up temp file manager
        tfm = new TempFileManager(GeneralSettings.TEMP_DIR);
        tfm.setFileLimit(5);
        //Please set this to null if not file has been opened on launch
        GeneralSettings.FILE_PATH = "null";


        List<TextButton> testMenuButtonList = new ArrayList<>();

        //open file
        TextButton button = new TextButton("Open File") {
            @Override
            public void onPress() {
                OpenFileDialog of = new OpenFileDialog();
                of.openFileWindow();
                GeneralSettings.FILE_PATH = of.getFilePath();

                // If the file exists, load it into the text editor.
                if (!GeneralSettings.FILE_PATH.equals("null")){
                    String content = "";
                    try{
                        File file = new File(GeneralSettings.FILE_PATH);
                        BufferedReader reader = new BufferedReader(new FileReader(file));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            content += line.replace("\t","    ");
                            content += '\n';
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    if(codeWindow != null) {
                        codeWindow.clear();
                    }
                    //create code window
                    codeWindow = new CodeWindow(new Vector2f(0f,0f), new Vector2f(1f, 2-GeneralSettings.FONT_SCALING_FACTOR*GeneralSettings.FONT_SIZE), new Vector3f(0.1f,0.1f,0.1f), new Vector3f(1,1,1), new Vector3f(0,0,0), content, GeneralSettings.CONSOLAS, GeneralSettings.FONT_SIZE, GeneralSettings.FONT_WIDTH, GeneralSettings.FONT_EDGE, GeneralSettings.TEXT_BOX_BORDER_WIDTH, size.y);
                    cursor = new Cursor(new Vector2f(codeWindow.getPosition()), codeWindow);
                }
            }
        };
        testMenuButtonList.add(button);

        //save file
        button = new TextButton("Save As") {
            @Override
            public void onPress() {
                OpenFileDialog of = new OpenFileDialog();
                //of.displayConsole(true);
                of.saveFileWindow();
                System.out.println(of.getFilePath());

                //If the use saved a file
                if (!of.getFilePath().equals("null")) {
                    SaveToFile stf = new SaveToFile(of.getFilePath());
                    //Prevent crash if codeWindow doe not have anything in it
                    if (codeWindow != null) {
                        stf.save(codeWindow.getTexts());
                    }
                }

            }
        };
        testMenuButtonList.add(button);

        button = new TextButton("Save") {
            @Override
            public void onPress() {
                //Saves contents of the text editor over the original source file
                //TempFileOperations tfo = new TempFileOperations();
                if (codeWindow != null && GeneralSettings.FILE_PATH != null & !GeneralSettings.FILE_PATH.equals("null")) {
                   SaveToFile stf = new SaveToFile(GeneralSettings.FILE_PATH);
                   stf.save(codeWindow.getTexts());
                }

            }
        };
        testMenuButtonList.add(button);


        button = new TextButton("Temp Save") {
            @Override
            public void onPress() {
                //Saves in temporary location
                //TempFileOperations tfo = new TempFileOperations();
                if (codeWindow != null) {
                   // tfo.showPrints(false);
                   // tfo.saveCodeEditorTextToFile(codeWindow.getTexts(), GeneralSettings.FILE_PATH, GeneralSettings.TEMP_DIR);
                    tfm.saveCodeEditorTextToFile(codeWindow.getTexts(), GeneralSettings.FILE_PATH, GeneralSettings.TEMP_DIR);
                }

            }
        };
        testMenuButtonList.add(button);

        //generate from file
        button = new TextButton("Generate Flowchart") {
            @Override
            public void onPress() {
                //Create temp file

                //Save To Temp Location
                if (!GeneralSettings.FILE_PATH.equals("null")) {
                    if (codeWindow != null) {
                        tfm.copyFiletoTempFile(GeneralSettings.FILE_PATH, GeneralSettings.TEMP_DIR);

                    }
                } else {
                    return;
                }

                //LC3Parser parser = new LC3Parser(GeneralSettings.FILE_PATH, true);
                //parser.ReadFile(GeneralSettings.FILE_PATH);

                tfm.update(); // Must be called if you change the files in temp!
                if(tfm.getMostRecent().equals("null")) {
                   return;
                }
                LC3Parser parser = new LC3Parser(tfm.getMostRecent(), false);
                parser.ReadFile(tfm.getMostRecent());

                parser.getFlowObjects();
                parser.createFlowchart();
            }
        };
        testMenuButtonList.add(button);


        button = new TextButton("Regenerate Flowchart From Editor") {
            @Override
            public void onPress() {
                //TODO: Nee way to keep track whether changes have been made in editor to know if we need to save or not...

                //Save what is in codeWindow
                if (codeWindow != null && GeneralSettings.FILE_PATH != null & !GeneralSettings.FILE_PATH.equals("null")) {
                    tfm.saveCodeEditorTextToFile(codeWindow.getTexts(), GeneralSettings.FILE_PATH, GeneralSettings.TEMP_DIR);
                } else {
                    return;
                }

                tfm.update(); // Must be called if you change the files in temp!
                if(tfm.getMostRecent().equals("null")) {
                    return;
                }
                LC3Parser parser = new LC3Parser(tfm.getMostRecent(), false);
                parser.ReadFile(tfm.getMostRecent());

                parser.getFlowObjects();
                parser.createFlowchart();

            }
        };
        testMenuButtonList.add(button);
        button = new TextButton("Regenerate Flowchart From Source") {
            @Override
            public void onPress() {


                if(GeneralSettings.FILE_PATH.equals("null")) {
                    return;
                }

                LC3Parser parser = new LC3Parser(GeneralSettings.FILE_PATH, true);
                parser.ReadFile(GeneralSettings.FILE_PATH);
                parser.getFlowObjects();
                parser.createFlowchart();

            }
        };
        testMenuButtonList.add(button);
        button = new TextButton("Text Editor View") {
            @Override
            public void onPress() {
                if(codeWindow != null && flowChartWindow != null) {
                    codeWindow.maximize();
                    flowChartWindow.minimize();
                }
            }
        };
        testMenuButtonList.add(button);
        button = new TextButton("Flowchart View") {
            @Override
            public void onPress() {
                if(codeWindow != null && flowChartWindow != null) {
                    codeWindow.minimize();
                    flowChartWindow.maximize();
                }
            }
        };
        testMenuButtonList.add(button);
        button = new TextButton("Splitscreen View") {
            @Override
            public void onPress() {
                if(codeWindow != null && flowChartWindow != null) {
                    codeWindow.goSplitScreen();
                    flowChartWindow.goSplitScreen();
                }
            }
        };


        testMenuButtonList.add(button);
        HeaderMenu file = new HeaderMenu(new Vector2f(-1f, 1-GeneralSettings.FONT_SIZE*GeneralSettings.FONT_SCALING_FACTOR - 2*GeneralSettings.TEXT_BUTTON_PADDING), "File", new Vector3f(0, 0, 0), GeneralSettings.HIGHLIGHT_COLOR, GeneralSettings.CURSOR_COLOR, GeneralSettings.CONSOLAS, GeneralSettings.FONT_SIZE, GeneralSettings.FONT_WIDTH, GeneralSettings.FONT_EDGE, testMenuButtonList);
        menuList.add(file);
    }

    public GUIFilledBox getGuiFilledBox() {
        return guiFilledBox;
    }

    public Vector2f getPosition(){
        return position;
    }

    public List<HeaderMenu> getMenuList(){
        return menuList;
    }

    public void setFlowChartWindow(FlowChartWindow flowChartWindow){
        this.flowChartWindow = flowChartWindow;
    }
    public void setCodeWindow(CodeWindow codeWindow){
        this.codeWindow = codeWindow;
    }

    public CodeWindow getCodeWindow(){
        return codeWindow;
    }

    public Cursor getCursor(){
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public void setAspectRatio(Vector2f aspectRatio){
        Vector2f size = guiFilledBox.getSize();
        size.y /= this.aspectRatio.y;
        size.y *= aspectRatio.y;
        guiFilledBox.setSize(size);
        for(HeaderMenu menu : menuList){
            menu.setAspectRatio(new Vector2f(aspectRatio));
        }
        guiFilledBox.setPosition(new Vector2f(-1, 1-(1-guiFilledBox.getPosition().y)/this.aspectRatio.y*aspectRatio.y));
        this.aspectRatio = aspectRatio;
        if(codeWindow != null) {
            codeWindow.setAspectRatio(aspectRatio, size.y);
        }
    }

    public FlowChartWindow getFlowChartWindow(){
        return flowChartWindow;
    }
}
