package persistence;

import model.Inventory;
import model.Product;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// Test suite for JsonReader. Modelled after JsonSerializationDemo from CPSC 210
public class JsonReaderTest {

    @Test
    void testReaderNoFileFound() {
        String saveLocation = "./data/InvisibleFile.json";
        JsonReader reader = new JsonReader(saveLocation);
        try {
            Inventory i = reader.read();
            fail("IOException was not thrown, but it should have been");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    void testReaderEmptyInventory() {
        String saveLocation = "./data/testReaderEmptyInventory.json";
        JsonReader reader = new JsonReader(saveLocation);
        try {
            Inventory i = reader.read();
            assertEquals(0, i.getProducts().size());
            assertEquals(15, i.getThreshold());
        } catch (IOException e) {
            fail("IOException was thrown unexpectedly");
        }
    }

    @Test
    void testReaderOneProductInInventory() {
        String saveLocation = "./data/testReaderOneProductInInventory.json";
        JsonReader reader = new JsonReader(saveLocation);
        try {
            Inventory i = reader.read();
            List<Product> products = i.getProducts();
            assertEquals(1, products.size());
            assertEquals("Apple", products.get(0).getName());
            assertEquals(15, i.getThreshold());
        } catch (IOException e) {
            fail("IOException was thrown unexpectedly");
        }
    }

    @Test
    void testReaderManyProductsInInventory() {
        String saveLocation = "./data/testReaderManyProductsInInventory.json";
        JsonReader reader = new JsonReader(saveLocation);
        try {
            Inventory i = reader.read();
            List<Product> products = i.getProducts();
            assertEquals(3, products.size());
            assertEquals("Apple", products.get(0).getName());
            assertEquals(0, products.get(0).getQuantitySold());
            assertEquals("Banana", products.get(1).getName());
            assertEquals(0, products.get(1).getQuantitySold());
            assertEquals("Strawberry", products.get(2).getName());
            assertEquals(0, products.get(2).getQuantitySold());
            assertEquals(15, i.getThreshold());
        } catch (IOException e) {
            fail("IOException was thrown unexpectedly");
        }
    }

    @Test
    void testReaderManyProductsInInventoryAndSold() {
        String saveLocation = "./data/testReaderManyProductsInInventoryAndSold.json";
        JsonReader reader = new JsonReader(saveLocation);
        try {
            Inventory i = reader.read();
            List<Product> products = i.getProducts();
            assertEquals(3, products.size());
            assertEquals("Apple", products.get(0).getName());
            assertEquals(25, products.get(0).getQuantitySold());
            assertEquals("Banana", products.get(1).getName());
            assertEquals(30, products.get(1).getQuantitySold());
            assertEquals("Strawberry", products.get(2).getName());
            assertEquals(9, products.get(2).getQuantitySold());
            assertEquals(15, i.getThreshold());
        } catch (IOException e) {
            fail("IOException was thrown unexpectedly");
        }
    }
}
