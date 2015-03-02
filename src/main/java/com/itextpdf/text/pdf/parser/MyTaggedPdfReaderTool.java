package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.PdfDictionary;

import java.io.IOException;

/**
 * Created by Niket on 26/02/15.
 */
public class MyTaggedPdfReaderTool extends TaggedPdfReaderTool {

    @Override
    public void inspectChildDictionary(PdfDictionary k) throws IOException {
        inspectChildDictionary(k, true);
    }
}
