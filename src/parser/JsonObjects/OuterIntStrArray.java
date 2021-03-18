package parser.JsonObjects;

public class OuterIntStrArray {

    InnerStrArray regexes;
    InnerIntArray columns;

    public InnerStrArray getRegexes() {
        return regexes;
    }

    public void setRegexes(InnerStrArray regexes) {
        this.regexes = regexes;
    }

    public InnerIntArray getColumns() {
        return columns;
    }

    public void setColumns(InnerIntArray columns) {
        this.columns = columns;
    }

    @Override
    public String toString() {
        return "OuterIntStrArray{" +
                "regexes=" + regexes +
                ", columns=" + columns +
                '}';
    }
}
