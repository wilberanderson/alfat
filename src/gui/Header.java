package gui;

import controllers.ApplicationController;
import controllers.codeWindow.CodeWindowController;
import controllers.gui.ButtonController;
import gui.Settings.SettingsMenu;
import main.EngineTester;
import main.GeneralSettings;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import parser.LC3Parser;


import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import gui.buttons.HeaderMenu;
import gui.buttons.TextButton;
import org.lwjgl.glfw.GLFW;
import rendering.renderEngine.MasterRenderer;

import javax.imageio.ImageIO;

public class Header {
    private List<HeaderMenu> menuList;
    private GUIFilledBox guiFilledBox;
    private Vector2f position;
    private Vector2f aspectRatio = new Vector2f(1, 1);
    private TempFileManager tfm; //Manages the temp file paths
    private LC3Parser parser = null;
    private String windowTitle = null;

    public Header(Vector2f position, Vector2f size, ApplicationController controller){
        menuList = new ArrayList<>();
        guiFilledBox = new GUIFilledBox(position, size, GeneralSettings.USERPREF.getHeaderColor3f());
        this.position = position;

        //Set up temp file manager
        //tfm = new TempFileManager(GeneralSettings.TEMP_DIR);
        tfm = new TempFileManager(GeneralSettings.USERPREF.getUserTempFileDirPath()); // Set to the last set user file path
        tfm.setFileLimit(5);
        //Please set this to null if not file has been opened on launch
        GeneralSettings.FILE_PATH = null;

        List<TextButton> testMenuButtonList = new ArrayList<>();
        List<TextButton> registerMenuButtonList = new ArrayList<>();
        List<TextButton> analyticsMenuButtonList = new ArrayList<>();
        List<TextButton> settingsMenuButtonList = new ArrayList<>();

        //open file
        TextButton button = new TextButton("Open File") {
            @Override
            public void onPress() {

                System.out.println(GeneralSettings.USERPREF.getUserTempFileDirPath());

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
                    if(controller.getCodeWindowController() != null) {
                        controller.getCodeWindowController().clear();

                    }
                    //create code window
                    controller.setCodeWindowController(new CodeWindowController(new Vector2f(0f,0f), new Vector2f(1f, 2-GeneralSettings.FONT_SCALING_FACTOR*GeneralSettings.FONT_SIZE), GeneralSettings.USERPREF.getTexteditorBGColor3f(), GeneralSettings.TEXT_COLOR, new Vector3f(0,0,0), content, GeneralSettings.FONT, GeneralSettings.FONT_SIZE, GeneralSettings.FONT_WIDTH, GeneralSettings.FONT_EDGE, GeneralSettings.TEXT_BOX_BORDER_WIDTH, size.y));

                    if (controller.getFlowchartWindowController() != null){
                        controller.getFlowchartWindowController().goSplitScreen();
                    }

                    //User Settings logic for file open

                    //Auto gen flowchart
                    if(GeneralSettings.USERPREF.getAutoGenFlowchart()) {
                        testMenuButtonList.get(4).onPress(); //gen flowchart
                    }
                    if(GeneralSettings.USERPREF.getSplitScreen()){
                        testMenuButtonList.get(9).onPress(); //Split screen
                    }
                    if(GeneralSettings.USERPREF.getFullscreen() > 0) {
                        testMenuButtonList.get(7).onPress(); //full editor
                    }
                    if(GeneralSettings.USERPREF.getFullscreen() < 0) {
                        //full flowchart
                        testMenuButtonList.get(4).onPress();
                        testMenuButtonList.get(8).onPress();
                    }
                }
            }
        };
        testMenuButtonList.add(button);

