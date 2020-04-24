package com.example.CS575.util;

import java.util.ArrayList;


public class Preprocessing {

    public static ArrayList<Line> preprocess(String text, int minLen, boolean tab, boolean brace) {
        ArrayList<Line> input = new ArrayList<>();
        int i = 1;
        for (String content : text.split("\n")) {
            input.add(new Line(i++, content));
        }
        return preprocess(input, minLen, tab, brace);
    }

    public static ArrayList<Line> preprocess(ArrayList<Line> text, int minLen, boolean tab, boolean brace) {
        text = removeComments(text);
        text = removePunctuations(text, tab, brace);

        int i = 0;
        while (i < text.size()) {
            if (text.get(i).content.length() < minLen) {
                text.remove(i);
            } else {
                ++i;
            }
        }

        return text;
    }

    // reference: leetcode
    public static ArrayList<Line> removeComments(ArrayList<Line> text) {
        ArrayList<Line> res = new ArrayList<>();

        StringBuilder textSB = new StringBuilder();
        for (Line line : text) {
            textSB.append("#" + line.lineNO + " ").append(line.content).append("\n");
        }

        int single = -1, multi = -1;
        int i = 0;

        while (i < textSB.length() - 1) {
            char first = textSB.charAt(i);
            char second = textSB.charAt(i + 1);

            if (first == '/' && second == '/') { // single-line comment //
                single = i;
                i += 2;
                while (i < textSB.length() && textSB.charAt(i) != '\n')
                    ++i;

                textSB.delete(single, i);
                i = single - 1;
            } else if (first == '/' && second == '*') { // multi-line comment /* */
                multi = i;
                i += 2;
                while (i + 1 < textSB.length()
                        && (textSB.charAt(i) != '*' || textSB.charAt(i + 1) != '/'))
                    ++i;

                textSB.delete(multi, i + 2);
                i = multi - 1;
            }
            ++i;
        }

        for (String content : textSB.toString().split("\n")) {
            int j = 1;
            while (j < content.length() && content.charAt(j) != ' ') ++j;
            res.add(new Line(Integer.parseInt(content.substring(1, j)), content.substring(j + 1)));
        }

        return res;
    }

    public static ArrayList<Line> removePunctuations(ArrayList<Line> lines, boolean tab, boolean brace) {
        ArrayList<Line> res = new ArrayList<Line>();

        for (Line line : lines) {
            line.content = line.content.trim();

            if (tab)
                line.content = line.content.replace("\\t", "");

            if (brace)
                line.content = line.content.replace("{", "").replace("}", "");

            if (!line.content.equals(""))
                res.add(line);

        }

        return res;
    }

    public static String removeBrace(String code) {
        return code.replace("{", "").replace("}", "");
    }


}
