package tagger;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import parser.TEIDocument;
import parser.TEIElement;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Niket on 22/02/15.
 */
public class StructureTreeInserter {

    public void addStructureTreeToDocument(String inputFile, String outputFile, TEIDocument teiDocument) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(inputFile);
        Document document = new Document(reader.getPageSize(1), 0f, 0f, 0f, 0f);
        document.setRole(PdfName.ARTIFACT);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputFile));
        writer.setTagged();
        writer.setUserProperties(true);
        document.open();
        int n = reader.getNumberOfPages();
        for (int i = 1; i <= n; i++) {
            PdfImportedPage importedPage = writer.getImportedPage(reader, i);
            importedPage.setRole(PdfName.ARTIFACT);
            Image image = Image.getInstance(importedPage);
            image.setRole(PdfName.ARTIFACT);
            document.add(image);
        }

        final PdfStructureTreeRoot root = writer.getStructureTreeRoot();
        List<TEIElement> teiElementList = teiDocument.getBody();
        for (TEIElement teiElement : teiElementList) {
            teiElement.toPdfStructureElement(root);
        }
        document.close();
        reader.close();
        writer.close();
    }

}
