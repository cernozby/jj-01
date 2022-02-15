package ukol1;

import java.awt.image.AreaAveragingScaleFilter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

class mainClass {
    public static void main(String[] args) throws Exception {

        Item item1 = new Item(10, 100, "kolobezka");
        Item item2 = new Item(12, 11, "hokejka");
        Item item3 = new Item(13, 123, "auto");

        ArrayList<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        items.add(item3);

        Receipt receipt = new Receipt("comp. s.r.o", "supr", 10000, items);

        OutputStream output = new OutputStream() {
            private StringBuilder string = new StringBuilder();

            @Override
            public void write(int b) throws IOException {
                this.string.append((char) b );
            }

            public String toString() {
                return this.string.toString();
            }
        };


        InputStream input1 = new ByteArrayInputStream("<?xml version=\"1.0\" ?><receipt total=\"1642\"><name>ACME corp.</name><itin>CZ12345678</itin><items><item amount=\"50\" unitPrice=\"24\">Nitroglycerin</item><item amount=\"4\" unitPrice=\"100\">Jet Propelled Pogo Stick</item><item amount=\"1\" unitPrice=\"42\">Hen Grenade</item></items></receipt>".getBytes());
        InputStream input2 = new ByteArrayInputStream("<?xml version=\"1.0\" ?><receipt total=\"1642\"><name>ACME corp.</name><itin>CZ12345678</itin><items><item amount=\"50\" unitPrice=\"24\">Nitroglycerin</item><item amount=\"4\" unitPrice=\"100\">Jet Propelled Pogo Stick</item><item amount=\"1\" unitPrice=\"42\">Hen Grenade</item></items></receipt>".getBytes());
        InputStream input3 = new ByteArrayInputStream("<?xml version=\"1.0\" ?><receipt total=\"1642\"><name>ACME corp.</name><itin>CZ12345678</itin><items><item amount=\"50\" unitPrice=\"24\">Nitroglycerin</item><item amount=\"4\" unitPrice=\"100\">Jet Propelled Pogo Stick</item><item amount=\"1\" unitPrice=\"42\">Hen Grenade</item></items></receipt>".getBytes());

        //sax
        var saxReceipt = new SAXReceiptReaderWriter();
        System.out.println(saxReceipt.loadReceipt(input1));

        //dom
        DOMReceiptReaderWriter domReceipt = new DOMReceiptReaderWriter();
        System.out.println(domReceipt.loadReceipt(input2));
        domReceipt.storeReceipt(output, receipt);

        //stax
        StAXReceiptReaderWriter stAXReceiptReaderWriter = new StAXReceiptReaderWriter();
        System.out.println(stAXReceiptReaderWriter.loadReceipt(input3));

        stAXReceiptReaderWriter.storeReceipt(output, receipt);
        System.out.println(output);
    }
}
