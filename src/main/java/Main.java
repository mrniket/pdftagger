import com.itextpdf.text.DocumentException;
import org.xml.sax.SAXException;
import parser.TEIDocument;
import parser.TEIParser;
import tagger.StructureTreeInserter;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by Niket on 23/12/14.
 */
public class Main {
    public static final String RESOURCES_DIR = "src/main/resources/";

    /**
     * Main method.
     * @param    args    no arguments needed
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, DocumentException {
        TEIParser parser = new TEIParser();
        TEIDocument teiDocument = parser.parseTEIXMLFile(RESOURCES_DIR + "KLEE.tei.xml");
        StructureTreeInserter structureTreeInserter = new StructureTreeInserter();
        structureTreeInserter.addStructureTreeToDocument(RESOURCES_DIR + "KLEE.pdf", teiDocument);
    }
}
