package start;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.MyTaggedPdfReaderTool;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import org.apache.commons.cli.*;
import org.grobid.core.engines.Engine;
import org.grobid.core.factory.GrobidFactory;
import org.grobid.core.mock.MockContext;
import org.grobid.core.utilities.GrobidProperties;
import org.xml.sax.SAXException;
import parser.TEIDocument;
import parser.TEIParser;
import tagger.StructureTreeInserter;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
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
     * start.Main method.
     * @param    args    no arguments needed
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, DocumentException, ParseException {
        CommandLineParser commandLineParser = new BasicParser();
        Options options = new Options();
        options.addOption("mode", true, "Specify the mode (tag or parse) you want to run the program in");
        options.addOption("s", true, "Source file");
        options.addOption("o", true, "Output file");
        CommandLine cmd = commandLineParser.parse(options, args);

        String mode = cmd.getOptionValue("mode");

        if (mode.equals("tag")) {
            System.out.println("tag mode selected");
            String sourceFilePath = cmd.getOptionValue("s");
            String outputFilePath = cmd.getOptionValue("o");
            File outputFile = new File(outputFilePath);
            String assetPath = outputFile.getParent() + "/pdfimages/";
            new File(assetPath).mkdirs();
            parseStructureOfPDF(sourceFilePath, assetPath);
            addStructuretoPDF(sourceFilePath, assetPath, outputFilePath);
            System.out.println("done");
        } else if (mode.equals("parse")) {
            String sourceFilePath = cmd.getOptionValue("s");
            if (cmd.hasOption("o")) {
                String outputFilePath = cmd.getOptionValue("o");
                parsePDF(sourceFilePath, outputFilePath);
                System.out.println("done");
            } else {
                parsePDF(sourceFilePath);
            }
        } else if (mode.equals("check")) {
            if (cmd.hasOption("s")) {
                System.out.println(isTaggedPDF(cmd.getOptionValue("s")));
            } else {
                System.err.println("Please provide a source file to check");
            }
        }
    }

    private static boolean isTaggedPDF(String sourceFile) throws IOException {
        return new PdfReader(sourceFile).isTagged();
    }

    private static void parsePDF(String sourceFile, String outputFile) throws IOException {
        MyTaggedPdfReaderTool reader = new MyTaggedPdfReaderTool();
        reader.convertToXml(new PdfReader(sourceFile),
                new FileOutputStream(outputFile));
    }

    private static void parsePDF(String sourceFile) throws IOException {
        MyTaggedPdfReaderTool reader = new MyTaggedPdfReaderTool();
        reader.convertToXml(new PdfReader(sourceFile), System.out);
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

    private static String parseStructureOfPDF(String pdfFilePath, String assetPath) {
        String pdfPath = RESOURCES_DIR + "KLEE.pdf";
        try {
            String pGrobidHome = "/Users/Niket/github-repos/grobid/grobid-home";
            String pGrobidProperties = "/Users/Niket/github-repos/grobid/grobid-home/config/grobid.properties";

            MockContext.setInitialContext(pGrobidHome, pGrobidProperties);
            GrobidProperties.getInstance();

            System.out.println(">>>>>>>> GROBID_HOME="+GrobidProperties.get_GROBID_HOME_PATH());

            Engine engine = GrobidFactory.getInstance().createEngine();

            String tei = engine.fullTextToTEI(pdfFilePath, false, false, assetPath, -1, -1, false);
            PrintWriter out = new PrintWriter(new FileOutputStream("extracted.tei.xml"));
            out.println(tei);
            out.close();
        }
        catch (Exception e) {
            // If an exception is generated, print a stack trace
            e.printStackTrace();
        }
        finally {
            try {
                MockContext.destroyInitialContext();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    private static void addStructuretoPDF(String inputFile, String assetPath, String outputFile) throws ParserConfigurationException, IOException, SAXException, DocumentException {
        TEIParser parser = new TEIParser();
        TEIDocument teiDocument = parser.parseTEIXMLFile("extracted.tei.xml", assetPath);
        StructureTreeInserter structureTreeInserter = new StructureTreeInserter();
        structureTreeInserter.addStructureTreeToDocument(inputFile, outputFile, teiDocument);
    }
}
