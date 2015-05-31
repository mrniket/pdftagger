package parser;

import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.PdfStructureElement;
import com.itextpdf.text.pdf.PdfStructureTreeRoot;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Niket on 29/05/15.
 */
public class FigureTEIElement extends TEIElement {

    private final String assetPath;
    String heading;
    String filePath;

    public FigureTEIElement(String heading, String filePath, HeaderTEIElement parentElement, String assetPath) {
        this.parentElement = parentElement;
        this.heading = heading;
        this.filePath = filePath;
        this.assetPath = assetPath;
    }

    @Override
    public TEIElementType getElementType() {
        return TEIElementType.FIGURE;
    }

    @Override
    public boolean isLeafNode() {
        return true;
    }

    @Override
    public String getContent() {
        try {
            return readFile(assetPath + filePath, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return heading;
    }

    public String getFilePath() {
        return filePath;
    }

    @Override
    public PdfStructureElement toPdfStructureElement(PdfStructureTreeRoot treeRoot) {
        PdfStructureElement pdfStructureElement = new PdfStructureElement(treeRoot, PdfName.FIGURE);
        pdfStructureElement.setAttribute(PdfName.ACTUALTEXT, new PdfString(getContent()));
        if (heading != null) {
            pdfStructureElement.setAttribute(PdfName.H, new PdfString(heading));
        }
        return pdfStructureElement;
    }

    @Override
    public PdfStructureElement toPdfStructureElement(PdfStructureElement parent) {
        PdfStructureElement pdfStructureElement = new PdfStructureElement(parent, PdfName.FIGURE);
        pdfStructureElement.setAttribute(PdfName.ACTUALTEXT, new PdfString(getContent()));
        if (heading != null) {
            pdfStructureElement.setAttribute(PdfName.H, new PdfString(heading));
        }
        return pdfStructureElement;
    }

    private static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
