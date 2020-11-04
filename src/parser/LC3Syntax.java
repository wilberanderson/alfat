package parser;

import java.util.ArrayList;

public class LC3Syntax {

    private ArrayList<Pair> operation = new ArrayList<Pair>();
    private ArrayList<Pair> dataMovement = new ArrayList<Pair>();
    private ArrayList<Pair> control = new ArrayList<Pair>();
    private ArrayList<Pair> registers = new ArrayList<Pair>();
    private ArrayList<Pair> reserved = new ArrayList<Pair>();


    public ArrayList<Pair> getReserved() { return reserved; }

    public void setReserved(ArrayList<Pair> reserved) { this.reserved = reserved; }


    public ArrayList<Pair> getOperation() {
        return operation;
    }

    public void setOperation(ArrayList<Pair> operation) {
        this.operation = operation;
    }

    public ArrayList<Pair> getDataMovement() {
        return dataMovement;
    }

    public void setDataMovement(ArrayList<Pair> dataMovement) {
        this.dataMovement = dataMovement;
    }

    public ArrayList<Pair> getControl() {
        return control;
    }

    public void setControl(ArrayList<Pair> control) {
        this.control = control;
    }

    public ArrayList<Pair> getRegisters() {
        return registers;
    }

    public void setRegisters(ArrayList<Pair> registers) {
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


    private int countCommands() {
        int cmdCount = 0;

        cmdCount += operation.size();
        cmdCount += dataMovement.size();
        cmdCount += reserved.size();
        return cmdCount;
    }


    @Override
    public String toString() {
        return "LC3Syntax{" +
                "operation=" + operation +
                ", dataMovemnet=" + dataMovement +
                ", control=" + control +
                ",reserved=" + reserved +
                ", registers=" + registers +
                '}';
    }
}
