package persistence;

import org.json.JSONObject;

// References: JsonSerializationDemo

public interface Writable {

    // EFFECTS: returns this as a JSON object
    JSONObject toJson();
}