package parser.JsonObjects;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.ArrayList;

public class InnerStrArray {
    public ArrayList<String> inner;
    @JsonCreator
    public InnerStrArray(final ArrayList<String> inner) {
        this.inner = inner;
    }

    @Override
    public String toString() {
        return "InnerStrArray{" +
                "inner=" + inner +
                '}';
    }
}
