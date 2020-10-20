package parser;

public class LC3Parser implements CodeReader {

    String infile;

    LC3Parser(String infile) {
        this.infile = infile;
    }

    @Override
    public void ReadFile(String infile) {
        this.infile = infile;
    }

    @Override
    public void getFlowObjects() {

    }


}
