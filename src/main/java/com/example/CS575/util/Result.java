package com.example.CS575.util;

import javafx.util.Pair;

import java.util.*;

public class Result {


    private ArrayList<LineMatch> matchedLines = null;
    private double similarity = 0;

    public Result() {
        this.matchedLines = new ArrayList<>();
    }

    public void addMatchedLine(int lineA, int lineB, double value) {
        this.matchedLines.add(new LineMatch(lineA, lineB, value));
    }

//    public void addMatchedLines(int[] matchedLines) {
//        for(int i = 0; i < matchedLines.length; ++i) {
//            if(matchedLines[i] != -1)
//                this.addMatchedLine(i, matchedLines[i]);
//        }
//    }

    public ArrayList<LineMatch> getMatchedLine() {
        return this.matchedLines;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    public double getSimilarity() {
        return this.similarity;
    }

    public void rematchLines(boolean ascending, double threshold) {
        Collections.sort(matchedLines, new Comparator<LineMatch>() {
            @Override
            public int compare(LineMatch o1, LineMatch o2) {
                if(o1.value != o2.value) {
                    if(ascending) return Double.compare(o1.value, o2.value);
                    else return Double.compare(o2.value, o1.value);
                }

                if(o1.lineA != o2.lineA) {
                    return Integer.compare(o1.lineA, o2.lineA);
                }

                return Integer.compare(o1.lineB, o2.lineB);
            }
        });

        Set<Integer> matchedA = new HashSet<>();
        Set<Integer> matchedB = new HashSet<>();

        int i = 0;
        while(i < matchedLines.size()) {
            LineMatch m = matchedLines.get(i);
            if(matchedA.contains(m.lineA) || matchedB.contains(m.lineB)) {
                matchedLines.remove(i); // has been matched before
                continue;
            }
            matchedA.add(m.lineA);
            matchedB.add(m.lineB);
            i++;
        }

    }
}
