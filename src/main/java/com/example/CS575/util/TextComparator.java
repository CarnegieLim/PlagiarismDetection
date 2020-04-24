package com.example.CS575.util;

import java.util.ArrayList;

public class TextComparator {

    public TextComparator() {
    }

    public static Result compare(ArrayList<Line> textA, ArrayList<Line> textB, String method, double threshold) {
        Result res = new Result();
        int sizeA = textA.size();
        int sizeB = textB.size();

        if (sizeA == 0 || sizeB == 0) return res;

        for (int i = 0; i < sizeA; ++i) {
            for (int j = 0; j < sizeB; ++j) {
                Line lineA = textA.get(i);
                Line lineB = textB.get(j);
                String A = lineA.content;
                String B = lineB.content;

                int minLen = Math.min(A.length(), B.length());
                double value = 0.0;
                switch (method) {
                    case "LCS":
                        int LCS = getLCS(A, B);
                        value = (double) LCS / (double) minLen;
                        break;
                    case "Levinstein":
                        value = getLevinsteinDistance(A, B);
                        break;
                    case "Jaro":
                        value = getJaroDistance(A, B, false);
                        break;
                    case "Jaro-Winkler":
                        value = getJaroDistance(A, B, true);
                        break;
                    default:
                        value = 0.0;
                }

                res.addMatchedLine(lineA.lineNO, lineB.lineNO, value);
            }
        }

        res.rematchLines(method.equals("Levinstein"), 0);
        ArrayList<LineMatch> matchedLines = res.getMatchedLine();
        int count = 0;
        for (int i = 0; i < matchedLines.size(); ++i) {
            LineMatch m = matchedLines.get(i);
            if ((method.equals("Levinstein") && m.value < threshold) || (!method.equals("Levinstein") && m.value > threshold)) {
                ++count;
            }
        }
        res.setSimilarity((double) count / (double) Math.min(sizeA, sizeB));

        return res;
    }

    public static int getLCS(String A, String B) {
        int lenA = A.length();
        int lenB = B.length();

        if (lenA == 0 || lenB == 0) return 0;

        int[][] dp = new int[lenA + 1][lenB + 1];

        for (int i = 1; i <= lenA; ++i) {
            for (int j = 1; j <= lenB; ++j) {
                if (A.charAt(i - 1) == B.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i][j - 1], dp[i - 1][j]);
                }
            }
        }

        return dp[lenA][lenB];
    }

    public static int getLevinsteinDistance(String A, String B) {
        int lenA = A.length();
        int lenB = B.length();

        if (lenA == 0 || lenB == 0) return 0;

        int[][] dp = new int[lenA + 1][lenB + 1];
        for (int i = 1; i <= lenA; ++i) {
            dp[i][0] = i;
        }

        for (int j = 1; j <= lenB; ++j) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= lenA; ++i) {
            for (int j = 1; j <= lenB; ++j) {
                if (A.charAt(i - 1) == B.charAt(j - 1)) {
                    dp[i][j] = Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1));
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j - 1] + 1, Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1));
                }
            }
        }

        return dp[lenA][lenB];
    }

    public static Double getJaroDistance(String A, String B, boolean JaroWinkler) {
        if (A == null || B == null) return 0.0;
        int lenA = A.length(), lenB = B.length();
        int res[] = getM(A, B);
        double m = res[0], t = res[1];
        if (m == 0) return 0.0;

        double distance = ((m / lenA + m / lenB + (m - t) / m)) / 3;
        if (!JaroWinkler) return distance;

        double P = 0.1;
        double L = Math.min(4, res[2]);
        return distance + P * L * (1 - distance); // jaro winkler
    }


    private static int[] getM(String A, String B) {
        char[] charA = A.toCharArray();
        char[] charB = B.toCharArray();
        int lenA = charA.length;
        int lenB = charB.length;

        if (lenA < lenB) {
            char[] tmp = charB;
            charB = charA;
            charA = tmp;
        }

        int window = Math.max(lenA / 2 - 1, 0), m = 0;

        boolean[] matchedB = new boolean[lenB];
        boolean[] matchedA = new boolean[lenA];

        for (int i = 0; i < lenB; i++) {
            char b = charB[i];
            for (int j = Math.max(i - window, 0);
                 j < Math.min(i + window + 1, lenA); j++) {
                if (!matchedA[j] && b == charA[j]) {
                    matchedA[j] = true;
                    matchedB[i] = true;
                    m++;
                    break;
                }
            }
        }

        int t = 0, commonP = 0, j = 0;

        for (int i = 0; i < lenB; i++) {
            if (matchedB[i]) {
                while (!matchedA[j]) j++;

                if (charB[i] != charA[j]) t++;
                j++;
            }
        }

        for (int i = 0; i < lenB; i++) {
            if (charA[i] == charB[i]) commonP++;
            else break;
        }

        int[] res = new int[]{m, t / 2, commonP};
        return res;
    }
}
