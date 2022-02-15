package ukol1;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.InputStream;
import java.io.OutputStream;

public class DOMReceiptReaderWriter implements ReceiptReaderWriter {

    public Receipt loadReceipt(InputStream input) throws Exception {
        Receipt receipt = new Receipt();

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.parse(input);

        Element root = doc.getDocumentElement();

        if (root.hasAttributes()) {
            receipt.setTotal(Integer.parseInt(root.getAttributes().getNamedItem("total").getTextContent()));
        }

        for (int i = 0; i < root.getChildNodes().getLength(); i++) {
            Node node = root.getChildNodes().item(i);

            if (node.getTextContent().length() > 0) {

                switch (i) {
                    case 0:
                        receipt.setName(node.getTextContent());
                    case 1:
                        receipt.setItin(node.getTextContent());
                    case 2:
                        for (int x = 0; x < node.getChildNodes().getLength(); x++) {
                            Node nodeItems = node.getChildNodes().item(x);
                            if (nodeItems.getTextContent().length() > 0 && nodeItems.hasAttributes()) {
                               Item item = new Item();
                               item.setName(nodeItems.getTextContent());
                               item.setAmount(Integer.parseInt(nodeItems.getAttributes().getNamedItem("amount").getTextContent()));
                               item.setUnitPrice(Integer.parseInt(nodeItems.getAttributes().getNamedItem("unitPrice").getTextContent()));
                               receipt.addToItems(item);
                            }
                        }
                }
            }
        }

        return receipt;
    }

    @Override
    public void storeReceipt(OutputStream output, Receipt receipt) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();

        Element rootElement = doc.createElement("receipt");
        rootElement.setAttribute("total", Integer.toString(receipt.getTotal()));
        doc.appendChild(rootElement);

        Element nameChild = doc.createElement("name");
        nameChild.appendChild(doc.createTextNode(receipt.getName()));
        rootElement.appendChild(nameChild);


        Element itinChild = doc.createElement("itin");
        itinChild.appendChild(doc.createTextNode(receipt.getItin()));
        rootElement.appendChild(itinChild);


        Element items = doc.createElement("items");
        rootElement.appendChild(items);

        for (Item item: receipt.getItems()) {
            Element newItem = doc.createElement("item");
            newItem.setAttribute("amount", Integer.toString(item.getAmount()));
            newItem.setAttribute("unitPrice", Integer.toString(item.getUnitPrice()));
            newItem.appendChild(doc.createTextNode(item.getName()));
            items.appendChild(newItem);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);
        transformer.transform(source, result);
    }
}
