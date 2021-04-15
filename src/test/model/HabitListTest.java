package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HabitListTest {

    private HabitList testHabitList;

    @BeforeEach
    void setup() {
        testHabitList = new HabitList();
    }

    @Test
    void testAddNewHabitOne() {
        testHabitList.addNewHabit("Personal Project", "Hang out with friends",
                10);
        assertEquals(1, testHabitList.habitListSize());
        assertTrue(testHabitList.habitListContains("Personal Project"));
    }

    @Test
    void testAddNewHabitMultiple() {
        testHabitList.addNewHabit("Habit 1", "Reward 1", 10);
        testHabitList.addNewHabit("Habit 2", "Reward 2", 15);
        testHabitList.addNewHabit("Habit 3", "Reward 3", 0);

        assertEquals(3, testHabitList.habitListSize());
        assertTrue(testHabitList.habitListContains("Habit 1"));
        assertTrue(testHabitList.habitListContains("Habit 2"));
        assertTrue(testHabitList.habitListContains("Habit 3"));
    }

    @Test
    void testAddPreexistingHabitOne() {
        Habit habit1 = new Habit("Habit 1", "Reward 1", 10);
        testHabitList.addPreexistingHabit(habit1);
        assertEquals(1, testHabitList.habitListSize());
        assertTrue(testHabitList.habitListContains("Habit 1"));
    }

    @Test
    void testAddPreexistingHabitMultiple() {
        Habit habit1 = new Habit("Habit 1", "Reward 1", 10);
        Habit habit2 = new Habit("Habit 2", "Reward 2", 40);
        testHabitList.addPreexistingHabit(habit1);
        testHabitList.addPreexistingHabit(habit2);
        assertEquals(2, testHabitList.habitListSize());
        assertTrue(testHabitList.habitListContains("Habit 1"));
        assertTrue(testHabitList.habitListContains("Habit 2"));
    }

    @Test
    void testDeleteHabit() {
        testHabitList.addNewHabit("Read", "Play video games", 5);
        testHabitList.addNewHabit("Practice Guitar", "Buy a new pick", 20);
        assertEquals(2, testHabitList.habitListSize());

        testHabitList.deleteHabit("Read");
        assertEquals(1, testHabitList.habitListSize());
        assertTrue(testHabitList.habitListContains("Practice Guitar"));
        assertFalse(testHabitList.habitListContains("Read"));
    }

    @Test
    void testLogHabitNotInList() {
        testHabitList.addNewHabit("Read", "Play video games", 5);
        testHabitList.addNewHabit("Practice Guitar", "Buy a new pick", 20);

        assertNull(testHabitList.logHabit("Workout", 2));
    }

    @Test
    void testLogHabitInList() {
        testHabitList.addNewHabit("Read", "Play video games", 5);
        testHabitList.addNewHabit("Practice Guitar", "Buy a new pick", 20);
        Map<String, Integer> testMap = testHabitList.logHabit("Practice Guitar", 3);

        assertTrue(testMap.containsKey("Buy a new pick"));
        assertTrue(testMap.containsValue(0));
        assertFalse(testMap.containsKey("Read"));
        assertFalse(testMap.containsValue(1));
    }

    @Test
    void testChangeRewardHabitNotInList() {
        testHabitList.addNewHabit("Read", "Play video games", 5);
        testHabitList.changeReward("Workout", "Hang out", 7);

        assertEquals("Play video games", testHabitList.getHabit("Read").getRewardName());
        assertEquals(5, testHabitList.getHabit("Read").getHoursNeededForReward());
        assertEquals(5, testHabitList.getHabit("Read").getHoursLeftForReward());
    }

    @Test
    void testChangeRewardHabitInListPosHours() {
        testHabitList.addNewHabit("Read", "Play video games", 5);
        testHabitList.changeReward("Read", "Hang out", 1);

        assertEquals("Hang out", testHabitList.getHabit("Read").getRewardName());
        assertEquals(1, testHabitList.getHabit("Read").getHoursNeededForReward());
        assertEquals(1, testHabitList.getHabit("Read").getHoursLeftForReward());
    }

    @Test
    void testChangeRewardHabitInListZeroHours() {
        testHabitList.addNewHabit("Read", "Play video games", 5);
        testHabitList.changeReward("Read", "Hang out", 0);

        assertEquals("Play video games", testHabitList.getHabit("Read").getRewardName());
        assertEquals(5, testHabitList.getHabit("Read").getHoursNeededForReward());
        assertEquals(5, testHabitList.getHabit("Read").getHoursLeftForReward());
    }

    @Test
    void testChangeRewardHabitInListNegHours() {
        testHabitList.addNewHabit("Read", "Play video games", 5);
        testHabitList.changeReward("Read", "Hang out", -2);

        assertEquals("Play video games", testHabitList.getHabit("Read").getRewardName());
        assertEquals(5, testHabitList.getHabit("Read").getHoursNeededForReward());
        assertEquals(5, testHabitList.getHabit("Read").getHoursLeftForReward());
    }

    @Test
    void testChangeRewardNoRewardName() {
        testHabitList.addNewHabit("Read", "Hang out", 10);
        testHabitList.changeReward("Read", "", 10);

        assertEquals("Hang out", testHabitList.getHabit("Read").getRewardName());
        assertEquals(10, testHabitList.getHabit("Read").getHoursNeededForReward());
        assertEquals(10, testHabitList.getHabit("Read").getHoursLeftForReward());
    }
    @Test
    void testChangeRewardReset() {
        testHabitList.addNewHabit("Read", "Play video games", 5);
        testHabitList.logHabit("Read", 4);
        testHabitList.changeReward("Read", "Hang out", 7);

        assertEquals("Hang out", testHabitList.getHabit("Read").getRewardName());
        assertEquals(7, testHabitList.getHabit("Read").getHoursNeededForReward());
        assertEquals(7, testHabitList.getHabit("Read").getHoursLeftForReward());
    }

    @Test
    void testGetHabitNull() {
        testHabitList.addNewHabit("Read", "Hang out", 10);
        assertNull(testHabitList.getHabit("Red"));
    }

    @Test
    void testGetHabitIndex() {
        testHabitList.addNewHabit("Read", "Hang out", 10);
        testHabitList.addNewHabit("Run", "Rest", 50);
        assertEquals("Read", testHabitList.getHabit(0).getHabitName());
        assertEquals("Run", testHabitList.getHabit(1).getHabitName());
    }

    @Test
    void testGetHabits() {
        testHabitList.addNewHabit("Run", "Rest Day", 5);
        testHabitList.addNewHabit("Read", "Hang out", 10);

        List<Habit> habits = testHabitList.getHabits();
        assertEquals(testHabitList.habitListSize(), habits.size());
        assertEquals("Run", habits.get(0).getHabitName());
        assertEquals("Read", habits.get(1).getHabitName());
    }

    @Test
    void testToString() {
        testHabitList.addNewHabit("Run", "Rest Day", 5);
        testHabitList.addNewHabit("Read", "Hang out", 10);
        assertTrue(testHabitList.toString().contains("Habit: Run" + "\n\tReward: Rest Day" +
                "\n\tHours Completed: 0" + "\n\tHours Left For Reward: 5" +
                "\n\tHours Needed For Reward: 5\n\n" +
                "Habit: Read"+ "\n\tReward: Hang out" +
                "\n\tHours Completed: 0" + "\n\tHours Left For Reward: 10" +
                "\n\tHours Needed For Reward: 10\n\n"));
    }
}