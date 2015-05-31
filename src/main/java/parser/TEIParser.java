package parser;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Niket on 22/02/15.
 */
public class TEIParser {

    public TEIDocument parseTEIXMLFile(String filePath, String assetPath) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(new File(filePath));
        document.getDocumentElement().normalize();
        Node bodyNode = document.getElementsByTagName("body").item(0);
        return parseBodyNode(bodyNode, assetPath);
    }

    private TEIDocument parseBodyNode(Node bodyNode, String assetPath) {
        TEIDocument teiDocument = new TEIDocument();
        NodeList divList = bodyNode.getChildNodes();
        List<Node> nodeList = removeDivs(divList);



        HeaderTEIElement currentHeaderElement = null;
        Map<Integer, HeaderTEIElement> headerLevelMap = new HashMap<Integer, HeaderTEIElement>();
        headerLevelMap.put(-1, null);

        for (int i = 0; i < nodeList.size(); i++) {
            Element node = (Element)nodeList.get(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (isHeader(node)) {
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
                } else if (isParagraph(node)) {
                    ParagraphTEIElement paragraphTEIElement = new ParagraphTEIElement(node.getTextContent(), currentHeaderElement);
                    if (currentHeaderElement == null) {
                        teiDocument.addTEIElement(paragraphTEIElement);
                    }
                } else if (isFigure(node)) {
                    String heading = "";
                    String filePath = null;

                    // get the figure heading
                    Element headNode = (Element)node.getElementsByTagName("head").item(0);
                    if (headNode != null) {
                        heading = headNode.getTextContent();
                    }

                    // get the figure graphicURL
                    Element graphicNode = (Element)node.getElementsByTagName("graphic").item(0);
                    if (graphicNode != null) {
                        filePath = graphicNode.getAttribute("url");
                    }

                    FigureTEIElement figureTEIElement = new FigureTEIElement(heading, filePath, currentHeaderElement, assetPath);
                    if (currentHeaderElement == null) {
                        teiDocument.addTEIElement(figureTEIElement);
                    }
                }
            }
        }
        return teiDocument;
    }

    private boolean isFigure(Node node) {
        return node.getNodeName().equals("figure");
    }

    private boolean isHeader(Node node) {
        return node.getNodeName().equals("head") && node.getAttributes().getNamedItem("n") != null;
    }

    private boolean isParagraph(Node node) {
        return node.getNodeName().equals("p");
    }

    private List<Node> removeDivs(NodeList nodeList) {
        List<Node> listWithoutDivs = new ArrayList<Node>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (node.getNodeName() == "div") {
                    listWithoutDivs.addAll(removeDivs(node.getChildNodes()));
                } else {
                    listWithoutDivs.add(node);
                }
            }
        }
        return listWithoutDivs;
    }


    private int getHeaderLevel(Node node) {
        assert node.getNodeName().equals("head");
        NamedNodeMap namedNodeMap = node.getAttributes();
        String levelString = namedNodeMap.getNamedItem("n").getTextContent();
        return StringUtils.countMatches(levelString, ".");
    }

}
