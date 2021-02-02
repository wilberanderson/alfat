package parser;

/**
 * A simple mapping object used with the LC3Syntax class
 * store mapped json parameters.
 * @see parser.LC3Syntax
*/
public class Pair {
    //A string to hold the name of the LC3 instruction
    public String name;
    //A int to note the amount of additional parameters associated with the instruction
    public int parameters;

    //A do nothing constructor
    public Pair() {
    }
    /**
     * Returns a string literal
     * @return string
     * */
    public String getName() {
        return name;
    }
    /**
     *Sets a name
     * */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns a int
     * @return int
     * */
    public int getParameters() {
        return parameters;
    }

    /**
     * Sets the int of parameters
     * */
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
