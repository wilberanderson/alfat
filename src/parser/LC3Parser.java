package parser;
import java.util.*;
import java.io.*;
import java.util.concurrent.Flow;

public class LC3Parser implements CodeReader {
    // attributes
    String infile;  // file path
    boolean verbose = true; // final release should have this changed to false
    ArrayList<FlowChartObject> flowchart = new ArrayList<>();

    HashMap<String, Integer> labelMap = new HashMap<>(); // map of labels -> line numbers
    List<LC3TLine> lines = new ArrayList<>();

    public LC3Parser(String infile, boolean verbose) {
        this.infile = infile;
        this.verbose = verbose;
    }

    //TODO: change from hardcoded to dynamically loaded from JSON

    //normal commands or reserved operations:
    String[] commands = {"ADD","AND","LD","LDI","LDR","LEA","NOT","RET","RTI","ST","STI","STR","TRAP",".ORIG",".FILL",".BLKW",".STRING",".END","PUTS","GETC","OUT","HALT"};

    //commands that can result in a jump to a new code block
    String[] jumps = {"JMP","JSR","JSRR"};


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
                    } else if (!jump && fragment.matches("^[a-zA-Z0-9\\-_]+")) {
                        //the command isn't a jump statement, so the label must be a variable i.e. string, etc.
                    }
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

        //  arrLine = {"LABEL:", "JGZ", "R1", "R2", "FINISH"}
        for (String fragment : arrLine) {
            //grab each command in the line, if they exist:
            if (Arrays.stream(commands).anyMatch(fragment.toUpperCase()::contains)) {
                comm = Arrays.stream(commands).filter(fragment.toUpperCase()::contains).findAny();
            } else if (Arrays.stream(jumps).anyMatch(fragment.toUpperCase()::contains) || fragment.matches("^BR[nzp]{0,3}$")) {
                comm = Arrays.stream(jumps).filter(fragment.toUpperCase()::contains).findAny();
                jump = true;
            } else if (fragment.matches("^R[0-7]")) {  //register
                registers.add(fragment);
            } else if (fragment.matches("^[x#]-?[0-9]+")) {
                //immediate value, literal or trap
                //just skip this for now
            } else if (fragment.matches("^[a-zA-Z0-9\\-_]+")) {
                //this is the (optional) label for the line
                label = fragment;
                labelMap.put(label, i);
            } else if (jump && fragment.matches("^[a-zA-Z0-9\\-_]+")) {   //jump statement, this matches a label
                //if the line is a jump statement,
                //this matches the label or labels pointed to by the command
                //if the language supports having the label BEFORE the command,
                //remove the `jump &&` statement as it will cause problems.
                targetLabel = fragment;
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
                }
            }
        }

        //remove all empty boxes
        flowchart.removeIf(box -> box.getLineCount() == 0);

        //create linkages
        for (FlowChartObject box : flowchart){
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
        }

        if (verbose){
            int n = 0;
            for (FlowChartObject box : flowchart){
                n++;
                System.out.println("┌────────────────────────────────────────────────────────────────────────────────┐");
                System.out.println(box.getFullText(true));
                if (box.isJumps()) System.out.println("┌╼ Target label: " +box.connection.label);
                else if (!box.alert.isEmpty()) System.out.println("┌╼ " + box.alert);
                System.out.println("└────────────────────────────────────────────────────────────────────────────────┘");
            }
        }
    }
}
