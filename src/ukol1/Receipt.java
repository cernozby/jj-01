package ukol1;

import java.util.ArrayList;

public class Receipt {
    private String name;
    private String itin;
    private int total;
    private ArrayList<Item> items;

    public Receipt(String name, String itin, int total, ArrayList<Item> items) {
        this.name = name;
        this.itin = itin;
        this.total = total;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public String getItin() {
        return itin;
    }

    public int getTotal() {
        return total;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "name='" + name + '\'' +
                ", itin='" + itin + '\'' +
                ", total=" + total +
                ", items=" + items +
                '}';
    }
}
