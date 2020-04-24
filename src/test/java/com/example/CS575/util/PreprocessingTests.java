package com.example.CS575.util;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import static com.example.CS575.util.Preprocessing.*;

public class PreprocessingTests {

    private ArrayList<Line> getInput() {
        ArrayList<Line> res = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("src/main/resources/samples/test.txt")));
            String s = null;
            int i = 1;
            while((s = br.readLine()) != null)
                res.add(new Line(i++, s + System.lineSeparator()));


        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    private void printOutput(ArrayList<Line> output) {
        for(Line o: output) {
            System.out.println("#" + o.lineNO + " " + o.content);
        }
    }

    @Test
    public void removeCommentTest() {
        ArrayList<Line> input = getInput();
        ArrayList<Line> output = removeComments(input);
        printOutput(output);
    }

    @Test
    public void removePunctuationsTest() {
        ArrayList<Line> input = getInput();
        ArrayList<Line> output = removePunctuations(input, true, true);
        printOutput(output);
    }

    @Test
    public void preprocessingTest() {
        ArrayList<Line> input = getInput();
        ArrayList<Line> output = preprocess(input, 5, true, true);
        printOutput(output);
    }

    @Test
    public void preprocessTest() {
        String text = "aaa";
        ArrayList<Line> output = preprocess(text, 5, true, true);
        printOutput(output);
    }
}
