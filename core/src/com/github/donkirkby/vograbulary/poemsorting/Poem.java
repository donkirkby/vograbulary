package com.github.donkirkby.vograbulary.poemsorting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Poem {
    private String title;
    private ArrayList<String> lines;

    public static List<Poem> load(String... lines) {
        return load(Arrays.asList(lines));
    }

    public static List<Poem> load(List<String> lines) {
        List<Poem> poems = new ArrayList<Poem>();
        Poem poem = null;
        String title = null;
        for (String line : lines) {
            if (line.startsWith("#")) {
                if (poem != null) {
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
                }
                poem.lines.add(line);
            }
        }
        poems.add(poem);
        return poems;
    }
    
    public String getTitle() {
        return title;
    }

    public List<String> getLines() {
        return lines;
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
