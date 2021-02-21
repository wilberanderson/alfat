package gui;

import controllers.ApplicationController;
import controllers.codeWindow.CodeWindowController;
import controllers.gui.ButtonController;
import gui.Settings.ReturnString;
import gui.Settings.SettingsMenu;
import gui.buttons.HeaderMenu;
import gui.buttons.TextButton;
import gui.windows.AnalysisWindow;
import gui.windows.PopupWindow;
import main.EngineTester;
import main.GeneralSettings;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import parser.Parser;
import rendering.renderEngine.MasterRenderer;

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
    private Parser parser = null;
    private String windowTitle = null;
    ApplicationController controller;

    public Header(Vector2f position, Vector2f size, ApplicationController controller){
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
                openFile();
            }
        };
        fileMenuButtonList.add(button);

        button = new TextButton("Save") {
            @Override
            public void onPress() {
                save();

            }
        };
        fileMenuButtonList.add(button);

        button = new TextButton("Save As") {
            @Override
            public void onPress() {
                saveAs();
            }
        };
        fileMenuButtonList.add(button);

        button = new TextButton("Save flowchart") {
            @Override
            public void onPress() {
                saveFlowchart();
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

        //Create the header menu
        HeaderMenu fileButton = new HeaderMenu(new Vector2f(-1f, 1 - GeneralSettings.FONT_HEIGHT - 2 * GeneralSettings.TEXT_BUTTON_PADDING), "File ", GeneralSettings.USERPREF.getMenuBtnBGColor3f(), GeneralSettings.HIGHLIGHT_COLOR, GeneralSettings.TEXT_COLOR, GeneralSettings.FONT, GeneralSettings.FONT_SIZE, GeneralSettings.FONT_WIDTH, GeneralSettings.FONT_EDGE, fileMenuButtonList);
        menuList.add(fileButton);

        //*****************************Flowchart*************************
        //Create the buttons
        List<TextButton> flowchartButtonList = new ArrayList<>();

        button = new TextButton("Generate Flowchart") {
            @Override
            public void onPress() {
                generate();
            }
        };
        flowchartButtonList.add(button);

        button = new TextButton("Regenerate Flowchart From Editor") {
            @Override
            public void onPress() {
                regenerateFromEditor();
            }
        };
        flowchartButtonList.add(button);

        button = new TextButton("Regenerate Flowchart From Source") {
            @Override
            public void onPress() {
                regenerateFromSource();
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
        this.aspectRatio = aspectRatio;
    }

    /**
     * Opens a file
     */
    public void openFile(){
        OpenFileDialog of = new OpenFileDialog();
        of.openFileWindow();
        of.setFilterList(GeneralSettings.USERPREF.getPreferredFiletype());
        GeneralSettings.FILE_PATH = of.getFilePath();


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
                generate();
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
     * TODO: Make this work
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
        System.out.println(of.getFilePath());

        //If the use saved a file
        if (of.getFilePath() != null) {
            SaveToFile stf = new SaveToFile(of.getFilePath());
            //Prevent crash if codeWindow doe not have anything in it
            if (controller.getCodeWindowController() != null) {
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

        //Determine the width and height of the image in pixels
        int width = (int) GeneralSettings.IMAGE_SIZE.x * GeneralSettings.DEFAULT_WIDTH / 2;
        int height = (int) GeneralSettings.IMAGE_SIZE.y * GeneralSettings.DEFAULT_HEIGHT / 2;

        //Create a frame buffer to render the image to
        int renderBuffer = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, renderBuffer);

        //Create a texture to load the data into
        int imageIndex = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, imageIndex);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB8, width, height, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, 0);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);

        //Configure frame buffer
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, imageIndex, 0);
        GL30.glDrawBuffers(GL30.GL_COLOR_ATTACHMENT0);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, renderBuffer);
        GL11.glViewport(0, 0, width, height);

        //Render the flowchart to the image
        MasterRenderer.renderScreenshot(controller.getFlowchartWindowController());

        //Load the data in the frame buffer into a byte buffer which can be saved to an image
        int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
        GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

        //Find where to save the file
        OpenFileDialog openFileDialog = new OpenFileDialog();
        openFileDialog.setFilterList("png,jpg");
        openFileDialog.saveFileWindow();

        //Ensure a valid file path is entered before saving
        String path = openFileDialog.getFilePath();
        if(path != null) {
            //Create the file
            File file = new File(path);
            String format = "PNG"; // Example: "PNG" or "JPG"
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            //Read the data from the byte buffer
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int i = (x + (width * y)) * bpp;
                    int r = buffer.get(i) & 0xFF;
                    int g = buffer.get(i + 1) & 0xFF;
                    int b = buffer.get(i + 2) & 0xFF;
                    image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
                }
            }

            //Attempt to save the image
            try {
                ImageIO.write(image, format, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Delete the frame buffer when done
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        GL11.glViewport(0, 0, GeneralSettings.DISPLAY_WIDTH, GeneralSettings.DISPLAY_HEIGHT);
        GL11.glDeleteTextures(imageIndex);
        GL30.glDeleteFramebuffers(renderBuffer);
    }

    /**
     * Opens the settings menu
     */
    public void settings(){
        SettingsMenu sMenu = new SettingsMenu();
    }

    /**
     * Generates the flowchart
     */
    public void generate(){
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
        parser = new Parser(tfm.getMostRecent(), false);
        parser.ReadFile(tfm.getMostRecent());

        parser.generateFlowObjects();
        controller.setFlowchartWindowController(parser.createFlowchart(controller));

        controller.flowchartView();
    }

    /**
     * Regenerates the flowchart from the modified code in the code window
     */
    public void regenerateFromEditor(){
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
        parser  = new Parser(tfm.getMostRecent(), false);
        parser.ReadFile(tfm.getMostRecent());

        parser.generateFlowObjects();
        parser.createFlowchart(controller);
    }

    /**
     * Regenerates the flowchart from the original file
     */
    public void regenerateFromSource(){
        if(GeneralSettings.FILE_PATH == null) {
            return;
        }

        parser = new Parser(GeneralSettings.FILE_PATH, true);
        parser.ReadFile(GeneralSettings.FILE_PATH);
        parser.generateFlowObjects();
        parser.createFlowchart(controller);
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
    }
}