        //save file
        button = new TextButton("Save As") {
            @Override
            public void onPress() {
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
        };
        testMenuButtonList.add(button);

        button = new TextButton("Save") {
            @Override
            public void onPress() {
                //Saves contents of the text editor over the original source file
                //TempFileOperations tfo = new TempFileOperations();
                if (controller.getCodeWindowController() != null && GeneralSettings.FILE_PATH != null){
                   SaveToFile stf = new SaveToFile(GeneralSettings.FILE_PATH);
                   stf.save(controller.getCodeWindowController().getTexts());
                }

            }
        };
        testMenuButtonList.add(button);

        button = new TextButton("Save flowchart") {
            @Override
            public void onPress() {

                if(GeneralSettings.SCREENSHOT_SIZE == null){
                    return;
                }

                GeneralSettings.SCREENSHOT_IN_PROGRESS = true;
                int width = (int)GeneralSettings.SCREENSHOT_SIZE.x*GeneralSettings.DEFAULT_WIDTH/2;
                int height= (int)GeneralSettings.SCREENSHOT_SIZE.y*GeneralSettings.DEFAULT_HEIGHT/2;

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


                MasterRenderer.renderScreenshot(controller.getFlowchartWindowController());

                int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
                ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
                GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer );

                OpenFileDialog openFileDialog = new OpenFileDialog();
                openFileDialog.setFilterList("png,jpg");
                openFileDialog.saveFileWindow();
                File file = new File(openFileDialog.getFilePath());
                String format = "PNG"; // Example: "PNG" or "JPG"
                BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

                for(int x = 0; x < width; x++)
                {
                    for(int y = 0; y < height; y++)
                    {
                        int i = (x + (width * y)) * bpp;
                        int r = buffer.get(i) & 0xFF;
                        int g = buffer.get(i + 1) & 0xFF;
                        int b = buffer.get(i + 2) & 0xFF;
                        image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
                    }
                }

                try {
                    ImageIO.write(image, format, file);
                } catch (IOException e) { e.printStackTrace(); }

                GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
                GL11.glViewport(0, 0, GeneralSettings.DISPLAY_WIDTH, GeneralSettings.DISPLAY_HEIGHT);
                GL11.glDeleteTextures(imageIndex);
                GL30.glDeleteFramebuffers(renderBuffer);
                GeneralSettings.SCREENSHOT_IN_PROGRESS = false;

            }
        };
        testMenuButtonList.add(button);

        //generate from file
        button = new TextButton("Generate Flowchart") {
            @Override
            public void onPress() {
                //Create temp file

                //Save To Temp Location
                if (GeneralSettings.FILE_PATH != null) {
                    if (controller.getCodeWindowController() != null) {
                        tfm.copyFiletoTempFile(GeneralSettings.FILE_PATH, GeneralSettings.USERPREF.getUserTempFileDirPath());

                    }
                } else {
                    return;
                }

                tfm.update(); // Must be called if you change the files in temp!
                if(tfm.getMostRecent() == null) {
                   return;
                }
                parser = new LC3Parser(tfm.getMostRecent(), false);
                parser.ReadFile(tfm.getMostRecent());

                parser.generateFlowObjects();
                controller.setFlowchartWindowController(parser.createFlowchart(controller));
                System.out.println(controller.getFlowchartWindowController());

                controller.flowchartView();
            }
        };
        testMenuButtonList.add(button);

        button = new TextButton("Regenerate Flowchart From Editor") {
            @Override
            public void onPress() {
                //TODO: Need way to keep track whether changes have been made in editor to know if we need to save or not...

                //Save what is in codeWindow
                if (controller.getCodeWindowController() != null && GeneralSettings.FILE_PATH != null) {
                    tfm.saveCodeEditorTextToFile(controller.getCodeWindowController().getTexts(), GeneralSettings.FILE_PATH, GeneralSettings.USERPREF.getUserTempFileDirPath());
                } else {
                    return;
                }

                tfm.update(); // Must be called if you change the files in temp!
                if(tfm.getMostRecent() == null) {
                    return;
                }
                parser  = new LC3Parser(tfm.getMostRecent(), false);
                parser.ReadFile(tfm.getMostRecent());

                parser.generateFlowObjects();
                parser.createFlowchart(controller);

            }
        };
        testMenuButtonList.add(button);

        button = new TextButton("Regenerate Flowchart From Source") {
            @Override
            public void onPress() {

                if(GeneralSettings.FILE_PATH == null) {
                    return;
                }

                parser = new LC3Parser(GeneralSettings.FILE_PATH, true);
                parser.ReadFile(GeneralSettings.FILE_PATH);
                parser.generateFlowObjects();
                parser.createFlowchart(controller);

            }
        };
        testMenuButtonList.add(button);

        button = new TextButton("Text Editor View") {
            @Override
            public void onPress() {
                controller.textEditorView();
            }
        };
        testMenuButtonList.add(button);

        button = new TextButton("Flowchart View") {
            @Override
            public void onPress() {
                controller.flowchartView();
            }
        };
        testMenuButtonList.add(button);

        button = new TextButton("Splitscreen View") {
            @Override
            public void onPress() {
                controller.splitScreen();
            }
        };
        testMenuButtonList.add(button);

        button = new TextButton("Reset zoom") {
            @Override
            public void onPress() {
                if(controller.getFlowchartWindowController() != null) {
                    controller.getFlowchartWindowController().resetZoom();
                    //TODO: Ensure this works after simplifying Header
                }
            }
        };
        testMenuButtonList.add(button);

