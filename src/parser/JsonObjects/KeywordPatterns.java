package parser.JsonObjects;

public class KeywordPatterns {
    private String procedurestart;
    private String procedureend;
    private String control;
    private String reserved;
    private String arithmetic;
    private String dataMovement;
    private String register;
    private String commentLine;
    private String constantHex;
    private String constantBinary;
    private String constantNumeric;
    private String constantCharacter;
    private String doubleQuotedString;
    private String emptySpace;
    private String label;
    private String comment;
    private String separator;


    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    public String getArithmetic() {
        return arithmetic;
    }

    public void setArithmetic(String arithmetic) {
        this.arithmetic = arithmetic;
    }

    public String getDataMovement() {
        return dataMovement;
    }

    public void setDataMovement(String dataMovement) {
        this.dataMovement = dataMovement;
    }

    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
        this.register = register;
    }

    public String getCommentLine() {
        return commentLine;
    }

    public void setCommentLine(String commentLine) {
        this.commentLine = commentLine;
    }

    public String getConstantHex() {
        return constantHex;
    }

    public void setConstantHex(String constantHex) {
        this.constantHex = constantHex;
    }

    public String getConstantNumeric() {
        return constantNumeric;
    }

    public void setConstantNumeric(String constantNumeric) {
        this.constantNumeric = constantNumeric;
    }

    public String getConstantCharacter() {
        return constantCharacter;
    }

    public void setConstantCharacter(String constantCharacter) {
        this.constantCharacter = constantCharacter;
    }

    public String getDoubleQuotedString() {
        return doubleQuotedString;
    }

    public void setDoubleQuotedString(String doubleQuotedString) {
        this.doubleQuotedString = doubleQuotedString;
    }

    public String getEmptySpace() {
        return emptySpace;
    }

    public void setEmptySpace(String emptySpace) {
        this.emptySpace = emptySpace;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getConstantBinary() {
        return constantBinary;
    }

    public void setConstantBinary(String constantBinary) {
        this.constantBinary = constantBinary;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getProcedurestart() {
        return procedurestart;
    }

    public void setProcedurestart(String procedurestart) {
        this.procedurestart = procedurestart;
    }

    public String getProcedureend() {
        return procedureend;
    }

    public void setProcedureend(String procedureend) {
        this.procedureend = procedureend;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    @Override
    public String toString() {
        return "KeywordPatterns{" +
                "procedurestart='" + procedurestart + '\'' +
                ", procedureend='" + procedureend + '\'' +
                ", control='" + control + '\'' +
                ", reserved='" + reserved + '\'' +
                ", arithmetic='" + arithmetic + '\'' +
                ", dataMovement='" + dataMovement + '\'' +
                ", register='" + register + '\'' +
                ", commentLine='" + commentLine + '\'' +
                ", constantHex='" + constantHex + '\'' +
                ", constantBinary='" + constantBinary + '\'' +
                ", constantNumeric='" + constantNumeric + '\'' +
                ", constantCharacter='" + constantCharacter + '\'' +
                ", doubleQuotedString='" + doubleQuotedString + '\'' +
                ", emptySpace='" + emptySpace + '\'' +
                ", label='" + label + '\'' +
                ", comment='" + comment + '\'' +
                ", separator='" + separator + '\'' +
                '}';
    }
}
