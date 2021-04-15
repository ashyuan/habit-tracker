package persistence;

// Represents a writer that writes HabitList to file
// References: JsonSerializationDemo

import model.HabitList;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class JsonWriter {

    private String destinationFile;
    private PrintWriter printWriter;

    // EFFECTS: constructs JSON writer to write data to specified file
    public JsonWriter(String destinationFile) {
        this.destinationFile = destinationFile;
    }

    // MODIFIES: this
    // EFFECTS: opens new PrintWriter, otherwise throws FileNotFoundException
    public void open() throws FileNotFoundException {
        printWriter = new PrintWriter(new File(destinationFile));
    }

    // MODIFIES: this
    // EFFECTS: writes HabitList as JSONObject and saves to file
    public void write(HabitList habitList) {
        JSONObject jsonObject = habitList.toJson();
        saveToFile(jsonObject.toString());
    }

    // MODIFIES: this
    // EFFECTS: closes printWriter
    public void close() {
        printWriter.close();
    }

    // MODIFIES: this
    // EFFECTS: saves printWriter to file
    public void saveToFile(String jsonObjectStr) {
        printWriter.print(jsonObjectStr);
    }
}
