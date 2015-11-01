package com.github.donkirkby.vograbulary.poemsorting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                title = line.replaceAll("^#+\\s*(.*\\S)\\s*#+$", "$1");
            }
            else {
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
        for (String line : lines) {
            StringBuilder sortedWords = new StringBuilder();
            String[] words = line.toLowerCase().split(" ");
            for (int i = 0; i < words.length; i++) {
                String word = words[i];
                if (i > 0) {
                    sortedWords.append(" ");
                }
                word.getChars(0, word.length(), buffer, 0);
                Arrays.sort(buffer, 0, word.length());
                sortedWords.append(buffer, 0, word.length());
            }
            poem.lines.add(sortedWords.toString());
        }
        return poem;
    }
}
