package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HabitTest {
    Habit testHabit;

    @BeforeEach
    void setup() {
        testHabit = new Habit("Run", "Rest Day", 5);
    }

    @Test
    void testConstructorCorrectParameters() {
        assertEquals("Run", testHabit.getHabitName());
        assertEquals("Rest Day", testHabit.getRewardName());
        assertEquals(5, testHabit.getHoursNeededForReward());
        assertEquals(0, testHabit.getHoursCompleted());
        assertEquals(5, testHabit.getHoursLeftForReward());
    }

    @Test
    void testConstructorZeroHours() {
        Habit habitZeroHours = new Habit("Habit", "Reward", 0);
        assertEquals(5, habitZeroHours.getHoursNeededForReward());
    }

    @Test
    void testConstructorNegHours() {
        Habit habitNegHours = new Habit("Habit", "Reward", -1);
        assertEquals(5, habitNegHours.getHoursNeededForReward());
    }

    @Test
    void testLogHoursLoggedNegHours() {
        assertNull(testHabit.logHours(-3));
        assertEquals(0, testHabit.getHoursCompleted());
        assertEquals(5, testHabit.getHoursLeftForReward());
    }

    @Test
    void testLogHoursLoggedHoursLessThanRewardHours() {
        assertEquals(0, testHabit.logHours(3));
        assertEquals(3, testHabit.getHoursCompleted());
        assertEquals(2, testHabit.getHoursLeftForReward());
    }

    @Test
    void testLogHoursLoggedHoursEqualRewardHours() {
        assertEquals(1,testHabit.logHours(5));
        assertEquals(5, testHabit.getHoursCompleted());
        assertEquals(testHabit.getHoursNeededForReward(), testHabit.getHoursLeftForReward());
    }

    @Test
    void testLogHoursLoggedHoursTwoTimesGreaterThanRewardHours() {
        assertEquals(2, testHabit.logHours(10));
        assertEquals(10, testHabit.getHoursCompleted());
        assertEquals(testHabit.getHoursNeededForReward(), testHabit.getHoursLeftForReward());
    }

    @Test
    void testLogHoursLoggedHoursGreaterThanRewardHoursWithRemainder() {
        assertEquals(3, testHabit.logHours(16));
        assertEquals(16, testHabit.getHoursCompleted());
        assertEquals(4, testHabit.getHoursLeftForReward());
    }

    @Test
    void testLogHoursLoggedHoursMultipleHours() {
        assertEquals(0, testHabit.logHours(2));
        assertEquals(2, testHabit.getHoursCompleted());
        assertEquals(3, testHabit.getHoursLeftForReward());

        assertEquals(1, testHabit.logHours(5));
        assertEquals(7, testHabit.getHoursCompleted());
        assertEquals(3, testHabit.getHoursLeftForReward());

        assertEquals(2, testHabit.logHours(9));
        assertEquals(16, testHabit.getHoursCompleted());
        assertEquals(4, testHabit.getHoursLeftForReward());
    }


    @Test
    void testResetRewardHours() {
        testHabit.logHours(10);
        testHabit.resetRewardHours();
        assertEquals(10, testHabit.getHoursCompleted());
        assertEquals(testHabit.getHoursNeededForReward(), testHabit.getHoursLeftForReward());
    }

    @Test
    void testSetRewardNamePosLength() {
        testHabit.setRewardName("R");
        assertEquals("R", testHabit.getRewardName());
    }

    @Test
    void testSetRewardNameZeroLength() {
        testHabit.setRewardName("");
        assertEquals("Rest Day", testHabit.getRewardName());
    }

    @Test
    void testSetHoursNeededForRewardPosHours() {
        testHabit.setHoursNeededForReward(1);
        assertEquals(1, testHabit.getHoursNeededForReward());
    }

    @Test
    void testSetHoursNeededForRewardZeroHours() {
        testHabit.setHoursNeededForReward(0);
        assertEquals(5, testHabit.getHoursNeededForReward());
    }

    @Test
    void testSetHoursNeededForRewardNegHours() {
        testHabit.setHoursNeededForReward(-1);
        assertEquals(5, testHabit.getHoursNeededForReward());
    }

    @Test
    void testSetHoursCompleted() {
        testHabit.setHoursCompleted(10);
        assertEquals(10, testHabit.getHoursCompleted());
    }

    @Test
    void testSetHoursLeftForRewardLessThanRewardHours() {
        testHabit.setHoursLeftForReward(2);
        assertEquals(2, testHabit.getHoursLeftForReward());
    }

    @Test
    void testSetHoursLeftForRewardZeroHours() {
        testHabit.setHoursLeftForReward(0);
        assertEquals(0, testHabit.getHoursLeftForReward());
    }

    @Test
    void testToString() {
        assertTrue(testHabit.toString().contains("Habit: Run" +
                "\n\tReward: Rest Day" +
                "\n\tHours Completed: 0" +
                "\n\tHours Left For Reward: 5" +
                "\n\tHours Needed For Reward: 5\n\n"));
    }
}
