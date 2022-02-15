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

public class SAXReceiptReaderWriter implements ReceiptReaderWriter {
    @Override
    public Receipt loadReceipt(InputStream input) throws Exception {

        var parserFactory = SAXParserFactory.newInstance();
        var parser = parserFactory.newSAXParser();

        Receipt receipt = new Receipt();

        parser.parse(input, new DefaultHandler() {
            private String actualAlementName = "";
            Item item = new Item();


            @Override
            public void startElement(String uri, String localName,
                                     String qName, Attributes attributes) throws SAXException {
                actualAlementName = qName;

                switch (actualAlementName) {
                    case "item" -> {
                        item.setAmount(Integer.parseInt(attributes.getValue("amount")));
                        item.setUnitPrice(Integer.parseInt(attributes.getValue("unitPrice")));
                    }
                    case "receipt" -> {
                        receipt.setTotal(Integer.parseInt(attributes.getValue(0)));
                    }

                }
            }

            @Override
            public void endElement(String uri, String localName, String qName)
                    throws SAXException {
                if ("item".equals(qName)) {
                    receipt.addToItems(item);
                    item = new Item();
                }
            }

            @Override
            public void characters(char[] ch, int start, int length)
                    throws SAXException {
                switch (actualAlementName) {
                    case "name" -> {receipt.setName(new String(ch, start, length));}
                    case "itin" -> {receipt.setItin(new String(ch, start, length));}
                    case "item" -> {item.setName(new String(ch, start, length));}
                }
            }
        });

        return receipt;
    }

    @Override
    public void storeReceipt(OutputStream output, Receipt receipt) throws Exception {
        throw new SAXException("Invalid operation");
    }
}
