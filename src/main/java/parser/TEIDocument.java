package parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Niket on 22/02/15.
 */
public class TEIDocument {

    private ArrayList<TEIElement> body;

    public TEIDocument() {
        body = new ArrayList<TEIElement>();
    }

    public void addTEIElement(TEIElement element) {
        body.add(element);
    }

    public List<TEIElement> getBody() {
        return body;
    }

}
