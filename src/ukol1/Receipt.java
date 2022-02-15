package ukol1;

import java.util.ArrayList;

public class Receipt {
    private String name;
    private String itin;
    private int total;
    private ArrayList<Item> items = new ArrayList<Item>();

    
    public Receipt() {}

    public Receipt(String name, String itin, int total, ArrayList<Item> items) {
        this.name = name;
        this.itin = itin;
        this.total = total;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItin() {
        return itin;
    }

    public void setItin(String itin) {
        this.itin = itin;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public void addToItems(Item item) {
        this.items.add(item);
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
