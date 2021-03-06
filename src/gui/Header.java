package gui;

import controllers.ApplicationController;
import controllers.codeWindow.CodeWindowController;
import controllers.gui.ButtonController;
import gui.Notifications.AppEvents;
import gui.Notifications.EventRegenerateEditor;
import gui.Settings.SettingsMenu;
import gui.TempFiles.FlowchartToPng;
import gui.TempFiles.SaveToFile;
import gui.TempFiles.TempFileManager;
import gui.buttons.HeaderMenu;
import gui.buttons.TextButton;
import gui.texts.GUIText;
import gui.windows.AnalysisWindow;
import gui.windows.PartialWindow;
import gui.windows.PopupWindow;
import main.EngineTester;
import main.GeneralSettings;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import parser.GlobalParser;
import parser.Parser;
import rendering.renderEngine.MasterRenderer;
import rendering.text.TextMaster;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Header {
    private List<HeaderMenu> menuList;
    private GUIFilledBox guiFilledBox;
    private Vector2f position;
    private Vector2f aspectRatio = new Vector2f(1, 1);
    private TempFileManager tfm; //Manages the temp file paths
    private Parser parser = null; //should NOT need this anymore TODO: REMOVE
    private String windowTitle = null;
    ApplicationController controller;
    GUIText notificationText;
    private boolean isBtnOpenable = true;
    private SettingsMenu settingsMenu = null;


    public Header(Vector2f position, Vector2f size, ApplicationController controller){
        //Register Events

        menuList = new ArrayList<>();
        guiFilledBox = new GUIFilledBox(position, size, GeneralSettings.USERPREF.getHeaderColor3f());
        this.position = position;
        this.controller = controller;
        //Set up temp file manager
        //tfm = new TempFileManager(GeneralSettings.TEMP_DIR);
        tfm = new TempFileManager(GeneralSettings.USERPREF.getUserTempFileDirPath()); // Set to the last set user file path
        tfm.setFileLimit(GeneralSettings.USERPREF.getTempFileLimit());
        //Please set this to null if not file has been opened on launch
        GeneralSettings.FILE_PATH = null;

        //*****************************File buttons*****************
        //Create the buttons
        List<TextButton> fileMenuButtonList = new ArrayList<>();

        TextButton button = new TextButton("Open File") {
            @Override
            public void onPress() {
                if(isBtnOpenable == true) {
                    isBtnOpenable = false;
                    openFile();
                    isBtnOpenable = true;
                }

            }
        };
        fileMenuButtonList.add(button);

        button = new TextButton("Save") {
            @Override
            public void onPress() {
                if(isBtnOpenable == true) {
                    isBtnOpenable = false;
                    save();
                    isBtnOpenable = true;
                }
            }
        };
        fileMenuButtonList.add(button);

        button = new TextButton("Save As") {
            @Override
            public void onPress() {
                if(isBtnOpenable == true) {
                    isBtnOpenable = false;
                    saveAs();
                    isBtnOpenable = true;
                }
            }
        };
        fileMenuButtonList.add(button);

        button = new TextButton("Save flowchart") {
            @Override
            public void onPress() {
                if(isBtnOpenable == true) {
                    isBtnOpenable = false;
                    saveFlowchart();
                    isBtnOpenable = true;
                }
            }
        };
        fileMenuButtonList.add(button);

        button = new TextButton("Settings Menu") {
            @Override
            public void onPress() {
                settings();
            }
        };
        fileMenuButtonList.add(button);

        /*
        button = new TextButton("Popup test") {
            @Override
            public void onPress() {
                PopupWindow popupWindow = new PopupWindow("Popup","Have you heard the tragedy of darth plagueis the wise? It's not a story the jedi would tell you.", "cancel", "continue");
                popupWindow.setDeleteOnLostFocus(false);
            }
        };
        fileMenuButtonList.add(button);
        */

        //Create the header menu
        HeaderMenu fileButton = new HeaderMenu(new Vector2f(-1f, 1 - GeneralSettings.FONT_HEIGHT - 2 * GeneralSettings.TEXT_BUTTON_PADDING), "File ", GeneralSettings.USERPREF.getMenuBtnBGColor3f(), GeneralSettings.HIGHLIGHT_COLOR, GeneralSettings.TEXT_COLOR, GeneralSettings.FONT, GeneralSettings.FONT_SIZE, GeneralSettings.FONT_WIDTH, GeneralSettings.FONT_EDGE, fileMenuButtonList);
        menuList.add(fileButton);

        //*****************************Flowchart*************************
        //Create the buttons
        List<TextButton> flowchartButtonList = new ArrayList<>();

        button = new TextButton("Generate Flowchart") {
            @Override
            public void onPress() {
                generate(GeneralSettings.OPEN_PARTIAL_FILE);
            }
        };
        flowchartButtonList.add(button);

        button = new TextButton("Regenerate Flowchart From Editor") {
            @Override
            public void onPress() {
                regenerateFromEditor(GeneralSettings.OPEN_PARTIAL_FILE);
            }
        };
        flowchartButtonList.add(button);

        button = new TextButton("Regenerate Flowchart From Source") {
            @Override
            public void onPress() {
                regenerateFromSource(GeneralSettings.OPEN_PARTIAL_FILE);
            }
        };
        flowchartButtonList.add(button);

        //Create the header menu
        HeaderMenu flowchartButton = new HeaderMenu(new Vector2f(fileButton.getPosition().x + fileButton.getSize().x, 1 - GeneralSettings.FONT_HEIGHT - 2 * GeneralSettings.TEXT_BUTTON_PADDING), "Flowchart ", GeneralSettings.USERPREF.getMenuBtnBGColor3f(), GeneralSettings.HIGHLIGHT_COLOR, GeneralSettings.TEXT_COLOR, GeneralSettings.FONT, GeneralSettings.FONT_SIZE, GeneralSettings.FONT_WIDTH, GeneralSettings.FONT_EDGE, flowchartButtonList);
        menuList.add(flowchartButton);

        //*******************View***********************
        //Create the buttons
        List<TextButton> viewButtonList = new ArrayList<>();

        button = new TextButton("Text Editor View") {
            @Override
            public void onPress() {
                textEditorView();
            }
        };
        viewButtonList.add(button);

        button = new TextButton("Flowchart View") {
            @Override
            public void onPress() {
                flowchartView();
            }
        };
        viewButtonList.add(button);

        button = new TextButton("Splitscreen View") {
            @Override
            public void onPress() {
                splitScreenView();
            }
        };
        viewButtonList.add(button);

        button = new TextButton("Reset zoom") {
            @Override
            public void onPress() {
                resetZoom();
            }
        };
        viewButtonList.add(button);

        //Create the header menu
        HeaderMenu viewButton = new HeaderMenu(new Vector2f(flowchartButton.getPosition().x + flowchartButton.getSize().x, 1 - GeneralSettings.FONT_HEIGHT - 2 * GeneralSettings.TEXT_BUTTON_PADDING), "View ", GeneralSettings.USERPREF.getMenuBtnBGColor3f(), GeneralSettings.HIGHLIGHT_COLOR, GeneralSettings.TEXT_COLOR, GeneralSettings.FONT, GeneralSettings.FONT_SIZE, GeneralSettings.FONT_WIDTH, GeneralSettings.FONT_EDGE, viewButtonList);
        menuList.add(viewButton);

        //*****************Analysis*******************
        //Create the buttons
        List<TextButton> analyticsMenuButtonList = new ArrayList<>();

        button = new TextButton("Partial Tag Start") {
            @Override
            public void onPress() {
                setPartialTag();
            }
        };
        analyticsMenuButtonList.add(button);

        button = new TextButton("Partial Tag End") {
            @Override
            public void onPress() {
                setPartialTagClosing();
            }
        };
        analyticsMenuButtonList.add(button);

        button = new TextButton("Registers") {
            @Override
            public void onPress() {
                registers();
            }
        };
        analyticsMenuButtonList.add(button);

        button = new TextButton("Invalid Labels") {
            @Override
            public void onPress() {
                invalidLabels();
            }
        };
        analyticsMenuButtonList.add(button);

        button = new TextButton("Clear") {
            @Override
            public void onPress() {
                clearRegisters();
            }
        };
        analyticsMenuButtonList.add(button);

        HeaderMenu analyticsButton = new HeaderMenu(new Vector2f(viewButton.getPosition().x + viewButton.getSize().x, 1 - GeneralSettings.FONT_HEIGHT - 2 * GeneralSettings.TEXT_BUTTON_PADDING), "Analysis ", GeneralSettings.USERPREF.getMenuBtnBGColor3f(), GeneralSettings.HIGHLIGHT_COLOR, GeneralSettings.TEXT_COLOR, GeneralSettings.FONT, GeneralSettings.FONT_SIZE, GeneralSettings.FONT_WIDTH, GeneralSettings.FONT_EDGE, analyticsMenuButtonList);
        menuList.add(analyticsButton);

        //Add the header menus to the button controller to allow them to be used
        for(HeaderMenu headerMenu: menuList){
            ButtonController.add(headerMenu);
        }
    }

    public GUIFilledBox getGuiFilledBox() {
        return guiFilledBox;
    }

    /**
     * Changes the background color based on the current color of the User Preferences
     * */
    public void changeHeadercolor() {
        guiFilledBox.setColor(GeneralSettings.USERPREF.getHeaderColor3f());
    }

    /**
     * Changes the buttons background color and highlight color based on the current colors of the User Preferences
     * */
    public void changeButtonColors() {
        for(int i = 0; i < menuList.size(); i++) {
            menuList.get(i).changeButtonColors(GeneralSettings.USERPREF.getMenuBtnBGColor3f(), GeneralSettings.USERPREF.getMenuBtnHLColor3f());
        }
    }

    /**
     * Changes the number of files that the temp file manger stores.
     * */
    public void changeTempFileManagerLimit(int limit) {
        tfm.setFileLimit(limit);
    }



    public Vector2f getPosition(){
        return position;
    }

    public void setAspectRatio(Vector2f aspectRatio){
        Vector2f size = guiFilledBox.getSize();
        size.y /= this.aspectRatio.y;
        size.y *= aspectRatio.y;
        guiFilledBox.setSize(size);
        for (HeaderMenu menu : menuList) {
            menu.setAspectRatio(new Vector2f(aspectRatio));
        }
        for (int i = 1; i < menuList.size(); i++) {
            HeaderMenu lastMenu = menuList.get(i - 1);
            menuList.get(i).setPosition(new Vector2f(lastMenu.getPosition().x + lastMenu.getSize().x, lastMenu.getPosition().y));
        }
        guiFilledBox.setPosition(new Vector2f(-1, 1 - (1 - guiFilledBox.getPosition().y) / this.aspectRatio.y * aspectRatio.y));
        getPosition().y = guiFilledBox.getPosition().y;

        //Update the position of any notifications that still exist
        if(notificationText != null) {
            notificationText.getPosition().x = 1 / aspectRatio.x - (float) notificationText.getLength() * 2;
            notificationText.getPosition().y = guiFilledBox.getPosition().y / aspectRatio.y + GeneralSettings.FONT_HEIGHT;
        }
        this.aspectRatio = aspectRatio;
    }

    /**
     * Opens a file
     */
    public void openFile(){
        OpenFileDialog of = new OpenFileDialog();
        of.setFilterList(GeneralSettings.USERPREF.getPreferredFiletype());
        of.openFileWindow();
        GeneralSettings.FILE_PATH = of.getFilePath();


        // If the file exists, load it into the text editor
        if (GeneralSettings.FILE_PATH != null){
            if (GeneralSettings.FILE_PATH.contains("/")){
                windowTitle = "ALFAT – " + GeneralSettings.FILE_PATH.substring(GeneralSettings.FILE_PATH.lastIndexOf('/')+1);
                GLFW.glfwSetWindowTitle(EngineTester.getWindow(), "ALFAT – " + GeneralSettings.FILE_PATH.substring(GeneralSettings.FILE_PATH.lastIndexOf('/')+1));
            } else {
                windowTitle = "ALFAT " + GeneralSettings.FILE_PATH;
                GLFW.glfwSetWindowTitle(EngineTester.getWindow(), "ALFAT " + GeneralSettings.FILE_PATH);
            }

            String content = "";
            // Load the text file:
            try {
                File file = new File(GeneralSettings.FILE_PATH);
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    // content += line.replace("\t", "    ")
                    content += line + '\n';
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (controller.getCodeWindowController() != null) {
                controller.getCodeWindowController().clear();
            }
            //create code window
            controller.setCodeWindowController(new CodeWindowController(new Vector2f(-1f, -1f), new Vector2f(1f, 2 - GeneralSettings.FONT_HEIGHT), GeneralSettings.USERPREF.getTexteditorBGColor3f(), GeneralSettings.TEXT_COLOR, new Vector3f(0, 0, 0), content, GeneralSettings.FONT, GeneralSettings.FONT_SIZE, GeneralSettings.FONT_WIDTH, GeneralSettings.FONT_EDGE, GeneralSettings.TEXT_BOX_BORDER_WIDTH, guiFilledBox.getSize().y, controller.getTextLineController()));

            if (controller.getFlowchartWindowController() != null) {
                controller.getFlowchartWindowController().goSplitScreen();
            }

            //User Settings logic for file open

            //Auto gen flowchart
            if(GeneralSettings.USERPREF.getAutoGenFlowchart()) {
                generate(GeneralSettings.OPEN_PARTIAL_FILE);
                if(GeneralSettings.USERPREF.getFullscreen() < 0) {
                    //full flowchart
                    flowchartView();
                }
            }
            if(GeneralSettings.USERPREF.getSplitScreen()){
                splitScreenView();
            }
            if(GeneralSettings.USERPREF.getFullscreen() > 0) {
                textEditorView();
            }
            if(GlobalParser.PARSER_MANAGER.isSyntaxValid()) {
                controller.notification.setEvent(AppEvents.INVALID_SYNTAX_FILE);
            } else {
                controller.notification.setEvent(AppEvents.OPEN_FILE);
            }
        }else {
            controller.notification.setEvent(AppEvents.OPEN_FILE_FAIL);
        }
    }
    /**
     * Opens a file from a given string
     */
    public void openFile(String path){
        GeneralSettings.FILE_PATH = path;


        // If the file exists, load it into the text editor.
        if (GeneralSettings.FILE_PATH != null){

            if (GeneralSettings.FILE_PATH.contains("/")){
                windowTitle = "ALFAT – " + GeneralSettings.FILE_PATH.substring(GeneralSettings.FILE_PATH.lastIndexOf('/')+1);
                GLFW.glfwSetWindowTitle(EngineTester.getWindow(), "ALFAT – " + GeneralSettings.FILE_PATH.substring(GeneralSettings.FILE_PATH.lastIndexOf('/')+1));
            } else {
                windowTitle = "ALFAT " + GeneralSettings.FILE_PATH;
                GLFW.glfwSetWindowTitle(EngineTester.getWindow(), "ALFAT " + GeneralSettings.FILE_PATH);
            }

            String content = "";
            // Load the text file:
            try {
                File file = new File(GeneralSettings.FILE_PATH);
                if(file == null){
                    return;
                }
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    // content += line.replace("\t", "    ")
                    content += line + '\n';
                }
            } catch (Exception e) {
                e.printStackTrace();
                //TODO: Provide proper warnings
                return;
            }


            if (controller.getCodeWindowController() != null) {
                controller.getCodeWindowController().clear();
            }
            //create code window
            controller.setCodeWindowController(new CodeWindowController(new Vector2f(-1f, -1f), new Vector2f(1f, 2 - GeneralSettings.FONT_HEIGHT), GeneralSettings.USERPREF.getTexteditorBGColor3f(), GeneralSettings.TEXT_COLOR, new Vector3f(0, 0, 0), content, GeneralSettings.FONT, GeneralSettings.FONT_SIZE, GeneralSettings.FONT_WIDTH, GeneralSettings.FONT_EDGE, GeneralSettings.TEXT_BOX_BORDER_WIDTH, guiFilledBox.getSize().y, controller.getTextLineController()));

            if (controller.getFlowchartWindowController() != null) {
                controller.getFlowchartWindowController().goSplitScreen();
            }

            //User Settings logic for file open

            //Auto gen flowchart
            if(GeneralSettings.USERPREF.getAutoGenFlowchart()) {
                generate(GeneralSettings.OPEN_PARTIAL_FILE);
                if(GeneralSettings.USERPREF.getFullscreen() < 0) {
                    //full flowchart
                    flowchartView();
                }
            }
            if(GeneralSettings.USERPREF.getSplitScreen()){
                splitScreenView();
            }
            if(GeneralSettings.USERPREF.getFullscreen() > 0) {
                textEditorView();
            }
        }
    }

    /**
     * Saves the file to the currently selected location
     */
    public void save(){
        //Saves contents of the text editor over the original source file
        //TempFileOperations tfo = new TempFileOperations();
        if (controller.getCodeWindowController() != null && GeneralSettings.FILE_PATH != null){
            SaveToFile stf = new SaveToFile(GeneralSettings.FILE_PATH);
            stf.save(controller.getCodeWindowController().getTexts());
        }
    }

    /**
     * Saves the file to a location selected with NFD
     */
    public void saveAs(){
        OpenFileDialog of = new OpenFileDialog();
        of.setFilterList(GeneralSettings.USERPREF.getPreferredFiletype());
        of.saveFileWindow();
        //System.out.println(of.getFilePath());

        //If the use saved a file
        if (of.getFilePath() != null) {
            SaveToFile stf = new SaveToFile(of.getFilePath());
            //Prevent crash if codeWindow doe not have anything in it
            if (controller.getCodeWindowController() != null) {
                GeneralSettings.FILE_PATH = of.getFilePath();
                if (GeneralSettings.FILE_PATH.contains("/")){
                    windowTitle = "ALFAT – " + GeneralSettings.FILE_PATH.substring(GeneralSettings.FILE_PATH.lastIndexOf('/')+1);
                    GLFW.glfwSetWindowTitle(EngineTester.getWindow(), "ALFAT – " + GeneralSettings.FILE_PATH.substring(GeneralSettings.FILE_PATH.lastIndexOf('/')+1));
                } else {
                    windowTitle = "ALFAT " + GeneralSettings.FILE_PATH;
                    GLFW.glfwSetWindowTitle(EngineTester.getWindow(), "ALFAT " + GeneralSettings.FILE_PATH);
                }
                stf.save(controller.getCodeWindowController().getTexts());
            }
        }
    }

    /**
     * Saves an image of the flowchart
     */
    public void saveFlowchart(){
        //If no flowchart has been generated return
        if (GeneralSettings.IMAGE_SIZE == null) {
            return;
        }

        //Get the file form the user
        OpenFileDialog ofd = new OpenFileDialog();
        ofd.setFilterList("png;png");
        ofd.saveFileWindow();
        String outFile = ofd.getFilePath();

        System.out.println("Saving to image file. This may take some time...");
        if(outFile == null) {
            System.out.println("Cancelled.");
            return; // no path or clicked cancel
        }

        //Determine the width and height of the image in pixels
        float adjust = 0;
        long check = Integer.MAX_VALUE;
        double widthf;
        int width;
        double heightf;
        int height;

        //Adjust the size of the pixels used to ensure that it can't go over the max int.
        //TODO:Place overhead into the flowchart to png class
        do {
            widthf = (1.5 * GeneralSettings.IMAGE_SIZE.x)  * (double)GeneralSettings.DEFAULT_WIDTH / (2f);
            widthf += -(adjust*1000);
            //System.out.println("widthf " + widthf);
            width = (int) widthf;
            //System.out.println("width " + width);
            heightf = (1.5 * GeneralSettings.IMAGE_SIZE.y)  * (double)GeneralSettings.DEFAULT_HEIGHT / (2f);
            heightf += -(adjust*1000);
            //System.out.println("heightf " + heightf);
            height = (int) heightf;
            //System.out.println("height " + height);
            adjust += .1;
        }while (check < ((long)width*(long)height*4));



        FlowchartToPng flowchartToPng = new FlowchartToPng(GeneralSettings.USERPREF.getUserTempFileDirPath());

        flowchartToPng.startImageSlice(width, height, controller, outFile);
        System.out.println("Done.");
    }

    /**
     * Opens the settings menu
     */
    public void settings(){
        if (settingsMenu == null) {
            settingsMenu = new SettingsMenu();
            settingsMenu.setVisible();
        } else {
            settingsMenu.setVisible();
        }

    }

    /**
     * Generates the flowchart
     */
    public void generate(boolean partial){
        //Create temp file

        //Ensure that a file is open
        if (GeneralSettings.FILE_PATH == null) {
            return;
        }

        //Save To Temp Location
        if (controller.getCodeWindowController() != null) {
            tfm.copyFiletoTempFile(GeneralSettings.FILE_PATH, GeneralSettings.USERPREF.getUserTempFileDirPath());

        }

        tfm.update(); // Must be called if you change the files in temp!
        if(tfm.getMostRecent() == null) {
            return;
        }

//        parser = new Parser( false);
//        parser.ReadFile(tfm.getMostRecent());
//        parser.generateFlowObjects();
//        controller.setFlowchartWindowController(parser.createFlowchart(controller));
//        controller.flowchartView();

        if (!partial) {
            if (GlobalParser.PARSER_MANAGER.attemptFileParse(tfm.getMostRecent())) {
                controller.setFlowchartWindowController(GlobalParser.PARSER_MANAGER.getParser().createFlowchart(controller));
                controller.flowchartView();
            }
        } else {
            // This is opening a partial file.
            // prompt user to input tag:
            // attempt to parse to tag:
            if(GlobalParser.PARSER_MANAGER.attemptPartialFileParse(tfm.getMostRecent(), GeneralSettings.PARTIAL_FILE_TAG_TARGET)){
                controller.setFlowchartWindowController(GlobalParser.PARSER_MANAGER.getParser().createFlowchart(controller));
                controller.flowchartView();
            }
        }
    }

    /**
     * Regenerates the flowchart from the modified code in the code window
     */
    public void regenerateFromEditor(boolean partial){
        //TODO: Need way to keep track whether changes have been made in editor to know if we need to save or not...

        //Save what is in codeWindow
        if (controller.getCodeWindowController() != null && GeneralSettings.FILE_PATH != null) {
            tfm.saveCodeEditorTextToFile(controller.getCodeWindowController().getTextLineController().getCodeWindowTextLines(), GeneralSettings.FILE_PATH, GeneralSettings.USERPREF.getUserTempFileDirPath());
        } else {
            return;
        }

        tfm.update(); // Must be called if you change the files in temp!
        if(tfm.getMostRecent() == null) {
            return;
        }
//        parser  = new Parser( false);
//        parser.ReadFile(tfm.getMostRecent());
//        parser.generateFlowObjects();
//        parser.createFlowchart(controller);
//        controller.setFlowchartWindowController(parser.createFlowchart(controller));
//        controller.flowchartView();
        if (!partial) {
            if(GlobalParser.PARSER_MANAGER.attemptFileParse(tfm.getMostRecent())){
                controller.setFlowchartWindowController(GlobalParser.PARSER_MANAGER.getParser().createFlowchart(controller));
                controller.flowchartView();
            }
        } else {
            // This is opening a partial file.
            // prompt user to input tag:
            // attempt to parse to tag:
            if(GlobalParser.PARSER_MANAGER.attemptPartialFileParse(tfm.getMostRecent(), GeneralSettings.PARTIAL_FILE_TAG_TARGET)){
                controller.setFlowchartWindowController(GlobalParser.PARSER_MANAGER.getParser().createFlowchart(controller));
                controller.flowchartView();
            }
        }

        ApplicationController.notification.setEvent(AppEvents.REGENERATE_FROM_EDITOR);

    }

    /**
     * Regenerates the flowchart from the original file
     */
    public void regenerateFromSource(boolean partial){
        if(GeneralSettings.FILE_PATH == null) {
            return;
        }

//        parser = new Parser(true);
//        parser.ReadFile(GeneralSettings.FILE_PATH);
//        parser.generateFlowObjects();
//        parser.createFlowchart(controller);
//        controller.setFlowchartWindowController(parser.createFlowchart(controller));
//        controller.flowchartView();


        if (!partial) {
            if(GlobalParser.PARSER_MANAGER.attemptFileParse(GeneralSettings.FILE_PATH)){
                controller.setFlowchartWindowController(GlobalParser.PARSER_MANAGER.getParser().createFlowchart(controller));
                controller.flowchartView();
            }
        } else {
            // This is opening a partial file.
            // prompt user to input tag:
            // attempt to parse to tag:
            if(GlobalParser.PARSER_MANAGER.attemptPartialFileParse(GeneralSettings.FILE_PATH, GeneralSettings.PARTIAL_FILE_TAG_TARGET)){
                controller.setFlowchartWindowController(GlobalParser.PARSER_MANAGER.getParser().createFlowchart(controller));
                controller.flowchartView();
            }
        }

        ApplicationController.notification.setEvent(AppEvents.REGENERATE_FROM_FILE);
    }

    /**
     * Sets ALFAT to display only the text editor
     */
    public void textEditorView(){
        controller.textEditorView();
    }

    /**
     * Sets ALFAT to display only the flowchart
     */
    public void flowchartView(){
        controller.flowchartView();
    }

    /**
     * Sets ALFAT to use a split screen display
     */
    public void splitScreenView(){
        controller.splitScreen();
    }

    /**
     * Sets zooming and panning to default
     */
    public void resetZoom(){
        if (controller.getFlowchartWindowController() != null) {
            controller.getFlowchartWindowController().resetZoom();
        }
    }

    /** Pan flowchart vertically.
     *
     * @param amount Distance moved where 2f is 100% of the viewport height.
     */
    public void screenPan(float amount){
        if (controller.getFlowchartWindowController() != null) {
            controller.getFlowchartWindowController().screenPan(amount);
        }
    }

    /**
     * Clears register highlighting
     */
    public void clearRegisters(){
        if (controller.getFlowchartWindowController() != null) {
            //A null alert is used to clear current highlighting
            controller.getFlowchartWindowController().locateAlert(null);
            //Clear the conditional window title
            GLFW.glfwSetWindowTitle(EngineTester.getWindow(), windowTitle);
        }
    }

    /**
     * Opens register usage menu and highlights registers based on the inputted string
     */
    public void registers(){
        if(controller.getFlowchartWindowController() != null) {
            AnalysisWindow analysisWindow = new AnalysisWindow(controller);
//            String args = ReturnString.search("Search", "Cancel", "Run", "Register Search");
//            if (args != null){
//                GLFW.glfwSetWindowTitle(EngineTester.getWindow(), windowTitle + " [" + args + "]");
//                controller.getFlowchartWindowController().locateRegisters(args);
//            } else {
//                controller.getFlowchartWindowController().clearHighlighting();
//            }
        }
    }

    /**
     * Highlights text boxes with invalid labels
     */
    public void invalidLabels(){
        if (controller.getFlowchartWindowController() != null) {
            controller.getFlowchartWindowController().locateAlert("invalid_label");
            GLFW.glfwSetWindowTitle(EngineTester.getWindow(), windowTitle + " [Invalid labels]");
        }
        setNotificationText("Invalid labels");
    }

    /**
     * Sets the target tag for opening a partial file.
     *
     */
    public void setPartialTag(){
        PartialWindow partialDialogueWindow = new PartialWindow(controller, true);
    }

    public void setPartialTagClosing(){
        PartialWindow partialDialogueWindow = new PartialWindow(controller, false);
    }

    public GUIText getNotificationText(){
        return notificationText;
    }

    public void setNotificationText(String textString){
        if(notificationText != null){
            TextMaster.removeGuiText(notificationText);
        }
        //TODO let me change the color of the text for this!!!!
        notificationText = new GUIText(textString, GeneralSettings.FONT_SIZE, new Vector2f(0, 0), GeneralSettings.FONT);
        notificationText.setPosition(new Vector2f((float) (1/aspectRatio.x-notificationText.getLength()*2), this.getPosition().y/aspectRatio.y + GeneralSettings.FONT_HEIGHT));
    }

    public void clearNotificationText(){
        if(notificationText != null) {
            TextMaster.removeGuiText(notificationText);
        }
        notificationText = null;
    }
}
