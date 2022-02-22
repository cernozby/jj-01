package ukol1;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;

public class SAXReceiptReaderWriter implements ReceiptReaderWriter {

    @Override
    public Receipt loadReceipt(InputStream input) throws Exception {

        var parserFactory = SAXParserFactory.newInstance();
        var parser = parserFactory.newSAXParser();

        ReceiptHandler receiptHandler = new ReceiptHandler();
        parser.parse(input, receiptHandler);

        return receiptHandler.getReceipt();
    }

    @Override
    public void storeReceipt(OutputStream output, Receipt receipt) throws Exception {
        throw new SAXException("Invalid operation");
    }
}
