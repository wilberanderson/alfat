package parser;
import gui.FlowChartWindow;
import gui.FlowchartLine;
import gui.terminators.ArrowHead;
import gui.terminators.Junction;
import gui.terminators.Terminator;
import gui.textBoxes.FlowChartTextBox;
import main.GeneralSettings;
import org.lwjgl.system.CallbackI;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.lang.reflect.Array;
import java.util.*;
import java.io.*;
import java.util.concurrent.Flow;

public class LC3Parser implements CodeReader {
    // attributes
    String infile;  // file path
    boolean verbose; // final release should have this changed to false
    ArrayList<FlowChartObject> flowchart = new ArrayList<>();

    HashMap<String, Integer> labelMap = new HashMap<>(); // map of labels -> line numbers
    List<LC3TLine> lines = new ArrayList<>();

    public LC3Parser(String infile, boolean verbose) {
        this.infile = infile;
        this.verbose = verbose;
    }

    //TODO: change from hardcoded to dynamically loaded from JSON
    JsonReader jr = new JsonReader(new File(GeneralSettings.SYNTAX_PATH));

    LC3Syntax syn = jr.mapJsonLC3Syntax();

    //normal commands or reserved operations:
    //String[] commands = {"ADD","AND","LD","LDI","LDR","LEA","NOT","RET","RTI","ST","STI","STR","TRAP",".ORIG",".FILL",".BLKW",".STRING",".END","PUTS","GETC","OUT","HALT"};

    String[] commands = syn.getCommands();

    //commands that can result in a jump to a new code block
    //String[] jumps = {"JMP","JSR","JSRR"};
    String[] jumps = syn.getJumps();

