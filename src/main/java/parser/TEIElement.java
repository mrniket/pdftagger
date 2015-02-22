package parser;

/**
 * Created by Niket on 22/02/15.
 */
public interface TEIElement {

    public TEIElementType getElementType();

    public boolean isLeafNode();

    public String getContent();
}
