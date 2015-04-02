package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.xml.XMLUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Set;

/**
 * Created by Niket on 26/02/15.
 */
public class MyTaggedPdfReaderTool extends TaggedPdfReaderTool {

    private PdfDictionary roleMap;

    @Override
    public void inspectChildDictionary(PdfDictionary k) throws IOException {
        inspectChildDictionary(k, true);
    }

    @Override
    public void convertToXml(PdfReader reader, OutputStream os, String charset)
            throws IOException {
        this.reader = reader;
        OutputStreamWriter outs = new OutputStreamWriter(os, charset);
        out = new PrintWriter(outs);
        out.println("<Document>");
        // get the StructTreeRoot from the root object
        PdfDictionary catalog = reader.getCatalog();
        PdfDictionary struct = catalog.getAsDict(PdfName.STRUCTTREEROOT);
        roleMap = struct.getAsDict(PdfName.ROLEMAP);
        if (struct == null)
            throw new IOException(MessageLocalization.getComposedMessage("no.structtreeroot.found"));
        // Inspect the child or children of the StructTreeRoot
        inspectChild(struct.getDirectObject(PdfName.K));
        out.println("</Document>");
        out.flush();
        out.close();
    }

    /**
     * If the child of a structured element is a dictionary, we inspect the
     * child; we may also draw a tag.
     *
     * @param k
     *            the child dictionary to inspect
     */
    @Override
    public void inspectChildDictionary(PdfDictionary k, boolean inspectAttributes) throws IOException {
        if (k == null)
            return;
        PdfName s = k.getAsName(PdfName.S);
        if (s != null) {
            String tagN = PdfName.decodeName(s.toString());
            String tag;
            if (roleMap != null && roleMap.get(new PdfName(tagN)) != null) {
                tag = roleMap.get(new PdfName(tagN)).toString().substring(1);
            } else {
                tag = fixTagName(tagN);
            }
            out.print("<");
            out.print(tag);
            if (inspectAttributes) {
                PdfDictionary a = k.getAsDict(PdfName.A);
                if (a != null) {
                    Set<PdfName> keys =  a.getKeys();
                    for (PdfName key : keys) {
                        out.print(' ');
                        PdfObject value = a.get(key);
                        value = PdfReader.getPdfObject(value);
                        out.print(xmlName(key));
                        out.print("=\"");
                        out.print(XMLUtil.escapeXML(value.toString(), false));
                        out.print("\"");
                    }
                }
            }
            out.println(">");
            PdfObject alt = k.get(PdfName.ALT);
            if (alt != null && alt.toString() != null) {
                out.print("<alt><![CDATA[");
                out.print(alt.toString().replaceAll("[\\000]*", ""));
                out.print("]]></alt>");
            }
            PdfDictionary dict = k.getAsDict(PdfName.PG);
            if (dict != null)
                parseTag(tagN, k.getDirectObject(PdfName.K), dict);
            inspectChild(k.getDirectObject(PdfName.K));
            out.print("</");
            out.print(tag);
            out.println(">");
        } else
            inspectChild(k.getDirectObject(PdfName.K));
    }

    private static String fixTagName(String tag) {
        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < tag.length(); ++k) {
            char c = tag.charAt(k);
            boolean nameStart =
                    c == ':'
                            || (c >= 'A' && c <= 'Z')
                            || c == '_'
                            || (c >= 'a' && c <= 'z')
                            || (c >= '\u00c0' && c <= '\u00d6')
                            || (c >= '\u00d8' && c <= '\u00f6')
                            || (c >= '\u00f8' && c <= '\u02ff')
                            || (c >= '\u0370' && c <= '\u037d')
                            || (c >= '\u037f' && c <= '\u1fff')
                            || (c >= '\u200c' && c <= '\u200d')
                            || (c >= '\u2070' && c <= '\u218f')
                            || (c >= '\u2c00' && c <= '\u2fef')
                            || (c >= '\u3001' && c <= '\ud7ff')
                            || (c >= '\uf900' && c <= '\ufdcf')
                            || (c >= '\ufdf0' && c <= '\ufffd');
            boolean nameMiddle =
                    c == '-'
                            || c == '.'
                            || (c >= '0' && c <= '9')
                            || c == '\u00b7'
                            || (c >= '\u0300' && c <= '\u036f')
                            || (c >= '\u203f' && c <= '\u2040')
                            || nameStart;
            if (k == 0) {
                if (!nameStart)
                    c = '_';
            }
            else {
                if (!nameMiddle)
                    c = '-';
            }
            sb.append(c);
        }
        return sb.toString();
    }
}