    /**Read an input file. Parse the input file line by line, and store them in the arrayList of LC3TLine objects.
     *  Create a hashmap of all labels and their line numbers, to make searching easier later.
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
                boolean first = true;
                //parse line:
                i++;    //line numbers start at 1
                if (verbose) System.out.println("\nparsing line #" + i + "`" + line + "`");
                line = line.replace("\t","    ");

                //take entire line before semicolon
                int index = line.indexOf(";");
                if (index == -1) index = line.length();
                String[] arrLine = line.substring(0, index).split(" ");

                //temp variables:
                Optional<String> comm = Optional.empty();
                String label = "";
                String targetLabel = "";
                List<String> registers = new ArrayList<>();
                boolean jump = false;

                //  arrLine = {"LABEL:", "JGZ", "R1", "R2", "FINISH"}
                for (String fragment : arrLine) {
                    if (verbose) System.out.print("["+fragment+"]");

                    //grab each command in the line, if they exist:
                    if (Arrays.asList(commands).contains(fragment.toUpperCase())) {
                        comm = Arrays.stream(commands).filter(fragment.toUpperCase()::equals).findAny();
                        first = false;
                    } else if (Arrays.asList(jumps).contains(fragment.toUpperCase()) || fragment.matches("^BR[nzp]{0,3}$")) {
                        comm = Optional.of(fragment);
                        jump = true;
                        first = false;
                    } else if (fragment.matches("^R[0-9](,)?")) {  //register
                        if (fragment.contains(",")){
                            if (!registers.contains(fragment.substring(0,fragment.length()-1))) {
                                registers.add(fragment.substring(0, fragment.length() - 1));
                            }
                        } else {
                            if (!registers.contains(fragment)) {
                                registers.add(fragment);
                            }
                        }
                        first = false;
                    } else if (fragment.matches("^[x#]-?[0-9]+")) {
                        //immediate value, literal or trap
                        //just skip this for now
                        first = false;
                    } else if (jump && fragment.matches("^[a-zA-Z0-9\\-_]+")) {   //jump statement, this matches a label
                        //if the line is a jump statement,
                        //this matches the label or labels pointed to by the command
                        //if the language supports having the label BEFORE the command,
                        //remove the `jump &&` statement as it will cause problems.
                        targetLabel = fragment;
                        first = false;
                    } else if (first && fragment.matches("^[a-zA-Z0-9\\-_]+")) {
                        //this is the (optional) label for the line
                        label = fragment;
                        labelMap.put(label, i);
                        first = false;
                    } /*else if (!jump && fragment.matches("^[a-zA-Z0-9\\-_]+")) {
                        //the command isn't a jump statement, so the label must be a variable i.e. string, etc.
                    }*/
                }

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
                lines.add(new LC3TLine(line, comm, label, targetLabel, jump, registers, i));
            }
        }
        catch (FileNotFoundException e) {
            if (verbose) System.out.println("File not found");
            e.printStackTrace();
        }
        catch (IOException e) {
            if (verbose) System.out.println("IO error occured");
            e.printStackTrace();
        }
    }

    /** Parse a single line and insert it into the list at index <i>i</i>.
     *
     * @param line The string of the line to insert
     * @param i The index of the line to insert
     */
    public void parseLine(String line, int i){
        if (verbose) System.out.println("parsing `" + line + "`");

        //take entire line before semicolon
        int index = line.indexOf(";");
        if (index == -1) index = line.length();
        String[] arrLine = line.substring(0, index).split(" ");

        //temp variables:
        Optional<String> comm = Optional.empty();
        String label = "";
        String targetLabel = "";
        List<String> registers = new ArrayList<>();
        boolean jump = false;
        boolean first = true;

        //  arrLine = {"LABEL:", "JGZ", "R1", "R2", "FINISH"}
        for (String fragment : arrLine) {
            //grab each command in the line, if they exist:
            if (Arrays.stream(commands).anyMatch(fragment.toUpperCase()::contains)) {
                comm = Arrays.stream(commands).filter(fragment.toUpperCase()::contains).findAny();
                first = false;
            } else if (Arrays.stream(jumps).anyMatch(fragment.toUpperCase()::contains) || fragment.matches("^BR[nzp]{0,3}$")) {
                comm = Arrays.stream(jumps).filter(fragment.toUpperCase()::contains).findAny();
                jump = true;
                first = false;
            } else if (fragment.matches("^R[0-7]")) {  //register
                registers.add(fragment);
                first = false;
            } else if (fragment.matches("^[x#]-?[0-9]+")) {
                //immediate value, literal or trap
                //just skip this for now
                first = false;
            } else if (first && fragment.matches("^[a-zA-Z0-9\\-_]+")) {
                //this is the (optional) label for the line
                label = fragment;
                labelMap.put(label, i);
                first = false;
            } else if (jump && fragment.matches("^[a-zA-Z0-9\\-_]+")) {   //jump statement, this matches a label
                //if the line is a jump statement,
                //this matches the label or labels pointed to by the command
                //if the language supports having the label BEFORE the command,
                //remove the `jump &&` statement as it will cause problems.
                targetLabel = fragment;
                first = false;
            } else if (!jump && fragment.matches("^[a-zA-Z0-9\\-_]+")) {
                //the command isn't a jump statement, so the label must be a variable i.e. string, etc.
            }
        }

        //log testing data, if verbose parsing is on:
        if (verbose) {
            System.out.println("Parsed line " + i);
            comm.ifPresent(s -> System.out.println(" " + s));
            if (!label.isEmpty()) System.out.println(" line label: " + label);
            if (jump) System.out.println(" jump targets: " + targetLabel);
            System.out.println(" registers used: " + registers);
        }

        // call constructor for TLine, then add the new object to the arraylist for the file
        // if the new value goes in the middle of the list, put it there:
        if (i < lines.size()){
            lines.add(i-1, new LC3TLine(line, comm, label, targetLabel, jump, registers, i));

            //renumber all proceeding objects:
            for (int j = i;j<lines.size();j++){
                lines.get(j).incrementLineNumber(1);
            }
        } else {
            // just append the new item to the end
            lines.add(new LC3TLine(line, comm, label, targetLabel, jump, registers, i));
        }
    }

    /** Create flowchart objects. <b>Only</b> call after the file has been parsed.
     *
     */
    @Override
    public void getFlowObjects() {
        //generate naive boxes for flowchart.
        if (verbose) System.out.println("\n\nBeginning flowchart parsing:");
        flowchart.add(new FlowChartObject());
        flowchart.get(flowchart.size() - 1).setBoxNumber(flowchart.size()-1);

        for (LC3TLine line : lines){
            if (!line.getLineText(true).isEmpty()) {
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
                    flowchart.get(flowchart.size() - 1).setStartLine(line.getLineNumber());
                    flowchart.get(flowchart.size() - 1).setLabel(line.getLabel());
                }
                // Add the line to the current box.
                flowchart.get(flowchart.size() - 1).addLine(line);
                flowchart.get(flowchart.size() - 1).lineCount++;
                //if the line is a jump/branch, end the box and start a new one.
                if (line.isJumps()) {
                    if (verbose) System.out.println("Line jumps, box ended.");
                    flowchart.get(flowchart.size() - 1).jumps = true;
                    flowchart.get(flowchart.size() - 1).target = line.getTarget();

                    flowchart.add(new FlowChartObject());
                    flowchart.get(flowchart.size() - 1).setStartLine(line.getLineNumber());
                }
            }
        }

        //remove all empty boxes
        flowchart.removeIf(box -> box.getLineCount() == 0);

        //create linkages
        int i = 1;
        for (FlowChartObject box : flowchart){
            box.setBoxNumber(i);
            // If the box jumps, find where it targets and link them.
            if (box.isJumps()){
                if (verbose) System.out.println("Creating linkage for box " + box.label + " targeting " + box.target);
                for (FlowChartObject candidate : flowchart){
                    if (verbose) System.out.println(" -> Checking against box " + box.label);
                    if (candidate.label.equals(box.target)){
                        if (verbose) System.out.println("✓ Match found");
                        box.connection = candidate;
                        break;
                    }
                }
                if (box.connection == null){
                    box.jumps = false;
                    box.alert += "BRANCH DOES NOT EXIST";
                }
            }
            i++;
        }

        if (verbose){
            int n = 0;
            for (FlowChartObject box : flowchart){
                n++;
                System.out.println("┌─ "+ box.getBoxNumber() + "\t──────────────────────────────────────────────────────────────────────────┐");
                System.out.println(box.getFullText(true));
                if (box.isJumps()) System.out.println("┌╼ Target label: " +box.connection.label + " @ box " + box.connection.getBoxNumber());
                else if (!box.alert.isEmpty()) System.out.println("┌╼ " + box.alert);
                System.out.println("└────────────────────────────────────────────────────────────────────────────────┘");
            }
        }
    }

    /** Create flowchart.
     * Creates flowchart boxes and draws lines.
     *
     */
    public void createFlowchart(){
        for(FlowChartTextBox textBox : FlowChartWindow.getFlowChartTextBoxList()){
            textBox.clear();
        }

        int i = 0;
        Vector2f location = new Vector2f(GeneralSettings.FLOWCHART_PAD_LEFT,1);
        List<FlowChartTextBox> textBoxes = new ArrayList<>();
        float max_right_width = -1000f;
        List<Vector2f> locations = new ArrayList<>();
        List<Vector2f> sizes = new ArrayList<>();

        //draw the boxes vertically offset
        for (FlowChartObject box : flowchart){
            if (verbose){
                System.out.println("Box " + i + " @ " + location);
                System.out.println("Starting @ line #" + box.getStartLine());
            }
            FlowChartTextBox textBox = new FlowChartTextBox(new Vector2f(location), box.getFullText(true), box.getStartLine()+1);
            textBoxes.add(textBox);
            textBox.setPosition(new Vector2f(textBox.getPosition().x, textBox.getPosition().y-textBox.getSize().y));
            if (verbose) System.out.println("Position: " + textBox.getPosition() + " Size: " + textBox.getSize());
            location.y = (location.y - textBox.getSize().y - GeneralSettings.FLOWCHART_PAD_TOP);
            i++;

            // Line helpers:
            max_right_width = Math.max(max_right_width,textBox.getSize().x);
            locations.add(textBox.getPosition());
            sizes.add(textBox.getSize());
        }
        // Pass flowchart boxes out:
        FlowChartWindow.setFlowChartTextBoxList(textBoxes);

        // Draw lines:
        List<FlowchartLine> linesList = new ArrayList<>();
        int jump_lines = 0;

        List<Integer> coverage = new ArrayList<Integer>(Collections.nCopies(flowchart.size()*2, 0));

        Vector3f rainbow[] = {new Vector3f(0.862745f, 0.196078f, 0.184313f),new Vector3f(0.796078f, 0.294117f, 0.086274f),new Vector3f(0.709803f, 0.537254f, 0),
                new Vector3f(0.521568f, .6f, 0),new Vector3f(0.164705f, 0.631372f, 0.596078f),
                new Vector3f(0.149019f, 0.545098f, 0.823529f),new Vector3f(0.423529f, 0.443137f, 0.768627f),
                new Vector3f(0.82745f, 0.211764f, 0.509803f)};

        for (int index = 0; index <= flowchart.size(); index++){
            // Draw line to next box (not in the last box)
            if (index < flowchart.size()-1){
                /*if (verbose) {
                    System.out.println("Adding vertical line from box " + index + " to box " + (index + 1));
                    System.out.println(locations.get(index) + " -> " + locations.get(index+1));
                }*/
                List<Vector2f> coordinates = new ArrayList<>();
                //first point: bottom of current box:
                coordinates.add(new Vector2f((- 1 + locations.get(index).x) + .05f, (-1 + locations.get(index).y)));
                //second point: top of next box:
                coordinates.add(new Vector2f((- 1 + locations.get(index).x) + .05f, (-1 + locations.get(index+1).y + sizes.get(index+1).y)));
                //if (verbose) System.out.println("from " + (locations.get(index).y) + " to " + (-1 + locations.get(index+1).y + sizes.get(index+1).y) + "\n");
                Terminator terminator;
                if(coordinates.get(coordinates.size()-1).y < coordinates.get((coordinates.size()-2)).y){
                    terminator = new ArrowHead(coordinates.get(coordinates.size()-1), false);
                    terminator = new Junction(coordinates.get(coordinates.size()-1));
                }else{
                    terminator = new ArrowHead(coordinates.get(coordinates.size()-1), true);
                    terminator = new Junction(coordinates.get(coordinates.size()-1));
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
                        System.out.println(Math.min(index,flowchart.get(index).connection.getBoxNumber()) + " -> " + Math.max(index,flowchart.get(index).connection.getBoxNumber()));
                    }

                    // Mark the nodes that have been covered
                    if (index == flowchart.get(index).connection.getBoxNumber() + 1){
                        coverage.set(index, coverage.get(index)+1);
                    } else {
                        for (int k = Math.min(index, flowchart.get(index).connection.getBoxNumber()); k < Math.max(index, flowchart.get(index).connection.getBoxNumber()); k++) {
                            coverage.set(k,coverage.get(k)+1);
                        }
                    }
                    if (verbose) System.out.println("coverage " + coverage);

                    List<Vector2f> coordinates = new ArrayList<>();

                    coordinates.add(new Vector2f((-1 + locations.get(index).x) + 2 * sizes.get(index).x / 3, (-1 + locations.get(index).y)));
                    coordinates.add(new Vector2f((-1 + locations.get(index).x) + 2 * sizes.get(index).x / 3, (-1 + locations.get(index).y) - (GeneralSettings.FLOWCHART_PAD_TOP / 3)));

                    float temp = max_right_width + GeneralSettings.FLOWCHART_PAD_LEFT;

                    if (index == flowchart.get(index).connection.getBoxNumber() - 1){
                        temp = sizes.get(index).x + GeneralSettings.FLOWCHART_PAD_LEFT;
                    } else if (index < flowchart.get(index).connection.getBoxNumber()){
                        temp = -1f;
                        for (Vector2f item : sizes.subList(index + 1,flowchart.get(index).connection.getBoxNumber()-1)){
                            if (item.x + GeneralSettings.FLOWCHART_PAD_LEFT - 1f> temp)
                                temp = item.x + GeneralSettings.FLOWCHART_PAD_LEFT - 1f;
                        }
                    } else {
                        temp = -1f;
                        for (Vector2f item : sizes.subList(flowchart.get(index).connection.getBoxNumber()-1,index)){
                            if (item.x + GeneralSettings.FLOWCHART_PAD_LEFT - 1f > temp)
                                temp = item.x + GeneralSettings.FLOWCHART_PAD_LEFT - 1f;
                        }
                    }

                    if (index == flowchart.get(index).connection.getBoxNumber()){
                        coordinates.add(new Vector2f((temp + coverage.get(index) * GeneralSettings.LINE_OFFSET), (-1 + locations.get(index).y) - (GeneralSettings.FLOWCHART_PAD_TOP / 3)));
                        coordinates.add(new Vector2f((temp + coverage.get(index) * GeneralSettings.LINE_OFFSET), (-1 + sizes.get(flowchart.get(index).connection.getBoxNumber() - 1).y + locations.get(flowchart.get(index).connection.getBoxNumber() - 1).y + (GeneralSettings.FLOWCHART_PAD_TOP / 3))));
                    } else {
                        coordinates.add(new Vector2f((temp + Collections.max(coverage.subList(Math.min(index, flowchart.get(index).connection.getBoxNumber()), Math.max(index, flowchart.get(index).connection.getBoxNumber()))) * GeneralSettings.LINE_OFFSET), (-1 + locations.get(index).y) - (GeneralSettings.FLOWCHART_PAD_TOP / 3)));
                        coordinates.add(new Vector2f((temp + Collections.max(coverage.subList(Math.min(index, flowchart.get(index).connection.getBoxNumber()), Math.max(index, flowchart.get(index).connection.getBoxNumber()))) * GeneralSettings.LINE_OFFSET), (-1 + sizes.get(flowchart.get(index).connection.getBoxNumber() - 1).y + locations.get(flowchart.get(index).connection.getBoxNumber() - 1).y + (GeneralSettings.FLOWCHART_PAD_TOP / 3))));
                    }
                    coordinates.add(new Vector2f((-1 + locations.get(index).x) + 2 * sizes.get(flowchart.get(index).connection.getBoxNumber() - 1).x / 3, (-1 + sizes.get(flowchart.get(index).connection.getBoxNumber() - 1).y + locations.get(flowchart.get(index).connection.getBoxNumber() - 1).y + (GeneralSettings.FLOWCHART_PAD_TOP / 3))));
                    coordinates.add(new Vector2f((-1 + locations.get(index).x) + 2 * sizes.get(flowchart.get(index).connection.getBoxNumber() - 1).x / 3, (-1 + sizes.get(flowchart.get(index).connection.getBoxNumber() - 1).y + locations.get(flowchart.get(index).connection.getBoxNumber() - 1).y)));
                    Terminator terminator;
                    if(coordinates.get(coordinates.size()-1).y < coordinates.get((coordinates.size()-2)).y){
                        terminator = new ArrowHead(coordinates.get(coordinates.size()-1), false);
                        terminator = new Junction(coordinates.get(coordinates.size()-1));
                    }else{
                        terminator = new ArrowHead(coordinates.get(coordinates.size()-1), true);
                        terminator = new Junction(coordinates.get(coordinates.size()-1));
                    }
                    FlowchartLine line = new FlowchartLine(coordinates, terminator);
                    line.setColor(rainbow[jump_lines % rainbow.length]);
                    linesList.add(line);

                    jump_lines++;
                }
            }
        }

        System.out.println("Lines added: " + linesList.size());
        System.out.println("width " + max_right_width + GeneralSettings.FLOWCHART_PAD_LEFT);
        FlowChartWindow.setFlowchartLineList(linesList);

    }
}
