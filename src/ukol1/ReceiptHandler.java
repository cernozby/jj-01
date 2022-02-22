package ukol1;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class ReceiptHandler extends DefaultHandler {
    private String actualElementName = "";
    private String name = "";
    private String itin = "";
    private String itemName = "";
    private int total = 0;
    private int itemAmount = 0;
    private int itemUnitPrice = 0;
    private final ArrayList<Item> items = new ArrayList<>();
    private Receipt receipt;

    @Override
    public void startElement(String uri, String localName,
                             String qName, Attributes attributes) throws SAXException {
        this.actualElementName = qName;
        this.setAttributeByAttribute(qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        initObjWhenElementEnd(qName);
    }


    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
       this.setAttributeByTextInside(ch, start, length);
    }

    public Receipt getReceipt() {
        return this.receipt;
    }

    public void initObjWhenElementEnd(String qName) {
        if ("item".equals(qName)) {
            items.add(new Item(itemAmount, itemUnitPrice, itemName));
        }
        if ("receipt".equals(qName)) {
            this.receipt = new Receipt(name, itin, total, items);

        }
    }

    private void setAttributeByAttribute(String name, Attributes attributes) {
        switch (name) {
            case "item" -> {
                itemAmount = Integer.parseInt(attributes.getValue("amount"));
                itemUnitPrice = Integer.parseInt(attributes.getValue("unitPrice"));
            }
            case "receipt" -> {
                total = Integer.parseInt(attributes.getValue(0));
            }

        }
    }

    private void setAttributeByTextInside(char[] ch, int start, int length) {
        switch (actualElementName) {
            case "name" -> {name = new String(ch, start, length);}
            case "itin" -> {itin = new String(ch, start, length);}
            case "item" -> {itemName = new String(ch, start, length);}
        }
    }

}
