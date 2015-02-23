package parser;

import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfStructureElement;
import com.itextpdf.text.pdf.PdfStructureTreeRoot;

/**
 * Created by Niket on 22/02/15.
 */
public class ParagraphTEIElement extends TEIElement {

    private String content;

    public ParagraphTEIElement(String content, TEIElement parentElement) {
        this(content);
        this.parentElement = parentElement;
        this.level = parentElement.getLevel() + 1;
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
        return new PdfStructureElement(treeRoot, PdfName.P);
    }
}
