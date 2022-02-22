package ukol1;

import javax.sql.rowset.spi.XmlWriter;
import javax.xml.stream.*;
import java.io.*;
import java.util.ArrayList;

public class StAXReceiptReaderWriter implements ReceiptReaderWriter {

    private String name = "";
    private String itin = "";
    private String itemName = "";
    private int total = 0;
    private int itemAmount = 0;
    private int itemUnitPrice = 0;
    private final ArrayList<Item> items = new ArrayList<>();
    private Receipt receipt;
    private XMLStreamWriter xmlWriter;
    private String actualElement;
    private XMLStreamReader reader;

    @Override
    public Receipt loadReceipt(InputStream input) throws Exception {
        this.setReader(input);

        boolean done = false;
        while (!done) {
            this.readReceipt();
            if (reader.hasNext()) reader.next();
            else done = true;
        }

        return new Receipt(name, itin, total, items);
    }

    private void readReceipt() {
        switch (reader.getEventType()) {
            case XMLStreamReader.START_ELEMENT -> {actualElement = reader.getName().toString();this.readerStartElement();}
            case XMLStreamReader.END_ELEMENT -> {this.readerEndElement();}
            case XMLStreamReader.CHARACTERS -> {this.readerCharacters();}
            default -> {}
        }
    }

    private void readerCharacters() {
        switch (actualElement) {
            case "name" -> {name = reader.getText();}
            case "itin" -> {itin = reader.getText();}
            case "item" -> {itemName = reader.getText();}
        }
    }
    private void readerEndElement() {
        if (reader.getName().toString().equals("item")) {
            items.add(new Item(itemAmount, itemUnitPrice, itemName));
        }
    }
    private void readerStartElement() {
        switch (actualElement) {
            case "receipt" -> {total = Integer.parseInt(reader.getAttributeValue(null, "total"));}
            case "item" -> {
                itemAmount =Integer.parseInt(reader.getAttributeValue(null, "amount"));
                itemUnitPrice = Integer.parseInt(reader.getAttributeValue(null, "unitPrice"));
            }
        }
    }

    private void setReader(InputStream input) throws XMLStreamException {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        this.reader = xmlInputFactory.createXMLStreamReader(input);
    }

    @Override
    public void storeReceipt(OutputStream output, Receipt receipt) throws Exception {
        this.receipt = receipt;
        this.setXmlWriter(output);

        xmlWriter.writeStartDocument();
        xmlWriter.writeStartElement("receipt");

        this.seTotal();
        this.writeName();
        this.writeItin();

        xmlWriter.writeStartElement("items");
        for (Item myItem: receipt.getItems()) {
            writeItem(myItem);
        }

        xmlWriter.writeEndElement();
        xmlWriter.writeEndElement();
    }

    private void writeItem(Item item) throws XMLStreamException {
        xmlWriter.writeStartElement("item");
        xmlWriter.writeAttribute("amount", Integer.toString(item.getAmount()));
        xmlWriter.writeAttribute("unitPrice", Integer.toString(item.getUnitPrice()));
        xmlWriter.writeCharacters(item.getName());
        xmlWriter.writeEndElement();
    }
    private void writeName() throws XMLStreamException {
        xmlWriter.writeStartElement("name");
        xmlWriter.writeCharacters(receipt.getName());
        xmlWriter.writeEndElement();
    }

    private void writeItin() throws XMLStreamException {
        xmlWriter.writeStartElement("itin");
        xmlWriter.writeCharacters(receipt.getItin());
        xmlWriter.writeEndElement();
    }

    private void setXmlWriter (OutputStream output) throws XMLStreamException {
        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
        this.xmlWriter = xmlOutputFactory.createXMLStreamWriter(output);
    }
    private void seTotal() throws XMLStreamException {
        this.xmlWriter.writeAttribute("total", Integer.toString(receipt.getTotal()));
    }
}
