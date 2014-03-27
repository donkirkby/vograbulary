package com.github.donkirkby.vograbulary.ultraghost;

public class Match {
    private UltraghostRandom random = new UltraghostRandom();
    private Student[] students;
    private int studentIndex = -1;

    public Match(int matchScore, Student... students) {
        this.students = students;
    }
    
    /**
     * Replace the default implementation of the random generator. Useful for
     * testing.
     */
    public void setRandom(UltraghostRandom random) {
        this.random = random;
    }

    public Puzzle createPuzzle(WordList wordList) {
        if (studentIndex < 0) {
            studentIndex = random.chooseStartingStudent(students.length);
        }
        else {
            studentIndex = (studentIndex + 1) % students.length;
        }
        return new Puzzle(
                random.generatePuzzle(), 
                students[studentIndex], 
                wordList);
    }
}
