package com.github.donkirkby.vograbulary.ultraghost;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Match {
    private UltraghostRandom random = new UltraghostRandom();
    private Student[] students;
    private int studentIndex = Integer.MIN_VALUE;
    private Puzzle puzzle;

    public Match(int matchScore, Student... students) {
        this.students = students;
    }
    
    private void checkStudentOrder() {
        if (studentIndex < 0) {
            for (int i = 0; i < students.length - 1; i++) {
                int source = random.chooseStartingStudent(students.length - i);
                Student student = students[i + source];
                students[i + source] = students[i];
                students[i] = student;
            }
            studentIndex = students.length - 1;
        }
    }
    
    /**
     * Replace the default implementation of the random generator. Useful for
     * testing.
     */
    public void setRandom(UltraghostRandom random) {
        this.random = random;
    }

    public Puzzle createPuzzle(WordList wordList) {
        checkStudentOrder();
        studentIndex = (studentIndex + 1) % students.length;
        puzzle = new Puzzle(
                random.generatePuzzle(), 
                students[studentIndex], 
                wordList);
        return puzzle;
    }

    public String getSummary() {
        checkStudentOrder();
        StringWriter writer = new StringWriter();
        try(PrintWriter printer = new PrintWriter(writer)) {
            for (Student student : students) {
                printer.println(student);
            }
            return writer.toString();
        }
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }
    
    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
    }
}
