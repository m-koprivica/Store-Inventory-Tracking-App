package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Test suite for Product class
class ProductTest {
    private Product testProduct;

    @BeforeEach
    void runBefore() {
        testProduct = new Product("Bananas", 30, 1.50);
    }

    @Test
    void testConstructor() {
        assertEquals("Bananas", testProduct.getName());
        assertEquals(30, testProduct.getQuantityMaximum());
        assertEquals(30, testProduct.getQuantityAvailable());
        assertEquals(0, testProduct.getQuantitySold());
        assertEquals(1.50, testProduct.getCost());
    }

    @Test
    void testSell() {
        testProduct.sell(20);
        assertEquals(30, testProduct.getQuantityMaximum());
        assertEquals(10, testProduct.getQuantityAvailable());
        assertEquals(20, testProduct.getQuantitySold());
    }

    @Test
    void testSellMultipleTimes() {
        testProduct.sell(20);
        assertEquals(30, testProduct.getQuantityMaximum());
        assertEquals(10, testProduct.getQuantityAvailable());
        assertEquals(20, testProduct.getQuantitySold());

        testProduct.sell(5);
        assertEquals(30, testProduct.getQuantityMaximum());
        assertEquals(5, testProduct.getQuantityAvailable());
        assertEquals(25, testProduct.getQuantitySold());
    }

    @Test
    void testSellAllUnits() {
        testProduct.sell(30);
        assertEquals(30, testProduct.getQuantityMaximum());
        assertEquals(0, testProduct.getQuantityAvailable());
        assertEquals(30, testProduct.getQuantitySold());
    }

    @Test
    void testRestockWithNothingToRestockWithNoRestocking() {
        double costToRestock = testProduct.restock(false);
        assertEquals(0, costToRestock);
        assertEquals(30, testProduct.getQuantityAvailable());
        assertEquals(0, testProduct.getQuantitySold());
    }

    @Test
    void testRestockWithNothingToRestockWithRestocking() {
        double costToRestock = testProduct.restock(true);
        assertEquals(0, costToRestock);
        assertEquals(30, testProduct.getQuantityAvailable());
        assertEquals(0, testProduct.getQuantitySold());
    }

    @Test
    void testRestockOnceWithNoRestocking() {
        testProduct.sell(20);
        double costToRestock = testProduct.restock(false);
        assertEquals((double) 20*1.50, costToRestock); // try without double
        assertEquals(10, testProduct.getQuantityAvailable());
        assertEquals(20, testProduct.getQuantitySold());
    }

    @Test
    void testRestockOnceWithRestocking() {
        testProduct.sell(20);
        double costToRestock = testProduct.restock(true);
        assertEquals((double) 20*1.50, costToRestock); // try without double
        assertEquals(30, testProduct.getQuantityAvailable());
        assertEquals(0, testProduct.getQuantitySold());
    }

    @Test
    void testRestockMultipleTimesWithNoRestocking() {
        testProduct.sell(20);
        double costToRestock = testProduct.restock(false);
        assertEquals((double) 20*1.50, costToRestock);
        assertEquals(10, testProduct.getQuantityAvailable());
        assertEquals(20, testProduct.getQuantitySold());

        testProduct.sell(5);
        costToRestock = testProduct.restock(false);
        assertEquals((double) 25*1.50, costToRestock);
        assertEquals(5, testProduct.getQuantityAvailable());
        assertEquals(25, testProduct.getQuantitySold());
    }

    @Test
    void testRestockMultipleTimesWithRestocking() {
        testProduct.sell(20);
        double costToRestock = testProduct.restock(false);
        assertEquals((double) 20*1.50, costToRestock);
        assertEquals(10, testProduct.getQuantityAvailable());
        assertEquals(20, testProduct.getQuantitySold());

        testProduct.sell(5);
        costToRestock = testProduct.restock(true);
        assertEquals((double) 25*1.50, costToRestock);
        assertEquals(30, testProduct.getQuantityAvailable());
        assertEquals(0, testProduct.getQuantitySold());
    }

    @Test
    void testRestockEverythingWithNoRestocking() {
        testProduct.sell(30);
        double costToRestock = testProduct.restock(false);
        assertEquals((double) 30*1.50, costToRestock);
        assertEquals(0, testProduct.getQuantityAvailable());
        assertEquals(30, testProduct.getQuantitySold());
    }

    @Test
    void testRestockEverythingWithRestocking() {
        testProduct.sell(30);
        double costToRestock = testProduct.restock(true);
        assertEquals((double) 30*1.50, costToRestock);
        assertEquals(30, testProduct.getQuantityAvailable());
        assertEquals(0, testProduct.getQuantitySold());
    }
}