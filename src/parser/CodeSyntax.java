package parser;

import java.util.ArrayList;

public class CodeSyntax {


    private ArrayList<Single> operation = new ArrayList<Single>();
    private ArrayList<Single> dataMovement = new ArrayList<Single>();
    private ArrayList<Single> control = new ArrayList<Single>();
    private ArrayList<Single> registers = new ArrayList<Single>();
    private ArrayList<Single> reserved = new ArrayList<Single>();


    public ArrayList<Single> getReserved() { return reserved; }

    public void setReserved(ArrayList<Single> reserved) { this.reserved = reserved; }


    public ArrayList<Single> getOperation() {
        return operation;
    }

    public void setOperation(ArrayList<Single> operation) {
        this.operation = operation;
    }

    public ArrayList<Single> getDataMovement() {
        return dataMovement;
    }

    public void setDataMovement(ArrayList<Single> dataMovement) {
        this.dataMovement = dataMovement;
    }

    public ArrayList<Single> getControl() {
        return control;
    }

    public void setControl(ArrayList<Single> control) {
        this.control = control;
    }

    public ArrayList<Single> getRegisters() {
        return registers;
    }

    public void setRegisters(ArrayList<Single> registers) {
        this.registers = registers;
    }

    public String[] getCommands() {
        int lastIn = 0;

        String[] commands = new String[countCommands()];


        for(int i = 0; i < operation.size(); i++){
            commands[lastIn] = operation.get(i).name;
            lastIn++;
        }

        for(int i = 0; i < dataMovement.size(); i++){
            commands[lastIn] = dataMovement.get(i).name;
            lastIn++;
        }


        for(int i = 0; i < reserved.size(); i++){
            commands[lastIn] = reserved.get(i).name;
            lastIn++;
        }

        return commands;
    }


    public String[] getJumps() {
        String[] jumps = new String[control.size()];

        for(int i = 0; i < control.size(); i++) {
            jumps[i] = control.get(i).name;
        }

        return jumps;
    }

    public String[] getRegisterNames(){
        String[] regs = new String[registers.size()];

        for(int i = 0; i < registers.size(); i++) {
            regs[i] = registers.get(i).name;
        }

        return regs;
    }

    private int countCommands() {
        int cmdCount = 0;

        cmdCount += operation.size();
        cmdCount += dataMovement.size();
        cmdCount += reserved.size();
        return cmdCount;
    }


    @Override
    public String toString() {
        return "CodeSyntax{" +
                "operation=" + operation +
                ", dataMovemnet=" + dataMovement +
                ", control=" + control +
                ",reserved=" + reserved +
                ", registers=" + registers +
                '}';
    }
}
