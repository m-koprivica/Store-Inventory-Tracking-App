package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Test suite for Inventory class
public class InventoryTest {
    private Inventory testInventory;
    private Product p1;
    private Product p2;
    private Product p3;

    @BeforeEach
    void runBefore() {
        testInventory = new Inventory();
        p1 = new Product("Apples", 50, 2.00);
        p2 = new Product("Pens", 15, 0.25);
        p3 = new Product("Books", 20, 1.35);
    }

    @Test
    void testConstructor() {
        assertTrue(testInventory.getProducts().isEmpty());
        assertEquals(15, testInventory.getThreshold());
    }

    @Test
    void testAddProductOnce() {
        testInventory.addProduct(p1);
        assertEquals(1, testInventory.getProducts().size());
        assertEquals(p1, testInventory.getProducts().get(0));
    }

    @Test
    void testAddProductMultipleTimes() {
        testInventory.addProduct(p1);
        testInventory.addProduct(p2);
        assertEquals(2, testInventory.getProducts().size());
        assertEquals(p1, testInventory.getProducts().get(0));
        assertEquals(p2, testInventory.getProducts().get(1));
    }

    @Test
    void testRemoveProductOnce() {
        testInventory.addProduct(p1);
        assertEquals(1, testInventory.getProducts().size());

        testInventory.removeProduct(p1);
        assertEquals(0, testInventory.getProducts().size());
    }

    @Test
    void testRemoveProductMultipleTimes() {
        testInventory.addProduct(p1);
        testInventory.addProduct(p2);
        testInventory.addProduct(p3);
        assertEquals(3, testInventory.getProducts().size());

        testInventory.removeProduct(p1);
        assertEquals(2, testInventory.getProducts().size());
        assertFalse(testInventory.getProducts().contains(p1));

        testInventory.removeProduct(p3);
        assertEquals(1, testInventory.getProducts().size());
        assertFalse(testInventory.getProducts().contains(p3));
    }

    @Test
    void testViewLowStockProductsWithFullProduct() {
        testInventory.addProduct(p1);
        List<Product> result = testInventory.viewLowStockProducts();
        assertEquals(0, result.size());
    }

    @Test
    void testViewLowStockProductWithNearlyFullProduct() {
        testInventory.addProduct(p1);
        p1.sell(1);
        List<Product> result = testInventory.viewLowStockProducts();
        assertEquals(0, result.size());
    }

    @Test
    void testViewLowStockProductsWithProductFullyOutOfStock() {
        testInventory.addProduct(p2);
        p2.sell(15);
        List<Product> result = testInventory.viewLowStockProducts();
        assertEquals(1, result.size());
        assertEquals(p2, result.get(0));
    }

    @Test
    void testViewLowStockProductsWithProductNearlyOutOfStock() {
        testInventory.addProduct(p3);
        p3.sell(19);
        List<Product> result = testInventory.viewLowStockProducts();
        assertEquals(1, result.size());
        assertEquals(p3, result.get(0));
    }

    @Test
    void testViewLowStockProductsWithManyProducts() {
        testInventory.addProduct(p1);
        testInventory.addProduct(p2);
        testInventory.addProduct(p3);
        p2.sell(15);
        p3.sell(19);
        List<Product> result = testInventory.viewLowStockProducts();
        assertEquals(2, result.size());
        assertTrue(result.contains(p2));
        assertTrue(result.contains(p3));
    }

    @Test
    void testViewLowStockProductsWithProductOnTheThreshold() {
        testInventory.addProduct(p3);
        p3.sell(10);
        testInventory.setThreshold(50);
        List<Product> result = testInventory.viewLowStockProducts();
        assertEquals(50, testInventory.getThreshold());
        assertEquals(1, result.size());
    }

    @Test
    void testFindProduct() {
        testInventory.addProduct(p1);
        Product apples = testInventory.findProduct("Apples");
        assertEquals(p1, apples);
    }

    @Test
    void testFindProductWhenManyProducts() {
        testInventory.addProduct(p2);
        testInventory.addProduct(p3);
        testInventory.addProduct(p1);
        Product apples = testInventory.findProduct("Apples");
        assertEquals(p1, apples);
    }

    @Test
    void testFindProductWhenNotInInventory() {
        testInventory.addProduct(p2);
        testInventory.addProduct(p3);
        testInventory.addProduct(p1);
        Product nothing = testInventory.findProduct("The Invisible Product");
        assertNull(nothing);
    }

    @Test
    void testToStringWithNoProducts() {
        assertTrue(testInventory.getProducts().isEmpty());
        String emptyProductList = testInventory.toString();
        assertEquals("There are no products in the inventory.", emptyProductList);
    }

    @Test
    void testToStringWithOneProduct() {
        testInventory.addProduct(p1);
        p1.sell(25);
        assertEquals(25, p1.getQuantityAvailable());
        assertEquals(50, p1.getQuantityMaximum());

        String productList = testInventory.toString();
        assertEquals("Apples has 25/50 units left.\n", productList);
    }

    @Test
    void testToStringWithManyProducts() {
        testInventory.addProduct(p1);
        testInventory.addProduct(p2);
        p1.sell(25);

        String productList = testInventory.toString();
        assertEquals("Apples has 25/50 units left.\nPens has 15/15 units left.\n", productList);
    }


}
