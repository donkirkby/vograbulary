package com.github.donkirkby.vograbulary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.donkirkby.vograbulary.poemsorting.Poem;

public class VograbularyBook {
    public static void main(String[] args) {
        try {
            PrintWriter writer = new PrintWriter("vograbulary.tex");
            try {
                List<String> documentFrame = loadTextAsset("document_frame.tex");
                for (String line : documentFrame) {
                    if (line.equals("## insert-puzzles ##")) {
                        populate(writer);
                    }
                    else {
                        writer.println(line);
                    }
                }
            } finally {
                writer.close();
            }
            Process process = Runtime.getRuntime().exec("pdflatex vograbulary.tex");
            process.waitFor();
            System.out.println("Done.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void populate(PrintWriter writer) {
//        boolean isLargePrint = false;
//        double fontScale = isLargePrint ? 4 : 1;
        
        List<Poem> poems = new ArrayList<Poem>();
        loadPoems("whitman.md", poems);
        loadPoems("lyrical_poetry.md", poems);
        Collections.shuffle(poems);

        final int poemCount = 5;
        ArrayList<Integer> solutionPositions = new ArrayList<Integer>();
        for (int i = 0; i < poemCount; i++) {
            solutionPositions.add(i);
        }
        Collections.shuffle(solutionPositions);
        for (int i = 0; i < poemCount; i++) {
            Poem poem = poems.get(i);
            String title = poem.getTitle();
            if (poem.getAuthor() != null) {
                title += ", by " + poem.getAuthor();
            }
            title = String.format(
                    "%d. %s (see solution %d)",
                    i+1,
                    title,
                    solutionPositions.get(i) + 1);
            writer.printf("\\poemtitle{%s}\n", title);
            writer.write("\\begin{verse}\n");
            Poem sorted = poem.sortWords();
            for (String line : sorted.getLines()) {
                String[] words = line.split(" ");
                for (int wordIndex = 0; wordIndex < words.length; wordIndex++) {
                    String word = words[wordIndex];
                    writer.write("\\(\\def\\stacktype{L}\\rule{0pt}{\\baselineskip}\n");
                    for (int charIndex = 0; charIndex < word.length(); charIndex++) {
                        String cString = word.substring(charIndex, charIndex+1);
                        char c = cString.charAt(0);
                        if (c < 'a' || 'z' < c) {
                            writer.write(cString);
                        }
                        else {
                            writer.printf("\\stackunder{\\Huge{\\_}}{%s}\n", cString);
                        }
                    }
                    writer.printf(
                            "\\)%s\n",
                            wordIndex == words.length-1 ? "\\\\" : "\\rule{5pt}{0pt}");
                }
            }
            writer.write("\\end{verse}");
        }
        writer.write("\\newpage\\Large\\textbf{Solutions}\n");
        for (int i = 0; i < poemCount; i++) {
            int poemIndex = solutionPositions.indexOf(i);
            Poem poem = poems.get(poemIndex);
            String title = String.format(
                            "%d. %s",
                            i+1,
                            poem.getTitle());
            writer.printf("\\poemtitle{%s}\n\\begin{verse}\n", title);
            for (String line : poem.getLines()) {
                writer.printf("%s\\\\\n", line);
            }
            writer.write("\\end{verse}\n");
        }
    }

    private static void loadPoems(final String assetName, List<Poem> poems) {
        List<String> poemsText = loadTextAsset(assetName);
        for (Poem poem : Poem.load(poemsText)) {
            if (poem.getLines().size() <= 20) {
                poems.add(poem);
            }
        }
    }
    
    private static List<String> loadTextAsset(String assetName) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream stream = classLoader.getResourceAsStream(
              "com/github/donkirkby/vograbulary/assets/"+assetName);
            ArrayList<String> lines = new ArrayList<String>();
            BufferedReader reader =new BufferedReader(new InputStreamReader(stream));
            try {
                String line;
                while (null != (line = reader.readLine())) {
                    lines.add(line);
                }
            } finally {
                    reader.close();
            }
            return lines;
        } catch (IOException ex) {
            throw new RuntimeException(
                    "Asset " + assetName + " failed to load.",
                    ex);
        }
    }
}