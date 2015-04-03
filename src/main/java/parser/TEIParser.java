package parser;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
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

        HeaderTEIElement currentHeaderElement = null;
        Map<Integer, HeaderTEIElement> headerLevelMap = new HashMap<Integer, HeaderTEIElement>();
        headerLevelMap.put(-1, null);

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                String nodeName = node.getNodeName();
                if (nodeName.equals("head")) {
                    if (currentHeaderElement == null) {
                        currentHeaderElement = new HeaderTEIElement(node.getTextContent(), 0);
                        teiDocument.addTEIElement(currentHeaderElement);
                    } else {
                        int level = getHeaderLevel(node);
                        if (currentHeaderElement.getLevel() == level) {
                            currentHeaderElement = new HeaderTEIElement(node.getTextContent(), headerLevelMap.get(level - 1), level);
                        } else if (level > currentHeaderElement.getLevel()) {
                            currentHeaderElement = new HeaderTEIElement(node.getTextContent(), currentHeaderElement, level);
                        } else if (level < currentHeaderElement.getLevel()) {
                            currentHeaderElement = new HeaderTEIElement(node.getTextContent(), headerLevelMap.get(level - 1), level);
                        }
                        if (level == 0) {
                            teiDocument.addTEIElement(currentHeaderElement);
                        }
                    }
                    headerLevelMap.put(currentHeaderElement.getLevel(), currentHeaderElement);
                } else if (nodeName.equals("p")) {
                    ParagraphTEIElement paragraphTEIElement = new ParagraphTEIElement(node.getTextContent(), currentHeaderElement);
                    if (currentHeaderElement == null) {
                        teiDocument.addTEIElement(paragraphTEIElement);
                    }
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
