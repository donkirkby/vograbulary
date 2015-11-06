package com.github.donkirkby.vograbulary;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.pdfclown.documents.Document;
import org.pdfclown.documents.Page;
import org.pdfclown.documents.contents.composition.BlockComposer;
import org.pdfclown.documents.contents.composition.PrimitiveComposer;
import org.pdfclown.documents.contents.composition.XAlignmentEnum;
import org.pdfclown.documents.contents.composition.YAlignmentEnum;
import org.pdfclown.documents.contents.fonts.StandardType1Font;
import org.pdfclown.documents.interaction.viewer.ViewerPreferences;
import org.pdfclown.documents.interchange.metadata.Information;
import org.pdfclown.files.File;
import org.pdfclown.files.SerializationModeEnum;
import org.pdfclown.util.math.geom.Dimension;

import com.github.donkirkby.vograbulary.poemsorting.Poem;

public class VograbularyBook {
    public static void main(String[] args) {
        try {
            File file = new File();
            Document document = file.getDocument();
            populate(document);
            serialize(file, "vograbulary.pdf");
            
            System.out.println("Done.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void populate(Document document) {
        boolean isLargePrint = false;
        double fontScale = isLargePrint ? 4 : 1;
        StandardType1Font titleFont = new StandardType1Font(
                document,
                StandardType1Font.FamilyEnum.Helvetica,
                true, // bold
                false); // italic
        double titleFontSize = 20*fontScale;
        StandardType1Font textFont = new StandardType1Font(
                document,
                StandardType1Font.FamilyEnum.Times,
                false, // bold
                false); // italic
        double textFontSize = 12*fontScale;
        StandardType1Font puzzleFont = new StandardType1Font(
                document,
                StandardType1Font.FamilyEnum.Courier,
                false, // bold
                false); // italic
        double puzzleFontSize = 22*fontScale;

        Page page = new Page(document);
        document.getPages().add(page);
        
        PrimitiveComposer composer = new PrimitiveComposer(page);
        BlockComposer blockComposer = new BlockComposer(composer);
        
        Dimension2D pageSize = page.getSize();
        Dimension2D paragraphSpace = new Dimension(0, 10);
        Rectangle2D titleFrame = new Rectangle2D.Double(
                20,
                50,
                pageSize.getWidth() - 40,
                pageSize.getHeight() - 90
                );
        blockComposer.begin(titleFrame, XAlignmentEnum.Center, YAlignmentEnum.Top);
        composer.setFont(titleFont, titleFontSize);
        blockComposer.showText("Vograbulary");
        blockComposer.showBreak(paragraphSpace);
        blockComposer.end();
        Rectangle2D usedSpace = blockComposer.getBoundBox();
        Rectangle2D bodyFrame = new Rectangle2D.Double(
                titleFrame.getX(),
                usedSpace.getMaxY(),
                titleFrame.getWidth(),
                titleFrame.getHeight() - usedSpace.getHeight());
        blockComposer.begin(bodyFrame, XAlignmentEnum.Left, YAlignmentEnum.Top);
        composer.setFont(textFont, textFontSize);
        List<String> introduction = loadTextAsset("introduction.md");
        for (String line : introduction) {
            blockComposer.showText(line);
            blockComposer.showText(" ");
        }
        blockComposer.showBreak(paragraphSpace);
        blockComposer.end();
        composer.setFont(puzzleFont, puzzleFontSize);
        List<Poem> poems = new ArrayList<Poem>();
        loadPoems("whitman.md", poems);
        loadPoems("lyrical_poetry.md", poems);
        Collections.shuffle(poems);
        
        double charWidth = puzzleFont.getWidth('_', puzzleFontSize);
        double charHeight = puzzleFont.getHeight("_", puzzleFontSize);
        double y = blockComposer.getBoundBox().getMaxY();
        final int poemCount = 5;
        ArrayList<Integer> solutionPositions = new ArrayList<Integer>();
        for (int i = 0; i < poemCount; i++) {
            solutionPositions.add(i);
        }
        Collections.shuffle(solutionPositions);
        for (int i = 0; i < poemCount; i++) {
            Poem poem = poems.get(i);
            composer.setFont(titleFont, textFontSize);
            composer.showText(
                    String.format(
                            "%d. %s (see solution %d)",
                            i+1,
                            poem.getAuthor().toUpperCase(),
                            solutionPositions.get(i) + 1),
                    new Point2D.Double(titleFrame.getX(), y));
            y += textFont.getHeight('X', textFontSize) * 1.5;
            Poem sorted = poem.sortWords();
            for (String line : sorted.getLines()) {
                double x = titleFrame.getX();
                String[] words = line.split(" ");
                for (String word : words) {
                    if (x > titleFrame.getX()) {
                        x += charWidth;
                    }
                    if (x + charWidth * word.length() > titleFrame.getMaxX()) {
                        y += 2*charHeight;
                        x = titleFrame.getX() + 2.5*charWidth;
                    }
                    if (y + 2*charHeight > titleFrame.getMaxY()) {
                        composer.flush();
                        page = new Page(document);
                        document.getPages().add(page);
                        
                        composer = new PrimitiveComposer(page);
                        y = titleFrame.getY();
                    }
                    for (int charIndex = 0; charIndex < word.length(); charIndex++) {
                        composer.setFont(puzzleFont, puzzleFontSize);
                        String cString = word.substring(charIndex, charIndex+1);
                        char c = cString.charAt(0);
                        if (c < 'a' || 'z' < c) {
                            composer.showText(cString, new Point2D.Double(x, y));
                        }
                        else {
                            composer.showText("_", new Point2D.Double(x, y));
                            composer.setFont(puzzleFont, puzzleFontSize*.75);
                            composer.showText(
                                    cString,
                                    new Point2D.Double(x+charWidth/2, y+charHeight),
                                    XAlignmentEnum.Center,
                                    YAlignmentEnum.Top,
                                    0);
                        }
                        x += charWidth * 1.25;
                    }
                }
                y += 2*charHeight;
            }
        }
        composer.flush();

        page = new Page(document);
        document.getPages().add(page);
        
        composer = new PrimitiveComposer(page);
        blockComposer = new BlockComposer(composer);
        
        titleFrame = new Rectangle2D.Double(
                20,
                50,
                pageSize.getWidth() - 40,
                pageSize.getHeight() - 90
                );
        blockComposer.begin(titleFrame, XAlignmentEnum.Center, YAlignmentEnum.Top);
        composer.setFont(titleFont, titleFontSize);
//        blockComposer.showText("Solutions");
        blockComposer.showBreak(paragraphSpace);
        blockComposer.end();
        usedSpace = blockComposer.getBoundBox();
        bodyFrame = new Rectangle2D.Double(
                titleFrame.getX(),
                titleFrame.getY() + usedSpace.getHeight(),
                titleFrame.getWidth(),
                titleFrame.getHeight() - usedSpace.getHeight());
        blockComposer.begin(bodyFrame, XAlignmentEnum.Left, YAlignmentEnum.Top);
        composer.setFont(textFont, textFontSize);
        for (int i = 0; i < poemCount; i++) {
            int poemIndex = solutionPositions.indexOf(i);
            Poem poem = poems.get(poemIndex);
            ArrayList<String> printLines = new ArrayList<String>();
            printLines.add(String.format(
                            "%d. %s",
                            i+1,
                            poem.getTitle()));
            printLines.addAll(poem.getLines());
            blockComposer.showBreak(paragraphSpace);
            for (String line : printLines) {
                int charCount = 0;
                while (true) {
                    charCount += blockComposer.showText(line.substring(charCount));
                    if (charCount >= line.length()) {
                        break;
                    }
                    composer.flush();
                    page = new Page(document);
                    document.getPages().add(page);
                    
                    composer = new PrimitiveComposer(page);
                    blockComposer = new BlockComposer(composer);
                    
                    bodyFrame = new Rectangle2D.Double(
                            20,
                            50,
                            pageSize.getWidth() - 40,
                            pageSize.getHeight() - 90
                            );
                    blockComposer.begin(bodyFrame, XAlignmentEnum.Left, YAlignmentEnum.Top);
                    composer.setFont(textFont, textFontSize);
                }
                blockComposer.showBreak();
            }
        }
        composer.flush();
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

    private static String serialize(File file, String filePath) throws IOException {
        applyDocumentSettings(file.getDocument());

        java.io.File outputFile = new java.io.File(filePath);
        file.save(outputFile, SerializationModeEnum.Standard);

        return outputFile.getPath();
    }

    private static void applyDocumentSettings(Document document) {
        ViewerPreferences view = new ViewerPreferences(document);
        document.setViewerPreferences(view);
        view.setDisplayDocTitle(true);

        Information info = document.getInformation();
        info.clear();
        info.setAuthor("Don Kirkby");
        info.setCreationDate(new Date());
        info.setCreator("Don Kirkby");
        info.setTitle("Vograbulary");
        info.setSubject("Word puzzles from the Vograbulary project");
        info.setKeywords("word puzzles");
    }
}