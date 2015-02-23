package parser;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Niket on 22/02/15.
 */
public class TEIParser {

    public TEIDocument parseTEIXMLFile(String filePath) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(new File(filePath));
        document.getDocumentElement().normalize();
        Node bodyNode = document.getElementsByTagName("body").item(0);
        return parseBodyNode(bodyNode);
    }

    private TEIDocument parseBodyNode(Node bodyNode) {
        TEIDocument teiDocument = new TEIDocument();
        NodeList nodeList = bodyNode.getChildNodes();
        TEIElement currentHeaderElement = null;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            int currentLevel = 0;
            Map<Integer, TEIElement> stack = new HashMap<Integer, TEIElement>();
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                String nodeName = node.getNodeName();

                TEIElement teiElement = null;
                if (nodeName.equals("head")) {
                    int headerLevel = getHeaderLevel(node);
                    if (headerLevel > currentLevel) {
                        if (currentHeaderElement != null) {
                            teiElement = new HeaderTEIElement(node.getTextContent(), currentHeaderElement, headerLevel);
                            stack.put(currentLevel, currentHeaderElement);
                            currentHeaderElement = teiElement;
                        }
                        currentLevel++;
                    }
                    if (headerLevel < currentLevel && teiElement.getParentElement() != null) {
                        teiElement = stack.remove(currentLevel);
                        currentLevel--;
                    }
                    if (headerLevel == currentLevel) {
                        if (currentLevel > 0) {
                            currentHeaderElement = new HeaderTEIElement(node.getTextContent(), stack.get(currentLevel -1), currentLevel - 1);
                        } else {
                            currentHeaderElement = new HeaderTEIElement(node.getTextContent());
                            teiDocument.addTEIElement(currentHeaderElement);
                        }
                        stack.put(currentLevel, currentHeaderElement);
                    }
                } else if (nodeName.equals("p")) {
                    teiElement = new ParagraphTEIElement(node.getTextContent(), currentHeaderElement);
                }
            }
        }
        return teiDocument;
    }


    private int getHeaderLevel(Node node) {
        assert node.getNodeName().equals("head");
        NamedNodeMap namedNodeMap = node.getAttributes();
        String levelString = namedNodeMap.getNamedItem("n").getTextContent();
        return StringUtils.countMatches(levelString, ".");
    }

}