        HeaderMenu fileButton = new HeaderMenu(new Vector2f(-1f, 1-GeneralSettings.FONT_SIZE*GeneralSettings.FONT_SCALING_FACTOR - 2*GeneralSettings.TEXT_BUTTON_PADDING), "File ", GeneralSettings.TEXT_BUTTON_BACKGROUND_COLOR, GeneralSettings.HIGHLIGHT_COLOR, GeneralSettings.TEXT_COLOR, GeneralSettings.FONT, GeneralSettings.FONT_SIZE, GeneralSettings.FONT_WIDTH, GeneralSettings.FONT_EDGE, testMenuButtonList);
        menuList.add(fileButton);

        button = new TextButton("Clear") {
            @Override
            public void onPress() {
                if(controller.getFlowchartWindowController() != null) {
                    controller.getFlowchartWindowController().locateRegister(null);
                    GLFW.glfwSetWindowTitle(EngineTester.getWindow(), windowTitle);
                }
            }
        };
        registerMenuButtonList.add(button);
        button = new TextButton("R0") {
            @Override
            public void onPress() {
                if(controller.getFlowchartWindowController() != null) {
                    controller.getFlowchartWindowController().locateRegister("R0");
                    GLFW.glfwSetWindowTitle(EngineTester.getWindow(), windowTitle + " [RO]");
                }
            }
        };
        registerMenuButtonList.add(button);
        button = new TextButton("R1") {
            @Override
            public void onPress() {
                if(controller.getFlowchartWindowController() != null) {
                    controller.getFlowchartWindowController().locateRegister("R1");
                    GLFW.glfwSetWindowTitle(EngineTester.getWindow(), windowTitle + " [R1]");

                }
            }
        };
        registerMenuButtonList.add(button);
        button = new TextButton("R2") {
            @Override
            public void onPress() {
                if(controller.getFlowchartWindowController() != null) {
                    controller.getFlowchartWindowController().locateRegister("R2");
                    GLFW.glfwSetWindowTitle(EngineTester.getWindow(), windowTitle + " [R2]");
                }
            }
        };
        registerMenuButtonList.add(button);
        button = new TextButton("R3") {
            @Override
            public void onPress() {
                if(controller.getFlowchartWindowController() != null) {
                    controller.getFlowchartWindowController().locateRegister("R3");
                    GLFW.glfwSetWindowTitle(EngineTester.getWindow(), windowTitle + " [R3]");
                }
            }
        };
        registerMenuButtonList.add(button);
        button = new TextButton("R4") {
            @Override
            public void onPress() {
                if(controller.getFlowchartWindowController() != null) {
                    controller.getFlowchartWindowController().locateRegister("R4");
                    GLFW.glfwSetWindowTitle(EngineTester.getWindow(), windowTitle + " [R4]");
                }
            }
        };
        registerMenuButtonList.add(button);
        button = new TextButton("R5") {
            @Override
            public void onPress() {
                if(controller.getFlowchartWindowController() != null) {
                    controller.getFlowchartWindowController().locateRegister("R5");
                    GLFW.glfwSetWindowTitle(EngineTester.getWindow(), windowTitle + " [R5]");
                }
            }
        };
        registerMenuButtonList.add(button);
        button = new TextButton("R6") {
            @Override
            public void onPress() {
                if(controller.getFlowchartWindowController() != null) {
                    controller.getFlowchartWindowController().locateRegister("R6");
                    GLFW.glfwSetWindowTitle(EngineTester.getWindow(), windowTitle + " [R6]");
                }
            }
        };
        registerMenuButtonList.add(button);
        button = new TextButton("R7") {
            @Override
            public void onPress() {
                if(controller.getFlowchartWindowController() != null) {
                    controller.getFlowchartWindowController().locateRegister("R7");
                    GLFW.glfwSetWindowTitle(EngineTester.getWindow(), windowTitle + " [R7]");
                }
            }
        };
        registerMenuButtonList.add(button);

        HeaderMenu registerButton = new HeaderMenu(new Vector2f(-1f + fileButton.getSize().x, 1-GeneralSettings.FONT_SIZE*GeneralSettings.FONT_SCALING_FACTOR - 2*GeneralSettings.TEXT_BUTTON_PADDING), "Registers ", GeneralSettings.TEXT_BUTTON_BACKGROUND_COLOR, GeneralSettings.HIGHLIGHT_COLOR, GeneralSettings.TEXT_COLOR, GeneralSettings.FONT, GeneralSettings.FONT_SIZE, GeneralSettings.FONT_WIDTH, GeneralSettings.FONT_EDGE, registerMenuButtonList);
        menuList.add(registerButton);

