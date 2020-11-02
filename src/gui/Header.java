package gui;

import main.GeneralSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Header {
    private List<HeaderMenu> menuList;
    private GUIFilledBox guiFilledBox;
    private Vector2f position;
    private FlowChartWindow flowChartWindow;
    private CodeWindow codeWindow;


    public Header(Vector2f position, Vector2f size){
        menuList = new ArrayList<>();
        guiFilledBox = new GUIFilledBox(position, size, GeneralSettings.HEADER_COLOR);
        this.position = position;



        List<TextButton> testMenuButtonList = new ArrayList<>();
        TextButton button = new TextButton("Open File") {
            @Override
            public void onPress() {
                System.out.println("Open File");

                //Test example notice that file path isn't hello world
                GeneralSettings.FILE_PATH = "Hello World";
                OpenFileDialog of = new OpenFileDialog();
                of.displayConsole(true);
                of.openWindow();
                GeneralSettings.FILE_PATH = of.getFilePath();
                System.out.println(GeneralSettings.FILE_PATH);

            }
        };
        testMenuButtonList.add(button);
        button = new TextButton("Generate Flowchart") {
            @Override
            public void onPress() {
                System.out.println("Tes success Button 1");
            }
        };
        testMenuButtonList.add(button);
        button = new TextButton("Regenerate Flowchart From Editor") {
            @Override
            public void onPress() {
                System.out.println("Tes success Button 1");
            }
        };
        testMenuButtonList.add(button);
        button = new TextButton("Regenerate Flowchart From Source") {
            @Override
            public void onPress() {
                System.out.println("Tes success Button 1");
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
}
