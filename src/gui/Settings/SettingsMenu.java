package gui.Settings;

import controllers.ApplicationController;
import gui.OpenFileDialog;
import main.GeneralSettings;
import org.lwjgl.util.vector.Vector3f;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This is a swing gui class that manages user settings for ALFAT
 * */
public class SettingsMenu extends Component {
    //The root frame of the gui
    private JFrame root;
    //The content displayed in the right panel
    private JPanel menuContent;
    //Associates JList content with display content
    private ArrayList<SettingsContent> fakeButtonscontent;
    //The split pane of the gui
    private JSplitPane jSplitPane;
    //A scroll pane for the left content e.g. fake buttons
    private ScrollPane scrollPane;
    //A JList of strings that act like buttons
    private JList<String> fakebutton;


    //Font types for content
    private Font labelFont = new Font("Verdana", Font.BOLD, 12);


    //GUI Height Defaults
    private int GUI_WIDTH = 1100;//800
    private int GUI_HEIGHT = 600;//500

    private int GUI_JSP_PADDING = 150;


    //Regx
    private String validFileType = "^(\\w+)$|(\\w+(,|;)\\w+)*$";
    private String validIntType = "^[1-9]\\d*$";
    private String validFloatType = "^\\d*\\.?\\d*$";

    //Prevents multiple settings menus from being opened
    public static boolean OPENABLE = true;

    /**
     * Builds and displays the settings GUI
     * */
    public SettingsMenu() {

        //Init fake button content
        setFakeButtonscontent();

        //Create the new window
        root = new JFrame();


        //Setup split pane
        jSplitPane = new JSplitPane();
        //jSplitPane.setResizeWeight(0);
        jSplitPane.setContinuousLayout(true);
        jSplitPane.setOneTouchExpandable(true);
        jSplitPane.setDividerSize(0);
        jSplitPane.setPreferredSize(new Dimension(GUI_WIDTH,GUI_HEIGHT));


        //Setup content
        scrollPane = new ScrollPane();
        scrollPane.add(buildButtons());
        initContent();
        jSplitPane.setRightComponent(menuContent);
        jSplitPane.setLeftComponent(scrollPane);
        jSplitPane.setDividerLocation(GUI_JSP_PADDING + jSplitPane.getInsets().left);
        root.add(BorderLayout.CENTER,jSplitPane);
        root.pack();

        //---------------------------------------------------------
        //Basic Window Settings
        root.setSize(GUI_WIDTH,GUI_HEIGHT);
        root.setTitle("Settings");
        root.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        //Just set window visible to false
        root.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        root.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                root.setVisible(false);
            }
        });


//Way to force everything to be removed
//        root.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//        //New close method
//        root.addWindowListener(new java.awt.event.WindowAdapter() {
//            @Override
//            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
//
//                headerBtn = null;
//                menuBtn = null;
//                textEditorBtn = null;
//                lineNumberBtn = null;
//                scrollBtnVert = null;
//                scrollBtnHoriz = null;
//                flowchartBox1Bar = null;
//                flowchartBox1Text = null;
//
//                branchTextColorBtn = null;
//                commandTextColorBtn = null;
//                commentTextColorBtn = null;
//                errorTextColorBtn = null;
//                immediateTextColorBtn = null;
//                labelTextColorBtn = null;
//                lineNumberTextColorBtn = null;
//                registerTextColorBtn = null;
//                separatorTextColorBtn = null;
//
//                fakeButtonscontent.removeAll(fakeButtonscontent);
//                fakeButtonscontent = null;
//                fakebutton.removeAll();
//                fakebutton = null;
//                root.dispose();
//                gui = null;
//                Runtime.getRuntime().gc();
//                System.out.println("Dead");
//            }
//        });

        //Set icon
        root.setIconImage(Toolkit.getDefaultToolkit().getImage("src/res/icon/alfatlogo2.png"));
        //root.setVisible(true);
        root.setVisible(false);

        //Listener to ensure only 1 settings menu can be opened at a time
