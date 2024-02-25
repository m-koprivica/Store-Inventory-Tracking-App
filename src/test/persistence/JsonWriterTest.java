package persistence;

import model.Inventory;
import model.Product;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// Test suite for JsonWriter. Modelled after JsonSerializationDemo from CPSC 210
public class JsonWriterTest {

    // EFFECTS: helper method that tests if two products are the same product
    // (Used since recreated products have different references than the originals)
    private void verifyAllDetailsOfAProduct(Product actual, Product expected) {
        assertEquals(actual.getName(), expected.getName());
        assertEquals(actual.getQuantityMaximum(), expected.getQuantityMaximum());
        assertEquals(actual.getQuantityAvailable(), expected.getQuantityAvailable());
        assertEquals(actual.getQuantitySold(), expected.getQuantitySold());
        assertEquals(actual.getCost(), expected.getCost());
    }

    @Test
    void testWriterInvalidFile() {
        try {
            Inventory i = new Inventory();
            JsonWriter writer = new JsonWriter("./data/\1nvalid::file/name.json");
            writer.fullSave(i);
            fail("IOException was not thrown, even though it should have been");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    void testWriterEmptyInventory() {
        try {
            String saveLocation = "./data/testWriterEmptyInventory.json";

            Inventory i = new Inventory();
            JsonWriter writer = new JsonWriter(saveLocation);
            writer.fullSave(i);

            JsonReader reader = new JsonReader(saveLocation);
            i = reader.read();
            assertEquals(0, i.getProducts().size());
            assertEquals(15, i.getThreshold());
        } catch (IOException e) {
            fail("IOException was unexpectedly thrown");
        }
    }

    @Test
    void testWriterOneProductInInventory() {
        try {
            String saveLocation = "./data/testWriterOneProductInInventory.json";

            Inventory i = new Inventory();
            Product apple = new Product("Apple", 50, 1.00);
            i.addProduct(apple);
            JsonWriter writer = new JsonWriter(saveLocation);
            writer.fullSave(i);

            JsonReader reader = new JsonReader(saveLocation);
            i = reader.read();
            assertEquals(1, i.getProducts().size());
            verifyAllDetailsOfAProduct(apple, i.getProducts().get(0));
            assertEquals(15, i.getThreshold());
        } catch (IOException e) {
            fail("IOException was unexpectedly thrown");
        }
    }

    @Test
    void testWriterManyProductsInInventory() {
        try {
            String saveLocation = "./data/testWriterManyProductsInInventory.json";

            Inventory i = new Inventory();
            Product apple = new Product("Apple", 50, 1.00);
            Product banana = new Product("Banana", 30, 1.50);
            Product strawberry = new Product("Strawberry", 10, 99.99);

            i.addProduct(apple);
            i.addProduct(banana);
            i.addProduct(strawberry);
            JsonWriter writer = new JsonWriter(saveLocation);
            writer.fullSave(i);

            JsonReader reader = new JsonReader(saveLocation);
            i = reader.read();
            assertEquals(3, i.getProducts().size());
            verifyAllDetailsOfAProduct(apple, i.getProducts().get(0));
            verifyAllDetailsOfAProduct(banana, i.getProducts().get(1));
            verifyAllDetailsOfAProduct(strawberry, i.getProducts().get(2));
            assertEquals(15, i.getThreshold());
        } catch (IOException e) {
            fail("IOException was unexpectedly thrown");
        }
    }

    @Test
    void testWriterManyProductsInInventoryAndSold() {
        try {
            String saveLocation = "./data/testWriterManyProductsInInventoryAndSold.json";

            Inventory i = new Inventory();
            Product apple = new Product("Apple", 50, 1.00);
            Product banana = new Product("Banana", 30, 1.50);
            Product strawberry = new Product("Strawberry", 10, 99.99);
            apple.sell(25);          // 25 left
            banana.sell(30);         // 0 left
            strawberry.sell(9);      // 1 left

            i.addProduct(apple);
            i.addProduct(banana);
            i.addProduct(strawberry);
            JsonWriter writer = new JsonWriter(saveLocation);
            writer.fullSave(i);

            JsonReader reader = new JsonReader(saveLocation);
            i = reader.read();
            assertEquals(3, i.getProducts().size());
            List<Product> products = i.getProducts();
            verifyAllDetailsOfAProduct(apple, products.get(0));
            verifyAllDetailsOfAProduct(banana, products.get(1));
            verifyAllDetailsOfAProduct(strawberry, products.get(2));
            assertEquals(15, i.getThreshold());
        } catch (IOException e) {
            fail("IOException was unexpectedly thrown");
        }
    }
}
