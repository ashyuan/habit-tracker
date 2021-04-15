package model;

import org.json.JSONObject;
import persistence.Writable;

import static java.lang.Math.floor;

// Represents a habit with a name, reward, hours of habit completed, and hours left to get reward.
public class Habit implements Writable {

    private String habitName;
    private String rewardName;
    private int hoursCompleted;
    private int hoursLeftForReward;
    private int hoursNeededForReward;

    // REQUIRES: hoursNeededForReward > 0
    // EFFECTS: sets name to habitName, sets reward to rewardName, sets hoursCompleted to 0,
    //          sets hoursLeftForReward to hoursNeededForReward,
    //          if negative or 0 hoursNeededForReward, then hoursNeededForReward will default to 5 hours
    public Habit(String habitName, String rewardName, int hoursNeededForReward) {
        this.habitName = habitName;
        this.rewardName = rewardName;
        hoursCompleted = 0;
        hoursLeftForReward = hoursNeededForReward;
        if (hoursNeededForReward > 0) {
            this.hoursNeededForReward = hoursNeededForReward;
        } else {
            this.hoursNeededForReward = 5;
        }
    }

    // Getters

    // EFFECTS: returns the string habitName
    public String getHabitName() {
        return habitName;
    }

    // EFFECTS: returns the string rewardName
    public String getRewardName() {
        return rewardName;
    }

    // EFFECTS: returns the integer hoursCompleted
    public int getHoursCompleted() {
        return hoursCompleted;
    }

    // EFFECTS: returns the integer hoursLeftForReward
    public int getHoursLeftForReward() {
        return hoursLeftForReward;
    }

    // EFFECTS: returns the integer hoursNeededForReward
    public int getHoursNeededForReward() {
        return hoursNeededForReward;
    }

    //Setters

    // MODIFIES: this
    // EFFECTS: if newRewardName length > 0, sets a new reward name
    public void setRewardName(String newRewardName) {
        if (newRewardName.length() > 0) {
            rewardName = newRewardName;
        }
    }

    // MODIFIES: this
    // EFFECTS: if hours > 0, sets new hours needed for reward
    public void setHoursNeededForReward(int hours) {
        if (hours > 0) {
            hoursNeededForReward = hours;
        }
    }

    // REQUIRES: hours >= 0
    // MODIFIES: this
    // EFFECTS: sets hours completed for habit
    public void setHoursCompleted(int hours) {
        hoursCompleted = hours;
    }

    // REQUIRES: hours >= 0
    // MODIFIES: this
    // EFFECTS: sets hours left for reward for habit
    public void setHoursLeftForReward(int hours) {
        hoursLeftForReward = hours;
    }


    // MODIFIES: this
    // EFFECTS: if hours > 0, adds hours to hoursCompleted and subtracts hours from hoursLeftForReward;
    //          if logged hours >= hoursNeededForReward, then return rewardName number of times hoursNeededForReward is
    //          reached and reset reward hours subtracted by remainder hours;
    public Integer logHours(int hours) {
        if (hours > 0) {
            this.hoursCompleted += hours;
            if (hours < hoursLeftForReward) {
                this.hoursLeftForReward -= hours;
                return 0;
            } else {
                int tempHours = hours;
                tempHours -= hoursLeftForReward;
                int completedTimes = 1;

                while (tempHours >= hoursNeededForReward) {
                    tempHours -= hoursNeededForReward;
                    completedTimes++;
                }
                if (tempHours == 0) {
                    resetRewardHours();
                } else {
                    hoursLeftForReward = hoursNeededForReward - tempHours;
                }
                return completedTimes;
            }
        }
        return 0;
    }


    // MODIFIES: this
    // EFFECTS: resets hours left for reward to hours needed to obtain reward
    public void resetRewardHours() {
        this.hoursLeftForReward = this.hoursNeededForReward;
    }

    // EFFECTS: returns string representation of Habit
    @Override
    public String toString() {
        return "Habit: " + habitName + "\n\tReward: " + rewardName
                + "\n\tHours Completed: " + hoursCompleted + "\n\tHours Left For Reward: " + hoursLeftForReward
                + "\n\tHours Needed For Reward: " + hoursNeededForReward + "\n\n";
    }

    // MODIFIES: jsonHabit
    // EFFECTS: adds habit name, reward, hours needed, hours completed, and hours left to jsonHabit
    @Override
    public JSONObject toJson() {
        JSONObject jsonHabit = new JSONObject();
        jsonHabit.put("habitName", habitName);
        jsonHabit.put("rewardName", rewardName);
        jsonHabit.put("hoursNeededForReward", hoursNeededForReward);
        jsonHabit.put("hoursCompleted", hoursCompleted);
        jsonHabit.put("hoursLeftForReward", hoursLeftForReward);
        return jsonHabit;
    }
}
