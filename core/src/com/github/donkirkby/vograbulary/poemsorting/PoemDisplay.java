package com.github.donkirkby.vograbulary.poemsorting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PoemDisplay {
    private int width;
    private int clueLineCount;
    private List<String> bodyLines = new ArrayList<String>();
    private List<String> clueColumns = new ArrayList<String>();
    
    public PoemDisplay(Poem poem, int width) {
        List<String> poemLines = new ArrayList<String>();
        Poem sortedPoem = poem.sortWords();
        final int poemLineCount = poem.getLines().size();
        for (int poemLineIndex = 0; poemLineIndex < poemLineCount; poemLineIndex++) {
            String poemLine = poem.getLines().get(poemLineIndex);
            String sortedLine = sortedPoem.getLines().get(poemLineIndex);
            String indent = "";
            int start = 0;
            int lastBreak = 0;
            for (int charIndex = 0; charIndex < poemLine.length(); charIndex++) {
                boolean shouldAdd;
                if (charIndex == poemLine.length() - 1) {
                    shouldAdd = true;
                    lastBreak = poemLine.length();
                }
                else {
                    if (poemLine.charAt(charIndex) == ' ') {
                        lastBreak = charIndex;
                    }
                    shouldAdd = charIndex - start >= width;
                }
                if (shouldAdd) {
                    poemLines.add(indent + poemLine.substring(start, lastBreak));
                    bodyLines.add(indent + sortedLine.substring(start, lastBreak));
                    start = lastBreak+1;
                    indent = "    ";
                }
            }
        }
        this.width = 0;
        for (String line : bodyLines) {
            this.width = Math.max(this.width, line.length());
        }
        char column[] = new char[bodyLines.size()];
        for (int charIndex = 0; charIndex < this.width; charIndex++) {
            int letterCount = 0;
            for (int lineIndex = 0; lineIndex < poemLines.size(); lineIndex++) {
                final String line = poemLines.get(lineIndex);
                char c = charIndex < line.length()
                        ? Character.toLowerCase(line.charAt(charIndex))
                        : ' ';
                if ('a' <= c && c <= 'z') {
                    column[letterCount++] = c;
                }
            }
            Arrays.sort(column, 0, letterCount);
            clueColumns.add(new String(column, 0, letterCount));
            clueLineCount = Math.max(clueLineCount, letterCount);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getBodyLineCount() {
        return bodyLines.size();
    }

    public char getBody(int lineIndex, int charIndex) {
        String line = bodyLines.get(lineIndex);
        return charIndex < line.length() ? line.charAt(charIndex) : ' ';
    }

    public int getClueLineCount() {
        return clueLineCount;
    }

    public char getClue(int lineIndex, int charIndex) {
        String line = charIndex < clueColumns.size() ? clueColumns.get(charIndex) : "";
        return lineIndex < line.length() ? line.charAt(lineIndex) : ' ';
    }
}
