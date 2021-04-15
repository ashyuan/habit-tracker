package persistence;

// References: JsonSerializationDemo

import model.Habit;
import model.HabitList;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest extends JsonTest{

    @Test
    void testJsonReaderNonExistentFile() {
        JsonReader jsonReader = new JsonReader("./data/nonExistentFile.json");
        try {
            HabitList habitList = jsonReader.read();
            fail("IOException not caught");
        } catch (IOException e) {
            // IOException caught; do nothing
        }
    }

    @Test
    void testJsonReaderEmptyHabitList() {
        JsonReader jsonReader = new JsonReader("./data/testJsonWriterEmptyHabitList.json");
        try {
            HabitList habitList = jsonReader.read();
            assertEquals(0, habitList.habitListSize());
        } catch (IOException e) {
            fail("IOException not expected to be thrown: Cannot read from file");
        }
    }

    @Test
    void testJsonReaderNonEmptyHabitList() {
        JsonReader jsonReader = new JsonReader("./data/testJsonWriterNonEmptyHabitList.json");
        try {
            HabitList habitList = jsonReader.read();
            List<Habit> habits = habitList.getHabits();
            assertEquals(2, habits.size());
            checkHabit("Habit 1", "Reward 1", 10, 5,
                    5, habits.get(0));
            checkHabit("Habit 2", "Reward 2", 40, 0,
                    40, habits.get(1));
        } catch (IOException e) {
            fail("IOException not expected to be thrown: Cannot read from file");
        }
    }
}
