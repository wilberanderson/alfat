package parser;
import java.util.*;

public class LC3Parser implements CodeReader {

    //infile is the path to a file.
    String infile;

    LC3Parser(String infile) {
        this.infile = infile;
    }

    //TODO: change from hardcoded to dynamically loaded from JSON
    //TODO: catch traps, which are in the form x2*

    //normal commands or reserved operations:
    String[] commands = {"ADD","AND","BR","LD","LDI","LDR","LEA","NOT","RET","RTI","ST","STI","STR","TRAP",".ORIG",".FILL",".BLKW",".STRING",".END","PUTS","GETC","OUT","HALT"};

    //commands that can result in a jump to a new code block
    String[] jumps = {"JMP","JSR","JSRR"};
//try {
//        File myObj = new File("D:\\Java\\JSONtest\\SelfNodes\\files\\input.asm");
//        Scanner myReader = new Scanner(myObj);
//        while (myReader.hasNextLine()) {
//            String data = myReader.nextLine();
//            System.out.println(data);
//        }
//        myReader.close();
//    } catch (FileNotFoundException e) {
//        System.out.println("An error occurred.");
//        e.printStackTrace();
//    }

    @Override
    public void ReadFile(String infile) {
        this.infile = infile;
        //sets file path.

        //open file, read into massive string
        String[] input = {".ORIG x3000",
                "  AND R0, R0, #0    ; Clear R0",
                "  AND R1, R1, #0    ; Clear R1",
                "  AND R3, R3, #0    ; Clear R3",
                "  LEA R0, NUM       ; pointer [mem]NUM",
                "  ADD R1, R1, R0    ; Store the pointer address of R0 into R1",
                "  LD R2, ASCII      ; load the ascii offset into R2",
                "",
                "FOR_LOOP",
                "  LDR R4, R1, #0    ; load the contents of mem address of R1 into R4",
                "  BRz END_LOOP",
                "  ADD R4, R4, R2    ; Add our number to the ASCII offset",
                "  STR R4, R1, #0    ; Store the new value in R4 into [mem] address R1",
                "  ADD R1, R1, #1    ; move our memory pointer down one",
                "  BRnzp FOR_LOOP    ; loop again until we get an x00 char",
                "END_LOOP",
                "",
                "  PUTs              ; print our string starting from [mem]address in R0",
                "HALT                ; Trap x25",
                "",
                "ASCII .fill  x30    ; Our ASCII offset",
                "NUM   .fill  x01    ; Our Number to print",
                "      .fill  x02     ",
                "      .fill  x03",
                "      .fill  x04",
                ".END"};

        //get number of lines.
        int lines = 26;
        for (int i = 1; i < lines+1; i++){
            //parse line:
            //System.out.println("parsing |" + i + "\t|: " + input[i]);

            //take entire line before semicolon
            int index = input[i].indexOf(";");
            if (index == -1) index = input[i].length();
            String[] arrLine = input[i].substring(0,index).split(" ");

            //temp variables:
            Optional<String> comm;
            String label;
            List<String> labelspointed = new ArrayList<>();
            List<String> registers= new ArrayList<>();
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
                } else if (fragment.matches("^[x#]?-?[0-9]+")) {
                    //immediate value, literal or trap
                    //just skip this for now
                } else if (fragment.matches("^[a-zA-Z0-9\\-_]+:")) {
                    //this is the (optional) label for the line
                    label = fragment;
                } else if (jump && fragment.matches("^[a-zA-Z0-9\\-_]+")) {   //jump statement, this matches a label
                    //if the line is a jump statement,
                    //this matches the label or labels pointed to by the command
                    //if the language supports having the label BEFORE the command,
                    //remove the `jump &&` statement as it will cause problems.
                    labelspointed.add(fragment);
                } else if (!jump && fragment.matches("^[a-zA-Z0-9\\-_]+")) {
                    //the command isn't a jump statement, so the label must be a variable i.e. string, etc.
                }
            }
            //call constructor for TLine, then add the new object to the arraylist for the file
            /*
            TLine object:
            line_text = "  AND R0, LABEL, #0    ; Clear R0"
            lineNumber = 1
            Command = "AND"
            Registers[] = "R0,R1";
            jump = true;
            label = null;
            target = LABEL;
            */
        }
    }

    @Override
    public void getFlowObjects() {

    }


}
