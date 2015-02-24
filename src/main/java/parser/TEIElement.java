package parser;

import com.itextpdf.text.pdf.PdfStructureElement;
import com.itextpdf.text.pdf.PdfStructureTreeRoot;

/**
 * Created by Niket on 22/02/15.
 */
public abstract class TEIElement {

    protected int level;
    protected TEIElement parentElement;

    public abstract TEIElementType getElementType();

    public abstract boolean isLeafNode();

    public abstract String getContent();

    public abstract PdfStructureElement toPdfStructureElement(PdfStructureTreeRoot treeRoot);

    public abstract PdfStructureElement toPdfStructureElement(PdfStructureElement parent);

    public int getLevel() {
        return level;
    }

    public TEIElement getParentElement() {
        return parentElement;
    }
}
