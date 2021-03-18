package parser.JsonObjects;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.ArrayList;

public class InnerIntArray {
    public ArrayList<Integer> inner;

    @JsonCreator
    public InnerIntArray(final ArrayList<Integer> inner) {
        this.inner = inner;
    }

    @Override
    public String toString() {
        return "InnerIntArray{" +
                "inner=" + inner +
                '}';
    }
}
