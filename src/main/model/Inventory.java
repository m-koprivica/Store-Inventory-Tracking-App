package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// Represents the collection of all products in store
public class Inventory implements Writable {
    private static final int DEFAULT_THRESHOLD = 15;

    private List<Product> products; // list of all products in the store
    private int threshold; // percentage of products' maximum quantity that determines if it
                           // is nearly out of stock

    // EFFECTS: constructs a store inventory with no products
    public Inventory() {
        this.products = new ArrayList<>();
        this.threshold = DEFAULT_THRESHOLD;
    }

    // REQUIRES: 0 < threshold < 100
    // MODIFIES: this
    // EFFECTS: changes the percentage of when a product is deemed
    //          "nearly out of stock" to given value.
    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    // REQUIRES: product does not share the same name with other products in this.products
    // MODIFIES: this
    // EFFECTS: adds given product to the list of products and logs the event into EventLog
    public void addProduct(Product product) {
        products.add(product);
        EventLog.getInstance().logEvent(new Event("Product added to the inventory"));
    }

    // REQUIRES: product must be in list of products
    // MODIFIES: this
    // EFFECTS: removes given product from list of products and logs the event into EventLog
    public void removeProduct(Product product) {
        products.remove(product);
        EventLog.getInstance().logEvent(new Event("Product deleted from the inventory"));
    }

    // REQUIRES: productList.size() > 0
    // EFFECTS: shows products that are close to, or already are, out of stock.
    //          Inventory threshold is used to classify whether a product is close to
    //          being out of stock
    public List<Product> viewLowStockProducts() {
        List<Product> lowStockProducts = new ArrayList<>();
        for (Product p: products) {
            double stockPercent = (double) p.getQuantityAvailable() / p.getQuantityMaximum() * 100;
            if (stockPercent <= threshold) {
                lowStockProducts.add(p);
            }
        }
        return lowStockProducts;
    }

    // EFFECTS: returns the product with the given name. If no product can be found, return null.
    public Product findProduct(String productName) {
        for (Product p: products) {
            if (p.getName().equals(productName)) {
                return p;
            }
        }
        return null;
    }

    // EFFECTS: Turns all the inventory's products into a string. If there are no products, returns a string
    // that says there are no products.
    @Override
    public String toString() {
        StringBuilder listOfProducts = new StringBuilder();
        if (products.isEmpty()) {
            return "There are no products in the inventory.";
        }
//        listOfProducts.append("The products in the inventory are:\n");
        for (Product p: products) {
            listOfProducts.append(p.getName() + " has " + p.getQuantityAvailable() + "/" + p.getQuantityMaximum()
                    + " units left.\n");
        }
        return listOfProducts.toString();
    }

    // EFFECTS: converts the inventory's data into JSON, including its products
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("threshold", threshold);
        json.put("products", productsToJson());
        return json;
    }

    // EFFECTS: returns products in the inventory as a JSON array collection
    // helper method for toJson
    private JSONArray productsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Product p: products) {
            jsonArray.put(p.toJson());
        }
        return jsonArray;
    }



    // getters
    public int getThreshold() {
        return threshold;
    }

    public List<Product> getProducts() {
        return products;
    }
}
