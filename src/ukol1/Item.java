package ukol1;

public class Item {
    private int amount;
    private int unitPrice;
    private String name;

    public Item(int amount, int unitPrice, String name) {
        this.amount = amount;
        this.unitPrice = unitPrice;
        this.name = name;
    }


    public int getAmount() {
        return amount;
    }


    public int getUnitPrice() {
        return unitPrice;
    }


    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Item{" +
                "amount=" + amount +
                ", unitPrice=" + unitPrice +
                ", name='" + name + '\'' +
                '}';
    }
}
