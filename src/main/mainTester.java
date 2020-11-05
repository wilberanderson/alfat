package main;

import parser.LC3Parser;

public class mainTester {
    public static void main(String[] args) {
        LC3Parser foo = new LC3Parser("tests/test3.txt", true);

        foo.ReadFile("tests/test3.txt");

        foo.getFlowObjects();

    }

}
