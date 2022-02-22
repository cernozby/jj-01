package ukol1;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DOMReceiptReaderWriter implements ReceiptReaderWriter {
    private String name = "";
    private String itin = "";
    private String itemName = "";
    private int total = 0;
    private int itemAmount = 0;
    private int itemUnitPrice = 0;
    private final ArrayList<Item> items = new ArrayList<>();

    public Receipt loadReceipt(InputStream input) throws Exception {

        Element root = this.getDocumentRoot(input);
        this.setTotal(root);

        for (int i = 0; i < root.getChildNodes().getLength(); i++) {
            this.setAttributeForReceive(root.getChildNodes().item(i), i);
        }

        return new Receipt(name, itin, total, items);
    }

    @Override
    public void storeReceipt(OutputStream output, Receipt receipt) throws Exception {
        Document doc = this.prepareDocument();

        Element rootElement = doc.createElement("receipt");
        rootElement.setAttribute("total", Integer.toString(receipt.getTotal()));
        doc.appendChild(rootElement);
        this.addName(receipt, doc, rootElement);
        this.addItin(receipt, doc, rootElement);

        Element items = doc.createElement("items");
        rootElement.appendChild(items);

        for (Item item: receipt.getItems()) {
            items.appendChild(this.setItem(doc, item));
        }

        this.makeOutput(doc, output);
    }

    private void addItin(Receipt receipt, Document doc, Element rootElement) {
        Element itinChild = doc.createElement("itin");
        itinChild.appendChild(doc.createTextNode(receipt.getItin()));
        rootElement.appendChild(itinChild);
    }

    private void addName(Receipt receipt, Document doc, Element rootElement) {
        Element nameChild = doc.createElement("name");
        nameChild.appendChild(doc.createTextNode(receipt.getName()));
        rootElement.appendChild(nameChild);
    }

    private void makeOutput(Document doc, OutputStream output) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);
        transformer.transform(source, result);
    }

    private Element setItem (Document doc, Item item) {
        Element newItem = doc.createElement("item");
        newItem.setAttribute("amount", Integer.toString(item.getAmount()));
        newItem.setAttribute("unitPrice", Integer.toString(item.getUnitPrice()));
        newItem.appendChild(doc.createTextNode(item.getName()));
        return newItem;
    }

    private Document prepareDocument() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        return documentBuilder.newDocument();
    }

    private Element getDocumentRoot(InputStream input) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.parse(input);

        return doc.getDocumentElement();
    }
;
    private void setTotal(Element root) {
        if (root.hasAttributes()) {
            total = Integer.parseInt(root.getAttributes().getNamedItem("total").getTextContent());
        }
    }

    private void setAttributeForReceive(Node node, int i) {
        if (node.getTextContent().length() > 0) {
            switch (i) {
                case 0 -> name = node.getTextContent();
                case 1 -> itin = node.getTextContent();
                case 2 -> this.setItemAttribute(node);
            }
        }
    }

    private void setItemAttribute(Node node) {
        for (int x = 0; x < node.getChildNodes().getLength(); x++) {
            Node nodeItems = node.getChildNodes().item(x);
            if (nodeItems.getTextContent().length() > 0 && nodeItems.hasAttributes()) {
                itemName = nodeItems.getTextContent();
                itemAmount = Integer.parseInt(nodeItems.getAttributes().getNamedItem("amount").getTextContent());
                itemUnitPrice = Integer.parseInt(nodeItems.getAttributes().getNamedItem("unitPrice").getTextContent());
                items.add(new Item(itemAmount, itemUnitPrice, itemName));
            }
        }
    }
}
