package com.github.donkirkby.vograbulary.russian;

public class Puzzle {
    private String clue;
    private String target1;
    private String target2;
    
    public Puzzle(String clue) {
        this.clue = clue;
        int targetPosition = 0;
        String[] words = clue.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (word.charAt(0) == '*') {
                // remove * and any other punctuation
                words[targetPosition++] = word.replaceAll("\\W*", "");
            }
        }
        target1 = words[0].toUpperCase();
        target2 = words[1].toUpperCase();
        if (words.length == 2) {
            this.clue = "";
        }
    }
    
    public String getClue() {
        return clue;
    }
    public String getTarget1() {
        return target1;
    }
    public String getTarget2() {
        return target2;
    }
}
