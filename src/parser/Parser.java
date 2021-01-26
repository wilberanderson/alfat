package parser;

import controllers.ApplicationController;
import controllers.flowchartWindow.FlowchartWindowController;
import gui.FlowchartLine;
import gui.terminators.ArrowHead;
import gui.terminators.Junction;
import gui.terminators.Terminator;
import gui.textBoxes.FlowchartTextBox;
import gui.texts.*;
import main.GeneralSettings;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.*;
import java.util.*;

public class Parser implements CodeReader {
    public float x_bound = -10;
    public float y_bound = -10;
    // attributes
    String infile;  // file path
    boolean verbose; // final release should have this changed to false
    ArrayList<FlowChartObject> flowchart = new ArrayList<>();

    HashMap<String, Integer> labelMap = new HashMap<>(); // map of labels -> line numbers
    List<CodeLine> lines = new ArrayList<>();

    public Parser(String infile, boolean verbose) {
        this.infile = infile;
        this.verbose = verbose;
    }

    public Parser(){
        //default constructor, only use for helper functions.
        this.verbose = false;
    }

    //TODO: change from hardcoded to dynamically loaded from JSON
    JsonReader jr = new JsonReader(new File(GeneralSettings.USERPREF.getSyntaxPath()));
    LC3Syntax syn = jr.mapJsonLC3Syntax();
    String[] commands = syn.getCommands();
    String[] jumps = syn.getJumps();