        button = new TextButton("Clear") {
            @Override
            public void onPress() {
                if(controller.getFlowchartWindowController() != null) {
                    controller.getFlowchartWindowController().locateAlert(null);
                    GLFW.glfwSetWindowTitle(EngineTester.getWindow(), windowTitle);
                }
            }
        };
        analyticsMenuButtonList.add(button);

        button = new TextButton("Invalid Labels") {
            @Override
            public void onPress() {
                if(controller.getFlowchartWindowController() != null) {
                    controller.getFlowchartWindowController().locateAlert("invalid_label");
                    GLFW.glfwSetWindowTitle(EngineTester.getWindow(), windowTitle + " [Invalid labels]");
                }
            }
        };
        analyticsMenuButtonList.add(button);

        HeaderMenu analyticsButton = new HeaderMenu(new Vector2f(-1f + fileButton.getSize().x + registerButton.getSize().x, 1-GeneralSettings.FONT_SIZE*GeneralSettings.FONT_SCALING_FACTOR - 2*GeneralSettings.TEXT_BUTTON_PADDING), "Analysis ", GeneralSettings.TEXT_BUTTON_BACKGROUND_COLOR, GeneralSettings.HIGHLIGHT_COLOR, GeneralSettings.TEXT_COLOR, GeneralSettings.FONT, GeneralSettings.FONT_SIZE, GeneralSettings.FONT_WIDTH, GeneralSettings.FONT_EDGE, analyticsMenuButtonList);
        menuList.add(analyticsButton);

        // User Settings Start
        button = new TextButton("Set Temp Folder Path") {
            @Override
            public void onPress() {

                OpenFileDialog of = new OpenFileDialog();

                of.saveFolderWindow();
                if(of.getFilePath() != null) {
                    GeneralSettings.USERPREF.setUserTempFileDirPath(of.getFilePath());
                    tfm.initializeDirectory(GeneralSettings.USERPREF.getUserTempFileDirPath());
                    tfm.update();
                }
            }
        };
        settingsMenuButtonList.add(button);


        button = new TextButton("Reset Temp Folder Path") {
            @Override
            public void onPress() {
                GeneralSettings.USERPREF.setUserTempFileDirPath(GeneralSettings.TEMP_DIR);
                tfm.initializeDirectory(GeneralSettings.USERPREF.getUserTempFileDirPath());
                tfm.update();

            }
        };
        settingsMenuButtonList.add(button);

        button = new TextButton("Settings Menu") {
            @Override
            public void onPress() {
                //SettingsMenu sMenu = new SettingsMenu();
                SettingsMenu.run();

            }
        };
        settingsMenuButtonList.add(button);

        HeaderMenu settingsButton = new HeaderMenu(new Vector2f(-1f + fileButton.getSize().x + registerButton.getSize().x + analyticsButton.getSize().x, 1-GeneralSettings.FONT_SIZE*GeneralSettings.FONT_SCALING_FACTOR - 2*GeneralSettings.TEXT_BUTTON_PADDING), "Settings ", GeneralSettings.TEXT_BUTTON_BACKGROUND_COLOR, GeneralSettings.HIGHLIGHT_COLOR, GeneralSettings.TEXT_COLOR, GeneralSettings.FONT, GeneralSettings.FONT_SIZE, GeneralSettings.FONT_WIDTH, GeneralSettings.FONT_EDGE, settingsMenuButtonList);
        menuList.add(settingsButton);

        for(HeaderMenu headerMenu: menuList){
            ButtonController.add(headerMenu);
        }
    }

    public GUIFilledBox getGuiFilledBox() {
        return guiFilledBox;
    }

    public void changeHeadercolor() {
        guiFilledBox.setColor(GeneralSettings.USERPREF.getHeaderColor3f());
    }

    public Vector2f getPosition(){
        return position;
    }

    public void setAspectRatio(Vector2f aspectRatio){
        Vector2f size = guiFilledBox.getSize();
        size.y /= this.aspectRatio.y;
        size.y *= aspectRatio.y;
        guiFilledBox.setSize(size);
        for(HeaderMenu menu : menuList){
            menu.setAspectRatio(new Vector2f(aspectRatio));
        }
        for(int i = 1; i < menuList.size(); i++){
            HeaderMenu lastMenu = menuList.get(i-1);
            menuList.get(i).setPosition(new Vector2f(lastMenu.getPosition().x + lastMenu.getSize().x, lastMenu.getPosition().y));
        }
        guiFilledBox.setPosition(new Vector2f(-1, 1-(1-guiFilledBox.getPosition().y)/this.aspectRatio.y*aspectRatio.y));
        this.aspectRatio = aspectRatio;
    }
}
