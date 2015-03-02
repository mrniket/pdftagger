import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.MyTaggedPdfReaderTool;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import org.xml.sax.SAXException;
import parser.TEIDocument;
import parser.TEIParser;
import tagger.StructureTreeInserter;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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
//        List<LocationTextExtractionStrategy.TextChunk> textChunkList = getTextChunks();

        addStructuretoPDF();

//        String pdfPath = RESOURCES_DIR + "KLEE.pdf";
//        try {
//            String pGrobidHome = "/Users/Niket/github-repos/grobid/grobid-home";
//            String pGrobidProperties = "/Users/Niket/github-repos/grobid/grobid-home/config/grobid.properties";
//
//            MockContext.setInitialContext(pGrobidHome, pGrobidProperties);
//            GrobidProperties.getInstance();
//
//            System.out.println(">>>>>>>> GROBID_HOME="+GrobidProperties.get_GROBID_HOME_PATH());
//
//            Engine engine = GrobidFactory.getInstance().createEngine();
//
//            // Biblio object for the result
//            BiblioItem resHeader = new BiblioItem();
//            Document document = new Document(pdfPath, "");
//            String tei = engine.fullTextToTEI(pdfPath, false, false, null, -1, -1, true);
//            document.setTei(tei);
////            System.out.println(engine.getAllBlocksClean(document));
////            System.out.println(tei);
//        }
//        catch (Exception e) {
//            // If an exception is generated, print a stack trace
//            e.printStackTrace();
//        }
//        finally {
//            try {
//                MockContext.destroyInitialContext();
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    private static List<LocationTextExtractionStrategy.TextChunk> getTextChunks() throws IOException {
        PdfReader reader = new PdfReader(RESOURCES_DIR + "KLEE.pdf");
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        PrintWriter out = new PrintWriter(new FileOutputStream(RESOURCES_DIR + "extracted text"));
        TextExtractionStrategy strategy;
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            strategy
                    = parser.processContent(i, new LocationTextExtractionStrategy());
            out.println(strategy.getResultantText());
        }
        out.flush();
        out.close();
        return null;
    }

    private static void addStructuretoPDF() throws ParserConfigurationException, IOException, SAXException, DocumentException {
        TEIParser parser = new TEIParser();
        TEIDocument teiDocument = parser.parseTEIXMLFile(RESOURCES_DIR + "KLEE.tei.xml");
        StructureTreeInserter structureTreeInserter = new StructureTreeInserter();
        structureTreeInserter.addStructureTreeToDocument(RESOURCES_DIR + "KLEE.pdf", teiDocument);

        MyTaggedPdfReaderTool reader = new MyTaggedPdfReaderTool();
        reader.convertToXml(new PdfReader(RESOURCES_DIR + "test.pdf"),
                new FileOutputStream(RESOURCES_DIR + "test.xml"));

    }
}
