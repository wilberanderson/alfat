package parser.JsonObjects;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.ArrayList;

public class InnerBooleanArray {
    public ArrayList<Boolean> inner;

    @JsonCreator
    public InnerBooleanArray(final ArrayList<Boolean> inner) {
        this.inner = inner;
    }

    @Override
    public String toString() {
        return "InnerBooleanArray{" +
                "inner=" + inner +
                '}';
    }
}
