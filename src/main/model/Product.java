package model;

import org.json.JSONObject;
import persistence.Writable;

// Represents a product in a store with a name, maximum capacity, quantity available
// to be sold, quantity of product already sold, and the cost to restock one unit of product.
public class Product implements Writable {
    private final String name; // name of product
    private final int quantityMaximum; // number of units of product when full
    private int quantityAvailable; // quantity of product available in the store
    private int quantitySold; // number of units of product sold
    private double cost; // price to restock one unit of product, in dollars

    // REQUIRES: quantityOrdered > 0, cost > 0
    // EFFECTS: constructs a product with given name and per-unit price from manufacturer.
    //          The quantity of product ordered is the available quantity, and no
    //          units of product have been sold yet. The quantity of units of product
    //          ordered is the maximum amount of stock the store can carry of that product.
    public Product(String name, int quantityOrdered, double cost) {
        this.name = name;
        this.quantityMaximum = quantityOrdered;
        this.quantityAvailable = quantityOrdered;
        this.quantitySold = 0;
        this.cost = cost;
    }

    // REQUIRES: unitsSold <= quantityAvailable
    // MODIFIES: this
    // EFFECTS: deducts the units of product sold from the available quantity,
    //          and adds it to the quantity sold
    //          Additionally, logs the event into EventLog
    public void sell(int unitsSold) {
        this.quantityAvailable = quantityAvailable - unitsSold;
        this.quantitySold = quantitySold + unitsSold;
        EventLog.getInstance().logEvent(new Event("Product sold"));
    }

    // MODIFIES: this
    // EFFECTS: outputs the cost to restock the product to maximum quantity. If restockingEnabled
    //          is true, then also restock the product to maximum quantity, set available
    //          quantity to maximum quantity, and reset quantity sold back to 0.
    //          Additionally, logs the event into EventLog
    public double restock(Boolean restockingEnabled) {
        double costToRestock = quantitySold * cost;
        if (restockingEnabled) {
            this.quantityAvailable = quantityMaximum;
            this.quantitySold = 0;
            EventLog.getInstance().logEvent(new Event("Product restocked"));
        }
        return costToRestock;
    }

    // EFFECTS: converts the product's data into JSON
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("quantityMaximum", quantityMaximum);
        json.put("quantityAvailable", quantityAvailable);
        json.put("quantitySold", quantitySold);
        json.put("cost", cost);
        return json;
    }

    // getters
    public String getName() {
        return name;
    }

    public int getQuantityMaximum() {
        return quantityMaximum;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public double getCost() {
        return cost;
    }
}
