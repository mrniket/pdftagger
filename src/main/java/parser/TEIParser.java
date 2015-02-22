package parser;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

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
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                String nodeName = node.getNodeName();
                TEIElement teiElement = null;
                if (nodeName.equals("head")) {
                    teiElement = new HeaderTEIElement(node.getTextContent());
                } else if (nodeName.equals("p")) {
                    teiElement = new ParagraphTEIElement(node.getTextContent());
                }
                if (teiElement != null) {
                    teiDocument.addTEIElement(teiElement);
                }
            }
        }
        return teiDocument;
    }

}
