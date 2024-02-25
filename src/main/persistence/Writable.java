package persistence;

import org.json.JSONObject;

// Interface for the savable Inventory and Product objects.
// Modelled after the JsonSerializationDemo from CPSC 210
public interface Writable {

    // EFFECTS: converts the object's data into JSON
    JSONObject toJson();
}