//        root.addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                // System.out.println("Window Closing");
//                OPENABLE = true;
//                //System.exit(0);
//            }
//        });


    }

    public void setVisible() {
        root.setVisible(true);
    }

    public static void run()  {
        SettingsMenu gui = new SettingsMenu();



//        Runnable myRunnable =
//                new Runnable(){
//            public void run(){
//
//                gui = new SettingsMenu();
//
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//
//                root.setVisible(false);
//
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                root.setVisible(true);
//
//            }
//        };
//        Thread thread = new Thread(myRunnable);
//        thread.start();


    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //                                                                Settings Menu driver / helper functions
    /**
     * Triggers the content switch
     * @see private JPanel buildButtons()
     * */
    private void clickButton(Point point) {
        int index = fakebutton.locationToIndex(point);
        updateMenucontent(fakeButtonscontent.get(index).content);
    }


    /**
     * Builds the left pane of the JSplitPane and returns a JPanel
     * @see public SettingsMenu()
     * */
    private JPanel buildButtons() {
        JPanel leftPanel = new JPanel();
        DefaultListModel<String> l1 = new DefaultListModel<String>();

        for(int i=0; i < fakeButtonscontent.size(); i++) {
            l1.addElement(fakeButtonscontent.get(i).name);
        }

        fakebutton = new JList<String>(l1);
        fakebutton.setFont(new Font("Arial",Font.BOLD,14));
        fakebutton.setPreferredSize(new Dimension(100,30));
        fakebutton.setBackground(Color.decode(panelBackgroundColor));
        fakebutton.setForeground(Color.decode(paneltextColor));
        fakebutton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clickButton(e.getPoint());
            }
        });

        leftPanel.setPreferredSize(new Dimension(100, 100));
        //leftPanel.setMinimumSize(new Dimension(50, 50));

        leftPanel.setLayout(new BorderLayout());
        leftPanel.add(BorderLayout.CENTER,fakebutton);

        return leftPanel;
    }

    /**
     * Sets the content on the right to the first index of the JList (fakebuttons) and selects it
     * */
    void initContent() {
        menuContent = fakeButtonscontent.get(0).content;
        fakebutton.setSelectedIndex(0);
    }

    /**
     * Updates the content on the right of the JSplitPane
     * */
    private void updateMenucontent(JPanel content) {
        jSplitPane.remove(jSplitPane.getRightComponent());
        jSplitPane.setRightComponent(content);
        jSplitPane.resetToPreferredSizes();
        jSplitPane.setDividerLocation(GUI_JSP_PADDING + jSplitPane.getInsets().left);
        jSplitPane.updateUI();
    }

    /**
     * Set's the fake buttons and their associated menu contenet
     * */
    private void setFakeButtonscontent() {
        fakeButtonscontent = new ArrayList<SettingsContent>();

        fakeButtonscontent.add(new SettingsContent("Display Settings", displaySettingsContent()));

        fakeButtonscontent.add(new SettingsContent("File Settings", fileSettingsContent()));

        fakeButtonscontent.add(new SettingsContent("GUI Color Picker", colorPickerContent()));

        fakeButtonscontent.add(new SettingsContent("Text Color & Font", textColorContent()));

        fakeButtonscontent.add(new SettingsContent("Help", helpContent()));
    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //                                                                          Swing Colors
    String panelBackgroundColor = "#15202B";
    String paneltextColor = "#FFFFFF";
    String buttonTextColor = "#00acee";
    String buttonBackgroundColor = "#22303C";

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //                                                                          Display Settings
    /**
     * Display settings content pane
     * */
    private JPanel displaySettingsContent() {

        JPanel main = new JPanel();

        main.setBackground(Color.decode(panelBackgroundColor));

        //Set up container for box layout
        JPanel container = new JPanel();
        BoxLayout boxLayout = new BoxLayout(container, BoxLayout.Y_AXIS);
        container.setLayout(boxLayout);
        container.setBackground(Color.decode(panelBackgroundColor));
        //Top of Layout
        JLabel ofbLable = new JLabel(" Open File Behavior ");
        ofbLable.setFont(new Font("Verdana", Font.BOLD, 18));
        ofbLable.setForeground(Color.decode(paneltextColor));
        ofbLable.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(ofbLable);


        //Options radio buttons
        JRadioButton ssYes = new JRadioButton("Yes");
        ssYes.setFont(labelFont);
        ssYes.setBackground(Color.decode(panelBackgroundColor));
        ssYes.setForeground(Color.decode(paneltextColor));
        JRadioButton ssNo = new JRadioButton("No");
        ssNo.setFont(labelFont);
        ssNo.setBackground(Color.decode(panelBackgroundColor));
        ssNo.setForeground(Color.decode(paneltextColor));
        JRadioButton fsYes = new JRadioButton("To Editor");
        fsYes.setFont(labelFont);
        fsYes.setBackground(Color.decode(panelBackgroundColor));
        fsYes.setForeground(Color.decode(paneltextColor));
        JRadioButton fsNo = new JRadioButton("To Flowchart");
        fsNo.setFont(labelFont);
        fsNo.setBackground(Color.decode(panelBackgroundColor));
        fsNo.setForeground(Color.decode(paneltextColor));
        ButtonGroup fsGroup = new ButtonGroup();


        //------------------------------------------------------
        //Auto generate flowchart option
        //FlowLayout
        JPanel subContainer = new JPanel(new FlowLayout());
        subContainer.setBackground(Color.decode(panelBackgroundColor));
        subContainer.setForeground(Color.decode(paneltextColor));
        JRadioButton yesBtn = new JRadioButton("Yes");
        yesBtn.setFont(labelFont);
        yesBtn.setBackground(Color.decode(panelBackgroundColor));
        yesBtn.setForeground(Color.decode(paneltextColor));
        JRadioButton noBtn = new JRadioButton("No");
        noBtn.setFont(labelFont);
        noBtn.setBackground(Color.decode(panelBackgroundColor));
        noBtn.setForeground(Color.decode(paneltextColor));
        ButtonGroup yesNoGroup = new ButtonGroup();
        yesNoGroup.add(yesBtn);
        yesNoGroup.add(noBtn);


        //Set GUI to current user setting
        if(GeneralSettings.USERPREF.getAutoGenFlowchart() == true) {
            yesBtn.setSelected(true);
            noBtn.setSelected(false);
        } else {
            noBtn.setSelected(true);
            yesBtn.setSelected(false);
        }

        //Radio button logic
        yesBtn.addActionListener(e->{

            GeneralSettings.USERPREF.setAutoGenFlowchart(true);
        });

        noBtn.addActionListener(e->{

            GeneralSettings.USERPREF.setAutoGenFlowchart(false);
        });

        JLabel msg1 =  new JLabel("Auto Generate Flowchart:");
        msg1.setFont(labelFont);
        msg1.setBackground(Color.decode(panelBackgroundColor));
        msg1.setForeground(Color.decode(paneltextColor));
        subContainer.add(msg1);
        subContainer.add(yesBtn);
        subContainer.add(noBtn);
        subContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(subContainer);


        //------------------------------------------------------
        //Split Screen Option
        //FlowLayout
        JPanel subContainer2 = new JPanel(new FlowLayout());
        subContainer2.setBackground(Color.decode(panelBackgroundColor));
        subContainer2.setForeground(Color.decode(paneltextColor));
        ButtonGroup yesNoGroup2 = new ButtonGroup();
        yesNoGroup2.add(ssYes);
        yesNoGroup2.add(ssNo);


        //Set GUI to current user setting
        if(GeneralSettings.USERPREF.getSplitScreen() == true) {
            ssYes.setSelected(true);
            ssNo.setSelected(false);

            fsNo.setEnabled(false);
            fsYes.setEnabled(false);
            fsGroup.clearSelection();

        } else {
            ssYes.setSelected(false);
            ssNo.setSelected(true);

            fsNo.setEnabled(true);
            fsYes.setEnabled(true);
        }

        //Radio button logic
        ssYes.addActionListener(e->{

            //Un gray out auto gen flowchart
             yesBtn.setEnabled(true);
             noBtn.setEnabled(true);


            fsNo.setEnabled(false);
            fsYes.setEnabled(false);
            fsGroup.clearSelection();

            GeneralSettings.USERPREF.setSplitScreen(true);
            GeneralSettings.USERPREF.setFullscreen(0); //false

        });

        ssNo.addActionListener(e->{

            fsNo.setEnabled(true);
            fsYes.setEnabled(true);
            fsYes.setSelected(true);

            GeneralSettings.USERPREF.setSplitScreen(false);
            GeneralSettings.USERPREF.setFullscreen(1);
        });

        JLabel msg2 = new JLabel("Split Screen:");
        msg2.setFont(labelFont);
        msg2.setForeground(Color.decode(paneltextColor));
        subContainer2.add(msg2);
        subContainer2.add(ssYes);
        subContainer2.add(ssNo);
        subContainer2.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(subContainer2);


        //------------------------------------------------------
        //Full Screen
        //FlowLayout
        JPanel subContainer3 = new JPanel(new FlowLayout());
        subContainer3.setBackground(Color.decode(panelBackgroundColor));
        subContainer3.setForeground(Color.decode(paneltextColor));

        fsGroup.add(fsYes);
        fsGroup.add(fsNo);


        //Set user settings to gui

        if(GeneralSettings.USERPREF.getFullscreen() == 0) {
            //False
            fsGroup.clearSelection();
        } else if (GeneralSettings.USERPREF.getFullscreen() > 0){
            //To editor
            fsYes.setSelected(true);
            fsNo.setSelected(false);
        } else if (GeneralSettings.USERPREF.getFullscreen() < 0) {

            //Toggle on auto gen flowchart and gray out
            yesBtn.setSelected(true);
            noBtn.setSelected(false);
            yesBtn.setEnabled(false);
            noBtn.setEnabled(false);
            GeneralSettings.USERPREF.setAutoGenFlowchart(true);

            //To Flowchart
            fsYes.setSelected(false);
            fsNo.setSelected(true);
        }

        //Radio button logic
        fsYes.addActionListener(e->{
            //Un gray out auto gen flowchart
            yesBtn.setEnabled(true);
            noBtn.setEnabled(true);



            GeneralSettings.USERPREF.setFullscreen(1);
        });
        //To flowchart
        fsNo.addActionListener(e->{
            //Toggle on auto gen flowchart and gray out
            yesBtn.setSelected(true);
            noBtn.setSelected(false);
            yesBtn.setEnabled(false);
            noBtn.setEnabled(false);
            GeneralSettings.USERPREF.setAutoGenFlowchart(true);

            GeneralSettings.USERPREF.setFullscreen(-1);
        });

        JLabel msg3 = new JLabel("Full Screen:");
        msg3.setFont(labelFont);
        msg3.setForeground(Color.decode(paneltextColor));
        subContainer3.add(msg3);
        subContainer3.add(fsYes);
        subContainer3.add(fsNo);
        subContainer3.setAlignmentX(Component.CENTER_ALIGNMENT);


        container.add(subContainer3);

        //// line width button options
        JPanel subContainer4 = new JPanel(new FlowLayout());
        subContainer4.setBackground(Color.decode(panelBackgroundColor));
        JLabel defaultWidthSizeLabel = new JLabel("Default line width:");
        defaultWidthSizeLabel.setForeground(Color.decode(paneltextColor));
        JTextField defaultSizeTextfield = new JTextField(GeneralSettings.USERPREF.getDefaultLineWidth()+"", 10);
        JButton btnSubmitDWsize = new JButton("Submit");
        btnSubmitDWsize.setBackground(Color.decode(buttonBackgroundColor));
        btnSubmitDWsize.setForeground(Color.decode(buttonTextColor));
        btnSubmitDWsize.addActionListener(e -> {

            if(defaultSizeTextfield.getText().matches(validFloatType)) {
                if (Float.parseFloat(defaultSizeTextfield.getText()) > 0) {
                    defaultSizeTextfield.setForeground(Color.BLACK);
                    GeneralSettings.USERPREF.setDefaultLineWidth(Float.parseFloat(defaultSizeTextfield.getText()));
                }
            } else {
                defaultSizeTextfield.setForeground(Color.RED);
            }
        });

        defaultSizeTextfield.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {

                if(defaultSizeTextfield.getText().matches(validFloatType)) {
                    if (Float.parseFloat(defaultSizeTextfield.getText()) > 0) {
                        defaultSizeTextfield.setForeground(Color.BLACK);

                    }
                } else {
                    defaultSizeTextfield.setForeground(Color.RED);

                }
            }
        });

        subContainer4.add(defaultWidthSizeLabel);
        subContainer4.add(defaultSizeTextfield);
        subContainer4.add(btnSubmitDWsize);
        container.add(subContainer4);


        JPanel subContainer5 = new JPanel(new FlowLayout());
        subContainer5.setBackground(Color.decode(panelBackgroundColor));
        JLabel highlightWidthSizeLabel = new JLabel("Highlighted line width:");
        highlightWidthSizeLabel.setForeground(Color.decode(paneltextColor));
        JTextField highlightSizeTextfield = new JTextField(GeneralSettings.USERPREF.getHighlightedLineWidth()+"", 10);
        JButton btnSubmitHWsize = new JButton("Submit");
        btnSubmitHWsize.setBackground(Color.decode(buttonBackgroundColor));
        btnSubmitHWsize.setForeground(Color.decode(buttonTextColor));

        highlightSizeTextfield.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(highlightSizeTextfield.getText().matches(validFloatType)) {
                    if (Float.parseFloat(highlightSizeTextfield.getText()) > 0) {
                        highlightSizeTextfield.setForeground(Color.BLACK);
                    }
                } else {
                    highlightSizeTextfield.setForeground(Color.RED);
                }
            }
        });


        btnSubmitHWsize.addActionListener(e -> {

            if(highlightSizeTextfield.getText().matches(validFloatType)) {
                if (Float.parseFloat(highlightSizeTextfield.getText()) > 0) {
                    highlightSizeTextfield.setForeground(Color.BLACK);
                    GeneralSettings.USERPREF.setHighlightedLineWidth(Float.parseFloat(highlightSizeTextfield.getText()));
                }
            } else {
                highlightSizeTextfield.setForeground(Color.RED);
            }
        });
        subContainer5.add(highlightWidthSizeLabel);
        subContainer5.add(highlightSizeTextfield);
        subContainer5.add(btnSubmitHWsize);
        container.add(subContainer5);



        JPanel subContainer6 = new JPanel(new FlowLayout());
        subContainer6.setBackground(Color.decode(panelBackgroundColor));
        JLabel resetLabel = new JLabel("Reset To Default:");
        resetLabel.setForeground(Color.decode(paneltextColor));
        JButton btnReset = new JButton("Reset");
        btnReset.setBackground(Color.decode(buttonBackgroundColor));
        btnReset.setForeground(Color.decode(buttonTextColor));

        btnReset.addActionListener(e -> {
            GeneralSettings.USERPREF.resetDisplaySettings();
            //Set GUI to current user setting
            if(GeneralSettings.USERPREF.getAutoGenFlowchart() == true) {
                yesBtn.setSelected(true);
                noBtn.setSelected(false);
            } else {
                noBtn.setSelected(true);
                yesBtn.setSelected(false);
            }

            //Set GUI to current user setting
            if(GeneralSettings.USERPREF.getSplitScreen() == true) {
                ssYes.setSelected(true);
                ssNo.setSelected(false);

                fsNo.setEnabled(false);
                fsYes.setEnabled(false);
                fsGroup.clearSelection();

            } else {
                ssYes.setSelected(false);
                ssNo.setSelected(true);

                fsNo.setEnabled(true);
                fsYes.setEnabled(true);
            }

            //Set user settings to gui

            if(GeneralSettings.USERPREF.getFullscreen() == 0) {
                //False
                fsGroup.clearSelection();
            } else if (GeneralSettings.USERPREF.getFullscreen() > 0){
                //To editor
                fsYes.setSelected(true);
                fsNo.setSelected(false);
            } else if (GeneralSettings.USERPREF.getFullscreen() < 0) {

                //Toggle on auto gen flowchart and gray out
                yesBtn.setSelected(true);
                noBtn.setSelected(false);
                yesBtn.setEnabled(false);
                noBtn.setEnabled(false);
                GeneralSettings.USERPREF.setAutoGenFlowchart(true);

                //To Flowchart
                fsYes.setSelected(false);
                fsNo.setSelected(true);
            }
            defaultSizeTextfield.setText(GeneralSettings.USERPREF.getDefaultLineWidth()+"");
            highlightSizeTextfield.setText(GeneralSettings.USERPREF.getHighlightedLineWidth()+"");
        });

        subContainer6.add(resetLabel);
        subContainer6.add(btnReset);
        container.add(subContainer6);

        main.add(BorderLayout.CENTER,container);
        return main;
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //                                                                          File settings
    /**
     * The file settings content pane
     * */
    private JPanel fileSettingsContent() {
         JPanel main = new JPanel();
         main.setBackground(Color.decode(panelBackgroundColor));
         JPanel superContainer = new JPanel(new GridLayout(0,1,10,10));
        superContainer.setForeground(Color.decode(paneltextColor));
        superContainer.setBackground(Color.decode(panelBackgroundColor));
         //superContainer.setBorder(BorderFactory.createDashedBorder(Color.MAGENTA));
        //-----------------------------------------

        //Change Syntax path
        JPanel syntaxFilePathPane = new JPanel(new FlowLayout());
        syntaxFilePathPane.setBackground(Color.decode(panelBackgroundColor));

        JLabel curSyntxPathJLable = new JLabel("Current Syntax File");
        curSyntxPathJLable.setForeground(Color.decode(paneltextColor));
        curSyntxPathJLable.setFont(labelFont);
        syntaxFilePathPane.add(curSyntxPathJLable);

        JTextField syntaxFilePath = new JTextField(GeneralSettings.USERPREF.getSyntaxPath());
        syntaxFilePath.setEditable(false);
        syntaxFilePathPane.add(syntaxFilePath);

        JButton changeSyntxPath = new JButton("Change File");
        changeSyntxPath.setForeground(Color.decode(buttonTextColor));
        changeSyntxPath.setBackground(Color.decode(buttonBackgroundColor));
        changeSyntxPath.addActionListener(e-> {
            OpenFileDialog ofd = new OpenFileDialog();
            ofd.setFilterList("json;");
            ofd.openFileWindow();
            if(ofd.getFilePath() != null) {
                GeneralSettings.USERPREF.setSyntaxPath(ofd.getFilePath());
                GeneralSettings.IS_SYNTAX_PATH_CHANGED = true;
                syntaxFilePath.setText(GeneralSettings.USERPREF.getSyntaxPath());
                syntaxFilePathPane.updateUI();
            }
        });
        syntaxFilePathPane.add(changeSyntxPath);


        //Change current directory
        //Sets up a panel that changes the user preferences for the default folder path
        JPanel tempFilePathPane = new JPanel(new FlowLayout());
        tempFilePathPane.setBackground(Color.decode(panelBackgroundColor));

        JLabel curTempFileDirLabel = new JLabel("Current Temp File Directory: ");
        curTempFileDirLabel.setForeground(Color.decode(paneltextColor));
        curTempFileDirLabel.setFont(labelFont);
        tempFilePathPane.add(curTempFileDirLabel);

        JTextField tempFilePath = new JTextField(GeneralSettings.USERPREF.getUserTempFileDirPath());
        tempFilePath.setEditable(false);
        tempFilePathPane.add(tempFilePath);

        JButton changePath = new JButton("Change Path");
        changePath.setForeground(Color.decode(buttonTextColor));
        changePath.setBackground(Color.decode(buttonBackgroundColor));
        changePath.addActionListener(e-> {
            OpenFileDialog ofd = new OpenFileDialog();
            ofd.saveFolderWindow();
            if(ofd.getFilePath() != null) {
                GeneralSettings.USERPREF.setUserTempFileDirPath(ofd.getFilePath());
                updateMenucontent(fileSettingsContent());
            }
        });
        tempFilePathPane.add(changePath);


        //------------------------------------------
        //Temp File Limit
        JPanel tempFileLimitPane = new JPanel(new FlowLayout());
        tempFileLimitPane.setBackground(Color.decode(panelBackgroundColor));

        JLabel curTempFileLimitJlabel = new JLabel("Number of Temp files stored: ");
        curTempFileLimitJlabel.setForeground(Color.decode(paneltextColor));
        curTempFileLimitJlabel.setFont(labelFont);
        tempFileLimitPane.add(curTempFileLimitJlabel);

        JTextField limitTextField = new JTextField(Integer.toString(GeneralSettings.USERPREF.getTempFileLimit()));
        limitTextField.setPreferredSize(new Dimension(100, 20));
        tempFileLimitPane.add(limitTextField);
        limitTextField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(!limitTextField.getText().matches(validIntType)) {
                    limitTextField.setForeground(Color.RED);
                } else {
                    limitTextField.setForeground(Color.BLACK);
                }
            }
        });

        JButton submitLimitChange = new JButton("Submit Change");
        submitLimitChange.setForeground(Color.decode(buttonTextColor));
        submitLimitChange.setBackground(Color.decode(buttonBackgroundColor));
        submitLimitChange.addActionListener(e->{

            if(!limitTextField.getText().matches(validIntType)) {
                limitTextField.setForeground(Color.RED);
            } else {
                GeneralSettings.USERPREF.setTempFileLimit(Integer.parseInt(limitTextField.getText()));
                GeneralSettings.MasterRendererUserPrefToggle = true;
                limitTextField.setForeground(Color.BLACK);
            }
        });
        tempFileLimitPane.add(submitLimitChange);

        //-----------------------------------------
        //Change preferred file type for open and save as
        //NOTE: Sets up a panel that changes the user preferences for the default folder path
        JPanel preferredFileTypePane = new JPanel(new FlowLayout());
        preferredFileTypePane.setBackground(Color.decode(panelBackgroundColor));

        JLabel enterPrefFileTypeLabel = new JLabel("Enter preferred file type");
        enterPrefFileTypeLabel.setForeground(Color.decode(paneltextColor));
        enterPrefFileTypeLabel.setFont(labelFont);
        preferredFileTypePane.add(enterPrefFileTypeLabel);

        JTextField pftTextField = new JTextField(GeneralSettings.USERPREF.getPreferredFiletype());
        pftTextField.setPreferredSize(new Dimension(300, 20));
        preferredFileTypePane.add(pftTextField);

        //Change color if file type is wrong
        pftTextField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(!pftTextField.getText().matches(validFileType)) {
                    pftTextField.setForeground(Color.RED);
                } else {
                    pftTextField.setForeground(Color.BLACK);
                }
            }
        });

        //System.out.println(userPreferences.getPreferredFiletype());

        JButton submitChange = new JButton("Submit Change");
        submitChange.setForeground(Color.decode(buttonTextColor));
        submitChange.setBackground(Color.decode(buttonBackgroundColor));
        submitChange.addActionListener(e->{

            if(!pftTextField.getText().matches(validFileType)) {
                pftTextField.setForeground(Color.RED);
            } else {
                GeneralSettings.USERPREF.setPreferredFileType(pftTextField.getText());
                pftTextField.setForeground(Color.BLACK);
            }
        });
        preferredFileTypePane.add(submitChange);


        //-----------------------------------------
        //Image header color and font and message string
        JPanel imageHeaderPanel = new JPanel(new FlowLayout());
        imageHeaderPanel.setBackground(Color.decode(panelBackgroundColor));

        JLabel headerMessageLabel = new JLabel("Image Header String");
        headerMessageLabel.setForeground(Color.decode(paneltextColor));
        imageHeaderPanel.add(headerMessageLabel);
        //Set with the default stuff from user preferences
        JTextField headerMessageField = new JTextField(GeneralSettings.USERPREF.getImageHeaderMessage());
        headerMessageField.setForeground(GeneralSettings.USERPREF.getHeaderMSGTextColor());
        headerMessageField.setBackground(GeneralSettings.USERPREF.getHeaderMSGBackgroundColor());
        headerMessageField.setPreferredSize(new Dimension(300, 26));
        headerMessageField.setFont(new Font("TimesRoman", Font.BOLD, 14));
        imageHeaderPanel.add(headerMessageField);

        JButton setHeaderMessageButton = new JButton("Set");
        setHeaderMessageButton.setForeground(Color.decode(buttonTextColor));
        setHeaderMessageButton.setBackground(Color.decode(buttonBackgroundColor));
        imageHeaderPanel.add(setHeaderMessageButton);

        setHeaderMessageButton.addActionListener(e -> {
            GeneralSettings.USERPREF.setImageHeaderMessage(headerMessageField.getText());
            GeneralSettings.USERPREF.setHeaderMSGTextColor(headerMessageField.getForeground());
            GeneralSettings.USERPREF.setHeaderMSGBackgroundColor(headerMessageField.getBackground());
        });




        JPanel imageHeaderColorPanel = new JPanel(new FlowLayout());
        imageHeaderColorPanel.setBackground(Color.decode(panelBackgroundColor));

        JButton fontColorButton = new JButton("Choose Font Color");
        fontColorButton.setForeground(Color.decode(buttonTextColor));
        fontColorButton.setBackground(Color.decode(buttonBackgroundColor));
        imageHeaderColorPanel.add(fontColorButton);

        fontColorButton.addActionListener(e-> {
            Color current = headerMessageField.getForeground();
            Color newColor = JColorChooser.showDialog(this,"Select a color",current);
            if(newColor != null) {
                headerMessageField.setForeground(newColor);
            }
        });


        JButton backgroundColorButton = new JButton("Choose Background Color");
        backgroundColorButton.setForeground(Color.decode(buttonTextColor));
        backgroundColorButton.setBackground(Color.decode(buttonBackgroundColor));
        imageHeaderColorPanel.add(backgroundColorButton);

        backgroundColorButton.addActionListener(e-> {
            Color current = headerMessageField.getBackground();
            Color newColor = JColorChooser.showDialog(this,"Select a color",current);
            if(newColor != null) {
                headerMessageField.setBackground(newColor);
            }
        });










        //-----------------------------------------
        //Rest button
        JPanel restFilePane = new JPanel(new FlowLayout());
        restFilePane.setBackground(Color.decode(panelBackgroundColor));

        JLabel restFileLabel = new JLabel("Rest to defaults");
        restFileLabel.setForeground(Color.decode(paneltextColor));

        JButton restBtn = new JButton("Reset");
        restBtn.setForeground(Color.decode(buttonTextColor));
        restBtn.setBackground(Color.decode(buttonBackgroundColor));
        restBtn.addActionListener(e-> {
            GeneralSettings.USERPREF.resetFileSettings();
            limitTextField.setText(Integer.toString(GeneralSettings.USERPREF.getTempFileLimit()));
            pftTextField.setText(GeneralSettings.USERPREF.getPreferredFiletype());
            headerMessageField.setText(GeneralSettings.USERPREF.getImageHeaderMessage());
            headerMessageField.setForeground(GeneralSettings.USERPREF.getHeaderMSGTextColor());
            headerMessageField.setBackground(GeneralSettings.USERPREF.getHeaderMSGBackgroundColor());



        });
        restFilePane.add(restFileLabel);
        restFilePane.add(restBtn);



        superContainer.add(syntaxFilePathPane);
        superContainer.add(tempFilePathPane);
        superContainer.add(tempFileLimitPane);
        superContainer.add(preferredFileTypePane);
        superContainer.add(imageHeaderPanel);
        superContainer.add(imageHeaderColorPanel);
        superContainer.add(restFilePane);
        main.add(superContainer);

       //main.add(BorderLayout.CENTER,tempFilePathPane);
       //main.add(BorderLayout.CENTER,preferredFileTypePane);

        return main;
    }


    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //                                                                          Color picker


    //Mock GUI background colors
    private static int mockGUImenuBtncolor = 0;
    private static int mockGUIbackgroundColor = 1;
    private static int mockGUIflowchartBoxBGcolor = 2;
    private static int mockGUIfloatchartNumberlineBGcolor = 3;
    private static int mockGUItexteditorColor = 4;
    private static int mockGUItexteditorLinenumberBGColor = 5;
    private static int mockGUIheaderColor = 6;
    private static int mockGUImenuBtncolorHL = 7;
    private static int mockGUIFlowchartHLColor = 8;
    private static int mockGUIScrollBarColor = 9;
    private Color[] mockGUIcolorPointers = new Color[10]; //NOTE: The point of this is to act like globals to set and change the mock gui colors.

    //******************** The text color content buttons ********************************
    //DON'T Ask me to refactor the order....
    private JButton branchTextColorBtn;
    private JButton commandTextColorBtn;
    private JButton commentTextColorBtn;
    private JButton errorTextColorBtn;
    private JButton immediateTextColorBtn;
    private JButton labelTextColorBtn;
    private JButton lineNumberTextColorBtn;
    private JButton registerTextColorBtn;
    private JButton separatorTextColorBtn;



    /**
     * This is a property change listener used to match the background color of the buttons form text color / color syntax
     * */
    class BackgroundColorListener implements PropertyChangeListener {
        private JButton btn1, btn2,btn3,btn4,btn5,btn6, btn7, btn8, btn9; private int colorIndex;
        public BackgroundColorListener(JButton btn1,JButton btn2,JButton btn3,JButton btn4,JButton
                btn5,JButton btn6,JButton btn7,JButton btn8,JButton btn9, int colorIndex) {
            this.colorIndex = colorIndex;
            this.btn1 = btn1; this.btn2 = btn2; this.btn3 = btn3; this.btn4 = btn4;
            this.btn5 = btn5; this.btn6 = btn6; this.btn7 = btn7; this.btn8 = btn8;
            this.btn9 = btn9;
        }
        public void propertyChange(PropertyChangeEvent evt) {
            Color newColor = mockGUIcolorPointers[colorIndex];
            btn1.setBackground(newColor);
            btn2.setBackground(newColor);
            btn3.setBackground(newColor);
            btn4.setBackground(newColor);
            btn5.setBackground(newColor);
            btn6.setBackground(newColor);
            btn7.setBackground(newColor);
            btn8.setBackground(newColor);
            btn9.setBackground(newColor);
        }
    }


    //Define Mock GUI buttons
    //Menu
    JButton headerBtn;
    JButton menuBtn;

    //Text editor
    JButton textEditorBtn;
    JButton lineNumberBtn;
    JButton scrollBtnVert;
    JButton scrollBtnHoriz;

    //Flowchart box 1
    JButton flowchartBox1Bar;
    JButton flowchartBox1Text;





    private JPanel colorPickerContent() {
        //The main JPanel uses GridBagLayout to position content
        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(Color.decode(panelBackgroundColor));
        GridBagConstraints gbc = new GridBagConstraints();

        //This is the internal dimensions of the mock gui within the content
        int mockGUI_Height = 460; //300
        int mockGUI_Width = 900; //600

        //Top label added
        JLabel topLabel = new JLabel("Color Picker & Preview (click any area to change the color)");
        topLabel.setForeground(Color.decode(paneltextColor));
        topLabel.setFont(labelFont);

        //Add top label to middle and fist row of grid bag
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL; //Set vertical layout
        main.add(topLabel, gbc);


        //Mock GUI JPanel is the mockGUI which is built out of JButtons
        //The layout manger must be set to NULL to allow the layered pane is display correctly
        JPanel mockGUIcontainer = new JPanel();
        mockGUIcontainer.setBackground(Color.decode(panelBackgroundColor));
        mockGUIcontainer.setLayout(null);
        //--------------------------------------------------
        //The layeredPan built with buttons
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0,0,mockGUI_Width,mockGUI_Height);
        layeredPane.setPreferredSize(new Dimension(mockGUI_Width,mockGUI_Height));

        //Create the buttons
        int barPadding = 20; //20

        Vector3f bgColor = GeneralSettings.USERPREF.getBackgroundColor3f();

        //Set GUI Colors
        mockGUIcolorPointers[mockGUIbackgroundColor] = GeneralSettings.USERPREF.getBackgroundColor();
        mockGUIcolorPointers[mockGUIflowchartBoxBGcolor] = GeneralSettings.USERPREF.getFlowchartBoxackgroundColor();
        mockGUIcolorPointers[mockGUIfloatchartNumberlineBGcolor] = GeneralSettings.USERPREF.getFlowchartBoxlinenumberBGColor();
        mockGUIcolorPointers[mockGUItexteditorColor] = GeneralSettings.USERPREF.getTexteditorBGColor();
        mockGUIcolorPointers[mockGUItexteditorLinenumberBGColor] = GeneralSettings.USERPREF.getTexteditorLinenumberBGColor();
        mockGUIcolorPointers[mockGUIheaderColor] = GeneralSettings.USERPREF.getHeaderColor();
        mockGUIcolorPointers[mockGUImenuBtncolor] = GeneralSettings.USERPREF.getMenuBtnBGColor();
        mockGUIcolorPointers[mockGUImenuBtncolorHL] = GeneralSettings.USERPREF.getMenuBtnHLColor();
        mockGUIcolorPointers[mockGUIFlowchartHLColor] = GeneralSettings.USERPREF.getFlowchartBoxHighlightColor();
        mockGUIcolorPointers[mockGUIScrollBarColor] = GeneralSettings.USERPREF.getScrollBarColor();

        //Set the listener for background color



        //Background
        JButton backgroundBtn = contentLayer(mockGUIcolorPointers[mockGUIbackgroundColor],0,0, mockGUI_Width,mockGUI_Height);
        //*************************** Set The Text Color Buttons **********************************
        branchTextColorBtn  = makeColorbtn(GeneralSettings.USERPREF.getBranchTextColor(), mockGUIcolorPointers[mockGUItexteditorColor], mockGUItexteditorColor,"Branch");
        commandTextColorBtn  = makeColorbtn(GeneralSettings.USERPREF.getCommandTextColor(), mockGUIcolorPointers[mockGUItexteditorColor], mockGUItexteditorColor,"Command");
        commentTextColorBtn  = makeColorbtn(GeneralSettings.USERPREF.getCommentColor(), mockGUIcolorPointers[mockGUItexteditorColor], mockGUItexteditorColor,"Comment");
        errorTextColorBtn  = makeColorbtn(GeneralSettings.USERPREF.getErrorColor(),mockGUIcolorPointers[mockGUItexteditorColor], mockGUItexteditorColor,"Error");
        immediateTextColorBtn  = makeColorbtn(GeneralSettings.USERPREF.getImmediateColor(), mockGUIcolorPointers[mockGUItexteditorColor], mockGUItexteditorColor,"Immediate");
        labelTextColorBtn  = makeColorbtn(GeneralSettings.USERPREF.getLabelColor(), mockGUIcolorPointers[mockGUItexteditorColor], mockGUItexteditorColor,"Label");
        lineNumberTextColorBtn  = makeColorbtn(GeneralSettings.USERPREF.getLineNumberColor(), mockGUIcolorPointers[mockGUItexteditorColor], mockGUItexteditorColor,"Line Number");
        registerTextColorBtn  = makeColorbtn(GeneralSettings.USERPREF.getRegisterColor(), mockGUIcolorPointers[mockGUItexteditorColor], mockGUItexteditorColor,"Register");
        separatorTextColorBtn  = makeColorbtn(GeneralSettings.USERPREF.getSeparatorColor(), mockGUIcolorPointers[mockGUItexteditorColor], mockGUItexteditorColor,"Separator");

        //Menu
        headerBtn = contentLayer(mockGUIcolorPointers[mockGUIheaderColor], 0,0, mockGUI_Width,barPadding);
        menuBtn = contentLayer(mockGUIcolorPointers[mockGUImenuBtncolor], 0,0, barPadding*4,barPadding);


        //Text editor
        textEditorBtn = contentLayer(mockGUIcolorPointers[mockGUItexteditorColor], barPadding,barPadding, barPadding*18,mockGUI_Height-barPadding);
        lineNumberBtn = contentLayer(mockGUIcolorPointers[mockGUItexteditorLinenumberBGColor], 0,barPadding, barPadding,mockGUI_Height-barPadding);
        scrollBtnVert = contentLayer(mockGUIcolorPointers[mockGUIScrollBarColor], (barPadding*18)+5,barPadding, barPadding-5, (mockGUI_Height-barPadding)-(barPadding-5));
        scrollBtnHoriz = contentLayer(mockGUIcolorPointers[mockGUIScrollBarColor], barPadding,(mockGUI_Height-barPadding)+5,(barPadding*17)+5, barPadding-5);


        //Flowchart box 1
        flowchartBox1Bar = contentLayer(mockGUIcolorPointers[mockGUIfloatchartNumberlineBGcolor], mockGUI_Width/2,barPadding*2, barPadding,(barPadding*7));
        flowchartBox1Text = contentLayer(mockGUIcolorPointers[mockGUIflowchartBoxBGcolor], mockGUI_Width/2+barPadding,barPadding*2, barPadding*20,(barPadding*7));

        //Flowchart box 2
        JButton flowchartBox2Bar = contentLayer(mockGUIcolorPointers[mockGUIfloatchartNumberlineBGcolor], mockGUI_Width/2,barPadding*12, barPadding,(barPadding*7));
        JButton flowchartBox2Text = contentLayer(mockGUIcolorPointers[mockGUIflowchartBoxBGcolor], mockGUI_Width/2+barPadding,barPadding*12, barPadding*20,(barPadding*7));

        //Lines And Arrows
        LineJButton flowchartLine1 = new LineJButton(Color.WHITE, 0,0,0,60,6);
        flowchartLine1.setBounds((mockGUI_Width/2)+barPadding,(barPadding*9),20,60);

        ArrowJButton arrowJButton = new ArrowJButton(Color.WHITE,ArrowJButton.DOWN);
        arrowJButton.setBounds((mockGUI_Width/2)+barPadding-4,(barPadding*12)-5,60,60);

        //Listener for panel resizing
       /* main.addComponentListener(new ComponentAdapter() {

            private int s1h = main.getSize().height;
            private int s1w = main.getSize().width;;
            public void currentSize() {

            }

            public void componentResized(ComponentEvent componentEvent) {

                int s2h = componentEvent.getComponent().getSize().height;
                int s2w = componentEvent.getComponent().getSize().width;

                if(s1h>0 && s1w>0) {
                    System.out.println("Window Resizing: s1w = " + s1w + ", s2w = " + s2w);
                    System.out.println("Window Resizing: s1h = " + s1h + ", s2h = " + s2h);
                    if(s2h>s1h)
                    headerBtn.setBounds(0, 0, headerBtn.getWidth()+barPadding, headerBtn.getHeight() + barPadding);
                    layeredPane.setBounds(0,0,layeredPane.getWidth() + barPadding,layeredPane.getHeight() + barPadding);
                    layeredPane.setPreferredSize(new Dimension( layeredPane.getWidth() + barPadding, layeredPane.getHeight() + barPadding));
                    main.getLayout().;
                    //Add the mockGUI container to the main JPanel and set the grid constraints to padding to size

                    //temp

                }
                s1w = s2w;
                s1h = s2h;
            }
        });*/


        //Set the listeners
        menuBtn.addActionListener(e-> {
            Color newColor = JColorChooser.showDialog(this,"Select a color",GeneralSettings.USERPREF.getBackgroundColor());
            if(newColor != null) {
                mockGUIcolorPointers[mockGUImenuBtncolor] = newColor;
                menuBtn.setBackground(mockGUIcolorPointers[mockGUImenuBtncolor]);
            }
            //Reset GBC
            gbc.ipadx = 0;
            gbc.ipady = 0;
            gbc.gridx = 2;
            gbc.gridy = 2;
            //Add remove and add new content
            main.remove(2);
            main.add(slectionIndicatiorMenuBtnHLPicker(menuBtn), gbc,2);
            main.revalidate();

        });


        backgroundBtn.addActionListener(e-> {
            Color newColor = JColorChooser.showDialog(this,"Select a color",GeneralSettings.USERPREF.getBackgroundColor());
            if(newColor != null) {
                mockGUIcolorPointers[mockGUIbackgroundColor] = newColor;
                backgroundBtn.setBackground(mockGUIcolorPointers[mockGUIbackgroundColor]);
            }

            //Reset GBC
            gbc.ipadx = 0;
            gbc.ipady = 0;
            gbc.gridx = 2;
            gbc.gridy = 2;
            //Add remove and add new content
            main.remove(2);
            main.add(slectionIndicatiorSingleColor(backgroundBtn,mockGUIbackgroundColor), gbc,2);
            main.revalidate();

        });





        headerBtn.addActionListener(e-> {
            Color newColor = JColorChooser.showDialog(this,"Select a color",GeneralSettings.USERPREF.getBackgroundColor());
            if(newColor != null) {
                mockGUIcolorPointers[mockGUIheaderColor] = newColor;
                headerBtn.setBackground(mockGUIcolorPointers[mockGUIheaderColor]);
            }

            //Reset GBC
            gbc.ipadx = 0;
            gbc.ipady = 0;
            gbc.gridx = 2;
            gbc.gridy = 2;
            //Add remove and add new content
            main.remove(2);
            main.add(slectionIndicatiorSingleColor(headerBtn,mockGUIheaderColor), gbc,2);
            main.revalidate();


        });
        //TODO: Account for the fact that there is two types of line number text color. For flowchart and code editor
        textEditorBtn.addPropertyChangeListener("background",new BackgroundColorListener(
                branchTextColorBtn, commandTextColorBtn, commentTextColorBtn,
                errorTextColorBtn, immediateTextColorBtn, labelTextColorBtn,
                lineNumberTextColorBtn, registerTextColorBtn,separatorTextColorBtn,
                mockGUItexteditorColor) );





        textEditorBtn.addActionListener(e->{
            Color newColor = JColorChooser.showDialog(this,"Select a color",GeneralSettings.USERPREF.getBackgroundColor());
            if(newColor != null) {
                mockGUIcolorPointers[mockGUItexteditorColor] = newColor;
                textEditorBtn.setBackground(mockGUIcolorPointers[mockGUItexteditorColor]);
            }

            //Reset GBC
            gbc.ipadx = 0;
            gbc.ipady = 0;
            gbc.gridx = 2;
            gbc.gridy = 2;
            //Add remove and add new content
            main.remove(2);
            main.add(slectionIndicatiorSingleColor(textEditorBtn,mockGUItexteditorColor), gbc,2);
            main.revalidate();


        });

        //TODO: Account for the fact that there is two types of line number text color. For flowchart and code editor
        textEditorBtn.addPropertyChangeListener("background",new BackgroundColorListener(
                branchTextColorBtn, commandTextColorBtn, commentTextColorBtn,
                errorTextColorBtn, immediateTextColorBtn, labelTextColorBtn,
                lineNumberTextColorBtn, registerTextColorBtn,separatorTextColorBtn,
                mockGUItexteditorColor) );



        lineNumberBtn.addActionListener(e-> {
            Color newColor = JColorChooser.showDialog(this,"Select a color",GeneralSettings.USERPREF.getBackgroundColor());
            if(newColor != null) {
                mockGUIcolorPointers[mockGUItexteditorLinenumberBGColor] = newColor;
                lineNumberBtn.setBackground(mockGUIcolorPointers[mockGUItexteditorLinenumberBGColor]);
            }


            //Reset GBC
            gbc.ipadx = 0;
            gbc.ipady = 0;
            gbc.gridx = 2;
            gbc.gridy = 2;
            //Add remove and add new content
            main.remove(2);
            main.add(slectionIndicatiorSingleColor(lineNumberBtn,mockGUItexteditorLinenumberBGColor), gbc,2);
            main.revalidate();


        });

        scrollBtnVert.addActionListener(e-> {
            Color newColor = JColorChooser.showDialog(this,"Select a color",GeneralSettings.USERPREF.getBackgroundColor());
            if(newColor != null) {
                mockGUIcolorPointers[mockGUIScrollBarColor] = newColor;
                scrollBtnVert.setBackground(mockGUIcolorPointers[mockGUIScrollBarColor]);
                scrollBtnHoriz.setBackground(mockGUIcolorPointers[mockGUIScrollBarColor]);
            }
            //Reset GBC
            gbc.ipadx = 0;
            gbc.ipady = 0;
            gbc.gridx = 2;
            gbc.gridy = 2;
            //Add remove and add new content
            main.remove(2);
            main.add(slectionIndicatiorSingleColor(scrollBtnVert,scrollBtnHoriz,mockGUIScrollBarColor), gbc,2);
            main.revalidate();
        });

        scrollBtnHoriz.addActionListener(e-> {
            Color newColor = JColorChooser.showDialog(this,"Select a color",GeneralSettings.USERPREF.getBackgroundColor());
            if(newColor != null) {
                mockGUIcolorPointers[mockGUIScrollBarColor] = newColor;
                scrollBtnVert.setBackground(mockGUIcolorPointers[mockGUIScrollBarColor]);
                scrollBtnHoriz.setBackground(mockGUIcolorPointers[mockGUIScrollBarColor]);
            }
            //Reset GBC
            gbc.ipadx = 0;
            gbc.ipady = 0;
            gbc.gridx = 2;
            gbc.gridy = 2;
            //Add remove and add new content
            main.remove(2);
            main.add(slectionIndicatiorSingleColor(scrollBtnVert,scrollBtnHoriz,mockGUIScrollBarColor), gbc,2);
            main.revalidate();
        });


        flowchartBox1Text.addActionListener(e->{
            Color newColor = JColorChooser.showDialog(this,"Select a color",GeneralSettings.USERPREF.getBackgroundColor());
            if(newColor != null) {
                mockGUIcolorPointers[mockGUIflowchartBoxBGcolor] = newColor;
                flowchartBox1Text.setBackground(mockGUIcolorPointers[mockGUIflowchartBoxBGcolor]);
                flowchartBox2Text.setBackground(mockGUIcolorPointers[mockGUIflowchartBoxBGcolor]);
            }

            //Reset GBC
            gbc.ipadx = 0;
            gbc.ipady = 0;
            gbc.gridx = 2;
            gbc.gridy = 2;
            //Add remove and add new content
            main.remove(2);
            main.add(selectionIndicatorFlowchartColorAndHL(flowchartBox2Text,flowchartBox1Text,mockGUIflowchartBoxBGcolor, mockGUIFlowchartHLColor), gbc,2);
            main.revalidate();

        });

        flowchartBox2Text.addActionListener(e->{
            Color newColor = JColorChooser.showDialog(this,"Select a color",GeneralSettings.USERPREF.getBackgroundColor());
            if(newColor != null) {
                mockGUIcolorPointers[mockGUIflowchartBoxBGcolor] = newColor;
                flowchartBox1Text.setBackground(mockGUIcolorPointers[mockGUIflowchartBoxBGcolor]);
                flowchartBox2Text.setBackground(mockGUIcolorPointers[mockGUIflowchartBoxBGcolor]);
            }


            //Reset GBC
            gbc.ipadx = 0;
            gbc.ipady = 0;
            gbc.gridx = 2;
            gbc.gridy = 2;
            //Add remove and add new content
            main.remove(2);
            main.add(selectionIndicatorFlowchartColorAndHL(flowchartBox2Text,flowchartBox1Text,mockGUIflowchartBoxBGcolor, mockGUIFlowchartHLColor), gbc,2);
            main.revalidate();


        });

        flowchartBox1Bar.addActionListener(e->{
            Color newColor = JColorChooser.showDialog(this,"Select a color",GeneralSettings.USERPREF.getBackgroundColor());
            if(newColor != null) {
                mockGUIcolorPointers[mockGUIfloatchartNumberlineBGcolor] = newColor;
                flowchartBox1Bar.setBackground(mockGUIcolorPointers[mockGUIfloatchartNumberlineBGcolor]);
                flowchartBox2Bar.setBackground(mockGUIcolorPointers[mockGUIfloatchartNumberlineBGcolor]);

                //Reset GBC
                gbc.ipadx = 0;
                gbc.ipady = 0;
                gbc.gridx = 2;
                gbc.gridy = 2;
                //Add remove and add new content
                main.remove(2);
                main.add(selectionIndicatorFlowchartLineNumbersColor(flowchartBox1Bar,flowchartBox2Bar,mockGUIfloatchartNumberlineBGcolor), gbc,2);
                main.revalidate();

            }

        });

        flowchartBox2Bar.addActionListener(e->{
            Color newColor = JColorChooser.showDialog(this,"Select a color",GeneralSettings.USERPREF.getBackgroundColor());
            if(newColor != null) {
                mockGUIcolorPointers[mockGUIfloatchartNumberlineBGcolor] = newColor;
                flowchartBox1Bar.setBackground(mockGUIcolorPointers[mockGUIfloatchartNumberlineBGcolor]);
                flowchartBox2Bar.setBackground(mockGUIcolorPointers[mockGUIfloatchartNumberlineBGcolor]);


                //Reset GBC
                gbc.ipadx = 0;
                gbc.ipady = 0;
                gbc.gridx = 2;
                gbc.gridy = 2;
                //Add remove and add new content
                main.remove(2);
                main.add(selectionIndicatorFlowchartLineNumbersColor(flowchartBox1Bar,flowchartBox2Bar,mockGUIfloatchartNumberlineBGcolor), gbc,2);
                main.revalidate();
            }

        });


        // Add the buttons
        layeredPane.add(backgroundBtn,  Integer.valueOf(0));
        layeredPane.add(lineNumberBtn, Integer.valueOf(1));
        layeredPane.add(scrollBtnVert, Integer.valueOf(7));
        layeredPane.add(scrollBtnHoriz, Integer.valueOf(8));
        layeredPane.add(headerBtn,  Integer.valueOf(1));
        layeredPane.add(menuBtn,  Integer.valueOf(2));
        layeredPane.add(textEditorBtn, Integer.valueOf(2));

        //Flowchart box 1
        layeredPane.add(flowchartBox1Bar, Integer.valueOf(3));
        layeredPane.add(flowchartBox1Text, Integer.valueOf(3));
        //Flowchart box 2
        layeredPane.add(flowchartBox2Bar, Integer.valueOf(3));
        layeredPane.add(flowchartBox2Text, Integer.valueOf(3));

        //Example Line
        layeredPane.add(flowchartLine1,Integer.valueOf(5));
        layeredPane.add(arrowJButton,Integer.valueOf(6));


        mockGUIcontainer.add(layeredPane);

        //Add the mockGUI container to the main JPanel and set the grid constraints to padding to size
        gbc.ipadx = mockGUI_Width;
        gbc.ipady = mockGUI_Height;
        gbc.gridx = 2;
        gbc.gridy = 1;
        main.add(mockGUIcontainer, gbc);



        //Add Selection Indicator to grid...

        gbc.ipadx = 0;
        gbc.ipady = 0;
        gbc.gridx = 2;
        gbc.gridy = 2;

        main.add(new JLabel(""), gbc);



        //Add driver buttons to the main
        JPanel driverButtons = new JPanel(new FlowLayout());
        driverButtons.setBackground(Color.decode(panelBackgroundColor));
        JButton dark = new JButton("Dark Theme");
        dark.setForeground(Color.decode(buttonTextColor));
        dark.setBackground(Color.decode(buttonBackgroundColor));
        dark.addActionListener(e->{
            //Menu Button background color
            mockGUIcolorPointers[mockGUImenuBtncolor] = new Color(
                    GeneralSettings.TEXT_BUTTON_BACKGROUND_COLOR.x,
                    GeneralSettings.TEXT_BUTTON_BACKGROUND_COLOR.y,
                    GeneralSettings.TEXT_BUTTON_BACKGROUND_COLOR.z);

            menuBtn.setBackground(mockGUIcolorPointers[mockGUImenuBtncolor]);


            //Menu Button background hl color
            mockGUIcolorPointers[mockGUImenuBtncolorHL] = new Color(
                    GeneralSettings.HIGHLIGHT_COLOR.x,
                    GeneralSettings.HIGHLIGHT_COLOR.y,
                    GeneralSettings.HIGHLIGHT_COLOR.z
            );

            //BackGround
            mockGUIcolorPointers[mockGUIbackgroundColor] = new Color(GeneralSettings.base02.x, GeneralSettings.base02.y,GeneralSettings.base02.z);
            backgroundBtn.setBackground(mockGUIcolorPointers[mockGUIbackgroundColor]);

            //scroll bars
            mockGUIcolorPointers[mockGUIScrollBarColor] = new Color(GeneralSettings.SCROLL_BAR_COLOR.x,GeneralSettings.SCROLL_BAR_COLOR.y,GeneralSettings.SCROLL_BAR_COLOR.z);
            scrollBtnHoriz.setBackground(mockGUIcolorPointers[mockGUIScrollBarColor]);
            scrollBtnVert.setBackground(mockGUIcolorPointers[mockGUIScrollBarColor]);



            //Header color
            mockGUIcolorPointers[mockGUIheaderColor] = new Color(
                    GeneralSettings.HEADER_COLOR.x,
                    GeneralSettings.HEADER_COLOR.y,
                    GeneralSettings.HEADER_COLOR.z
                    );
            headerBtn.setBackground(mockGUIcolorPointers[mockGUIheaderColor]);

            //Text editor
            mockGUIcolorPointers[mockGUItexteditorColor] = new Color(GeneralSettings.base03.x, GeneralSettings.base03.y,GeneralSettings.base03.z);
            textEditorBtn.setBackground(mockGUIcolorPointers[mockGUItexteditorColor]);
            mockGUIcolorPointers[mockGUItexteditorLinenumberBGColor] = new Color(GeneralSettings.LINE_NUMBER_BACKGROUND_COLOR.x, GeneralSettings.LINE_NUMBER_BACKGROUND_COLOR.y,GeneralSettings.LINE_NUMBER_BACKGROUND_COLOR.z);
            lineNumberBtn.setBackground(mockGUIcolorPointers[mockGUItexteditorLinenumberBGColor]);


            //Flowchart color background
            mockGUIcolorPointers[mockGUIflowchartBoxBGcolor] = new Color(GeneralSettings.base03.x, GeneralSettings.base03.y,GeneralSettings.base03.z);
            flowchartBox1Text.setBackground(mockGUIcolorPointers[mockGUIflowchartBoxBGcolor]);
            flowchartBox2Text.setBackground(mockGUIcolorPointers[mockGUIflowchartBoxBGcolor]);

            //Flowchart color background highlight
            mockGUIcolorPointers[mockGUIFlowchartHLColor] = new Color(
                    GeneralSettings.TEXT_COLOR.x,
                    GeneralSettings.TEXT_COLOR.y,
                    GeneralSettings.TEXT_COLOR.z);



            //Flowchart line number color background
            mockGUIcolorPointers[mockGUIfloatchartNumberlineBGcolor] = new Color(GeneralSettings.LINE_NUMBER_BACKGROUND_COLOR.x, GeneralSettings.LINE_NUMBER_BACKGROUND_COLOR.y,GeneralSettings.LINE_NUMBER_BACKGROUND_COLOR.z);
            flowchartBox1Bar.setBackground(mockGUIcolorPointers[mockGUIfloatchartNumberlineBGcolor]);
            flowchartBox2Bar.setBackground(mockGUIcolorPointers[mockGUIfloatchartNumberlineBGcolor]);


            //Clear selection indicator
            //Reset GBC
            gbc.ipadx = 0;
            gbc.ipady = 0;
            gbc.gridx = 2;
            gbc.gridy = 2;
            //Add remove and add new content
            main.remove(2);
            main.add(new JLabel(""), gbc,2);
            main.revalidate();

        });

        JButton light = new JButton("Light Theme");
        light.setForeground(Color.decode(buttonTextColor));
        light.setBackground(Color.decode(buttonBackgroundColor));
        light.addActionListener(e-> {


            //Menu Button background color
            mockGUIcolorPointers[mockGUImenuBtncolor] = new Color(
                    GeneralSettings.TEXT_BUTTON_BACKGROUND_COLOR.x,
                    GeneralSettings.TEXT_BUTTON_BACKGROUND_COLOR.y,
                    GeneralSettings.TEXT_BUTTON_BACKGROUND_COLOR.z);

            menuBtn.setBackground(mockGUIcolorPointers[mockGUImenuBtncolor]);


            //Menu Button background hl color
            mockGUIcolorPointers[mockGUImenuBtncolorHL] = new Color(
                    GeneralSettings.base01_light.x,
                    GeneralSettings.base01_light.y,
                    GeneralSettings.base01_light.z
            );


            //BackGround
            mockGUIcolorPointers[mockGUIbackgroundColor] = new Color(
                    GeneralSettings.base02_light.x,
                    GeneralSettings.base02_light.y,
                    GeneralSettings.base02_light.z);

            backgroundBtn.setBackground(mockGUIcolorPointers[mockGUIbackgroundColor]);

            //scroll bars
            mockGUIcolorPointers[mockGUIScrollBarColor] = new Color(GeneralSettings.SCROLL_BAR_COLOR.x,GeneralSettings.SCROLL_BAR_COLOR.y,GeneralSettings.SCROLL_BAR_COLOR.z);
            scrollBtnHoriz.setBackground(mockGUIcolorPointers[mockGUIScrollBarColor]);
            scrollBtnVert.setBackground(mockGUIcolorPointers[mockGUIScrollBarColor]);

            //Header color
            mockGUIcolorPointers[mockGUIheaderColor] = new Color(
                    GeneralSettings.base03_light.x,
                    GeneralSettings.base03_light.y,
                    GeneralSettings.base03_light.z
            );
            headerBtn.setBackground(mockGUIcolorPointers[mockGUIheaderColor]);

            //Text editor
            mockGUIcolorPointers[mockGUItexteditorColor] = new Color(
                    GeneralSettings.base03_light.x,
                    GeneralSettings.base03_light.y,
                    GeneralSettings.base03_light.z);

            textEditorBtn.setBackground(mockGUIcolorPointers[mockGUItexteditorColor]);


            mockGUIcolorPointers[mockGUItexteditorLinenumberBGColor] = new Color(
                    GeneralSettings.base00_light.x,
                    GeneralSettings.base00_light.y,
                    GeneralSettings.base00_light.z);

            lineNumberBtn.setBackground(mockGUIcolorPointers[mockGUItexteditorLinenumberBGColor]);


            //Flowchart color background
            mockGUIcolorPointers[mockGUIflowchartBoxBGcolor] = new Color(
                    GeneralSettings.base03_light.x,
                    GeneralSettings.base03_light.y,
                    GeneralSettings.base03_light.z);

            flowchartBox1Text.setBackground(mockGUIcolorPointers[mockGUIflowchartBoxBGcolor]);
            flowchartBox2Text.setBackground(mockGUIcolorPointers[mockGUIflowchartBoxBGcolor]);

            //Flowchart highlight color background
            mockGUIcolorPointers[mockGUIFlowchartHLColor] = new Color(
                    GeneralSettings.base2_light.x,
                    GeneralSettings.base2_light.y,
                    GeneralSettings.base2_light.z);


            //Flowchart line number color background
            mockGUIcolorPointers[mockGUIfloatchartNumberlineBGcolor] = new Color(
                    GeneralSettings.base00_light.x,
                    GeneralSettings.base00_light.y,
                    GeneralSettings.base00_light.z);

            flowchartBox1Bar.setBackground(mockGUIcolorPointers[mockGUIfloatchartNumberlineBGcolor]);
            flowchartBox2Bar.setBackground(mockGUIcolorPointers[mockGUIfloatchartNumberlineBGcolor]);



            //Clear selection indicator
            //Reset GBC
            gbc.ipadx = 0;
            gbc.ipady = 0;
            gbc.gridx = 2;
            gbc.gridy = 2;
            //Add remove and add new content
            main.remove(2);
            main.add(new JLabel(""), gbc,2);
            main.revalidate();

        });

        JButton apply = new JButton("Apply Color");
        apply.setForeground(Color.decode(buttonTextColor));
        apply.setBackground(Color.decode(buttonBackgroundColor));
        apply.addActionListener(e->{

            //Menu Button Background Color
            GeneralSettings.USERPREF.setMenuBtnBGColor(mockGUIcolorPointers[mockGUImenuBtncolor]);

            //Menu button highlight color
            GeneralSettings.USERPREF.setMenuBtnHLColor(mockGUIcolorPointers[mockGUImenuBtncolorHL]);

            //Background
            GeneralSettings.USERPREF.setBackgroundColor(mockGUIcolorPointers[mockGUIbackgroundColor]);

            //header color
            GeneralSettings.USERPREF.setHeaderColor(mockGUIcolorPointers[mockGUIheaderColor]);

            //Text editor color
            GeneralSettings.USERPREF.setTextEditorBGColor(mockGUIcolorPointers[mockGUItexteditorColor]);

            //Text editor line number BG color
            GeneralSettings.USERPREF.setTexteditorLinenumberBGColor(mockGUIcolorPointers[mockGUItexteditorLinenumberBGColor]);

            //Text editor scroll bar color
            GeneralSettings.USERPREF.setScrollBarColor(mockGUIcolorPointers[mockGUIScrollBarColor]);

            //Flowchart box BG colors
            GeneralSettings.USERPREF.setFlowchartBoxbackgroundColor(mockGUIcolorPointers[mockGUIflowchartBoxBGcolor]);

            //Flowchart highlight color
            GeneralSettings.USERPREF.setFlowchartBoxHighlightColor(mockGUIcolorPointers[mockGUIFlowchartHLColor]);

            //Flowchart box line number BG color
            GeneralSettings.USERPREF.setFlowchartBoxlinenumberBGColor(mockGUIcolorPointers[mockGUIfloatchartNumberlineBGcolor]);


            //Toggle change in master renderer
            GeneralSettings.MasterRendererUserPrefToggle = true;


            //Clear selection indicator
            //Reset GBC
            gbc.ipadx = 0;
            gbc.ipady = 0;
            gbc.gridx = 2;
            gbc.gridy = 2;
            //Add remove and add new content
            main.remove(2);
            main.add(new JLabel(""), gbc,2);
            main.revalidate();

        });

        JButton cancel = new JButton("Cancel");
        cancel.setForeground(Color.decode(buttonTextColor));
        cancel.setBackground(Color.decode(buttonBackgroundColor));
        cancel.addActionListener(e-> {
            mockGUIcolorPointers[mockGUImenuBtncolor] = GeneralSettings.USERPREF.getMenuBtnBGColor();

            menuBtn.setBackground(mockGUIcolorPointers[mockGUImenuBtncolor]);


            //Menu Button background hl color
            mockGUIcolorPointers[mockGUImenuBtncolorHL] = GeneralSettings.USERPREF.getMenuBtnHLColor();

            //BackGround
            mockGUIcolorPointers[mockGUIbackgroundColor] = GeneralSettings.USERPREF.getBackgroundColor();
            backgroundBtn.setBackground(mockGUIcolorPointers[mockGUIbackgroundColor]);

            //scroll bars
            mockGUIcolorPointers[mockGUIScrollBarColor] = GeneralSettings.USERPREF.getScrollBarColor();
            scrollBtnHoriz.setBackground(mockGUIcolorPointers[mockGUIScrollBarColor]);
            scrollBtnVert.setBackground(mockGUIcolorPointers[mockGUIScrollBarColor]);

            //Header color
            mockGUIcolorPointers[mockGUIheaderColor] = GeneralSettings.USERPREF.getHeaderColor();
            headerBtn.setBackground(mockGUIcolorPointers[mockGUIheaderColor]);

            //Text editor
            mockGUIcolorPointers[mockGUItexteditorColor] = GeneralSettings.USERPREF.getTexteditorBGColor();
            textEditorBtn.setBackground(mockGUIcolorPointers[mockGUItexteditorColor]);

            //Text editor line number
            mockGUIcolorPointers[mockGUItexteditorLinenumberBGColor] = GeneralSettings.USERPREF.getTexteditorLinenumberBGColor();
            lineNumberBtn.setBackground(mockGUIcolorPointers[mockGUItexteditorLinenumberBGColor]);

            //Flowchart color background
            mockGUIcolorPointers[mockGUIflowchartBoxBGcolor] = GeneralSettings.USERPREF.getFlowchartBoxackgroundColor();
            flowchartBox1Text.setBackground(mockGUIcolorPointers[mockGUIflowchartBoxBGcolor]);
            flowchartBox2Text.setBackground(mockGUIcolorPointers[mockGUIflowchartBoxBGcolor]);

            //Flowchart color background highlight
            mockGUIcolorPointers[mockGUIFlowchartHLColor] = GeneralSettings.USERPREF.getFlowchartBoxHighlightColor();

            //Flowchart line number color background
            mockGUIcolorPointers[mockGUIfloatchartNumberlineBGcolor] = GeneralSettings.USERPREF.getTexteditorLinenumberBGColor();
            flowchartBox1Bar.setBackground(mockGUIcolorPointers[mockGUIfloatchartNumberlineBGcolor]);
            flowchartBox2Bar.setBackground(mockGUIcolorPointers[mockGUIfloatchartNumberlineBGcolor]);

            //Clear selection indicator
            //Reset GBC
            gbc.ipadx = 0;
            gbc.ipady = 0;
            gbc.gridx = 2;
            gbc.gridy = 2;
            //Add remove and add new content
            main.remove(2);
            main.add(new JLabel(""), gbc,2);
            main.revalidate();
        });

        driverButtons.add(dark);
        driverButtons.add(light);
        driverButtons.add(cancel);
        driverButtons.add(apply);

        gbc.fill = GridBagConstraints.CENTER;
        gbc.ipadx = 0;
        gbc.ipady = 0;
        gbc.gridx = 2;
        gbc.gridy = 3;
        main.add(driverButtons, gbc);


        //The outer JPanel is a boarder layout where main is set within a scroll pane
        //This allows the grid bag layout to display centered and indicate there is more content
        JPanel outer = new JPanel(new BorderLayout());
        ScrollPane scrollPane1 = new ScrollPane();
        scrollPane1.add(main);
        outer.add(scrollPane1,BorderLayout.CENTER);
        return outer;
    }

    /**
     * Selection Indicator Menu Button highlight picker
     * */
    private JLayeredPane slectionIndicatiorMenuBtnHLPicker(JButton sourceBtn) {

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0,0,300,50);
        layeredPane.setPreferredSize(new Dimension(300,50));

        JButton currentColorBtn = contentLayer(mockGUIcolorPointers[mockGUImenuBtncolor], 0,0, 300,50);

        currentColorBtn.addActionListener(e-> {
            Color newColor = JColorChooser.showDialog(this,"Select a color",GeneralSettings.USERPREF.getBackgroundColor());
            if(newColor != null) {
                mockGUIcolorPointers[mockGUImenuBtncolor] = newColor;
                sourceBtn.setBackground(mockGUIcolorPointers[mockGUImenuBtncolor]);
                currentColorBtn.setBackground(mockGUIcolorPointers[mockGUImenuBtncolor]);
            }
        });

        JButton highlightColorBtn = contentLayer(mockGUIcolorPointers[mockGUImenuBtncolorHL], 150,0, 150,50);

        highlightColorBtn.addActionListener(e-> {
            Color newColor = JColorChooser.showDialog(this,"Select a color",GeneralSettings.USERPREF.getBackgroundColor());
            if(newColor != null) {
                mockGUIcolorPointers[mockGUImenuBtncolorHL] = newColor;
                highlightColorBtn.setBackground(mockGUIcolorPointers[mockGUImenuBtncolorHL]);
            }
        });

        JLabel textInBack = new JLabel("<html>Color<br/>Selected</html>");
        textInBack.setFont(new Font("Arial",Font.BOLD,18));
        textInBack.setForeground(Color.BLACK);
        textInBack.setBounds(2,1,300,50);


        JLabel textInFront = new JLabel("<html>Color<br/>Selected</html>");
        textInFront.setFont(new Font("Arial",Font.BOLD,18));
        textInFront.setForeground(Color.WHITE);
        textInFront.setBounds(0,0,300,50);


        JLabel textInBack2 = new JLabel("<html>Highlight<br/>Color</html>");
        textInBack2.setFont(new Font("Arial",Font.BOLD,18));
        textInBack2.setForeground(Color.BLACK);
        textInBack2.setBounds(150,1,100,50);


        JLabel textInFront2 = new JLabel("<html>Highlight<br/>Color</html>");
        textInFront2.setFont(new Font("Arial",Font.BOLD,18));
        textInFront2.setForeground(Color.WHITE);
        textInFront2.setBounds(152,0,100,50);

        //Add components
        layeredPane.add(currentColorBtn, Integer.valueOf(0));
        layeredPane.add(textInBack, Integer.valueOf(1));
        layeredPane.add(textInFront, Integer.valueOf(2));
        layeredPane.add(highlightColorBtn, Integer.valueOf(3));
        layeredPane.add(textInBack2, Integer.valueOf(4));
        layeredPane.add(textInFront2, Integer.valueOf(5));

        return layeredPane;
    }

    /**
     * Selection indicator flowchart LineNumber
     * */
    private JLayeredPane selectionIndicatorFlowchartLineNumbersColor(JButton sourceBtn, JButton sourceBtn2, int colorInt) {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0,0,300,50);
        layeredPane.setPreferredSize(new Dimension(300,50));

        JButton currentColorBtn = contentLayer(mockGUIcolorPointers[colorInt], 0,0, 300,50);

        currentColorBtn.addActionListener(e-> {
            Color newColor = JColorChooser.showDialog(this,"Select a color",GeneralSettings.USERPREF.getBackgroundColor());
            if(newColor != null) {
                mockGUIcolorPointers[colorInt] = newColor;
                sourceBtn.setBackground(mockGUIcolorPointers[colorInt]);
                sourceBtn2.setBackground(mockGUIcolorPointers[colorInt]);
                currentColorBtn.setBackground(mockGUIcolorPointers[colorInt]);
            }
        });

        JLabel textInBack = new JLabel("<html>Color<br/>Selected</html>");
        textInBack.setFont(new Font("Arial",Font.BOLD,18));
        textInBack.setForeground(Color.BLACK);
        textInBack.setBounds(2,1,300,50);


        JLabel textInFront = new JLabel("<html>Color<br/>Selected</html>");
        textInFront.setFont(new Font("Arial",Font.BOLD,18));
        textInFront.setForeground(Color.WHITE);
        textInFront.setBounds(0,0,300,50);

        //Add components
        layeredPane.add(currentColorBtn, Integer.valueOf(0));
        layeredPane.add(textInBack, Integer.valueOf(1));
        layeredPane.add(textInFront, Integer.valueOf(2));

        return layeredPane;
    }

    /**
     * Selection indicator flowchart color and highlight picker
     * */
    private JLayeredPane selectionIndicatorFlowchartColorAndHL(JButton sourceBtn, JButton sourceBtn2, int colorInt, int colorInt2) {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0,0,300,50);
        layeredPane.setPreferredSize(new Dimension(300,50));

        JButton currentColorBtn = contentLayer(mockGUIcolorPointers[colorInt], 0,0, 300,50);

        currentColorBtn.addActionListener(e-> {
            Color newColor = JColorChooser.showDialog(this,"Select a color",GeneralSettings.USERPREF.getBackgroundColor());
            if(newColor != null) {
                mockGUIcolorPointers[colorInt] = newColor;
                sourceBtn.setBackground(mockGUIcolorPointers[colorInt]);
                sourceBtn2.setBackground(mockGUIcolorPointers[colorInt]);
                currentColorBtn.setBackground(mockGUIcolorPointers[colorInt]);
            }
        });

        JButton highlightColorBtn = contentLayer(mockGUIcolorPointers[colorInt2], 150,0, 150,50);

        highlightColorBtn.addActionListener(e-> {
            Color newColor = JColorChooser.showDialog(this,"Select a color",GeneralSettings.USERPREF.getBackgroundColor());
            if(newColor != null) {
                mockGUIcolorPointers[colorInt2] = newColor;
                highlightColorBtn.setBackground(mockGUIcolorPointers[colorInt2]);
            }
        });

        JLabel textInBack = new JLabel("<html>Color<br/>Selected</html>");
        textInBack.setFont(new Font("Arial",Font.BOLD,18));
        textInBack.setForeground(Color.BLACK);
        textInBack.setBounds(2,1,300,50);


        JLabel textInFront = new JLabel("<html>Color<br/>Selected</html>");
        textInFront.setFont(new Font("Arial",Font.BOLD,18));
        textInFront.setForeground(Color.WHITE);
        textInFront.setBounds(0,0,300,50);


        JLabel textInBack2 = new JLabel("<html>Highlight<br/>Color</html>");
        textInBack2.setFont(new Font("Arial",Font.BOLD,18));
        textInBack2.setForeground(Color.BLACK);
        textInBack2.setBounds(150,1,100,50);


        JLabel textInFront2 = new JLabel("<html>Highlight<br/>Color</html>");
        textInFront2.setFont(new Font("Arial",Font.BOLD,18));
        textInFront2.setForeground(Color.WHITE);
        textInFront2.setBounds(152,0,100,50);

        //Add components
        layeredPane.add(currentColorBtn, Integer.valueOf(0));
        layeredPane.add(textInBack, Integer.valueOf(1));
        layeredPane.add(textInFront, Integer.valueOf(2));
        layeredPane.add(highlightColorBtn, Integer.valueOf(3));
        layeredPane.add(textInBack2, Integer.valueOf(4));
        layeredPane.add(textInFront2, Integer.valueOf(5));

        return layeredPane;
    }

    /**
     * Selection Indicator single color
     * */
    private JLayeredPane slectionIndicatiorSingleColor(JButton sourceBtn, int colorInt) {

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0,0,300,50);
        layeredPane.setPreferredSize(new Dimension(300,50));

        JButton currentColorBtn = contentLayer(mockGUIcolorPointers[colorInt], 0,0, 300,50);

        currentColorBtn.addActionListener(e-> {
            Color newColor = JColorChooser.showDialog(this,"Select a color",GeneralSettings.USERPREF.getBackgroundColor());
            if(newColor != null) {
                mockGUIcolorPointers[colorInt] = newColor;
                sourceBtn.setBackground(mockGUIcolorPointers[colorInt]);
                currentColorBtn.setBackground(mockGUIcolorPointers[colorInt]);
            }
        });

        JLabel textInBack = new JLabel("<html>Color<br/>Selected</html>");
        textInBack.setFont(new Font("Arial",Font.BOLD,18));
        textInBack.setForeground(Color.BLACK);
        textInBack.setBounds(2,1,300,50);


        JLabel textInFront = new JLabel("<html>Color<br/>Selected</html>");
        textInFront.setFont(new Font("Arial",Font.BOLD,18));
        textInFront.setForeground(Color.WHITE);
        textInFront.setBounds(0,0,300,50);

        //Add components
        layeredPane.add(currentColorBtn, Integer.valueOf(0));
        layeredPane.add(textInBack, Integer.valueOf(1));
        layeredPane.add(textInFront, Integer.valueOf(2));


        return layeredPane;
    }

    private JLayeredPane slectionIndicatiorSingleColor(JButton sourceBtn,JButton sourceBtn2, int colorInt) {

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0,0,300,50);
        layeredPane.setPreferredSize(new Dimension(300,50));

        JButton currentColorBtn = contentLayer(mockGUIcolorPointers[colorInt], 0,0, 300,50);

        currentColorBtn.addActionListener(e-> {
            Color newColor = JColorChooser.showDialog(this,"Select a color",GeneralSettings.USERPREF.getBackgroundColor());
            if(newColor != null) {
                mockGUIcolorPointers[colorInt] = newColor;
                sourceBtn.setBackground(mockGUIcolorPointers[colorInt]);
                sourceBtn2.setBackground(mockGUIcolorPointers[colorInt]);
                currentColorBtn.setBackground(mockGUIcolorPointers[colorInt]);
            }
        });

        JLabel textInBack = new JLabel("<html>Color<br/>Selected</html>");
        textInBack.setFont(new Font("Arial",Font.BOLD,18));
        textInBack.setForeground(Color.BLACK);
        textInBack.setBounds(2,1,300,50);


        JLabel textInFront = new JLabel("<html>Color<br/>Selected</html>");
        textInFront.setFont(new Font("Arial",Font.BOLD,18));
        textInFront.setForeground(Color.WHITE);
        textInFront.setBounds(0,0,300,50);

        //Add components
        layeredPane.add(currentColorBtn, Integer.valueOf(0));
        layeredPane.add(textInBack, Integer.valueOf(1));
        layeredPane.add(textInFront, Integer.valueOf(2));


        return layeredPane;
    }






    /**
     * Returns a customized JButton:
     * (x,y) position relative to layered panel. W, H width and height of the button.
     * */
    private JButton contentLayer(Color color, int x, int y, int w, int h) {
        JButton layer = new JButton();
        layer.setBounds(x,y,w,h);
        layer.setBorderPainted(false);
        layer.setBackground(color);
        layer.setOpaque(true);
        return layer;
    }


    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //                                                                          Syntax Color
    private JPanel textColorContent() {

        JPanel mainPane = new JPanel();
        mainPane.setBackground(Color.decode(panelBackgroundColor));






        //Syntax Color Options
        JPanel syntaxColorOptionsContainerPane = new JPanel();
        syntaxColorOptionsContainerPane.setBackground(Color.decode(panelBackgroundColor));
        JPanel syntaxColorOptionsPane = new JPanel(new GridBagLayout());
        syntaxColorOptionsPane.setBackground(Color.decode(buttonBackgroundColor));
        GridBagConstraints scopGBC = new GridBagConstraints();

        //Add the title Syntax color options
        JLabel sycoLabel = new JLabel("Syntax Color Options:");
        sycoLabel.setForeground(Color.decode(paneltextColor));
        sycoLabel.setFont(labelFont);
        scopGBC.gridx = 0; scopGBC.gridy = 0; scopGBC.fill = GridBagConstraints.CENTER; scopGBC.gridwidth = 4;

        syntaxColorOptionsPane.add(sycoLabel, scopGBC);

        syntaxColorOptionsContainerPane.add(BorderLayout.CENTER, syntaxColorOptionsPane);

        //Make labels and buttons
        //SEE Top of Background Color Listener class ...

        //Add listeners TODO: Add the action listeners for the buttons make a function to do this


        //Add labels and buttons to panel
        //Row 1
        scopGBC.gridwidth = 1; //Reset to single cell
        scopGBC.gridx = 0; scopGBC.gridy = 1; scopGBC.insets = new Insets(5,5,5,5); //Set padding
        syntaxColorOptionsPane.add(branchTextColorBtn, scopGBC);
        scopGBC.gridx = 1; scopGBC.gridy = 1;
        syntaxColorOptionsPane.add(commandTextColorBtn, scopGBC);
        scopGBC.gridx = 2; scopGBC.gridy = 1;
        syntaxColorOptionsPane.add(commentTextColorBtn, scopGBC);
        scopGBC.gridx = 3; scopGBC.gridy = 1;
        syntaxColorOptionsPane.add(errorTextColorBtn, scopGBC);
        //Row 2
        scopGBC.gridx = 0; scopGBC.gridy = 2;
        syntaxColorOptionsPane.add(immediateTextColorBtn, scopGBC);
        scopGBC.gridx = 1; scopGBC.gridy = 2;
        syntaxColorOptionsPane.add(labelTextColorBtn, scopGBC);
        scopGBC.gridx = 2; scopGBC.gridy = 2;
        syntaxColorOptionsPane.add(lineNumberTextColorBtn, scopGBC);
        //Row 3
        scopGBC.gridx = 0; scopGBC.gridy = 3;
        syntaxColorOptionsPane.add(registerTextColorBtn, scopGBC);
        scopGBC.gridx = 1; scopGBC.gridy = 3;
        syntaxColorOptionsPane.add(separatorTextColorBtn, scopGBC);
        //Row 4

        //Row 5
        //Driver buttons
        //Default, Set All, Cancel, Apply
        JPanel driverButtons = new JPanel(new FlowLayout());
        driverButtons.setBackground(Color.decode(panelBackgroundColor));
        JButton defaultBtn = new JButton("Default");
        defaultBtn.setForeground(Color.decode(buttonTextColor));
        defaultBtn.setBackground(Color.decode(buttonBackgroundColor));
        JButton setALLBtn = new JButton("Set All");
        setALLBtn.setForeground(Color.decode(buttonTextColor));
        setALLBtn.setBackground(Color.decode(buttonBackgroundColor));
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setForeground(Color.decode(buttonTextColor));
        cancelBtn.setBackground(Color.decode(buttonBackgroundColor));
        JButton applyBtn = new JButton("Apply");
        applyBtn.setForeground(Color.decode(buttonTextColor));
        applyBtn.setBackground(Color.decode(buttonBackgroundColor));

        driverButtons.add(defaultBtn); driverButtons.add(setALLBtn);
        driverButtons.add(cancelBtn); driverButtons.add(applyBtn);

        defaultBtn.addActionListener(e-> {

            branchTextColorBtn.setForeground(new Color(GeneralSettings.branchColor.x,GeneralSettings.branchColor.y,GeneralSettings.branchColor.z));
            commandTextColorBtn.setForeground(new Color(GeneralSettings.commandColor.x,GeneralSettings.commandColor.y,GeneralSettings.commandColor.z));
            commentTextColorBtn.setForeground(new Color(GeneralSettings.commentColor.x,GeneralSettings.commentColor.y,GeneralSettings.commentColor.z));
            errorTextColorBtn.setForeground(new Color(GeneralSettings.errorColor.x,GeneralSettings.errorColor.y,GeneralSettings.errorColor.z));
            immediateTextColorBtn.setForeground(new Color(GeneralSettings.immediateColor.x,GeneralSettings.immediateColor.y,GeneralSettings.immediateColor.z));


            labelTextColorBtn.setForeground(new Color(GeneralSettings.labelColor.x, GeneralSettings.labelColor.y, GeneralSettings.labelColor.z));
            lineNumberTextColorBtn.setForeground(new Color(GeneralSettings.TEXT_COLOR.x, GeneralSettings.TEXT_COLOR.y, GeneralSettings.TEXT_COLOR.z));
            registerTextColorBtn.setForeground(new Color(GeneralSettings.registerColor.x, GeneralSettings.registerColor.y, GeneralSettings.registerColor.z));
            separatorTextColorBtn.setForeground(new Color(GeneralSettings.separatorColor.x, GeneralSettings.separatorColor.y, GeneralSettings.separatorColor.z));
        });

        setALLBtn.addActionListener(e-> {
            Color newColor = JColorChooser.showDialog(this,"Select a color",GeneralSettings.USERPREF.getBackgroundColor());
            if(newColor != null) {
                branchTextColorBtn.setForeground(newColor);
                commandTextColorBtn.setForeground(newColor);
                commentTextColorBtn.setForeground(newColor);
                errorTextColorBtn.setForeground(newColor);
                immediateTextColorBtn.setForeground(newColor);
                labelTextColorBtn.setForeground(newColor);
                lineNumberTextColorBtn.setForeground(newColor);
                registerTextColorBtn.setForeground(newColor);
                separatorTextColorBtn.setForeground(newColor);
            }
        });

        cancelBtn.addActionListener(e->{
            branchTextColorBtn.setForeground(GeneralSettings.USERPREF.getBranchTextColor());
            commandTextColorBtn.setForeground(GeneralSettings.USERPREF.getCommandTextColor());
            commentTextColorBtn.setForeground(GeneralSettings.USERPREF.getCommentColor());
            errorTextColorBtn.setForeground(GeneralSettings.USERPREF.getErrorColor());
            immediateTextColorBtn.setForeground(GeneralSettings.USERPREF.getImmediateColor());
            labelTextColorBtn.setForeground(GeneralSettings.USERPREF.getLabelColor());
            lineNumberTextColorBtn.setForeground(GeneralSettings.USERPREF.getLineNumberColor());
            registerTextColorBtn.setForeground(GeneralSettings.USERPREF.getRegisterColor());
            separatorTextColorBtn.setForeground(GeneralSettings.USERPREF.getSeparatorColor());
        });

        applyBtn.addActionListener(e-> {
            GeneralSettings.USERPREF.setBranchTextColor(branchTextColorBtn.getForeground());
            GeneralSettings.USERPREF.setCommandTextColor(commandTextColorBtn.getForeground());
            GeneralSettings.USERPREF.setCommentColor(commentTextColorBtn.getForeground());
            GeneralSettings.USERPREF.setErrorColor(errorTextColorBtn.getForeground());
            GeneralSettings.USERPREF.setImmediateColor(immediateTextColorBtn.getForeground());
            GeneralSettings.USERPREF.setLabelColor(labelTextColorBtn.getForeground());
            GeneralSettings.USERPREF.setLineNumberColor(lineNumberTextColorBtn.getForeground());
            GeneralSettings.USERPREF.setRegisterColor(registerTextColorBtn.getForeground());
            GeneralSettings.USERPREF.setSeparatorColor(separatorTextColorBtn.getForeground());
            GeneralSettings.MasterRendererUserPrefToggle = true;
        });

        scopGBC.gridx = 0; scopGBC.gridy = 4; scopGBC.gridwidth = 4;
        syntaxColorOptionsPane.add(driverButtons, scopGBC);

        //Row 6
        JLabel fontLabel = new JLabel("Choose Font (Applies on restart):");
        fontLabel.setFont(labelFont);
        fontLabel.setForeground(Color.decode(paneltextColor));

        scopGBC.gridx = 0; scopGBC.gridy = 5; scopGBC.gridwidth = 4;
        syntaxColorOptionsPane.add(fontLabel,scopGBC);

        //Row 7
        //Font drop down
        AvailableFonts availableFonts = new AvailableFonts();
        String[] petStrings = availableFonts.getFontNamesStrings();
        JComboBox fontDropDownComboBox = new JComboBox(petStrings);
        fontDropDownComboBox.setPrototypeDisplayValue("12345678901234567890"); //Sets a width for the box
        fontDropDownComboBox.setFont(new Font("Verdana", Font.BOLD, 14)); //Make the font larger
        fontDropDownComboBox.setSelectedIndex(availableFonts.getNameIndexFromPath(GeneralSettings.USERPREF.getCurrentFontInUse())); //Set the selection index
        fontDropDownComboBox.addActionListener(e->{
            JComboBox cb = (JComboBox)e.getSource();
            String fontName = (String)cb.getSelectedItem();
            String filePath = availableFonts.FONT_PATHS.get(availableFonts.getNameIndexFromName(fontName));
            //System.out.println(fontName);
            //System.out.println(availableFonts.FONT_PATHS.get(availableFonts.getNameIndexFromName(fontName)));
            //Prevent a change to a path that does not exist?
            GeneralSettings.USERPREF.setCurrentFontInUse(filePath);
        });

        scopGBC.gridx = 0; scopGBC.gridy = 6; scopGBC.gridwidth = 4;
        syntaxColorOptionsPane.add(fontDropDownComboBox,scopGBC);




        mainPane.add(syntaxColorOptionsContainerPane);
        return mainPane;
    }


    /**
     * Returns a vbox of a label and a JButton
     * */
    private JPanel vbox2(JLabel label, JButton btn) {
        JPanel main = new JPanel();
        BoxLayout boxLayout = new BoxLayout(main, BoxLayout.Y_AXIS);
        main.setLayout(boxLayout);
        main.add(label);
        main.add(btn);
        return main;
    }


    /**
     * Returns a customized JButton:
     * (x,y) position relative to layered panel. W, H width and height of the button.
     * */
    private JButton makeColorbtn(Color textColor,  Color backgroundColor, int colorIndex, String text) {
        JButton btn = new JButton(text);
        btn.setForeground(textColor);
        btn.setBorderPainted(false);
        btn.setBackground(backgroundColor);
        btn.setOpaque(true);

        btn.addActionListener(e-> {
            Color newColor = JColorChooser.showDialog(this,"Select a color",GeneralSettings.USERPREF.getBackgroundColor());
            if(newColor != null) {
                mockGUIcolorPointers[colorIndex] = newColor;
                btn.setForeground(mockGUIcolorPointers[colorIndex]);
            }
        });
        return btn;
    }

    private JPanel helpContent(){
        JPanel mainPane = new JPanel();
        BoxLayout boxLayout = new BoxLayout(mainPane, BoxLayout.Y_AXIS);
        mainPane.setLayout(boxLayout);

        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        String url = "/alfat_help_page.html";
        java.net.URL helpURL = SettingsMenu.class.getResource(url);
        if (helpURL != null) {
            try {
                editorPane.setPage(helpURL);
            } catch (IOException e) {
                System.err.println("Attempted to read a bad URL: " + url);
            }
        } else {
            System.err.println("Couldn't find file: " + url);
        }

        JScrollPane editorScrollPane = new JScrollPane(editorPane);
        editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        /*editorScrollPane.setPreferredSize(new Dimension(500, 500));
        editorScrollPane.setMinimumSize(new Dimension(50, 50));*/




        mainPane.add(BorderLayout.CENTER, editorScrollPane);
        return mainPane;
    }

}
