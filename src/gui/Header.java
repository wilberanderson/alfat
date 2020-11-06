package gui;

import gui.textBoxes.CodeWindow;
import main.EngineTester;
import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import parser.CodeReader;
import utils.MyFile;


import java.awt.geom.GeneralPath;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import gui.buttons.HeaderMenu;
import gui.buttons.TextButton;

public class Header {
    private List<HeaderMenu> menuList;
    private GUIFilledBox guiFilledBox;
    private Vector2f position;
    private FlowChartWindow flowChartWindow;
    private CodeWindow codeWindow;
    private Cursor cursor;


    public Header(Vector2f position, Vector2f size){
        menuList = new ArrayList<>();
        guiFilledBox = new GUIFilledBox(position, size, GeneralSettings.HEADER_COLOR);
        this.position = position;



        List<TextButton> testMenuButtonList = new ArrayList<>();
        TextButton button = new TextButton("Open File") {
            @Override
            public void onPress() {
                //System.out.println("Open File");

                //Test example notice that file path isn't hello world
                GeneralSettings.FILE_PATH = "";
                OpenFileDialog of = new OpenFileDialog();
                //of.displayConsole(true);
                of.openWindow();
                GeneralSettings.FILE_PATH = of.getFilePath();

                // If the file exists, load it into the text editor.
                if (!GeneralSettings.FILE_PATH.equals("null")){
                    String content = "";
                    try{
                        File file = new File(GeneralSettings.FILE_PATH);
                        BufferedReader reader = new BufferedReader(new FileReader(file));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            content += line;
                            content += '\n';
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    if(codeWindow != null) {
                        codeWindow.clear();
                    }
                    //create code window
                    codeWindow = new CodeWindow(new Vector2f(0f,0f), new Vector2f(1f, 2-GeneralSettings.FONT_SCALING_FACTOR*GeneralSettings.FONT_SIZE), new Vector3f(0.1f,0.1f,0.1f), new Vector3f(1,1,1), new Vector3f(0,0,0), content, GeneralSettings.TACOMA, GeneralSettings.FONT_SIZE, GeneralSettings.FONT_WIDTH, GeneralSettings.FONT_EDGE, GeneralSettings.TEXT_BOX_BORDER_WIDTH);
                    cursor = new Cursor(new Vector2f(codeWindow.getPosition()), codeWindow);
                }
            }
        };
        testMenuButtonList.add(button);
        button = new TextButton("Generate Flowchart") {
            @Override
            public void onPress() {
                System.out.println("Test success Button 1");
            }
        };
        testMenuButtonList.add(button);
        button = new TextButton("Regenerate Flowchart From Editor") {
            @Override
            public void onPress() {
                System.out.println("Test success Button 2");
            }
        };
        testMenuButtonList.add(button);
        button = new TextButton("Regenerate Flowchart From Source") {
            @Override
            public void onPress() {
                System.out.println("Test success Button 3");
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
        HeaderMenu file = new HeaderMenu(new Vector2f(-1f, 1-GeneralSettings.FONT_SIZE*GeneralSettings.FONT_SCALING_FACTOR - 2*GeneralSettings.TEXT_BUTTON_PADDING), "File", new Vector3f(0, 0, 0), GeneralSettings.HIGHLIGHT_COLOR, GeneralSettings.CURSOR_COLOR, GeneralSettings.TACOMA, GeneralSettings.FONT_SIZE, GeneralSettings.FONT_WIDTH, GeneralSettings.FONT_EDGE, testMenuButtonList);
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
}
