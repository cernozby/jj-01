package ukol1;

public class Item {
    private int amount;
    private int unitPrice;
    private String name;


    public Item() {}

    /**
     * Konstruktor
     *
     * @param amount
     * @param unitPrice
     * @param name
     */
    public Item(int amount, int unitPrice, String name) {
        this.amount = amount;
        this.unitPrice = unitPrice;
        this.name = name;
    }


    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
