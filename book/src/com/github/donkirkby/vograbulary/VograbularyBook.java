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
import com.github.donkirkby.vograbulary.poemsorting.PoemDisplay;

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
            process.getOutputStream().close();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
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
        final int poemCount = 3;
        List<PoemDisplay> chosenPoems = new ArrayList<PoemDisplay>();
        for (Poem poem : poems) {
            PoemDisplay display = new PoemDisplay(poem, 45);
            if (display.getBodyLineCount()*3 + display.getClueLineCount() < 50) {
                chosenPoems.add(display);
                if (chosenPoems.size() >= poemCount) {
                    break;
                }
            }
        }

        ArrayList<Integer> solutionPositions = new ArrayList<Integer>();
        for (int i = 0; i < chosenPoems.size(); i++) {
            solutionPositions.add(i);
        }
        Collections.shuffle(solutionPositions);
        for (int i = 0; i < chosenPoems.size(); i++) {
            PoemDisplay display = chosenPoems.get(i);
            Poem poem = display.getPoem();
            String title = poem.getTitle().replace("&", "\\&");
            if (poem.getAuthor() != null) {
                title += " by " + poem.getAuthor();
            }
            title = String.format(
                    "%d. %s (see solution %d)",
                    i+1,
                    title,
                    solutionPositions.get(i) + 1);
            writer.write("\\begin{tabular}{p{0.97\\linewidth}}\n\\phantom{.}\\\\\n");
            writer.printf("\\poemtitle{%s}\\\\\n", title);
            writer.write("\\begin{tabular}{|c");
            char columnType = 'c';
            for (int charIndex = 0; charIndex < display.getWidth(); charIndex++) {
                writer.write(' ');
                columnType = (charIndex / 5) % 2 == 1
                        ? 'G'  // gray
                        : 'c';
                writer.write(columnType);
            }
            writer.printf("%c|}\n", columnType);
            writer.write("\\hline\n");
            for (int lineIndex = 0; lineIndex < display.getBodyLineCount(); lineIndex++) {
                writer.write("\\phantom{.}");
                for (int charIndex = 0; charIndex < display.getWidth(); charIndex++) {
                    writer.write('&');
                    final char c = display.getBody(lineIndex, charIndex);
                    if (c == ' ') {
                        writer.write("\\pzsp");
                    }
                    else if (c < 'a' || 'z' < c) {
                        writer.printf("\\puzzlesize{%c}", c);
                    }
                    else {
                        writer.write("\\hdash");
                    }
                }
                writer.printf("&\\phantom{.}\\\\\n\n");
                for (int charIndex = 0; charIndex < display.getWidth(); charIndex++) {
                    writer.write('&');
                    final char c = display.getBody(lineIndex, charIndex);
                    if ('a' <= c && c <= 'z') {
                        writer.write(c);
                    }
                }
                writer.write("& \\\\\n");
            }
            writer.write("\\hline\n");
            for (int lineIndex = 0; lineIndex < display.getClueLineCount(); lineIndex++) {
                for (int charIndex = 0; charIndex < display.getWidth(); charIndex++) {
                    writer.write('&');
                    writer.write(display.getClue(lineIndex, charIndex));
                }
                writer.write("&\\\\\n");
            }
            writer.write("\\hline\n\\end{tabular}\n");
            writer.write("\\end{tabular}\n\n");
        }
        writer.write("\\newpage\\Large\\textbf{Solutions}\n");
        for (int i = 0; i < chosenPoems.size(); i++) {
            int poemIndex = solutionPositions.indexOf(i);
            Poem poem = chosenPoems.get(poemIndex).getPoem();
            String title = String.format(
                            "%d. %s",
                            i+1,
                            poem.getTitle().replace("&", "\\&"));
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