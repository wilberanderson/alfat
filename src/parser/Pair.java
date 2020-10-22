package parser;



/*
* A simple mapping object used with the Syntax class
* to read in the json and store it's values
*
* */
public class Pair {

    public String name;
    public int parameters;

    Pair() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParameters() {
        return parameters;
    }

    public void setParameters(int parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "name='" + name + '\'' +
                ", parameters=" + parameters +
                '}';
    }
}
