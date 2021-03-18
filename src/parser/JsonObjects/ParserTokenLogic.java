package parser.JsonObjects;

public class ParserTokenLogic {

    OuterIntStrArray command;
    OuterIntStrArray control;
    OuterIntStrArray register;
    OuterIntStrArray immediate;
    OuterIntStrArray label;
    OuterIntStrArray procedurestart;
    OuterIntStrArray procedureend;
    OuterIntStrArray userdefined;
    OuterIntStrArray comment;
    OuterIntStrArray separator;


    public OuterIntStrArray getCommand() {
        return command;
    }

    public void setCommand(OuterIntStrArray command) {
        this.command = command;
    }

    public OuterIntStrArray getControl() {
        return control;
    }

    public void setControl(OuterIntStrArray control) {
        this.control = control;
    }

    public OuterIntStrArray getRegister() {
        return register;
    }

    public void setRegister(OuterIntStrArray register) {
        this.register = register;
    }

    public OuterIntStrArray getImmediate() {
        return immediate;
    }

    public void setImmediate(OuterIntStrArray immediate) {
        this.immediate = immediate;
    }

    public OuterIntStrArray getLabel() {
        return label;
    }

    public void setLabel(OuterIntStrArray label) {
        this.label = label;
    }

    public OuterIntStrArray getProcedurestart() {
        return procedurestart;
    }

    public void setProcedurestart(OuterIntStrArray procedurestart) {
        this.procedurestart = procedurestart;
    }

    public OuterIntStrArray getProcedureend() {
        return procedureend;
    }

    public void setProcedureend(OuterIntStrArray procedureend) {
        this.procedureend = procedureend;
    }

    public OuterIntStrArray getUserdefined() {
        return userdefined;
    }

    public void setUserdefined(OuterIntStrArray userdefined) {
        this.userdefined = userdefined;
    }

    public OuterIntStrArray getComment() {
        return comment;
    }

    public void setComment(OuterIntStrArray comment) {
        this.comment = comment;
    }

    public OuterIntStrArray getSeparator() {
        return separator;
    }

    public void setSeparator(OuterIntStrArray separator) {
        this.separator = separator;
    }

    @Override
    public String toString() {
        return "ParserTokenLogic{" +
                "command=" + command +
                ", control=" + control +
                ", register=" + register +
                ", immediate=" + immediate +
                ", label=" + label +
                ", procedurestart=" + procedurestart +
                ", procedureend=" + procedureend +
                ", userdefined=" + userdefined +
                ", comment=" + comment +
                ", separator=" + separator +
                '}';
    }
}
