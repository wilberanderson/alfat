package gui.Settings;

import gui.OpenFileDialog;
import main.GeneralSettings;
import org.lwjgl.util.vector.Vector3f;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/*
* TODO:
*  Add start up sizes to display
*  Add menu and header bar color options
*  Add highlight color option
*  Add light theme
*  Add temp file store limit
*  Add font options...
* */
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
    private int GUI_WIDTH = 800;
    private int GUI_HEIGHT = 500;
    private int GUI_JSP_PADDING = 150;


    //Regx
    private String validFileType = "^(\\w+)$|(\\w+(,|;)\\w+)*$";
    private String validIntType = "^\\d+$";



    /**
     * Builds and displays the settings GUI
     * */
    public SettingsMenu() {
        //Set OS default look and feel
        try {
            //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        //Init fake button content
        setFakeButtonscontent();

        //Create the new window
        root = new JFrame();


        //Setup split pane
        jSplitPane = new JSplitPane();
        //jSplitPane.setResizeWeight(0);
        jSplitPane.setContinuousLayout(true);
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


//Way to force everything to be removed
//        root.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//        //New close method
//        root.addWindowListener(new java.awt.event.WindowAdapter() {
//            @Override
//            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
//                fakeButtonscontent.removeAll(fakeButtonscontent);
//                fakeButtonscontent = null;
//                fakebutton.removeAll();
//                fakebutton = null;
//                mockGUIbackgroundColor = null;
//                mockGUIflowchartBoxBGcolor= null;
//                mockGUIfloatchartNumberlineBGcolor= null;
//                mockGUItexteditorColor= null;
//                mockGUItexteditorLinenumberBGColor= null;
//                mockGUIheaderColor= null;
//                root.dispose();
//                Runtime.getRuntime().gc();
//            }
//        });

        //Set icon
        root.setIconImage(Toolkit.getDefaultToolkit().getImage("src/res/icon/icon.png"));


        root.setVisible(true);
    }

    public static void run()  {
        SettingsMenu gui = new SettingsMenu();
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

        fakeButtonscontent.add(new SettingsContent("Color & Font", colorAndfontContent()));
    }

    /**
     * Display settings content pane
     * */
    private JPanel displaySettingsContent() {

        JPanel main = new JPanel();

        //Set up container for box layout
        JPanel container = new JPanel();
        BoxLayout boxLayout = new BoxLayout(container, BoxLayout.Y_AXIS);
        container.setLayout(boxLayout);

        //Top of Layout
        JLabel ofbLable = new JLabel(" Open File Behavior ");
        ofbLable.setFont(new Font("Verdana", Font.BOLD, 18));
        ofbLable.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(ofbLable);


        //Options radio buttons
        JRadioButton ssYes = new JRadioButton("Yes");
        ssYes.setFont(labelFont);
        JRadioButton ssNo = new JRadioButton("No");
        ssNo.setFont(labelFont);
        JRadioButton fsYes = new JRadioButton("To Editor");
        fsYes.setFont(labelFont);
        JRadioButton fsNo = new JRadioButton("To Flowchart");
        fsNo.setFont(labelFont);
        ButtonGroup fsGroup = new ButtonGroup();


        //------------------------------------------------------
        //Auto generate flowchart option
        //FlowLayout
        JPanel subContainer = new JPanel(new FlowLayout());
        JRadioButton yesBtn = new JRadioButton("Yes");
        yesBtn.setFont(labelFont);
        JRadioButton noBtn = new JRadioButton("No");
        noBtn.setFont(labelFont);
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
        subContainer.add(msg1);
        subContainer.add(yesBtn);
        subContainer.add(noBtn);
        subContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(subContainer);


        //------------------------------------------------------
        //Split Screen Option
        //FlowLayout
        JPanel subContainer2 = new JPanel(new FlowLayout());

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
        subContainer2.add(msg2);
        subContainer2.add(ssYes);
        subContainer2.add(ssNo);
        subContainer2.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(subContainer2);


        //------------------------------------------------------
        //Full Screen
        //FlowLayout
        JPanel subContainer3 = new JPanel(new FlowLayout());


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
            //To Flowchart
            fsYes.setSelected(false);
            fsNo.setSelected(true);
        }

        //Radio button logic
        fsYes.addActionListener(e->{

            GeneralSettings.USERPREF.setFullscreen(1);
        });

        fsNo.addActionListener(e->{

            GeneralSettings.USERPREF.setFullscreen(-1);
        });

        JLabel msg3 = new JLabel("Full Screen:");
        msg3.setFont(labelFont);
        subContainer3.add(msg3);
        subContainer3.add(fsYes);
        subContainer3.add(fsNo);
        subContainer3.setAlignmentX(Component.CENTER_ALIGNMENT);


        container.add(subContainer3);

        main.add(BorderLayout.CENTER,container);
        return main;
    }


    /**
     * The file settings content pane
     * */
    private JPanel fileSettingsContent() {
         JPanel main = new JPanel();
         JPanel superContainer = new JPanel(new GridLayout(0,1,10,10));
         //superContainer.setBorder(BorderFactory.createDashedBorder(Color.MAGENTA));
        //-----------------------------------------

        //Change Syntax path
        JPanel syntaxFilePathPane = new JPanel(new FlowLayout());
        JLabel curSyntxPathJLable = new JLabel("Current Syntax File");
        curSyntxPathJLable.setFont(labelFont);
        syntaxFilePathPane.add(curSyntxPathJLable);
        JTextField syntaxFilePath = new JTextField(GeneralSettings.USERPREF.getSyntaxPath());
        syntaxFilePath.setEditable(false);
        syntaxFilePathPane.add(syntaxFilePath);
        JButton changeSyntxPath = new JButton("Change File");
        changeSyntxPath.addActionListener(e-> {
            OpenFileDialog ofd = new OpenFileDialog();
            ofd.openFileWindow();
            if(ofd.getFilePath() != null) {
                GeneralSettings.USERPREF.setSyntaxPath(ofd.getFilePath());
                syntaxFilePath.setText(GeneralSettings.USERPREF.getSyntaxPath());
                syntaxFilePathPane.updateUI();
            }
        });
        syntaxFilePathPane.add(changeSyntxPath);

        //Change current directory
        //Sets up a panel that changes the user preferences for the default folder path
        JPanel tempFilePathPane = new JPanel(new FlowLayout());
        JLabel curTempFileDirLabel = new JLabel("Current Temp File Directory: ");
        curTempFileDirLabel.setFont(labelFont);
        tempFilePathPane.add(curTempFileDirLabel);
        JTextField tempFilePath = new JTextField(GeneralSettings.USERPREF.getUserTempFileDirPath());
        tempFilePath.setEditable(false);
        tempFilePathPane.add(tempFilePath);
        JButton changePath = new JButton("Change Path");
        changePath.addActionListener(e-> {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    OpenFileDialog ofd = new OpenFileDialog();
                    ofd.saveFolderWindow();
                    if(ofd.getFilePath() != null) {
                        GeneralSettings.USERPREF.setUserTempFileDirPath(ofd.getFilePath());
                        tempFilePath.setText(GeneralSettings.USERPREF.getUserTempFileDirPath());
                        tempFilePathPane.updateUI();
                        //updateMenucontent(fileSettingsContent());
                    }
                }
            });
        });
        tempFilePathPane.add(changePath);


        //------------------------------------------
        //Temp File Limit





        //-----------------------------------------
        //Change preferred file type for open and save as
        //NOTE: Sets up a panel that changes the user preferences for the default folder path
        //TODO: Make it more clear what the correct file type format should be, maybe add a reset button
        JPanel preferredFileTypePane = new JPanel(new FlowLayout());
        JLabel enterPrefFileTypeLabel = new JLabel("Enter preferred file type");
        enterPrefFileTypeLabel.setFont(labelFont);
        preferredFileTypePane.add(enterPrefFileTypeLabel);
        JTextField pft = new JTextField(GeneralSettings.USERPREF.getPreferredFiletype());
        pft.setPreferredSize(new Dimension(300, 20));
        preferredFileTypePane.add(pft);

        //Change color if file type is wrong
        pft.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(!pft.getText().matches(validFileType)) {
                    pft.setForeground(Color.RED);
                } else {
                    pft.setForeground(Color.BLACK);
                }
            }
        });

        //System.out.println(userPreferences.getPreferredFiletype());

        JButton submitChange = new JButton("Submit Change");
        submitChange.addActionListener(e->{

            if(!pft.getText().matches(validFileType)) {
                pft.setForeground(Color.RED);
            } else {
                GeneralSettings.USERPREF.setPreferredFileType(pft.getText());
                pft.setForeground(Color.BLACK);
            }
        });

        preferredFileTypePane.add(submitChange);


        superContainer.add(syntaxFilePathPane);
        superContainer.add(tempFilePathPane);
        superContainer.add(preferredFileTypePane);

        main.add(superContainer);

       //main.add(BorderLayout.CENTER,tempFilePathPane);
       //main.add(BorderLayout.CENTER,preferredFileTypePane);

        return main;
    }



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

    private Color[] mockGUIcolorPointers = new Color[9];






    private JPanel colorAndfontContent() {
        //The main JPanel uses GridBagLayout to position content
        JPanel main = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        //This is the internal dimensions of the mock gui within the content
        int mockGUI_Height = 300;
        int mockGUI_Width = 600;

        //Top label added
        JLabel topLabel = new JLabel("Color Picker & Preview");
        topLabel.setFont(labelFont);

        //Add top label to middle and fist row of grid bag
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL; //Set vertical layout
        main.add(topLabel, gbc);


        //Mock GUI JPanel is the mockGUI which is built out of JButtons
        //The layout manger must be set to NULL to allow the layered pane is display correctly
        JPanel mockGUIcontainer = new JPanel();
        mockGUIcontainer.setLayout(null);
        //--------------------------------------------------
        //The layeredPan built with buttons
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0,0,mockGUI_Width,mockGUI_Height);
        layeredPane.setPreferredSize(new Dimension(mockGUI_Width,mockGUI_Height));

        //Create the buttons
        int barPadding = 20;

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


        //Background
        JButton backgroundBtn = contentLayer(mockGUIcolorPointers[mockGUIbackgroundColor],0,0, mockGUI_Width,mockGUI_Height);

        //Menu
        JButton headerBtn = contentLayer(mockGUIcolorPointers[mockGUIheaderColor], 0,0, mockGUI_Width,barPadding);
        JButton menuBtn = contentLayer(mockGUIcolorPointers[mockGUImenuBtncolor], 0,0, barPadding*4,barPadding);


        //Text editor
        JButton textEditorBtn = contentLayer(mockGUIcolorPointers[mockGUItexteditorColor], barPadding,barPadding, barPadding*12,mockGUI_Height-barPadding);
        JButton lineNumberBtn = contentLayer(mockGUIcolorPointers[mockGUItexteditorLinenumberBGColor], 0,barPadding, barPadding,mockGUI_Height-barPadding);

        //Flowchart box 1
        JButton flowchartBox1Bar = contentLayer(mockGUIcolorPointers[mockGUIfloatchartNumberlineBGcolor], mockGUI_Width/2,barPadding*2, barPadding,mockGUI_Height-(barPadding*11));
        JButton flowchartBox1Text = contentLayer(mockGUIcolorPointers[mockGUIflowchartBoxBGcolor], mockGUI_Width/2+barPadding,barPadding*2, barPadding*7,mockGUI_Height-(barPadding*11));

        //Flowchart box 2
        JButton flowchartBox2Bar = contentLayer(mockGUIcolorPointers[mockGUIfloatchartNumberlineBGcolor], mockGUI_Width/2,barPadding*8, barPadding,mockGUI_Height-(barPadding*11));
        JButton flowchartBox2Text = contentLayer(mockGUIcolorPointers[mockGUIflowchartBoxBGcolor], mockGUI_Width/2+barPadding,barPadding*8, barPadding*7,mockGUI_Height-(barPadding*11));

        //Lines And Arrows
        LineJButton flowchartLine1 = new LineJButton(Color.WHITE, 0,0,0,30,6);
        flowchartLine1.setBounds((mockGUI_Width/2)+barPadding,(barPadding*2)+ mockGUI_Height-(barPadding*11),6,35);

        ArrowJButton arrowJButton = new ArrowJButton(Color.WHITE,ArrowJButton.DOWN);
        arrowJButton.setBounds((mockGUI_Width/2)+barPadding-4,(barPadding*2)+ mockGUI_Height-(barPadding*11)+32,50,50);

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

        JButton dark = new JButton("Dark Theme");

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
            GeneralSettings.USERPREF.setTexteditorBGColor(mockGUIcolorPointers[mockGUItexteditorColor]);

            //Text editor line number BG color
            GeneralSettings.USERPREF.setTexteditorLinenumberBGColor(mockGUIcolorPointers[mockGUItexteditorLinenumberBGColor]);

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

        driverButtons.add(dark);
        driverButtons.add(light);
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
     * Selection indicator flowchart numberliens
     * */



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




    /**
     * Returns a customized JButton
     * */
    private JButton contentLayer(Color color, int x, int y, int w, int h) {
        JButton layer = new JButton();
        layer.setBounds(x,y,w,h);
        layer.setBorderPainted(false);
        layer.setBackground(color);
        layer.setOpaque(true);
        return layer;
    }




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

        fakebutton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clickButton(e.getPoint());
            }
        });

        leftPanel.setPreferredSize(new Dimension(100, 100));

        leftPanel.setLayout(new BorderLayout());
        leftPanel.add(BorderLayout.CENTER,fakebutton);

        return leftPanel;
    }

}
