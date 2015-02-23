package parser;

import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfStructureElement;
import com.itextpdf.text.pdf.PdfStructureTreeRoot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Niket on 22/02/15.
 */
public class HeaderTEIElement extends TEIElement {

    private String content;
    private List<TEIElement> childElements;

    public HeaderTEIElement(String content, TEIElement parentElement, int level) {
        this(content);
        this.parentElement = parentElement;
        this.level = level;
    }

    public HeaderTEIElement(String content) {
        this.content = content;
        this.childElements = new ArrayList<TEIElement>();
        this.level = 0;
    }

    @Override
    public TEIElementType getElementType() {
        return TEIElementType.HEADER;
    }

    @Override
    public boolean isLeafNode() {
        return false;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public PdfStructureElement toPdfStructureElement(PdfStructureTreeRoot treeRoot) {
        return new PdfStructureElement(treeRoot, PdfName.H1);
    }

    public void setContent(String content) {
        this.content = content;
    }


    public int getLevel() {
        return level;
    }
}
