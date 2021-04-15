package persistence;

// References: JsonSerializationDemo

import model.Habit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkHabit(String habitName, String rewardName, int hoursNeededForReward, int hoursCompleted,
                              int hoursLeftForReward, Habit habit) {
        assertEquals(habitName, habit.getHabitName());
        assertEquals(rewardName, habit.getRewardName());
        assertEquals(hoursNeededForReward, habit.getHoursNeededForReward());
        assertEquals(hoursCompleted, habit.getHoursCompleted());
        assertEquals(hoursLeftForReward, habit.getHoursLeftForReward());
    }
}
