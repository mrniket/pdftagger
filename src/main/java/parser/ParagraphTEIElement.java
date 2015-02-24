package parser;

import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfStructureElement;
import com.itextpdf.text.pdf.PdfStructureTreeRoot;

/**
 * Created by Niket on 22/02/15.
 */
public class ParagraphTEIElement extends TEIElement {

    private String content;

    public ParagraphTEIElement(String content, HeaderTEIElement parentElement) {
        this(content);
        this.parentElement = parentElement;
        if (parentElement != null) {
            this.level = parentElement.getLevel() + 1;
            parentElement.addChildElement(this);
        }
    }

    public ParagraphTEIElement(String content) {
        this.content = content;
        this.level = 0;
    }

    @Override
    public TEIElementType getElementType() {
        return TEIElementType.PARAGRAPH;
    }

    @Override
    public boolean isLeafNode() {
        return true;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public PdfStructureElement toPdfStructureElement(PdfStructureTreeRoot treeRoot) {
        PdfStructureElement pdfStructureElement = new PdfStructureElement(treeRoot, PdfName.P);
        pdfStructureElement.setAttribute(PdfName.SUBTYPE, new PdfName("Paragraph"));
        return pdfStructureElement;
    }

    @Override
    public PdfStructureElement toPdfStructureElement(PdfStructureElement parent) {
        PdfStructureElement pdfStructureElement = new PdfStructureElement(parent, PdfName.P);
        pdfStructureElement.setAttribute(PdfName.SUBTYPE, new PdfName("Paragraph"));
        return pdfStructureElement;
    }
}
