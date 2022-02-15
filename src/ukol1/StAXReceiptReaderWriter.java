package ukol1;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;

public class StAXReceiptReaderWriter implements ReceiptReaderWriter {
    @Override
    public Receipt loadReceipt(InputStream input) throws Exception {

        String actualElement = "";
        Item item = new Item();
        Receipt receipt = new Receipt();

        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(input);

        int level = 0;

        boolean done = false;
        while (!done) {
            switch (reader.getEventType()) {
                case XMLStreamReader.START_ELEMENT -> {
                    actualElement = reader.getName().toString();
                    padding(level++);
                    switch (actualElement) {
                        case "receipt" -> {receipt.setTotal(Integer.parseInt(reader.getAttributeValue(null, "total")));}
                        case "item" -> {
                            item.setAmount(Integer.parseInt(reader.getAttributeValue(null, "amount")));
                            item.setUnitPrice(Integer.parseInt(reader.getAttributeValue(null, "unitPrice")));
                        }
                    }
                }
                case XMLStreamReader.END_ELEMENT -> {
                    if (reader.getName().toString().equals("item")) {
                        receipt.addToItems(item);
                        item = new Item();
                    }
                }
                case XMLStreamReader.CHARACTERS -> {
                    switch (actualElement) {
                        case "name" -> {receipt.setName(reader.getText());}
                        case "itin" -> {receipt.setItin(reader.getText());}
                        case "item" -> {item.setName(reader.getText());}
                    }
                }

                default -> {}
            }
            if (reader.hasNext()) reader.next();
            else done = true;
        }

        return receipt;
    }

    private static void padding(int level) {
        for (int i = 0; i < level; i++)
            System.out.print("   ");
    }

    @Override
    public void storeReceipt(OutputStream output, Receipt receipt) throws Exception {
        StringWriter buf = new StringWriter();

        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
        XMLStreamWriter xmlWriter = xmlOutputFactory.createXMLStreamWriter(output);

        xmlWriter.writeStartDocument();

        xmlWriter.writeStartElement("receipt");
        xmlWriter.writeAttribute("total", Integer.toString(receipt.getTotal()));

        xmlWriter.writeStartElement("name");
        xmlWriter.writeCharacters(receipt.getName());
        xmlWriter.writeEndElement();

        xmlWriter.writeStartElement("itin");
        xmlWriter.writeCharacters(receipt.getItin());
        xmlWriter.writeEndElement();

        xmlWriter.writeStartElement("items");
        for (Item myItem: receipt.getItems()) {
            xmlWriter.writeStartElement("item");
            xmlWriter.writeAttribute("amount", Integer.toString(myItem.getAmount()));
            xmlWriter.writeAttribute("unitPrice", Integer.toString(myItem.getUnitPrice()));
            xmlWriter.writeCharacters(myItem.getName());
            xmlWriter.writeEndElement();
        }

        xmlWriter.writeEndElement();
        xmlWriter.writeEndElement();

    }
}
