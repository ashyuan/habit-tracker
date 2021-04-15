package ui;

import model.HabitList;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

// Habit Tracker Swing Application
public class HabitTrackerApp {

    private static final String JSON_DESTINATION = "./data/habit-tracker.json";
    private HabitList habitList;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: runs the HabitTracker application
    public HabitTrackerApp() throws FileNotFoundException {
        runHabitTracker();
    }

    // MODIFIES: this
    // EFFECTS: processes user input for main menu
    private void runHabitTracker() {
        boolean continueRunningMain = true;
        String mainCommand;

        init();

        while (continueRunningMain) {
            displayMainMenu();
            mainCommand = input.next();
            mainCommand = mainCommand.toLowerCase();

            if (mainCommand.equals("q")) {
                continueRunningMain = false;
            } else if (mainCommand.equals("1")) {
                runHabitList();
            } else {
                processMainCommands(mainCommand);
            }
        }

        System.out.println("\nSuccessfully quit");
    }

    // MODIFIES: this
    // EFFECTS: processes user input for habit list
    private void runHabitList() {
        boolean continueRunningHabitList = true;
        String habitListCommand;

        while (continueRunningHabitList) {
            displayHabitList();
            habitListCommand = input.next();
            habitListCommand = habitListCommand.toLowerCase();

            if (habitListCommand.equals("r")) {
                continueRunningHabitList = false;
            }
        }

    }

    // MODIFIES: this
    // EFFECTS: initializes HabitList and input
    private void init() {
        input = new Scanner(System.in);
        habitList = new HabitList();
        jsonWriter = new JsonWriter(JSON_DESTINATION);
        jsonReader = new JsonReader(JSON_DESTINATION);
    }

    // EFFECTS: displays main menu to user
    private void displayMainMenu() {
        System.out.println("\nSelect number:");
        System.out.println("\t1 -> View habit list");
        System.out.println("\t2 -> Add new habit");
        System.out.println("\t3 -> Delete habit");
        System.out.println("\t4 -> Log habit");
        System.out.println("\t5 -> Change reward");
        System.out.println("\t6 -> Save habit list to file");
        System.out.println("\t7 -> Load habit list from file");
        System.out.println("\tq -> Quit");
    }

    // EFFECTS: displays list of existing habits to user
    private void displayHabitList() {
        System.out.println(habitList);
        System.out.println("\nType 'r' to return to Main Menu");
    }

    // MODIFIES: this
    // EFFECTS: processes user inputs from main menu
    private void processMainCommands(String mainCommand) {
        switch (mainCommand) {
            case "2":
                processAddNewHabit();
                break;
            case "3":
                processDeleteHabit();
                break;
            case "4":
                processLogHabit();
                break;
            case "5":
                processChangeReward();
                break;
            case "6":
                saveHabitList();
                break;
            case "7":
                loadHabitList();
                break;
            default:
                System.out.println("Not a valid selection");
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: processes adding a new habit function
    private void processAddNewHabit() {
        boolean notAccepted = true;

        while (notAccepted) {
            System.out.println("Enter new habit name: ");
            String habitName = input.next() + input.nextLine();

            System.out.println("Enter reward name: ");
            String rewardName = input.next() + input.nextLine();

            System.out.println("Enter target hours needed to obtain reward: ");
            int hoursNeededForReward = input.nextInt();
            System.out.println();

            if (hoursNeededForReward <= 0) {
                System.out.println("Invalid hours given");
            } else {
                habitList.addNewHabit(habitName, rewardName, hoursNeededForReward);
                System.out.println("New habit, " + habitName + ", created successfully");
                notAccepted = false;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: processes delete habit function
    private void processDeleteHabit() {
        boolean notAccepted = true;

        while (notAccepted) {
            System.out.println("Enter existing habit name to delete: ");
            String habitName = input.next() + input.nextLine();

            if (!habitList.habitListContains(habitName)) {
                System.out.println("Invalid habit name given");
            } else {
                habitList.deleteHabit(habitName);
                System.out.println("Habit, " + habitName + ", deleted successfully");
                notAccepted = false;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: processes logging a habit function
    private void processLogHabit() {
        boolean notAccepted = true;

        while (notAccepted) {
            System.out.println("Enter existing habit to log: ");
            String habitName = input.next() + input.nextLine();

            System.out.println("Enter number of hours completed: ");
            int hours = input.nextInt();

            if (!habitList.habitListContains(habitName)) {
                System.out.println("Invalid habit name given");
            } else {
                Map<String, Integer> rewardNameAndTimesCompleted = habitList.logHabit(habitName, hours);
                StringBuilder result = new StringBuilder();
                for (String key : rewardNameAndTimesCompleted.keySet()) {
                    int numTimesCompleted = rewardNameAndTimesCompleted.get(key);
                    for (int i = 0; i < numTimesCompleted; i++) {
                        result.append(key).append("\n");
                    }
                }
                notAccepted = false;
                System.out.println("Reward(s) unlocked:\n" + result);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: processes changing a reward function
    private void processChangeReward() {
        boolean notAccepted = true;

        while (notAccepted) {
            System.out.println("Enter existing habit name to change reward: ");
            String habitName = input.next() + input.nextLine();
            System.out.println(habitList.getHabit(habitName));

            System.out.println("Enter new reward: ");
            String newReward = input.next() + input.nextLine();

            System.out.println("Enter hours needed to obtain new reward: ");
            int newHours = input.nextInt();

            if (!habitList.habitListContains(habitName)) {
                System.out.println("Invalid habit name given\n");
            } else {
                habitList.changeReward(habitName, newReward, newHours);
                System.out.println("Reward changed successfully");
                System.out.println(habitList.getHabit(habitName));
                notAccepted = false;
            }
        }
    }

    // EFFECTS: saves HabitList to file
    private void saveHabitList() {
        try {
            jsonWriter.open();
            jsonWriter.write(habitList);
            jsonWriter.close();
            System.out.println("Saved habit list to " + JSON_DESTINATION);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to save to file: " + JSON_DESTINATION);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads habit list from file
    private void loadHabitList() {
        try {
            habitList = jsonReader.read();
            System.out.println("Loaded habit list from" + JSON_DESTINATION);
        } catch (IOException e) {
            System.out.println("Unable to load habit list from file: " + JSON_DESTINATION);
        }
    }
}
