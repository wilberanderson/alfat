package gui.Settings;

import gui.OpenFileDialog;
import gui.UserPreferences;
import main.GeneralSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/*
* TODO:
*  Set fixed gui size
*  Make padding look better
*  Add the color and font settings
*  Style this GUI to match the color of ALFAT
* */
/**
 * This is a swing gui class that manages user settings for ALFAT
 * */
public class SettingsMenu {
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


    //GUI Height Defaults
    private int GUI_WIDTH = 800;
    private int GUI_HEIGHT = 300;
    private int GUI_JSP_PADDING = 150;


    //Regx
    private String validFileType = "^(\\w+)$|(\\w+(,|;)\\w+)*$";


    /**
     * Builds and displays the settings GUI
     * */
    public SettingsMenu() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
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
        //Set
        root.setIconImage(Toolkit.getDefaultToolkit().getImage("src/res/icon/icon.png"));
        //Set OS default look and feel

        root.setVisible(true);
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
        jSplitPane.setRightComponent(content);
        jSplitPane.resetToPreferredSizes();
        jSplitPane.setDividerLocation(GUI_JSP_PADDING + jSplitPane.getInsets().left);
    }

    /**
     * Set's the fake buttons and their associated menu contenet
     * */
    private void setFakeButtonscontent() {
        fakeButtonscontent = new ArrayList<SettingsContent>();
        SettingsContent sc = new SettingsContent();

        fakeButtonscontent.add(new SettingsContent("Display Settings", displaySettingsContent()));

        fakeButtonscontent.add(new SettingsContent("File Settings", fileSettingsContent()));
        sc = new SettingsContent();
        sc.name = "Color & Font";
        sc.content.add(new JLabel("Color & Font Settings"));

        fakeButtonscontent.add(sc);
    }

    /**
     * Display settings content pane
     * */
    private JPanel displaySettingsContent() {

        UserPreferences userPref = new UserPreferences();


        JPanel main = new JPanel();

        //Set up container for box layout
        JPanel container = new JPanel();
        BoxLayout boxLayout = new BoxLayout(container, BoxLayout.Y_AXIS);
        container.setLayout(boxLayout);

        //Top of Layout
        JLabel ofbLable = new JLabel("- Open File Behavior -");
        ofbLable.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(ofbLable);


        //Options radio buttons
        JRadioButton ssYes = new JRadioButton("Yes");
        JRadioButton ssNo = new JRadioButton("No");
        JRadioButton fsYes = new JRadioButton("To Editor");
        JRadioButton fsNo = new JRadioButton("To Flowchart");
        ButtonGroup fsGroup = new ButtonGroup();


        //------------------------------------------------------
        //Auto generate flowchart option
        //FlowLayout
        JPanel subContainer = new JPanel(new FlowLayout());
        JRadioButton yesBtn = new JRadioButton("Yes");
        JRadioButton noBtn = new JRadioButton("No");
        ButtonGroup yesNoGroup = new ButtonGroup();
        yesNoGroup.add(yesBtn);
        yesNoGroup.add(noBtn);


        //Set GUI to current user setting
        if(userPref.getAutoGenFlowchart() == true) {
            yesBtn.setSelected(true);
            noBtn.setSelected(false);
        } else {
            noBtn.setSelected(true);
            yesBtn.setSelected(false);
        }

        //Radio button logic
        yesBtn.addActionListener(e->{

            userPref.setAutoGenFlowchart(true);
        });

        noBtn.addActionListener(e->{

            userPref.setAutoGenFlowchart(false);
        });

        subContainer.add(new JLabel("Auto Generate Flowchart:"));
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
        if(userPref.getSplitScreen() == true) {
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

            userPref.setSplitScreen(true);
            userPref.setFullscreen(0); //false

        });

        ssNo.addActionListener(e->{

            fsNo.setEnabled(true);
            fsYes.setEnabled(true);
            fsYes.setSelected(true);

            userPref.setSplitScreen(false);
            userPref.setFullscreen(1);
        });

        subContainer2.add(new JLabel("Split Screen:"));
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

        if(userPref.getFullscreen() == 0) {
            //False
            fsGroup.clearSelection();
        } else if (userPref.getFullscreen() > 0){
            //To editor
            fsYes.setSelected(true);
            fsNo.setSelected(false);
        } else if (userPref.getFullscreen() < 0) {
            //To Flowchart
            fsYes.setSelected(false);
            fsNo.setSelected(true);
        }

        //Radio button logic
        fsYes.addActionListener(e->{

            userPref.setFullscreen(1);
        });

        fsNo.addActionListener(e->{

            userPref.setFullscreen(-1);
        });

        subContainer3.add(new JLabel("Full Screen:"));
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
        UserPreferences userPreferences = new UserPreferences();

        JPanel main = new JPanel(new GridLayout(2,1,0,0));


        //-----------------------------------------
        //Change current directory
        //Sets up a panel that changes the user preferences for the default folder path
        JPanel tempFilePathPane = new JPanel(new FlowLayout());
        tempFilePathPane.add(new JLabel("Current Temp File Directory: "));
        JTextField tempFilePath = new JTextField(userPreferences.getUserTempFileDirPath());
        //tempFilePath.setPreferredSize(new Dimension(300, 40));
        //tempFilePath.setMaximumSize(new Dimension(tempFilePath.getText().length()*tempFilePath.getFont().getSize(), 40));
        tempFilePath.setEditable(false);
        tempFilePathPane.add(tempFilePath);
        JButton changePath = new JButton("Change Path");
        changePath.addActionListener(e-> {
            OpenFileDialog ofd = new OpenFileDialog();
            ofd.saveFolderWindow();
            if(ofd.getFilePath() != null) {
                userPreferences.setUserTempFileDirPath(ofd.getFilePath());
                tempFilePath.setColumns(userPreferences.getUserTempFileDirPath().length());
                tempFilePath.setText(userPreferences.getUserTempFileDirPath());
                //updateMenucontent(fileSettingsContent());
            }
        });
        tempFilePathPane.add(changePath);


        //-----------------------------------------
        //Change preferred file type for open and save as
        //NOTE: Sets up a panel that changes the user preferences for the default folder path
        //TODO: Make it more clear what the correct file type format should be, maybe add a reset button
        JPanel preferredFileTypePane = new JPanel(new FlowLayout());
        preferredFileTypePane.add(new JLabel("Enter preferred file type"));
        JTextField pft = new JTextField(userPreferences.getPreferredFiletype());
        pft.setPreferredSize(new Dimension(300, 30));
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
                userPreferences.setPreferredFileType(pft.getText());
                pft.setForeground(Color.BLACK);
            }
        });

        preferredFileTypePane.add(submitChange);


        main.add(BorderLayout.CENTER,tempFilePathPane);
        main.add(BorderLayout.CENTER,preferredFileTypePane);


        return main;
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
