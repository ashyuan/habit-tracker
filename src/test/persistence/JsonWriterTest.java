package persistence;

import model.Habit;
import model.HabitList;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// References: JsonSerializationDemo

public class JsonWriterTest extends JsonTest{

    @Test
    void testJsonWriterInvalidFileName() {
        try {
            HabitList habitList = new HabitList();
            JsonWriter jsonWriter = new JsonWriter("./data/invalid\file\0name.json");
            jsonWriter.open();
            fail("IOException expected");
        } catch (IOException e) {
            // IOException caught; do nothing
        }
    }

    @Test
    void testJsonWriterEmptyHabitList() {
        try {
            HabitList habitList = new HabitList();
            JsonWriter jsonWriter = new JsonWriter("./data/testJsonWriterEmptyHabitList.json");
            jsonWriter.open();
            jsonWriter.write(habitList);
            jsonWriter.close();

            JsonReader jsonReader = new JsonReader("./data/testJsonWriterEmptyHabitList.json");
            habitList = jsonReader.read();
            assertEquals(0, habitList.habitListSize());
        } catch (IOException e) {
            fail("IOException not expected to be thrown");
        }
    }

    @Test
    void testJsonWriterNonEmptyHabitList() {
        try {
            HabitList habitList = new HabitList();
            habitList.addNewHabit("Habit 1", "Reward 1", 10);
            habitList.addNewHabit("Habit 2" ,"Reward 2", 40);
            habitList.getHabit("Habit 1").logHours(5);
            JsonWriter jsonWriter = new JsonWriter("./data/testJsonWriterNonEmptyHabitList.json");
            jsonWriter.open();
            jsonWriter.write(habitList);
            jsonWriter.close();

            JsonReader jsonReader = new JsonReader("./data/testJsonWriterNonEmptyHabitList.json");
            habitList = jsonReader.read();
            List<Habit> habits = habitList.getHabits();
            assertEquals(2, habits.size());
            checkHabit("Habit 1", "Reward 1", 10, 5,
                    5, habits.get(0));
            checkHabit("Habit 2", "Reward 2", 40, 0,
                    40, habits.get(1));
        } catch (IOException e) {
            fail("IOException not expected to be thrown");
        }
    }
}
