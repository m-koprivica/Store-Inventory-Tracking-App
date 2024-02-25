package ui;

import model.Event;
import model.EventLog;
import model.Inventory;
import model.Product;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import static java.awt.event.WindowEvent.WINDOW_CLOSED;

// The graphical UI of the Inventory Tracking Application.
// I used McGrath's "JAVA in easy steps, fifth edition" to learn JSwing
public class GraphicalUI extends JFrame implements ActionListener {
    private Inventory inventory;

    private final ImageIcon logo = new ImageIcon("src/main/ui/logo.jpg");
    // image is from https://stock.adobe.com/ca/search/images?k=inventory+management+icon

    private final JLabel welcomeMessage = new JLabel("Welcome to the Inventory Tracking App!");

    private JButton viewAllProductsButton;
    private JButton viewLowStockProductsButton;
    private JButton saveInventoryButton;
    private JButton loadInventoryButton;
    private JButton exitAppButton;

    private JButton addProductButton;
    private JButton deleteProductButton;
    private JButton sellProductButton;
    private JButton restockProductButton;

    private Container contentPane;

    private JPanel topPanel;
    private JPanel bottomRowPanel;
    private JPanel viewInventoryPanel;
    private JPanel logoPanel;
    private JPanel productPanel;

    private final String saveLocation = "./data/InventoryTrackingSave.json";


