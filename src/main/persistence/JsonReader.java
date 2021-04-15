package persistence;

// Represents a reader that reads HabitList from JSON data stored in file
// References: JsonSerializationDemo

import model.Habit;
import model.HabitList;
import org.json.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class JsonReader {
    private String sourceFile;

    // EFFECTS: constructs reader to read data from source file
    public JsonReader(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    // EFFECTS: reads and returns HabitList from file;
    //          otherwise, throws IOException if there is an error reading data
    public HabitList read() throws IOException {
        String sourceData = readFile(sourceFile);
        JSONObject sourceDataObject = new JSONObject(sourceData);
        return parseHabitList(sourceDataObject);
    }

    // EFFECTS: reads file and returns it as string
    private String readFile(String sourceFile) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(sourceFile),StandardCharsets.UTF_8)) {
            stream.forEach(stringBuilder::append);
        }
        return stringBuilder.toString();
    }

    // EFFECTS: returns HabitList from JSONObject
    private HabitList parseHabitList(JSONObject jsonObject) {
        HabitList habitList = new HabitList();
        addHabits(habitList, jsonObject);
        return habitList;
    }

    // MODIFIES: HabitList
    // EFFECTS: parses habits from JSONObject and adds habits to HabitList
    private void addHabits(HabitList habitList, JSONObject jsonObject) {
        JSONArray habitArray = jsonObject.getJSONArray("habits");
        for (Object jsonHabit : habitArray) {
            JSONObject nextHabit = (JSONObject) jsonHabit;
            addHabit(habitList, nextHabit);
        }
    }

    // MODIFIES: HabitList
    // EFFECTS: parses habit from JSONObject, constructs habit, and adds it to HabitList
    private void addHabit(HabitList habitList, JSONObject jsonObject) {
        String habitName = jsonObject.getString("habitName");
        String rewardName = jsonObject.getString("rewardName");
        int hoursNeededForReward = jsonObject.getInt("hoursNeededForReward");
        int hoursCompleted = jsonObject.getInt("hoursCompleted");
        int hoursLeftForReward = jsonObject.getInt("hoursLeftForReward");
        Habit habit = new Habit(habitName, rewardName, hoursNeededForReward);
        habit.setHoursCompleted(hoursCompleted);
        habit.setHoursLeftForReward(hoursLeftForReward);
        habitList.addPreexistingHabit(habit);
    }
}
