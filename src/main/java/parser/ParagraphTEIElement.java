package parser;

/**
 * Created by Niket on 22/02/15.
 */
public class ParagraphTEIElement implements TEIElement {

    private String content;
    private TEIElement parentElement;

    public ParagraphTEIElement(String content, TEIElement parentElement) {
        this(content);
        this.parentElement = parentElement;
    }

    public ParagraphTEIElement(String content) {
        this.content = content;
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
}
