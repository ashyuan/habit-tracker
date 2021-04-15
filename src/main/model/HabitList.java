package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.*;

// Represents list of all habits
public class HabitList implements Writable {

    private final List<Habit> habits;
    private Map<String, Integer> rewardNameAndTimesCompleted;

    // EFFECTS: constructs new list of habits with name
    public HabitList() {
        this.habits = new ArrayList<>();
        rewardNameAndTimesCompleted = new HashMap<>();

    }

    // MODIFIES: this
    // EFFECTS: adds a new habit to list
    public void addNewHabit(String habitName, String rewardName, int hoursNeededForReward) {
        Habit habit = new Habit(habitName, rewardName, hoursNeededForReward);
        this.habits.add(habit);
    }

    // MODIFIES: this
    // EFFECTS: adds pre-existing habit to list
    public void addPreexistingHabit(Habit habit) {
        this.habits.add(habit);
    }


    // MODIFIES: this
    // EFFECTS: deletes habit from habit list
    public void deleteHabit(String habitName) {
        habits.removeIf(habit -> habit.getHabitName().equals(habitName));
    }

    // REQUIRES: hours > 0
    // MODIFIES: this
    // EFFECTS: adds given hours to habit; removes the number of hours from hours left for reward;
    //          if hours needed for reward is reached, prints out given reward and resets
    //          hoursLeftForReward to hoursNeededForReward;
    //          returns (rewardName, Number of times completed) if habit is found, else nothing
    public Map<String, Integer> logHabit(String habitName, int hours) {
        for (Habit habit : habits) {
            if (habit.getHabitName().equals(habitName)) {
                int numTimesCompleted = habit.logHours(hours);

                rewardNameAndTimesCompleted.put(habit.getRewardName(), numTimesCompleted);
                return rewardNameAndTimesCompleted;
            }
        }
        return null;
    }

    // MODIFIES: this
    // EFFECTS: if newRewardName length > 0 and hours > 0, changes reward and reward hours for given habit; resets hours
    //          left until reward
    public void changeReward(String habitName, String newRewardName, int hours) {
        if (newRewardName.length() > 0 && hours > 0) {
            for (Habit habit : habits) {
                if (habit.getHabitName().equals(habitName)) {
                    habit.setRewardName(newRewardName);
                    habit.setHoursNeededForReward(hours);
                    habit.resetRewardHours();
                }
            }
        }
    }

    // Getters

    // EFFECTS: gets habit from list of habits with habit name
    public Habit getHabit(String habitName) {
        for (Habit habit: habits) {
            if (habit.getHabitName().equals(habitName)) {
                return habit;
            }
        }
        return null;
    }

    // EFFECTS: gets habit from list of habits by index
    public Habit getHabit(int index) {
        return habits.get(index);
    }

    // EFFECTS: gets list of habits in habit list
    public List<Habit> getHabits() {
        return habits;
    }

    // EFFECTS: returns size of HabitList
    public int habitListSize() {
        return habits.size();
    }

    // EFFECTS: returns true if habit list contains given habit
    public boolean habitListContains(String habitName) {
        for (Habit habit : habits) {
            if (habit.getHabitName().equals(habitName)) {
                return true;
            }
        }
        return false;
    }

    // EFFECTS: returns string representation of Habit List
    @Override
    public String toString() {
        StringBuilder habitListDisplay = new StringBuilder();
        for (Habit habit: habits) {
            habitListDisplay.append(habit.toString());
        }
        return habitListDisplay.toString();
    }

    // MODIFIES: jsonHabitList
    // EFFECTS: adds habits to jsonHabitList
    @Override
    public JSONObject toJson() {
        JSONObject jsonHabitList = new JSONObject();
        jsonHabitList.put("habits", habitsToJson());
        return jsonHabitList;
    }

    // EFFECTS: transfer habits to JSONArray
    private JSONArray habitsToJson() {
        JSONArray habitArray = new JSONArray();

        for (Habit habit : habits) {
            habitArray.put(habit.toJson());
        }
        return habitArray;
    }
}

