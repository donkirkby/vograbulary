package com.github.donkirkby.vograbulary;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Date;

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
        Page page = new Page(document); // Instantiates the page inside the
                                        // document context.
        document.getPages().add(page); // Puts the page in the pages collection.

        PrimitiveComposer composer = new PrimitiveComposer(page);
        BlockComposer blockComposer = new BlockComposer(composer);

        StandardType1Font titleFont = new StandardType1Font(
                document,
                StandardType1Font.FamilyEnum.Helvetica,
                true, // bold
                false); // italic
        int titleFontSize = 20;
        StandardType1Font textFont = new StandardType1Font(
                document,
                StandardType1Font.FamilyEnum.Times,
                false, // bold
                false); // italic
        int textFontSize = 12;
        StandardType1Font puzzleFont = new StandardType1Font(
                document,
                StandardType1Font.FamilyEnum.Courier,
                false, // bold
                false); // italic
        int puzzleFontSize = 12;

        Dimension2D pageSize = page.getSize();
        Rectangle2D titleFrame = new Rectangle2D.Double(
                20,
                50,
                pageSize.getWidth() - 90,
                pageSize.getHeight() - 250
                );
        blockComposer.begin(titleFrame, XAlignmentEnum.Center, YAlignmentEnum.Top);
        composer.setFont(titleFont, titleFontSize);
        blockComposer.showText("Vograbulary");
        blockComposer.end();
        Rectangle2D usedSpace = blockComposer.getBoundBox();
        Rectangle2D bodyFrame = new Rectangle2D.Double(
                titleFrame.getX(),
                usedSpace.getMaxY() + 10,
                titleFrame.getWidth(),
                titleFrame.getHeight() - usedSpace.getHeight());
        blockComposer.begin(bodyFrame, XAlignmentEnum.Left, YAlignmentEnum.Top);
        composer.setFont(textFont, textFontSize);
        blockComposer.showText(
                "Each puzzle starts with a poem, then the letters in each " +
                "word are sorted. To solve the puzzle, find the original " +
                "words and read the poem. Solutions are listed at the end.");
        blockComposer.showBreak();
        composer.setFont(puzzleFont, puzzleFontSize);
        blockComposer.showText("ARX 23");
        blockComposer.showBreak();
        blockComposer.showText("ARX 25");
        blockComposer.end();

        composer.flush();
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