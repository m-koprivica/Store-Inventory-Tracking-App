package persistence;

import model.Inventory;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

// Writes a JSON save for the inventory
// Modelled based on the JsonSerializationDemo of CPSC 210
public class JsonWriter {
    private final String saveLocation; // where the save will be located
    private PrintWriter writer;  // the writer that makes products and inventory -> json file

    // EFFECTS: constructs a Jsonwriter to write to the given save location
    public JsonWriter(String saveLocation) {
        this.saveLocation = saveLocation;
        // actually needs to be like this for tests :(
    }

    // MODIFIES: this
    // EFFECTS: turns on writer, saves the current state of the inventory as a JSON file,
    // and then closes the writer. Throws FileNotFoundException if the save location is inaccessible
    public void fullSave(Inventory i) throws FileNotFoundException {
        open();
        write(i);
        close();
    }

    // MODIFIES: this
    // EFFECTS: turns on writer. Throws FileNotFoundException if the save location is inaccessible
    // Helper method for fullSave
    private void open() throws FileNotFoundException {
        writer = new PrintWriter(saveLocation);
    }

    // MODIFIES: this
    // EFFECTS: saves/writes the current state of the inventory to a file
    // Helper method for fullSave
    private void write(Inventory i) {
        JSONObject json = i.toJson();               // inventory->json
        saveToFile(json.toString(4));     //json->string with tab indentation
    }

    // MODIFIES: this
    // EFFECTS: closes the writer
    // Helper method for fullSave
    private void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes given string (which was a JSON object) into the file
    // Helper method for write, which is a helper for fullSave
    private void saveToFile(String json) {
        writer.print(json);
    }
}
