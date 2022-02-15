package ukol1;

import java.io.InputStream;
import java.io.OutputStream;

public interface ReceiptReaderWriter {
    /**
     * Nacte ze streamu XML soubor a dle nej vytvori prislusny objekt reprezentujici uctenku
     */
    public Receipt loadReceipt(InputStream input) throws Exception;

    /**
     * Ulozi do prislusneho streamu XML soubor predstavujici danou uctenku
     */
    public void storeReceipt(OutputStream output, Receipt receipt) throws Exception;
}