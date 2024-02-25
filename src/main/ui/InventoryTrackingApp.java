package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Store inventory tracking application
public class InventoryTrackingApp {
    private Inventory inventory;
    private Scanner scanner = new Scanner(System.in);
    private final String saveLocation = "./data/InventoryTrackingSave.json";

    // EFFECTS: runs the  Store Inventory Tracking application with an empty inventory
    public InventoryTrackingApp() {
        inventory = new Inventory();
        start();
    }

    // MODIFIES: this
    // EFFECTS: displays menu of available options which can be selected by user
    public void start() {
        while (true) {
            showMenu();
            String input = scanner.nextLine();
            input = input.toLowerCase();

            if (input.equals("q")) {
                break;
            } else {
                selectInput(input);
            }
        }
    }

    // EFFECTS: displays all available functions of the app
    public void showMenu() {
        System.out.println("\nWhat do you want to do?");
//        System.out.println("\ta - add a product");
//        System.out.println("\td - delete a product");
        System.out.println("\tp - view product options");
        System.out.println("\tv - view all products");
//        System.out.println("\ts - sell a product");
        System.out.println("\tl - view low stock products");
//        System.out.println("\tr - restock a product");
        System.out.println("\tn - save inventory");
        System.out.println("\tm - load inventory");
        System.out.println("\tq - quit app");
    }

    // EFFECTS: does a function according to given input.
    public void selectInput(String input) {
        switch (input) {
//            case "a":
//                inputtedAdd();
//                break;
//            case "d":
//                inputtedDelete();
//                break;
            case "v":
                inputtedViewAll();
                break;
//            case "s":
//                inputtedSell();
//                break;
            case "l":
                inputtedSeeLowStock();
                break;
//            case "r":
//                inputtedRestock();
//                break;
            case "n":
                inputtedSave();
                break;
            case "m":
                inputtedLoad();
                break;
            case "p":
                inputtedProductOptions();
                break;
        }
    }

    // EFFECTS: displays menu of options that can be performed on an individual product,
    // which can be selected by user
    public void inputtedProductOptions() {
        while (true) {
            showProductMenu();
            String input = scanner.nextLine();
            input = input.toLowerCase();

            if (input.equals("q")) {
                break;
            } else {
                selectProductInput(input);
            }
        }
    }

    // EFFECTS: displays all available functions that can be performed on individual products
    public void showProductMenu() {
        System.out.println("\nWhat do you want to do with a product?");
        System.out.println("\ta - add a product");
        System.out.println("\td - delete a product");
        System.out.println("\ts - sell a product");
        System.out.println("\tr - restock a product");
        System.out.println("\tq - go back to main menu");
    }

    // EFFECTS: does a function according to the input
    public void selectProductInput(String input) {
        switch (input) {
            case "a":
                inputtedAdd();
                break;
            case "d":
                inputtedDelete();
                break;
            case "s":
                inputtedSell();
                break;
            case "r":
                inputtedRestock();
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: directs user to create a new product, and adds product to
    //          inventory
    public void inputtedAdd() {
        System.out.println("What is the product's name?");
        String productName = scanner.nextLine();
        System.out.println("How many units of " + productName + " were ordered?");
        int productQuantity = scanner.nextInt();
        scanner.nextLine();
        System.out.println("What is the cost to order a single unit of " + productName
                + " from the distributor?");
        double productCost = scanner.nextDouble();
        scanner.nextLine();
        inventory.addProduct(new Product(productName, productQuantity, productCost));
        System.out.println(productName + " has been added to the inventory!");
    }

    // MODIFIES: this
    // EFFECTS: removes the given product from the inventory
    public void inputtedDelete() {
        System.out.println("What is the name of the product you want to delete?");
        String productName = scanner.nextLine();
        boolean removed = false;
        for (Product p : inventory.getProducts()) {
            if (p.getName().equals(productName)) {
                inventory.removeProduct(p);
                removed = true;
                break;
            }
        }
        if (removed) {
            System.out.println("The product " + productName + " has been deleted.");
        } else {
            System.out.println("Could not find " + productName + " in inventory...");
        }
    }

    // EFFECTS: outputs the names of all products in the inventory
    public void inputtedViewAll() {
        List<String> outputtedProducts = new ArrayList<>();
        if (inventory.getProducts().size() == 0) {
            System.out.println("There are no products in the inventory.");
        } else {
            for (Product p : inventory.getProducts()) {
                outputtedProducts.add(p.getName());
            }
            System.out.println(outputtedProducts);
        }
    }

    // MODIFIES: this
    // EFFECTS: removes some units from given product. If the product could not be found
    //          or user tried to sell more units than available, the method outputs error message.
    public void inputtedSell() {
        System.out.println("What is the name of the product you want to sell?");
        String productName = scanner.nextLine();
        Product productToSell = inventory.findProduct(productName);
        if (productToSell == null) {
            System.out.println("Could not find " + productName);
        }
        if (productToSell != null) {
            System.out.println("The product " + productName + " has " + productToSell.getQuantityAvailable()
                    + " units left. How many would you like to sell?");
            int sellQuantity = scanner.nextInt();
            scanner.nextLine();
            if (sellQuantity <= productToSell.getQuantityAvailable()) {
                productToSell.sell(sellQuantity);
                System.out.println("Sold " + sellQuantity + " unit(s) of " + productName + " !");
            } else {
                System.out.println("You cannot sell more units than you have!");
            }
        }
    }

    // EFFECTS: outputs the names of low stock or out of stock products
    public void inputtedSeeLowStock() {
        List<Product> lowStockProducts = inventory.viewLowStockProducts();
        List<String> lowStockProductNames = new ArrayList<>();
        for (Product p: lowStockProducts) {
            lowStockProductNames.add(p.getName());
        }
        if (lowStockProducts.size() == 0) {
            System.out.println("No products need to be restocked!");
        } else {
            System.out.println("Products that should be restocked are: " + lowStockProductNames);
        }
    }

    // MODIFIES: this
    // EFFECTS: restocks the given product to full capacity, and outputs the cost to restock.
    //          If the product could not be found, output an error message.
    public void inputtedRestock() {
        System.out.println("What product do you want to restock?");
        String productName = scanner.nextLine();
        Product productToRestock = inventory.findProduct(productName);
        if (productToRestock == null) {
            System.out.println("Could not find " + productName);
        }
        if (productToRestock != null) {
            double priceToRestock = productToRestock.restock(true);
            System.out.println("It costed $" + String.format("%.2f", priceToRestock)
                    + " to restock " + productName);
        }
    }

    // EFFECTS: saves the current state of the inventory and all its products
    public void inputtedSave() {
        JsonWriter writer = new JsonWriter(saveLocation);
        try {
            writer.fullSave(inventory);
        } catch (FileNotFoundException e) {
            System.out.println("Could not find the save location: " + saveLocation);
        }
        System.out.println("The inventory was successfully saved to" + saveLocation);
    }

    // MODIFIES: this
    // EFFECTS: loads an inventory from a json file, and overwrites the current inventory
    public void inputtedLoad() {
        JsonReader reader = new JsonReader(saveLocation);
        try {
            this.inventory = reader.read();
        } catch (IOException e) {
            System.out.println("Could not find an earlier save in: " + saveLocation);
        }
        System.out.println("Loaded from " + saveLocation + " successfully");
    }
}
