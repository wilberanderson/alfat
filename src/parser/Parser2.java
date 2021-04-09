package parser;

import controllers.ApplicationController;
import controllers.flowchartWindow.FlowchartWindowController;
import gui.FlowchartLine;
import gui.terminators.ArrowHead;
import gui.terminators.Junction;
import gui.terminators.Terminator;
import gui.textBoxes.FlowchartTextBox;
import gui.texts.*;
import gui.windows.PopupWindow;
import main.EngineTester;
import main.GeneralSettings;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.CallbackI;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import parser.LogicScripter.ParserLogicScripter;
import parser.LogicScripter.TokenMatcher;

import java.io.*;
import java.util.*;

public class Parser2  {
    public float x_bound = -10;
    public float y_bound = 10;
    // attributes
    String infile;  // file path
    boolean verbose; // final release should have this changed to false
    boolean openToTag = false;
    String targetFileTag = "";
    String closingFileTag = "";
    private boolean invalidFlag = false; // invalid labels found in parsing? Highlight after flowchart generation
    public ArrayList<FlowChartObject> flowchart = new ArrayList<>();

    HashMap<String, Integer> labelMap = new HashMap<>(); // map of labels -> line numbers
    HashMap<FlowChartObject, FlowChartObject> codeBlockMap = new HashMap<>();
    List<CodeLine> lines = new ArrayList<>();

    private SimpleTokenizer simpleTokenizer = new SimpleTokenizer();

    //JsonReader jr = new JsonReader(new File("CodeSyntax/LC3.json"));
    //JsonReader jr = new JsonReader(new File("CodeSyntax/x86.json"));
    private CodeSyntax syn = JsonReader.mapJsonToCodeSyntax(new File("CodeSyntax/LC3.json"));

    private ParserLogicScripter parserLogicScripter;


    /**
     * Clears out clears out values of parser.
     * */
    public void clear() {
        invalidFlag = false;
        flowchart.clear();
        labelMap.clear();
        lines.clear();
    }

    /**
     * Set the code syntax of parser
     * This also sets the SimpleTokenizer split string regex and comment regex.
     * Warning: This assumes that codeSyntax is valid and contains these...
     * */
    public void setCodeSyntax(CodeSyntax codeSyntax) {
        this.syn = codeSyntax;
        if(syn.getColumnLengths() != null) {
            simpleTokenizer.setColumnLength(syn.getColumnLengths().inner);
        } else {
            simpleTokenizer.setColumnLength(null);
        }

        simpleTokenizer.setSplitRegex(syn.getKeywordPatterns().getEmptySpace());
        simpleTokenizer.setCommentRegex(syn.getKeywordPatterns().getComment());
        parserLogicScripter = new ParserLogicScripter(syn);
    }

    public Parser2(boolean verbose, boolean openToTag) {
        this.verbose = true;
        this.openToTag = openToTag;
    }

    public Parser2(){
        //default constructor, only use for helper functions.
        this.verbose = false;
    }