    /**Read an input file. Parse the input file line by line, and store them in the arrayList of CodeLine objects.
     *
     * @param infile The absolute or relative location of the file, as a string.
     */
    @Override
    public void ReadFile(String infile) {
        //prepare to read file:
        this.infile = infile;
        File file = new File(infile);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            //get number of lines.
            int i = 0;
            String line;
            while ((line = br.readLine()) != null) {
                // HERE
                boolean first = true;
                //parse line:
                i++;    //line numbers start at 1
                if (verbose) System.out.println("\nparsing line #" + i + "`" + line + "`");
                line = line.replace("\t", "    ");

                //take entire line before semicolon
                int index = line.indexOf(";");
                if (index == -1) index = line.length();
                String[] arrLine = line.substring(0, index).split("[ ,\t]");

                //temp variables:
                Optional<String> comm = Optional.empty();
                String label = "";
                String targetLabel = "";
                List<String> registers = new ArrayList<>();
                List<TextWord> formattedString = new ArrayList<>();
                boolean jump = false;

                //  arrLine = {"LABEL:", "JGZ", "R1", "R2", "FINISH"}
                for (String fragment : arrLine) {
                    if (verbose) System.out.print("[" + fragment + "]");

                    //grab each command in the line, if they exist:
                    if (Arrays.asList(commands).contains(fragment.toUpperCase())) {
                        comm = Arrays.stream(commands).filter(fragment.toUpperCase()::equals).findAny();
                        formattedString.add(new CommandWord(comm.get(), new Vector2f(0f, 0), "\t"));
                        first = false;
                    } else if (Arrays.asList(jumps).contains(fragment.toUpperCase()) || fragment.matches("^BR[nzp]{0,3}$")) {
                        comm = Optional.of(fragment);
                        formattedString.add(new CommandWord(comm.get(), new Vector2f(0f, 0), "\t"));
                        jump = true;
                        first = false;
                    } else if (fragment.matches("^R[0-9](,)?")) {  //register
                        if (fragment.contains(",")) {
                            if (!registers.contains(fragment.substring(0, fragment.length() - 1))) {
                                registers.add(fragment.substring(0, fragment.length() - 1));
                                formattedString.add(new RegisterWord(fragment.substring(0, fragment.length()-1), new Vector2f(0f, 0), "\t"));
                            }
                        } else {
                            if (!registers.contains(fragment)) {
                                registers.add(fragment);
                                formattedString.add(new RegisterWord(fragment, new Vector2f(0f, 0), "\t"));
                            }
                        }
                        first = false;
                    } else if (fragment.matches("^[x#]-?[0-9]+")) {
                        //immediate value, literal or trap
                        //just skip this for now
                        first = false;
                        formattedString.add(new ImmediateWord(fragment, new Vector2f(0f, 0), "\t"));
                    } else if (jump && fragment.matches("^[a-zA-Z0-9\\-_]+")) {   //jump statement, this matches a label
                        //if the line is a jump statement,
                        //this matches the label or labels pointed to by the command
                        //if the language supports having the label BEFORE the command,
                        //remove the `jump &&` statement as it will cause problems.
                        targetLabel = fragment;
                        formattedString.add(new LabelWord(fragment, new Vector2f(0f, 0), ""));
                        first = false;
                    } else if (first && fragment.matches("^[a-zA-Z0-9\\-_]+")) {
                        //this is the (optional) label for the line
                        label = fragment;
                        labelMap.put(label, i);
                        formattedString.add(new LabelWord(fragment, new Vector2f(0f, 0), ""));
                        first = false;
                    } else if (!jump && fragment.matches("^[a-zA-Z0-9\\-_]+")) {
                        //the command isn't a jump statement, so the label must be a variable i.e. string, etc.
                        formattedString.add(new LabelWord(fragment, new Vector2f(0f, 0), "\t"));
                    }
                }

                if (index < line.length() - 1) {
                    formattedString.add(new CommentWord(line.substring(index), new Vector2f(0f, 0), "\t"));
                }

                //Put formatted text into an object
                TextLine FormLine = new TextLine(formattedString);

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
                lines.add(new CodeLine(line, comm, label, targetLabel, jump, registers, i));
                // Assign formatted text object to the new LC3Tline class
                lines.get(lines.size() - 1).setTextLine(FormLine);

            }
            br.close();
        } catch (FileNotFoundException e) {
            if (verbose) System.out.println("File not found");
            e.printStackTrace();
        } catch (IOException e) {
            if (verbose) System.out.println("IO error occured");
            e.printStackTrace();
        }
    }

    /** getFormattedLine(String line)
     *
     * @param line The line to be parsed
     * @return
     */
    public TextLine getFormattedLine(String line){
        boolean first = true;
        //parse line:
        line = line.replace("\t", "    ");

        //take entire line before semicolon
        int index = line.indexOf(";");
        if (index == -1) index = line.length();
        String[] arrLine = line.substring(0, index).split("[ ,\t]");

        //temp variables:
        Optional<String> comm = Optional.empty();
        String label = "";
        String targetLabel = "";
        List<String> registers = new ArrayList<>();
        List<TextWord> formattedString = new ArrayList<>();
        boolean jump = false;

        //  arrLine = {"LABEL:", "JGZ", "R1", "R2", "FINISH"}
        for (String fragment : arrLine) {
            if (verbose) System.out.print("[" + fragment + "]");

            //grab each command in the line, if they exist:
            if (Arrays.asList(commands).contains(fragment.toUpperCase())) {
                comm = Arrays.stream(commands).filter(fragment.toUpperCase()::equals).findAny();
                formattedString.add(new CommandWord(comm.get(), new Vector2f(0f, 0), "\t"));
                first = false;
            } else if (Arrays.asList(jumps).contains(fragment.toUpperCase()) || fragment.matches("^BR[nzp]{0,3}$")) {
                comm = Optional.of(fragment);
                formattedString.add(new CommandWord(comm.get(), new Vector2f(0f, 0), "\t"));
                jump = true;
                first = false;
            } else if (fragment.matches("^R[0-9](,)?")) {  //register
                if (fragment.contains(",")) {
                    if (!registers.contains(fragment.substring(0, fragment.length() - 1))) {
                        registers.add(fragment.substring(0, fragment.length() - 1));
                        formattedString.add(new RegisterWord(fragment.substring(0, fragment.length()-1), new Vector2f(0f, 0), "\t"));
                    }
                } else {
                    if (!registers.contains(fragment)) {
                        registers.add(fragment);
                        formattedString.add(new RegisterWord(fragment, new Vector2f(0f, 0), "\t"));
                    }
                }
                first = false;
            } else if (fragment.matches("^[x#]-?[0-9]+")) {
                //immediate value, literal or trap
                //just skip this for now
                first = false;
                formattedString.add(new ImmediateWord(fragment, new Vector2f(0f, 0), "\t"));
            } else if (jump && fragment.matches("^[a-zA-Z0-9\\-_]+")) {   //jump statement, this matches a label
                //if the line is a jump statement,
                //this matches the label or labels pointed to by the command
                //if the language supports having the label BEFORE the command,
                //remove the `jump &&` statement as it will cause problems.
                targetLabel = fragment;
                formattedString.add(new LabelWord(fragment, new Vector2f(0f, 0), ""));
                first = false;
            } else if (first && fragment.matches("^[a-zA-Z0-9\\-_]+")) {
                //this is the (optional) label for the line
                label = fragment;
                formattedString.add(new LabelWord(fragment, new Vector2f(0f, 0), ""));
                first = false;
            } else if (!jump && fragment.matches("^[a-zA-Z0-9\\-_]+")) {
                //the command isn't a jump statement, so the label must be a variable i.e. string, etc.
                formattedString.add(new LabelWord(fragment, new Vector2f(0f, 0), "\t"));
            }
        }

        if (index < line.length() - 1) {
            formattedString.add(new CommentWord(line.substring(index), new Vector2f(0f, 0), "\t"));
        }

        //Put formatted text into an object
        TextLine FormLine = new TextLine(formattedString);

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
     * Create flowchart objects. <b>Only</b> call after the file has been parsed.
     */
    @Override
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
            //This line has a label, start a new box:
            if (verbose) System.out.println("line label: \"" + line.getLabel() + "\"");
            if (!line.getLabel().isEmpty()) {
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


            //if the line is a jump/branch, end the box and start a new one.
            if (line.isJumps()) {
                if (verbose) System.out.println("Line jumps, box ended.");
                flowchart.get(flowchart.size() - 1).jumps = true;
                flowchart.get(flowchart.size() - 1).target = line.getTarget();

                flowchart.add(new FlowChartObject());
                flowchart.get(flowchart.size() - 1).setStartLine(line.getLineNumber());
            }
        }

        //remove all empty boxes
        flowchart.removeIf(box -> box.getLineCount() == 0);
        flowchart.removeIf(box -> box.getFullText(true).isBlank());

        //create linkages
        int i = 1;
        for (FlowChartObject box : flowchart) {
            box.setBoxNumber(i);
            // If the box jumps, find where it targets and link them.
            if (box.isJumps()) {
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
                }
            }
            i++;
        }

        if (verbose) {
            int n = 0;
            for (FlowChartObject box : flowchart) {
                n++;
                System.out.println("─ " + box.getBoxNumber() + "\t──────────────────────────────────────────────────────────────────────────");
                System.out.println(box.getFullText(true));
                if (box.isJumps())
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
        List<Vector2f> locations = new ArrayList<>();
        List<Vector2f> sizes = new ArrayList<>();

        //draw the boxes vertically offset
        for (FlowChartObject box : flowchart) {
            if (verbose) {
                System.out.println("Box " + i + " @ " + location);
                System.out.println("Starting @ line #" + box.getStartLine());
            }

            flowchartWindowController.getFlowchartTextBoxController().add(new Vector2f(location.x, location.y), box.getTextLines(), box.getStartLine() + 1, box.getRegisters(), box.alert);
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
        // Pass flowchart boxes out:
//        flowchartWindowController.setFlowChartTextBoxList(textBoxes);

        // Draw lines:
        List<FlowchartLine> linesList = new ArrayList<>();
        int jump_lines = 0;

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
                Terminator terminator;
                if (coordinates.get(coordinates.size() - 1).y < coordinates.get((coordinates.size() - 2)).y) {
                    terminator = new ArrowHead(coordinates.get(coordinates.size() - 1), false);
                } else {
                    terminator = new ArrowHead(coordinates.get(coordinates.size() - 1), true);
                }
                FlowchartLine line = new FlowchartLine(coordinates, terminator);
                linesList.add(line);
                //if (verbose) System.out.println();
            }

            // If jump, draw line to target box:

            if (index < flowchart.size()) {
                if (flowchart.get(index).isJumps()) {
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
                        for (Vector2f item : sizes.subList(flowchart.get(index).connection.getBoxNumber() - 1, index)) {
                            if (item.x + GeneralSettings.FLOWCHART_PAD_LEFT - 1f > temp)
                                temp = item.x + GeneralSettings.FLOWCHART_PAD_LEFT - 1f;
                        }
                    }

                    x_bound = Math.max(x_bound, (temp + jump_lines * GeneralSettings.LINE_OFFSET));

                    if (flowchart.get(index) == flowchart.get(index).connection) {
                        coordinates.add(new Vector2f((-.95f + temp), (locations.get(index).y) - (GeneralSettings.FLOWCHART_PAD_TOP / 3)));
                        coordinates.add(new Vector2f((-.95f + temp), (sizes.get(flowchart.get(index).connection.getBoxNumber() - 1).y + locations.get(flowchart.get(index).connection.getBoxNumber() - 1).y + (GeneralSettings.FLOWCHART_PAD_TOP / 3))));
                    } else {
                        coordinates.add(new Vector2f((.25f + temp + jump_lines * GeneralSettings.LINE_OFFSET), (locations.get(index).y) - (GeneralSettings.FLOWCHART_PAD_TOP / 3)));
                        coordinates.add(new Vector2f((.25f + temp + jump_lines * GeneralSettings.LINE_OFFSET), (sizes.get(flowchart.get(index).connection.getBoxNumber() - 1).y + locations.get(flowchart.get(index).connection.getBoxNumber() - 1).y + (GeneralSettings.FLOWCHART_PAD_TOP / 3))));
                    }
                    coordinates.add(new Vector2f((locations.get(index).x) + 2 * sizes.get(flowchart.get(index).connection.getBoxNumber() - 1).x / 3, (sizes.get(flowchart.get(index).connection.getBoxNumber() - 1).y + locations.get(flowchart.get(index).connection.getBoxNumber() - 1).y + (GeneralSettings.FLOWCHART_PAD_TOP / 3))));
                    coordinates.add(new Vector2f((locations.get(index).x) + 2 * sizes.get(flowchart.get(index).connection.getBoxNumber() - 1).x / 3, (sizes.get(flowchart.get(index).connection.getBoxNumber() - 1).y + locations.get(flowchart.get(index).connection.getBoxNumber() - 1).y)));
                    Terminator terminator;
                    if (coordinates.get(coordinates.size() - 1).y < coordinates.get((coordinates.size() - 2)).y) {
                        terminator = new ArrowHead(coordinates.get(coordinates.size() - 1), false);
                    } else {
                        terminator = new ArrowHead(coordinates.get(coordinates.size() - 1), true);
                    }
                    FlowchartLine line = new FlowchartLine(coordinates, terminator);
                    line.setColor(rainbow[jump_lines % rainbow.length]);
                    linesList.add(line);

                    jump_lines++;
                }
            }
        }

        if (verbose) System.out.println("Lines added: " + linesList.size());

        // get y_bound coordinate:
        if(locations.size() > 0) {
            y_bound = locations.get(locations.size() - 1).y;
        }  else {
            y_bound = 0;
        }

        if (verbose)
            System.out.println("(" + (Math.abs(x_bound) + 1f) + ", " + (Math.abs(y_bound) + 1f + GeneralSettings.FLOWCHART_PAD_TOP) + ")");
        GeneralSettings.IMAGE_SIZE = new Vector2f(Math.abs(x_bound) + 1f, Math.abs(y_bound) + 1f + GeneralSettings.FLOWCHART_PAD_TOP);
        Matrix3f translation = new Matrix3f();
        translation.setIdentity();
        translation.m00 = 2 / GeneralSettings.IMAGE_SIZE.x;
        translation.m11 = 2 / GeneralSettings.IMAGE_SIZE.y;
        translation.m20 = -.2f;
        translation.m21 = -(y_bound * translation.m11) - .875f;
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
        return flowchartWindowController;
    }
}