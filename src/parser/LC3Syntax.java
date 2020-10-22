package parser;

import java.util.ArrayList;

public class LC3Syntax {

    private ArrayList<Pair> operation = new ArrayList<Pair>();
    private ArrayList<Pair> dataMovemnet = new ArrayList<Pair>();
    private ArrayList<Pair> control = new ArrayList<Pair>();
    private ArrayList<Pair> registers = new ArrayList<Pair>();

    public ArrayList<Pair> getOperation() {
        return operation;
    }

    public void setOperation(ArrayList<Pair> operation) {
        this.operation = operation;
    }

    public ArrayList<Pair> getDataMovemnet() {
        return dataMovemnet;
    }

    public void setDataMovemnet(ArrayList<Pair> dataMovemnet) {
        this.dataMovemnet = dataMovemnet;
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


    @Override
    public String toString() {
        return "LC3Syntax{" +
                "operation=" + operation +
                ", dataMovemnet=" + dataMovemnet +
                ", control=" + control +
                ", registers=" + registers +
                '}';
    }
}