    /**Read an input file. Parse the input file line by line, and store them in the arrayList of CodeLine objects.
     *
     * @param infile The absolute or relative location of the file, as a string.
     */
    public void ReadFile(String infile, boolean openToTag) {
        invalidFlag = false;
        this.openToTag = openToTag;
        boolean ready = !openToTag; // flag to start saving parsed info
        boolean done = false;
        ArrayList<Integer> gotoList = new ArrayList<>();

        if (openToTag){
            this.targetFileTag = "[ \t]*"+ GeneralSettings.PARTIAL_FILE_TAG_TARGET + "\\b.*";
            if (!GeneralSettings.PARTIAL_FILE_TAG_ENDING.isBlank()) {
                this.closingFileTag = "[ \t]*" + GeneralSettings.PARTIAL_FILE_TAG_ENDING + "\\b.*";
            }
        }
        //prepare to read file:
        this.infile = infile;
        File file = new File(infile);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            //get number of lines.
            int i = 0;
            String line;
            while ((line = br.readLine()) != null) {
                i++;    //line numbers start at 1
                if (!ready){
                    ready = line.matches(this.targetFileTag);
                }
                if (ready && !done) {
                    // HERE
                    boolean first = true;
                    //parse line:
                    if (verbose) System.out.println("\nparsing line #" + i + "`" + line + "`");
                    // replaces tabs with spaces
                    //line = line.replace("\t", "    ");

                    simpleTokenizer.setVerbose(false);
                    String[] arrLine = simpleTokenizer.tokenizeString(line);
                    int columnFragmentIndex = 0; // The start index of the column fragment as a string fragment
                    int columnFragment = 0; // The actual fragment of the column

                    //temp variables:
                    Optional<String> comm = Optional.empty();
                    String label = "";
                    String targetLabel = "";
                    List<String> registers = new ArrayList<>();
                    List<TextWord> formattedString = new ArrayList<>();
                    boolean jump = false;
                    boolean ret = false;
                    int codeBlock = 0;



                    //  arrLine = {"LABEL:", "JGZ", "R1", "R2", "FINISH"}
                    for (String fragment : arrLine) {
                        if (verbose) {System.out.print("[" + fragment + "]");}

                        columnFragment = simpleTokenizer.getColumnFragment(columnFragmentIndex);

                        columnFragmentIndex++;
                        //System.out.println("column Fragment:" + columnFragment);

                        // TODO: make regex matches
                        if (parserLogicScripter.procedureStartmatcher.isMatch(fragment,columnFragment)){
                            // block start
                            codeBlock = 1;
                        } else if (parserLogicScripter.procedureEndmatcher.isMatch(fragment,columnFragment)){
                            // block start
                            codeBlock = 2;
                        }
                        if (parserLogicScripter.procedureEndmatcher.isMatch(fragment,columnFragment) || parserLogicScripter.procedureEndmatcher.isMatch(fragment,columnFragment)){
                            first = false;
                        }




                        //grab each command in the line, if they exist:
                        if (parserLogicScripter.commandMatcher.isMatch(fragment,columnFragment)) {
                            comm = Optional.of(fragment);
                            formattedString.add(new CommandWord(comm.get(), new Vector2f(0f, 0)));
                            first = false;
                        } else if (parserLogicScripter.controlMatcher.isMatch(fragment,columnFragment)) { //Control
                            comm = Optional.of(fragment);
                            formattedString.add(new BranchWord(comm.get(), new Vector2f(0f, 0)));
                            jump = true;
                            first = false;
                        } else if (parserLogicScripter.registerMatcher.isMatch(fragment,columnFragment)) {  //register
                            if (fragment.contains(",")) {
                                if (!registers.contains(fragment.substring(0, fragment.length() - 1))) {
                                    registers.add(fragment.substring(0, fragment.length() - 1));
                                }
                                formattedString.add(new RegisterWord(fragment.substring(0, fragment.length() - 1), new Vector2f(0f, 0)));
                                formattedString.add(new LabelWord(",", new Vector2f(0f, 0)));
                            } else {
                                if (!registers.contains(fragment)) {
                                    registers.add(fragment);
                                }
                                formattedString.add(new RegisterWord(fragment, new Vector2f(0f, 0)));
                            }
                            first = false;
                        } else if (parserLogicScripter.immediateMatcher.isMatch(fragment,columnFragment)) {
                            //immediate value, literal or trap
                            //just skip this for now
                            first = false;
                            formattedString.add(new ImmediateWord(fragment, new Vector2f(0f, 0)));
                        } else if (jump && parserLogicScripter.labelMatcher.isMatch(fragment,columnFragment)) {   //jump statement, this matches a label
                            //if the line is a jump statement,
                            //this matches the label or labels pointed to by the command
                            //if the language supports having the label BEFORE the command,
                            //remove the `jump &&` statement as it will cause problems.
                            // target label for the line.
                            targetLabel = fragment;
                            formattedString.add(new LabelWord(fragment, new Vector2f(0f, 0)));
                            first = false;
                        } else if (jump && parserLogicScripter.immediateMatcher.isMatch(fragment,columnFragment)) {
                            // probably a goto label
                            gotoList.add(Integer.parseInt(fragment));
                            formattedString.add(new LabelWord(fragment, new Vector2f(0f, 0)));
                            targetLabel = fragment;
                        } else if (parserLogicScripter.procedureEndmatcher.isMatch(fragment,columnFragment)) {
                            ret = true;
                            jump = true;
                            formattedString.add(new BranchWord(fragment, new Vector2f(0f, 0)));
                            first = false;
                        } else if (first && parserLogicScripter.labelMatcher.isMatch(fragment,columnFragment)) {
                            //this is the (optional) label for the line
                            label = fragment;
                            labelMap.put(label, i);
                            formattedString.add(new LabelWord(fragment, new Vector2f(0f, 0)));
                            first = false;
                            //} else if (!jump && fragment.matches("^[a-zA-Z0-9\\-_\"\\\\\\[\\]\\!<>]+")) {
                        }else if (!jump && parserLogicScripter.labelMatcher.isMatch(fragment, columnFragment)
                                && !parserLogicScripter.procedureEndmatcher.isMatch(fragment,columnFragment)
                                && !parserLogicScripter.procedureStartmatcher.isMatch(fragment,columnFragment)) {
                            //TODO: Regex again
                            //Checks if a label is a implicit goto label
                            jump = true;
                            targetLabel = fragment;
                            formattedString.add(new LabelWord(fragment, new Vector2f(0f, 0)));
                            first = false;
                        } else if (!jump && parserLogicScripter.userDefinedMatcher.isMatch(fragment,columnFragment)) {
                            //the command isn't a jump statement, so the label must be a variable i.e. string, etc.
                            formattedString.add(new LabelWord(fragment, new Vector2f(0f, 0)));
                        } else if (parserLogicScripter.commentMatcher.isMatch(fragment,columnFragment)) {
                            formattedString.add(new CommentWord(fragment, new Vector2f(0f, 0)));
                        } else if (parserLogicScripter.separatorMatcher.isMatch(fragment,columnFragment)) {
                            formattedString.add(new SeparatorWord(fragment, new Vector2f(0f, 0f)));
                        } else {
                            formattedString.add(new ErrorWord(fragment, new Vector2f(0f, 0f)));
                        }

                    }


                    //Put formatted text into an object
                    FormattedTextLine FormLine = new FormattedTextLine(formattedString);

                    //log testing data, if verbose parsing is on:
                    if (verbose) {
                        System.out.println("\nParsed line " + i);
                        comm.ifPresent(s -> System.out.println(" command: \"" + s + "\""));
                        if (!label.isEmpty()) System.out.println(" line label: \"" + label + "\"");
                        if (jump) System.out.println(" Line is a jump line (break, jump, etc.)");
                        if (jump) System.out.println(" jump targets: \"" + targetLabel + "\"");
                        System.out.println(" registers used: " + registers);
                    }

                    //call constructor for TLine, then add the new object to the arraylist for the file
                    lines.add(new CodeLine(line, comm, label, targetLabel, jump, registers, i, ret, codeBlock));
                    // Assign formatted text object to the new Tline class
                    lines.get(lines.size() - 1).setTextLine(FormLine);
                }
                if (ready && !done && !closingFileTag.isBlank()) {
                    // check if closing tag reached
                    done = line.matches(this.closingFileTag);
                }
            }
            br.close();
            if (!ready){
                // Tag was never found
                System.out.println("Invalid tag provided. Check the source file or the tag and try again.");
                // TODO: throw popup here, probably.
            }
            for (CodeLine l :lines){
                for (Integer j : gotoList){
                    if (l.getLineNumber() == j){
                        l.setLabel(j.toString());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            if (verbose) System.out.println("File not found");
            e.printStackTrace();
        } catch (IOException e) {
            if (verbose) System.out.println("IO error occurred");
            e.printStackTrace();
        }
    }





    /** getFormattedLine(String line)
     *
     * @param line The line to be parsed
     * @return
     */
    public EditableFormattedTextLine getFormattedLine(String line){
        boolean first = true;
        //parse line:
//        line = line.replace("\t", "    ");
        simpleTokenizer.setVerbose(true);
        String[] arrLine = simpleTokenizer.tokenizeString(line);


        //temp variables:
        Optional<String> comm = Optional.empty();
        String label = "";
        String targetLabel = "";
        List<String> registers = new ArrayList<>();
        List<TextWord> formattedString = new ArrayList<>();
        boolean jump = false;

        int columnFragmentIndex = 0; // The start index of the column fragment as a string fragment
        int columnFragment = 0; // The actual fragment of the column


        //  arrLine = {"LABEL:", "JGZ", "R1", "R2", "FINISH"}
        for (String fragment : arrLine) {
            if (verbose) System.out.print("[" + fragment + "]");

            columnFragment = simpleTokenizer.getColumnFragment(columnFragmentIndex);
            columnFragmentIndex++;

            //grab each command in the line, if they exist:
            if (parserLogicScripter.commandMatcher.isMatch(fragment,columnFragment)) {
                comm = Optional.of(fragment);
                formattedString.add(new CommandWord(comm.get(), new Vector2f(0f, 0)));
                first = false;
            } else if (parserLogicScripter.controlMatcher.isMatch(fragment,columnFragment)) {
                comm = Optional.of(fragment);
                formattedString.add(new BranchWord(comm.get(), new Vector2f(0f, 0)));
                jump = true;
                first = false;
            } else if (parserLogicScripter.registerMatcher.isMatch(fragment,columnFragment)) {  //register

                if (fragment.contains(",")) {
                    if (!registers.contains(fragment.substring(0, fragment.length() - 1))) {
                        registers.add(fragment.substring(0, fragment.length() - 1));
                    }
                    formattedString.add(new RegisterWord(fragment.substring(0, fragment.length()-1), new Vector2f(0f, 0)));
                    formattedString.add(new LabelWord(",", new Vector2f(0f, 0)));
                } else {
                    if (!registers.contains(fragment)) {
                        registers.add(fragment);
                    }
                    formattedString.add(new RegisterWord(fragment, new Vector2f(0f, 0)));
                }
                first = false;
            } else if (parserLogicScripter.immediateMatcher.isMatch(fragment,columnFragment))
             {
                //immediate value, literal or trap
                //just skip this for now
                first = false;
                formattedString.add(new ImmediateWord(fragment, new Vector2f(0f, 0)));
            } else if (jump && parserLogicScripter.labelMatcher.isMatch(fragment,columnFragment)) {   //jump statement, this matches a label
                //if the line is a jump statement,
                //this matches the label or labels pointed to by the command
                //if the language supports having the label BEFORE the command,
                //remove the `jump &&` statement as it will cause problems.
                targetLabel = fragment;
                formattedString.add(new LabelWord(fragment, new Vector2f(0f, 0)));
                first = false;
            } else if (parserLogicScripter.commentMatcher.isMatch(fragment,columnFragment)) {
                formattedString.add(new CommentWord(fragment, new Vector2f(0f, 0)));
            }
            else if (first && parserLogicScripter.labelMatcher.isMatch(fragment,columnFragment)) {
                //this is the (optional) label for the line
                label = fragment;
                formattedString.add(new LabelWord(fragment, new Vector2f(0f, 0)));
                first = false;
            } else if (!jump && parserLogicScripter.userDefinedMatcher.isMatch(fragment,columnFragment))
            {
                //the command isn't a jump statement, so the label must be a variable i.e. string, etc.
                formattedString.add(new LabelWord(fragment, new Vector2f(0f, 0)));
            }
            else if (parserLogicScripter.separatorMatcher.isMatch(fragment,columnFragment)){
                formattedString.add(new SeparatorWord(fragment, new Vector2f(0f,0f)));
            } else {
                formattedString.add(new ErrorWord(fragment, new Vector2f(0f, 0f)));
            }
        }



        //Put formatted text into an object
        EditableFormattedTextLine FormLine = new EditableFormattedTextLine(formattedString, line);

        //log testing data, if verbose parsing is on:
        if (verbose) {
            comm.ifPresent(s -> System.out.println(" command: \"" + s + "\""));
            if (!label.isEmpty()) System.out.println(" line label: \"" + label + "\"");
            if (jump) System.out.println(" Line is a jump line (break, jump, etc.)");
            if (jump) System.out.println(" jump targets: \"" + targetLabel + "\"");
            System.out.println(" registers used: " + registers);
        }

        return FormLine;
    }

    /**
     * Default formatted text line. Used when there is no valid syntax in use.
     * Defaults to making all text white.
     * */
    public EditableFormattedTextLine getFormattedLineDefault(String line) {
        List<TextWord> formattedString = new ArrayList<>();
        formattedString.add(new LabelWord(line, new Vector2f(0f, 0)));
        return new EditableFormattedTextLine(formattedString, line);
    }

    /**
     * Create flowchart objects. <b>Only</b> call after the file has been parsed.
     */
    public void generateFlowObjects() {
        //generate naive boxes for flowchart.
        if (verbose) System.out.println("\n\nBeginning flowchart parsing:");
        flowchart.add(new FlowChartObject());
        flowchart.get(flowchart.size() - 1).setBoxNumber(flowchart.size() - 1);

        for (CodeLine line : lines) {
            if (verbose) {
                System.out.println();
                System.out.println("line text \"" + line.getLineText(true) + "\"");
                System.out.println("jumps = " + line.isJumps());
            }
            //This line has a label or starts a code block, start a new box:
            if (verbose) System.out.println("line label: \"" + line.getLabel() + "\"");
            if (!line.getLabel().isEmpty() || line.getCodeBlock() == 1) {
                //start new box
                if (verbose) System.out.println("label found \"" + line.getLabel() + "\"");
                flowchart.add(new FlowChartObject());
                flowchart.get(flowchart.size() - 1).setStartLine(line.getLineNumber() - 1);
                flowchart.get(flowchart.size() - 1).setLabel(line.getLabel());
            }

            // Add the line to the current box.
            if (flowchart.get(flowchart.size() - 1).getStartLine() == line.getLineNumber() - 1) {
                //first line in box, don't add if blank
                if (!line.getLineText(true).isBlank()) {
                    flowchart.get(flowchart.size() - 1).addLine(line);
                    flowchart.get(flowchart.size() - 1).lineCount++;
                } else {
                    flowchart.get(flowchart.size() - 1).setStartLine(flowchart.get(flowchart.size() - 1).getStartLine() + 1);
                }
            } else {
                flowchart.get(flowchart.size() - 1).addLine(line);
                flowchart.get(flowchart.size() - 1).lineCount++;
            }

            // Create code blocks
            if (flowchart.get(flowchart.size() - 1).codeBlock == 0) {
                // doesn't have blocking yet
                flowchart.get(flowchart.size() - 1).codeBlock = line.getCodeBlock();
            } else if (flowchart.get(flowchart.size() - 1).codeBlock == 1 && line.getCodeBlock() == 2){
                // box is a single, enclosed codeBlock.
                flowchart.get(flowchart.size() - 1).codeBlock = 3;
            }

            //if the line is a jump/branch, end the box and start a new one.
            if (line.isJumps()) {
                if (verbose) System.out.println("Line jumps, box ended.");
                flowchart.get(flowchart.size() - 1).jumps = true;
                if (line.isReturns()) {
                    // line returns
                    flowchart.get(flowchart.size() - 1).setReturns(true);
                } else {
                    // line doesn't return; i.e. has a target label:
                    flowchart.get(flowchart.size() - 1).target = line.getTarget();
                }

                flowchart.add(new FlowChartObject());
                flowchart.get(flowchart.size() - 1).setStartLine(line.getLineNumber());
            }
        }

        //remove all empty boxes
        flowchart.removeIf(box -> box.getLineCount() == 0);
        flowchart.removeIf(box -> box.getFullText(true).isBlank());
        //create linkages
        int i = 1;
        List<FlowChartObject> codeBlocks = new ArrayList<>();
        for (FlowChartObject box : flowchart) {
            // Link jumps:
            box.setBoxNumber(i);
            // If the box jumps, find where it targets and link them.
            if (box.isJumps() && !box.isReturns()) {
                if (verbose) System.out.println("Creating linkage for box " + box.label + " targeting " + box.target);

                for (FlowChartObject candidate : flowchart) {
                    if (verbose) System.out.println(" -> Checking against box " + box.label);
                    if (candidate.label.equals(box.target)) {
                        if (verbose) System.out.println("✓ Match found");
                        box.connection = candidate;
                        break;
                    }
                }
                if (box.connection == null) {
                    box.jumps = false;
                    box.alert += "invalid_label";
                    // popup goes here
                    if (!invalidFlag) {
                        // only show a single pop up for invalid labels.
                        PopupWindow popup = new PopupWindow("Warning", "Invalid label found", "cancel", "continue");
                        GLFW.glfwMakeContextCurrent(EngineTester.getWindow());
                        invalidFlag = true;
                    }
                }
            }
            //System.out.println(box.codeBlock);
            // Link code block sections:
            if ((codeBlocks.size() & 1) == 0){
                // even or no block:
                if (box.codeBlock == 1) {
//                    System.out.println("Box starts, inserted");
                    codeBlocks.add(box);
                }
                if (box.codeBlock == 3) {
//                    System.out.println("Box is self contained, inserted");
                    codeBlocks.add(box);
                    codeBlocks.add(box);
                }
            } else {
                // odd or middle of block
                if (box.codeBlock == 2) {
//                    System.out.println("Box ends, inserted");
                    codeBlocks.add(box);
                }
            }

            i++;
        }

        for (FlowChartObject obj : codeBlocks){
            System.out.print(obj.boxNumber + ",");
        }
        System.out.println();

        for (i = 0; i < codeBlocks.size() - codeBlocks.size()%2 ; i += 2){
            // Key is end of box, value is start of box
            codeBlockMap.put(codeBlocks.get(i+1),codeBlocks.get(i));
            System.out.println("mapping box " + codeBlocks.get(i+1).boxNumber + " to box " + codeBlocks.get(i).boxNumber);
        }

        if (verbose) {
            int n = 0;
            for (FlowChartObject box : flowchart) {
                n++;
                System.out.println("─ " + box.getBoxNumber() + "\t──────────────────────────────────────────────────────────────────────────");
                System.out.println(box.getFullText(true));
                System.out.println(" Code block status: " + box.codeBlock);
                if (box.isJumps() && !box.isReturns())
                    System.out.println(" Target label: " + box.connection.label + " @ box " + box.connection.getBoxNumber());
                else if (!box.alert.isEmpty()) System.out.println("┌╼ " + box.alert);
                System.out.println("────────────────────────────────────────────────────────────────────────────────");
            }
        }
    }

    /**
     * Create flowchart.
     * Creates flowchart boxes and draws lines.
     */
    public FlowchartWindowController createFlowchart(ApplicationController controller) {
        FlowchartWindowController flowchartWindowController = controller.getFlowchartWindowController();
        if (flowchartWindowController == null) {
            flowchartWindowController = new FlowchartWindowController(controller.getTextLineController());
        } else {
            flowchartWindowController.clear();
        }

        int i = 0;
        Vector2f location = new Vector2f(GeneralSettings.FLOWCHART_PAD_LEFT - 1, 0);
        List<FlowchartTextBox> textBoxes = new ArrayList<>();
        float max_right_width = -1000f;
        float magic_number = -.1f;
        List<Vector2f> locations = new ArrayList<>();
        List<Vector2f> sizes = new ArrayList<>();
        Vector2f bottomRight = new Vector2f(location);
        Vector2f topLeft = new Vector2f(location);

        //draw the boxes vertically offset
        for (FlowChartObject box : flowchart) {
            if (verbose) {
                System.out.println("Box " + i + " @ " + location);
                System.out.println("Starting @ line #" + box.getStartLine());
            }

            flowchartWindowController.getFlowchartTextBoxController().add(new Vector2f(location.x, location.y), box.getTextLines(), box.getStartLine() + 1, box.getRegisters(), box.alert, i);
//            FlowchartTextBox textBox = new FlowchartTextBox(new Vector2f(location), box.getTextLines(), box.getStartLine()+1, box.getRegisters(), box.alert);
//            for(TextLine line : textBox.getTextLines()){
//                flowchartWindowController.getTextLineController().add(line);
//            }
//            textBoxes.add(textBox);
//            textBox.setPosition(new Vector2f(textBox.getPosition().x, textBox.getPosition().y-textBox.getSize().y));

            FlowchartTextBox textBox = flowchartWindowController.getFlowchartTextBoxController().getTextBoxes().get(flowchartWindowController.getFlowchartTextBoxController().getTextBoxes().size() - 1);
            if (verbose) System.out.println("Position: " + textBox.getPosition() + " Size: " + textBox.getSize());
            location.y = (location.y - textBox.getSize().y - GeneralSettings.FLOWCHART_PAD_TOP);
            i++;

            // Line helpers:
            max_right_width = Math.max(max_right_width, textBox.getSize().x);
            locations.add(textBox.getPosition());
            sizes.add(textBox.getSize());
        }
        //All boxes have been added to the flowchart window, unload boxes which are initially off of the screen

        flowchartWindowController.unloadFlowchartBoxes();

        // Pass flowchart boxes out:
//        flowchartWindowController.setFlowChartTextBoxList(textBoxes);

        // Draw lines:
        List<FlowchartLine> linesList = new ArrayList<>();
        int jump_lines = 0;
        int codeBlockLines = 0;

        Vector3f rainbow[] = {GeneralSettings.magenta, GeneralSettings.red, GeneralSettings.orange, GeneralSettings.yellow, GeneralSettings.green, GeneralSettings.cyan, GeneralSettings.blue, GeneralSettings.violet};

        for (int index = 0; index <= flowchart.size(); index++) {
            // Draw line to next box (not in the last box)
            if (index < flowchart.size() - 1) {
                /*if (verbose) {
                    System.out.println("Adding vertical line from box " + index + " to box " + (index + 1));
                    System.out.println(locations.get(index) + " -> " + locations.get(index+1));
                }*/
                List<Vector2f> coordinates = new ArrayList<>();
                //first point: bottom of current box:
                coordinates.add(new Vector2f((locations.get(index).x) + .05f, (locations.get(index).y)));
                //second point: top of next box:
                coordinates.add(new Vector2f((locations.get(index).x) + .05f, (locations.get(index + 1).y + sizes.get(index + 1).y)));
                //if (verbose) System.out.println("from " + (locations.get(index).y) + " to " + (-1 + locations.get(index+1).y + sizes.get(index+1).y) + "\n");
                Terminator terminator = new ArrowHead(coordinates.get(coordinates.size() - 1), GeneralSettings.ARROWHEAD_DOWN);

                FlowchartLine line = new FlowchartLine(coordinates, terminator);
                linesList.add(line);
                //if (verbose) System.out.println();
            }

            // If jump, draw line to target box:

            if (index < flowchart.size()) {
                if (flowchart.get(index).isJumps() && !flowchart.get(index).isReturns()) {
                    if (verbose) {
                        System.out.println("Adding jumping line from box " + index + " to box " + (flowchart.get(index).connection.getBoxNumber()));
                        System.out.println(Math.min(index, flowchart.get(index).connection.getBoxNumber()) + " -> " + Math.max(index, flowchart.get(index).connection.getBoxNumber()));
                    }

                    List<Vector2f> coordinates = new ArrayList<>();

                    coordinates.add(new Vector2f((locations.get(index).x) + 2 * sizes.get(index).x / 3, (locations.get(index).y)));
                    coordinates.add(new Vector2f((locations.get(index).x) + 2 * sizes.get(index).x / 3, (locations.get(index).y) - (GeneralSettings.FLOWCHART_PAD_TOP / 3)));

                    float temp = max_right_width + GeneralSettings.FLOWCHART_PAD_LEFT;

                    if (flowchart.get(index) == flowchart.get(index).connection) {
                        temp = sizes.get(index).x + GeneralSettings.FLOWCHART_PAD_LEFT;
                    } else if (index < flowchart.get(index).connection.getBoxNumber()) {
                        temp = -1f;
                        for (Vector2f item : sizes.subList(index + 1, flowchart.get(index).connection.getBoxNumber() - 1)) {
                            if (item.x + GeneralSettings.FLOWCHART_PAD_LEFT - 1f > temp)
                                temp = item.x + GeneralSettings.FLOWCHART_PAD_LEFT - 1f;
                        }
                    } else {
                        temp = -1f;
                        for (Vector2f item : sizes.subList(flowchart.get(index).connection.getBoxNumber() - 1, index+1)) {
                            if (item.x + GeneralSettings.FLOWCHART_PAD_LEFT - 1f > temp)
                                temp = item.x + GeneralSettings.FLOWCHART_PAD_LEFT - 1f;
                        }
                    }

                    if (flowchart.get(index) == flowchart.get(index).connection) {
                        coordinates.add(new Vector2f((-.95f + temp), (locations.get(index).y) - (GeneralSettings.FLOWCHART_PAD_TOP / 3)));
                        coordinates.add(new Vector2f((-.95f + temp), (sizes.get(flowchart.get(index).connection.getBoxNumber() - 1).y + locations.get(flowchart.get(index).connection.getBoxNumber() - 1).y + (GeneralSettings.FLOWCHART_PAD_TOP / 3))));
                    } else {
                        coordinates.add(new Vector2f((.25f + temp + jump_lines * GeneralSettings.LINE_OFFSET), (locations.get(index).y) - (GeneralSettings.FLOWCHART_PAD_TOP / 3)));
                        coordinates.add(new Vector2f((.25f + temp + jump_lines * GeneralSettings.LINE_OFFSET), (sizes.get(flowchart.get(index).connection.getBoxNumber() - 1).y + locations.get(flowchart.get(index).connection.getBoxNumber() - 1).y + (GeneralSettings.FLOWCHART_PAD_TOP / 3))));
                    }
                    coordinates.add(new Vector2f((locations.get(index).x) + 2 * sizes.get(flowchart.get(index).connection.getBoxNumber() - 1).x / 3, (sizes.get(flowchart.get(index).connection.getBoxNumber() - 1).y + locations.get(flowchart.get(index).connection.getBoxNumber() - 1).y + (GeneralSettings.FLOWCHART_PAD_TOP / 3))));
                    coordinates.add(new Vector2f((locations.get(index).x) + 2 * sizes.get(flowchart.get(index).connection.getBoxNumber() - 1).x / 3, (sizes.get(flowchart.get(index).connection.getBoxNumber() - 1).y + locations.get(flowchart.get(index).connection.getBoxNumber() - 1).y)));
                    Terminator terminator = new ArrowHead(coordinates.get(coordinates.size() - 1), GeneralSettings.ARROWHEAD_DOWN);

                    FlowchartLine line = new FlowchartLine(coordinates, terminator);
                    line.setColor(rainbow[jump_lines % rainbow.length]);
                    linesList.add(line);

                    jump_lines++;
                }
            }

            // Code block drawing
            if (index < flowchart.size() && codeBlockMap.containsKey(flowchart.get(index))){
                System.out.println("Adding codeBlock line ending @ box " + index + ", starting @ box " + (codeBlockMap.get(flowchart.get(index)).boxNumber));

                List<Vector2f> coordinates = new ArrayList<>();

                // first point: bottom left of last box of the block
                coordinates.add(locations.get(index));
                // second point: same as ^ but .1 to the left
                coordinates.add(new Vector2f(locations.get(index).x-.1f,locations.get(index).y));

                // third point: straight up from point 2, in line with the top of the first block box
                coordinates.add(new Vector2f(locations.get(codeBlockMap.get(flowchart.get(index)).boxNumber-1).x -.1f,locations.get(codeBlockMap.get(flowchart.get(index)).boxNumber-1).y + sizes.get(codeBlockMap.get(flowchart.get(index)).boxNumber-1).y));
                // last point: top left corner of first box in the block
                coordinates.add(new Vector2f(locations.get(codeBlockMap.get(flowchart.get(index)).boxNumber-1).x,locations.get(codeBlockMap.get(flowchart.get(index)).boxNumber-1).y + sizes.get(codeBlockMap.get(flowchart.get(index)).boxNumber-1).y));

                // terminators can't be null
                Terminator terminator = new Junction(coordinates.get(coordinates.size() - 1));

                FlowchartLine line = new FlowchartLine(coordinates, terminator);
                line.setColor(rainbow[codeBlockLines % rainbow.length]);
                linesList.add(line);

                codeBlockLines++;
            }
        }
        System.out.println(bottomRight);

        if (verbose) System.out.println("Lines added: " + linesList.size());

//        // get y_bound coordinate:
//        if(locations.size() > 0) {
//            y_bound = locations.get(locations.size() - 1).y;
//        }  else {
//            y_bound = 0;
//        }

        for (Vector2f coordinate : locations){
            bottomRight.x = Math.max(bottomRight.x,coordinate.x);
            bottomRight.y = Math.min(bottomRight.y,coordinate.y);
            topLeft.x = Math.min(topLeft.x,coordinate.x);
            topLeft.y = Math.max(topLeft.y,coordinate.y);
        }

        for (FlowchartLine line : linesList){
            for (Vector2f coordinate : line.getPositions()){
                bottomRight.x = Math.max(bottomRight.x,coordinate.x);
                bottomRight.y = Math.min(bottomRight.y,coordinate.y);
                topLeft.x = Math.min(topLeft.x,coordinate.x);
                topLeft.y = Math.max(topLeft.y,coordinate.y);
            }
        }
        Vector2f midpoint = new Vector2f((.5f * (bottomRight.x + topLeft.x)),(.5f * (bottomRight.y + topLeft.y)));

        // Screenshotting helper commands
        if (true) {
            //System.out.println("(x,y) area of the flowchart: (" + (Math.abs(bottomRight.x) + Math.abs(topLeft.x) + ", " + (Math.abs(bottomRight.y - topLeft.x) + 1f + GeneralSettings.FLOWCHART_PAD_TOP) + ")");
            System.out.println("Top left: " + topLeft);
            System.out.println("Bottom right: " + bottomRight);
            System.out.println("Center of flowchart is at coordinates (" + midpoint.x + "," + midpoint.y + ")");

            // outline flowchart
            List<Vector2f> coordinates = new ArrayList<>();
            coordinates.add(topLeft);
            coordinates.add(new Vector2f(bottomRight.x, topLeft.y));
            coordinates.add(bottomRight);
            coordinates.add(new Vector2f(topLeft.x, bottomRight.y));
            coordinates.add(topLeft);
            Terminator terminator = new Junction(coordinates.get(coordinates.size() - 1));
            FlowchartLine line = new FlowchartLine(coordinates, terminator);
            line.setColor(new Vector3f(0f, 0f, 0f));
            linesList.add(line);

            // show padding outline
            coordinates = new ArrayList<>();
            coordinates.add(new Vector2f(topLeft.x - .2f, topLeft.y + .2f));
            coordinates.add(new Vector2f(bottomRight.x + .2f, topLeft.y + .2f));
            coordinates.add(new Vector2f(bottomRight.x + .2f, bottomRight.y - .2f));
            coordinates.add(new Vector2f(topLeft.x - .2f, bottomRight.y - .2f));
            coordinates.add(new Vector2f(topLeft.x - .2f, topLeft.y + .2f));
            terminator = new Junction(coordinates.get(coordinates.size() - 1));
            line = new FlowchartLine(coordinates, terminator);
            line.setColor(GeneralSettings.magenta);
            linesList.add(line);

            // dot at center
            coordinates = new ArrayList<>();
//        coordinates.add(topLeft);
//        coordinates.add(bottomRight);
            coordinates.add(midpoint);
            terminator = new Junction(coordinates.get(coordinates.size() - 1));
            line = new FlowchartLine(coordinates, terminator);
            line.setColor(new Vector3f(255f, 255f, 255f));
            linesList.add(line);
        }

        GeneralSettings.IMAGE_SIZE =
                new Vector2f(Math.abs(bottomRight.x) + Math.abs(topLeft.x) - GeneralSettings.SCREENSHOT_PADDING_SIZE,
                Math.abs(bottomRight.y) + Math.abs(topLeft.y) - GeneralSettings.FLOWCHART_PAD_TOP);


        Matrix3f translation = new Matrix3f();
        translation.setIdentity();
        magic_number = -0.9f; //This is the zoom reset padding for some reason
        translation.m00 = 1f;   // X scaling
        translation.m11 = 1f;   // Y scale
        translation.m20 = 0f;   // X translation
        translation.m21 = - ((bottomRight.y + topLeft.y) *  translation.m11) + magic_number; // Y translation

        GeneralSettings.EXTRA = 0;




//        System.out.println(infile + ": " + translation.m00 + "," + translation.m11);
//        System.out.println("y_bound: " + y_bound);
//        System.out.println("m20 and m21" + ": " + translation.m20 + "," + translation.m21);
        GeneralSettings.IMAGE_TRANSLATION = translation;


        //Find line overlaps:
        for (FlowchartLine line1 : linesList) {
            for (FlowchartLine line2 : linesList) {
                if ((line1.getPositions().size() > 2 && line2.getPositions().size() > 2) && linesList.indexOf(line1) < linesList.indexOf(line2)) {
                    if (line1.getPositions().get(line1.getPositions().size() - 3).y == line2.getPositions().get(line2.getPositions().size() - 3).y) {
                        if (line1.getPositions().get(line1.getPositions().size() - 3).x > line2.getPositions().get(line2.getPositions().size() - 3).x) {
                            line2.getPositions().remove(line2.getPositions().size() - 1);
                            line2.getPositions().remove(line2.getPositions().size() - 1);
                            line2.setTerminator(new Junction(line2.getPositions().get(line2.getPositions().size() - 1)));
                        } else {
                            line1.getPositions().remove(line1.getPositions().size() - 1);
                            line1.getPositions().remove(line1.getPositions().size() - 1);
                            line1.setTerminator(new Junction(line1.getPositions().get(line1.getPositions().size() - 1)));
                        }
                    }
                }
            }
        }

        flowchartWindowController.setFlowchartLineList(linesList);
        if (invalidFlag)
            flowchartWindowController.locateAlert("invalid_label");
        return flowchartWindowController;
    }

    public FlowchartWindowController createFlowchart2(ApplicationController controller){
        FlowchartWindowController flowchartWindowController = controller.getFlowchartWindowController();
        if (flowchartWindowController == null) {
            flowchartWindowController = new FlowchartWindowController(controller.getTextLineController());
        } else {
            flowchartWindowController.clear();
        }

        /*
        boxes:
        place first box at origin.
        if jump: split.
            if label already place, do nothing.
            otherwise place one box left and mark the right as jump label.
        if label:
            if label already marked down, put the box there and mark both jumps as pointing to box.
            otherwise, place label as the next box vertically.

        lines: connect all connections.

         */

        return flowchartWindowController;
    }

}
