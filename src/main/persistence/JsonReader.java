package persistence;

import model.Inventory;
import model.Product;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Reads the Json save file and loads it in the application
// Modelled based on the JsonSerializationDemo of CPSC 210
public class JsonReader {
    private final String saveLocation; // where the save is located

    // EFFECTS: constructs a reader that can load the save file
    public JsonReader(String saveLocation) {
        this.saveLocation = saveLocation;
    }

    // EFFECTS: reads the save file and loads it into the application.
    // Throws IOException if an error occurs during reading
    public Inventory read() throws IOException {
        String data = readFile(saveLocation);           //json file->string
        JSONObject jsonObject = new JSONObject(data);   //string->json object
        return recreateInventory(jsonObject);           //json object -> inventory
    }

    // EFFECTS: reads source file as string and returns it. Throws IOException if an error occurs during reading
    // Helper method from JsonSerializationDemo from CPSC 210
    private String readFile(String saveLocation) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(saveLocation), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: recreates the inventory based on the save JSON file, with the same threshold and
    // list of products
    public Inventory recreateInventory(JSONObject jsonObject) {
        int threshold = jsonObject.getInt("threshold");
        Inventory i = new Inventory();
        i.setThreshold(threshold);
        addProducts(i, jsonObject);
        return i;
    }

    // EFFECTS: recreates products and their information from based on the save JSON file,
    // and adds them to the inventory
    private void addProducts(Inventory i, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("products");
        for (Object json: jsonArray) {
            JSONObject nextProduct = (JSONObject) json;
            String name = nextProduct.getString("name");
            int quantityMaximum = nextProduct.getInt("quantityMaximum");
            int quantitySold = nextProduct.getInt("quantitySold");
            double cost = nextProduct.getDouble("cost");
            Product recreatedProduct = new Product(name,quantityMaximum, cost);
            i.addProduct(recreatedProduct);
            recreatedProduct.sell(quantitySold); //also changes quantityAvailable to correct amount
            // Order between the last two lines was reversed so that the event logs make sense, while also
            // not adding a "specific" event to Product constructor (product added to inventory).
        }
    }
}
