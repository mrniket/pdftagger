package parser;

import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfString;
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

    public HeaderTEIElement(String content, HeaderTEIElement parentElement, int level) {
        this(content);
        this.parentElement = parentElement;
        this.level = level;
        if (parentElement != null) {
            parentElement.addChildElement(this);
        }
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
        PdfStructureElement pdfStructureElement = new PdfStructureElement(treeRoot, PdfName.H1);
        pdfStructureElement.setAttribute(PdfName.SUBTYPE, new PdfName("Header"));
        for (TEIElement element : this.getChildElements()) {
            element.toPdfStructureElement(pdfStructureElement);
        }
        pdfStructureElement.setAttribute(PdfName.ACTUALTEXT, new PdfString(getContent()));
        return pdfStructureElement;
    }

    @Override
    public PdfStructureElement toPdfStructureElement(PdfStructureElement parent) {
        PdfStructureElement pdfStructureElement = new PdfStructureElement(parent, PdfName.H1);
        pdfStructureElement.setAttribute(PdfName.SUBTYPE, new PdfName("Header"));
        for (TEIElement element : this.getChildElements()) {
            element.toPdfStructureElement(pdfStructureElement);
        }
        pdfStructureElement.setAttribute(PdfName.ACTUALTEXT, new PdfString(getContent()));
        return pdfStructureElement;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public int getLevel() {
        return level;
    }

    public void addChildElement(TEIElement element) {
        childElements.add(element);
    }

    private List<TEIElement> getChildElements() {
        return childElements;
    }
}
