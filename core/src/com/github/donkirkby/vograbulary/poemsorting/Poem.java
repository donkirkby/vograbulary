package com.github.donkirkby.vograbulary.poemsorting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Poem {
    private String title;
    private String author;
    private ArrayList<String> lines;

    public static List<Poem> load(String... lines) {
        return load(Arrays.asList(lines));
    }

    public static List<Poem> load(List<String> lines) {
        List<Poem> poems = new ArrayList<Poem>();
        Poem poem = null;
        String title = null;
        Pattern authorPattern = Pattern.compile(" by\\s+([^#]+)#+$");
        Matcher authorMatcher = authorPattern.matcher(lines.get(0));
        String author = null;
        int startLine = 0;
        if (authorMatcher.find()) {
            author = authorMatcher.group(1).trim();
            startLine = 1;
        }
        for (int i = startLine; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.startsWith("#")) {
                if (poem != null) {
                    poem.checkAuthor(author);
                    poems.add(poem);
                    poem = null;
                }
                title = line.replaceAll("^#+([^#]*)#+$", "$1").trim();
            }
            else if (line.length() > 0) {
                if (poem == null){
                    poem = new Poem();
                    poem.title = title;
                    poem.lines = new ArrayList<String>();
                    poem.lines.add(line);
                }
                else {
                    String lastLine = poem.lines.get(poem.lines.size()-1);
                    if (lastLine.endsWith("  ")) {
                        poem.lines.set(poem.lines.size()-1, lastLine.substring(
                                0,
                                lastLine.length()-2));
                        poem.lines.add(line);
                    }
                    else {
                        poem.lines.set(
                                poem.lines.size()-1,
                                lastLine + " " + line.replaceAll("^ +", ""));
                    }
                }
            }
        }
        poem.checkAuthor(author);
        poems.add(poem);
        return poems;
    }
    
    private void checkAuthor(String author) {
        if (author != null) {
            this.author = author;
        }
        else {
            String lastLine = lines.get(lines.size()-1);
            if (lastLine.equals(lastLine.toUpperCase())) {
                this.author = lastLine;
                lines.remove(lines.size() - 1);
            }
        }
    }
    
    public String getTitle() {
        return title;
    }

    public List<String> getLines() {
        return lines;
    }
    
    public String getAuthor() {
        return author;
    }

    public Poem sortWords() {
        Poem poem = new Poem();
        poem.lines = new ArrayList<String>();
        char[] buffer = new char[1000];
        Pattern letters = Pattern.compile("[A-Za-z]+");
        for (String line : lines) {
            StringBuilder sortedWords = new StringBuilder();
            Matcher matcher = letters.matcher(line.toLowerCase());
            while (matcher.find()) {
                // append any punctuation or spaces that didn't match
                sortedWords.append(line.substring(
                        sortedWords.length(),
                        matcher.start()));
                matcher.group().getChars(0, matcher.group().length(), buffer, 0);
                Arrays.sort(buffer, 0, matcher.group().length());
                sortedWords.append(buffer, 0, matcher.group().length());
            }
            sortedWords.append(line.substring(
                    sortedWords.length(),
                    line.length()));
            poem.lines.add(sortedWords.toString());
        }
        return poem;
    }
}