    // EFFECTS: runs the graphical UI of the Store Inventory Tracking application with an empty inventory
    public GraphicalUI() {
        super("Inventory Tracking App");
        inventory = new Inventory();
        initializeWindow();

        topPanel = new JPanel();
        topPanel.add(welcomeMessage);
        initializeBottomRowOptions();
        initializeInventoryOptions();
        logoPanel = new JPanel();
        logoPanel.add(new JLabel(logo));
        initializeProductMenu();

        contentPane = getContentPane();
        contentPane.add("North", topPanel);
        contentPane.add("South", bottomRowPanel);
        contentPane.add("West", viewInventoryPanel);
        contentPane.add("East", productPanel);
        contentPane.add("Center", logoPanel);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            // EFFECTS: prints out all events in the event log to the console when the application is closed
            @Override
            public void windowClosed(WindowEvent e) {
                EventLog el = EventLog.getInstance();
                for (Event event : el) {
                    System.out.println(event.toString());
                }
            }
        }); // Now listening for window events

        validate();
        repaint();


    }


    // EFFECTS: creates the application's window
    public void initializeWindow() {
        setSize(850, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    // EFFECTS: Creates buttons for viewing all products
    public void initializeInventoryOptions() {
        viewInventoryPanel = new JPanel(new GridLayout(2, 1, 5, 1));

        viewAllProductsButton = new JButton("View all products");
        viewLowStockProductsButton = new JButton("View low stock products");

        viewAllProductsButton.addActionListener(this);
        viewLowStockProductsButton.addActionListener(this);

        viewInventoryPanel.add(viewAllProductsButton);
        viewInventoryPanel.add(viewLowStockProductsButton);
    }

    // EFFECTS: Creates buttons for saving, loading and exiting the app
    public void initializeBottomRowOptions() {
        bottomRowPanel = new JPanel();

        saveInventoryButton = new JButton("Save");
        loadInventoryButton = new JButton("Load");
        exitAppButton = new JButton("Quit");

        saveInventoryButton.addActionListener(this);
        loadInventoryButton.addActionListener(this);
        exitAppButton.addActionListener(this);

        bottomRowPanel.add(saveInventoryButton);
        bottomRowPanel.add(loadInventoryButton);
        bottomRowPanel.add(exitAppButton);
    }

    // EFFECTS: Shows options relating to individual products
    public void initializeProductMenu() {
        productPanel = new JPanel(new GridLayout(2, 2, 5, 1));

        addProductButton = new JButton("Add a product");
        deleteProductButton = new JButton("Delete a product");
        sellProductButton = new JButton("Sell a product");
        restockProductButton = new JButton("Restock a product");

        addProductButton.addActionListener(this);
        deleteProductButton.addActionListener(this);
        sellProductButton.addActionListener(this);
        restockProductButton.addActionListener(this);

        productPanel.add(addProductButton);
        productPanel.add(deleteProductButton);
        productPanel.add(sellProductButton);
        productPanel.add(restockProductButton);
    }

    // EFFECTS: performs an action based on which button was pressed
    @Override
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == viewAllProductsButton) {
            inputtedViewAllProducts();
        }
        if (event.getSource() == viewLowStockProductsButton) {
            inputtedViewLowStockProducts();
        }
        if (event.getSource() == addProductButton) {
            inputtedAddProduct();
        }
        if (event.getSource() == deleteProductButton) {
            inputtedDeleteProduct();
        }
        if (event.getSource() == sellProductButton) {
            inputtedSellProduct();
        }
        if (event.getSource() == restockProductButton) {
            inputtedRestockProduct();
        }
        if (event.getSource() == saveInventoryButton) {
            inputtedSaveInventory();
        }
        if (event.getSource() == loadInventoryButton) {
            inputtedLoadInventory();
        }
        if (event.getSource() == exitAppButton) {
            inputtedExitApp();
        }
    }

    // EFFECTS: shows all the products in the inventory, along with how many there are in stock.
    private void inputtedViewAllProducts() {
        if (inventory.getProducts().isEmpty()) {
            JOptionPane.showMessageDialog(this, inventory.toString(),
                    "Inventory", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "The products in the inventory are:\n" + inventory.toString(),
                    "Inventory",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // EFFECTS: shows all low stock products in the inventory, along with how many there are in stock.
    private void inputtedViewLowStockProducts() {
        List<Product> lowStockProducts = inventory.viewLowStockProducts();
        if (lowStockProducts.isEmpty()) {
            JOptionPane.showMessageDialog(this, "There are no low stock products!",
                    "Low-Stock Product Finder", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder message = new StringBuilder();
            message.append("Products that are in low stock are:\n");
            for (Product p: inventory.viewLowStockProducts()) {
                message.append(p.getName() + " with " + p.getQuantityAvailable() + "/" + p.getQuantityMaximum()
                        + " units left\n");
            }
            JOptionPane.showMessageDialog(this, message.toString(),
                    "Low-Stock Product Finder", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // MODIFIES: this
    // EFFECTS: creates a product specified by the user and adds it to the inventory
    private void inputtedAddProduct() {
        String productName = promptAddProductName();
        if (productName == null) {
            operationCancelled();
        } else {
            String productQuantityNotParsed = promptAddProductQuantity();
            if (productQuantityNotParsed == null || productQuantityNotParsed.equals("")) {
                operationCancelled();
            } else {
                int productQuantity = Integer.parseInt(productQuantityNotParsed);
                String productCostNotParsed = promptAddProductCost(productName);
                if (productCostNotParsed == null || productCostNotParsed.equals("")) {
                    operationCancelled();
                } else {
                    double productCost = Double.parseDouble(productCostNotParsed);
                    inventory.addProduct(new Product(productName, productQuantity, productCost));
                    messageAddProductSuccess(productName);
                }
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: deletes the product specified by the user and removes it from the inventory
    private void inputtedDeleteProduct() {
        String productName = JOptionPane.showInputDialog(this,
                "What is the name of the product you want to delete?",
                "Product Deleter",
                JOptionPane.PLAIN_MESSAGE);
        Product toBeDeleted = inventory.findProduct(productName);
        if (productName == null) {
            operationCancelled();
        } else if (toBeDeleted == null) {
            cannotFindProductError(productName);
        } else {
            inventory.removeProduct(toBeDeleted);
            JOptionPane.showMessageDialog(this,
                    "Product " + productName + " has been successfully deleted!",
                    "Product Deleted",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // MODIFIES: this
    // EFFECTS: sells the product specified by the user.
    private void inputtedSellProduct() {
        String productName = promptSellProductName();
        Product productToSell = inventory.findProduct(productName);
        checkForInputErrorAndCancel(productName, productToSell);
        if (productToSell != null) {
            String sellQuantityNotParsed = promptSellProductQuantity(productName, productToSell);
            if (sellQuantityNotParsed == null) {
                operationCancelled();
            } else {
                int sellQuantity = Integer.parseInt(sellQuantityNotParsed);
                if (sellQuantity <= productToSell.getQuantityAvailable()) {
                    productToSell.sell(sellQuantity);
                    messageSellProductSuccess(productName, sellQuantity);
                } else {
                    messageSellProductFail();
                }
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: restocks the product specified by the user, and shows the cost of the restocking.
    private void inputtedRestockProduct() {
        String productName = JOptionPane.showInputDialog(this,
                "What product do you want to restock?",
                "Product Restocker",
                JOptionPane.PLAIN_MESSAGE);
        Product productToRestock = inventory.findProduct(productName);
        checkForInputErrorAndCancel(productName, productToRestock);
        if (productToRestock != null) {
            double priceToRestock = productToRestock.restock(true);
            JOptionPane.showMessageDialog(this,
                    "It costed $" + String.format("%.2f", priceToRestock)
                            + " to restock " + productName + ".",
                    "Product Restocked",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // EFFECTS: saves the current state of the inventory and all its products
    private void inputtedSaveInventory() {
        JsonWriter writer = new JsonWriter(saveLocation);
        try {
            writer.fullSave(inventory);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this,
                    "Could not find save location: " + saveLocation,
                    "Saving Error", JOptionPane.ERROR_MESSAGE);
        }
        JOptionPane.showMessageDialog(this,
                "The inventory was successfully saved to: " + saveLocation,
                "Save Successful", JOptionPane.INFORMATION_MESSAGE);
    }

    // MODIFIES: this
    // EFFECTS: loads an inventory from a json file, and overwrites the current inventory
    private void inputtedLoadInventory() {
        JsonReader reader = new JsonReader(saveLocation);
        try {
            this.inventory = reader.read();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Could not find an earlier save in: " + saveLocation,
                    "Loading Error", JOptionPane.ERROR_MESSAGE);
        }
        JOptionPane.showMessageDialog(this,
                "Loaded from " + saveLocation + " successfully",
                "Load Successful", JOptionPane.INFORMATION_MESSAGE);
    }

    // EFFECTS: gives the option to quit the app
    private void inputtedExitApp() {
        int n = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to exit the app? All unsaved work will be lost.",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION);
        switch (n) {
            case 0:
                this.dispose();
                break;
            case 1:
                break;
        }
    }


    // EFFECTS: If an operation was cancelled, shows operationCancelled message.
    //          If given product is not in the inventory, shows cannotFindProductError message.
    private void checkForInputErrorAndCancel(String productName, Product productObject) {
        if (productName == null) {
            operationCancelled();
        } else if (productObject == null) {
            cannotFindProductError(productName);
        }
    }

    // messages

    // EFFECTS: a warning message indicating that an operation was cancelled
    private void operationCancelled() {
        JOptionPane.showMessageDialog(this,
                "Operation was cancelled.",
                "Operation Cancelled",
                JOptionPane.WARNING_MESSAGE);
    }

    // EFFECTS: an error message that indicates that a product could not be found
    private void cannotFindProductError(String productName) {
        JOptionPane.showMessageDialog(this,
                "Error: Could not find product " + productName + " in the inventory.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    // prompts and messages for inputtedSellProduct()

    // EFFECTS: asks user for the name of the product to sell
    private String promptSellProductName() {
        return JOptionPane.showInputDialog(this,
                "What is the name of the product you want to sell?",
                "Product Seller",
                JOptionPane.PLAIN_MESSAGE);
    }

    // EFFECTS: asks user for the number of units of given product to sell
    private String promptSellProductQuantity(String productName, Product productToSell) {
        return JOptionPane.showInputDialog(this,
                "The product " + productName + " has " + productToSell.getQuantityAvailable()
                        + " units left. How many would you like to sell?",
                "Product Seller",
                JOptionPane.PLAIN_MESSAGE);
    }

    // EFFECTS: a message that indicates quantity and name of product sold
    private void messageSellProductSuccess(String productName, int sellQuantity) {
        JOptionPane.showMessageDialog(this,
                "Sold " + sellQuantity + " unit(s) of " + productName + " !",
                "Product Sold",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // EFFECTS: a message that indicates illegal number of units given
    private void messageSellProductFail() {
        JOptionPane.showMessageDialog(this,
                "You cannot sell more units than you have!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }


    // prompts and messages for inputtedAddProduct()

    // EFFECTS: asks user for name of product to add
    private String promptAddProductName() {
        return JOptionPane.showInputDialog(this,
                "What is the name of the product you want to add?",
                "Product Maker",
                JOptionPane.PLAIN_MESSAGE);
    }

    // EFFECTS: asks user for the number of units that the product should have in total
    private String promptAddProductQuantity() {
        return JOptionPane.showInputDialog(this,
                "How many units were ordered?",
                "Product Maker",
                JOptionPane.PLAIN_MESSAGE);
    }

    // EFFECTS: asks user for the cost to order a single unit of the given product from the distributor
    private String promptAddProductCost(String productName) {
        return JOptionPane.showInputDialog(this,
                "What is the cost (in $) to order a single unit of " + productName + " from the distributor?",
                "Product Maker",
                JOptionPane.PLAIN_MESSAGE);
    }

    // EFFECTS: shows a message confirming that product has been added to the inventory
    private void messageAddProductSuccess(String productName) {
        JOptionPane.showMessageDialog(this, productName + " has been added to the inventory!");
    }

}
