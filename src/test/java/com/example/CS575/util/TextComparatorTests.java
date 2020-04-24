package com.example.CS575.util;

import javafx.util.Pair;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import static com.example.CS575.util.Preprocessing.*;

public class TextComparatorTests {

    private ArrayList<Line> getInput(int index) {
        ArrayList<Line> res = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("src/main/resources/samples/sample" + index + ".c")));
            String s = null;
            int i = 1;
            while((s = br.readLine()) != null)
                res.add(new Line(i++, s + System.lineSeparator()));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    private void printOutput(ArrayList<String> output) {
        for(String o: output) {
            System.out.println(o);
        }
    }

    @Test
    public void compareByLCSTest() {
        int minLen = 8;
        ArrayList<Line> textA = preprocess(getInput(1), minLen, true, true);
        ArrayList<Line> textB = preprocess(getInput(2), minLen, true, true);

        Result result = TextComparator.compare(textA, textB, "LCS", 0.9);

        System.out.println("similarity: " + result.getSimilarity());

        System.out.println("matched lines: ");
        ArrayList<LineMatch> matchedLines = result.getMatchedLine();
        for(int i = 0; i < matchedLines.size(); ++i) {
            LineMatch m = matchedLines.get(i);
            System.out.println("line " + m.lineA + " -> line " + m.lineB + ", " + m.value);
        }

    }

    @Test
    public void compareByLevinsteinTest() {
        int minLen = 4;
        ArrayList<Line> textA = preprocess(getInput(1), minLen, true, true);
        ArrayList<Line> textB = preprocess(getInput(2), minLen, true, true);

        Result result = TextComparator.compare(textA, textB, "Levinstein", 5.0);

        System.out.println("similarity: " + result.getSimilarity());

        System.out.println("matched lines: ");
        ArrayList<LineMatch> matchedLines = result.getMatchedLine();
        for(int i = 0; i < matchedLines.size(); ++i) {
            LineMatch m = matchedLines.get(i);
            System.out.println("line " + m.lineA + " -> line " + m.lineB + ", " + m.value);
        }

    }



    @Test
    public void getLCSTest() {
        String A = "hello";
        String B = "aheo";

        int LCS = TextComparator.getLCS(A, B);
        System.out.println("The LCS between '" + A + "' and '" + B + "' is " + LCS);
        assert(LCS == 3);
    }

    @Test
    public void getLevinsteinDistanceTest() {
        String A = "ros";
        String B = "horse";

        int distance = TextComparator.getLevinsteinDistance(A, B);
        System.out.println("The levinstein distance between '" + A + "' and '" + B + "' is " + distance);
        assert(distance == 3);
    }
}
