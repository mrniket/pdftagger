package tagger;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.codec.Base64;
import parser.TEIDocument;
import parser.TEIElement;
import parser.TEIElementType;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by Niket on 22/02/15.
 */
public class StructureTreeInserter {

    public void addStructureTreeToDocument(String fileName, TEIDocument teiDocument) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(fileName);
        Document document = new Document(reader.getPageSize(1), 0f, 0f, 0f, 0f);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("src/main/resources/test.pdf"));
        writer.setTagged();
        writer.setUserProperties(true);
        document.open();
        int n = reader.getNumberOfPages();
        for (int i = 1; i <= n; i++) {
            PdfImportedPage importedPage = writer.getImportedPage(reader, i);
            document.add(Image.getInstance(importedPage));
        }

        PdfStructureTreeRoot root = writer.getStructureTreeRoot();
        List<TEIElement> teiElementList = teiDocument.getBody();
        for (TEIElement teiElement : teiElementList) {
            teiElement.toPdfStructureElement(root);
        }
        document.close();
        reader.close();
        writer.close();
    }

}
