package parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Niket on 22/02/15.
 */
public class HeaderTEIElement implements TEIElement {

    private String content;
    private List<TEIElement> childElements;
    private TEIElement parentElement;

    public HeaderTEIElement(String content, TEIElement parentElement) {
        this(content);
        this.parentElement = parentElement;
    }

    public HeaderTEIElement(String content) {
        this.content = content;
        this.childElements = new ArrayList<TEIElement>();
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

    public void setContent(String content) {
        this.content = content;
    }



}
