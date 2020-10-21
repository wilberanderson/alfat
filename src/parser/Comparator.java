package parser;

public interface Comparator {
    boolean isCommand(String text);
    boolean isComment(String text);
}
