package parser;

/**
 * A simple mapping object used with the CodeSyntax class
 * store mapped json parameters.
 * @see parser.CodeSyntax
 */

public class Single {
    public String name;
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

    @Override
    public String toString() {
        return "Single{" +
                "name='" + name + '\'' +
                '}';
    }
}
