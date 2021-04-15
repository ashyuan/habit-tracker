package ui;

import model.Habit;
import model.HabitList;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;


// Habit Tracker Swing application
// References: https://docs.oracle.com/javase/tutorial/uiswing/examples/components

public class HabitTrackerFrame extends JPanel implements ActionListener {
    private HabitList habitList;

    private JPanel habitListPanel;
    private JTable habitListTable;
    private DefaultTableModel habitListTableModel;
    private HabitListTableMouseListener habitListTableMouseListener;
    private JPopupMenu popupMenu;
    private JMenuItem menuItemLogHabit;
    private JMenuItem menuItemChangeReward;
    private JMenuItem menuItemDeleteHabit;
    private JButton btnAddNewHabit;

    private JDialog mainMenuDialog;
    private JPanel mainMenuPanel;
    private JButton btnLoadList;
    private JButton btnNewList;

    private JPanel topPanel;
    private JPanel btnPanel;

    private static final String DESTINATION_FILE = "./data/habit-tracker.json";
    private final JsonWriter jsonWriter;
    private final JsonReader jsonReader;

    // EFFECTS: initializes habit list and JSON components and constructs habit tracker frame components
    public HabitTrackerFrame() {
        super(new BorderLayout());

        habitList = new HabitList();
        jsonWriter = new JsonWriter(DESTINATION_FILE);
        jsonReader = new JsonReader(DESTINATION_FILE);

        constructMainMenuDialog();

        constructHabitListTable();

        constructTopPanel();

        constructBtnPanel();
    }

    // MODIFIES: this and main menu panel
    // EFFECTS: constructs main menu dialog and panel with load list and new list buttons
    public void constructMainMenuDialog() {
        mainMenuDialog = new JDialog();
        mainMenuDialog.setTitle("Main Menu");
        mainMenuPanel = new JPanel();
        mainMenuDialog.setContentPane(mainMenuPanel);

        constructMainMenuButtons();
        createMenuBox(mainMenuPanel, btnLoadList, btnNewList);

        mainMenuDialog.setBackground(new Color(66, 88, 114));
        mainMenuPanel.setBackground(new Color(224, 241, 255));
    }

    // MODIFIES: this and main menu panel
    // EFFECTS: constructs buttons for load list from file and new habit list;
    //          if load list is pressed, read from JSON file; catches IOException if file cannot be loaded;
    //          catches NullPointerException if file is not found;
    //          if new list is pressed, new habit list will be provided;
    //          disposes main menu dialog if any buttons are pressed
    private void constructMainMenuButtons() {
        btnLoadList = new JButton("Load from file");
        btnNewList = new JButton("New habit list");

        btnLoadList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    habitList = jsonReader.read();
                    habitListToTableModel();
                    mainMenuDialog.dispose();
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(mainMenuPanel, "File could not be loaded");
                } catch (NullPointerException npe) {
                    JOptionPane.showMessageDialog(mainMenuPanel, "File not found");
                }
            }
        });
        btnNewList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainMenuDialog.dispose();
            }
        });
    }

    // MODIFIES: this and habitListTableModel
    // EFFECTS: for each habit in habit list, adds a row with habit information into the table model
    private void habitListToTableModel() {
        for (int i = 0; i < habitList.habitListSize(); i++) {
            Habit habit = habitList.getHabit(i);
            String habitName = habit.getHabitName();
            String rewardName = habit.getRewardName();
            int hoursNeededForReward = habit.getHoursNeededForReward();
            int hoursCompleted = habit.getHoursCompleted();
            int hoursLeftForReward = habit.getHoursLeftForReward();

            Object[] data = {habitName, rewardName, hoursNeededForReward, hoursCompleted, hoursLeftForReward};
            habitListTableModel.addRow(data);
            habitListTableModel.fireTableDataChanged();
        }
    }

    // MODIFIES: this and main menu panel
    // EFFECTS: creates a box to vertically display loadList and newList buttons;
    //          adds title to menu box
    public void createMenuBox(JPanel mainMenuPanel, JButton loadList, JButton newList) {
        Box box = Box.createVerticalBox();
        box.add(Box.createVerticalStrut(150));
        box.add(loadList);
        box.add(Box.createVerticalStrut(20));
        box.add(newList);
        mainMenuPanel.add(box);
    }

    // MODIFIES: this, habitListPanel
    // EFFECTS: constructs habit list table model and habit list table to display habit list with column names;
    //          adds pop-up menu to change habits to habitListTable;
    //          adds TableMouseListener to table
    public void constructHabitListTable() {
        habitListPanel = new JPanel();

        String[] columnNames = {"Habit",
                "Reward",
                "Hours Needed for Reward",
                "Hours Completed",
                "Hours Left for Reward"};

        habitListTableModel = new DefaultTableModel(columnNames, 0);
        habitListTable = new JTable(habitListTableModel) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        habitListTableMouseListener = new HabitListTableMouseListener(habitListTable);

        constructPopupMenu();
        habitListTable.setComponentPopupMenu(popupMenu);
        habitListTable.addMouseListener(habitListTableMouseListener);
        habitListTable.setBackground(new Color(224, 241, 255));
        habitListTable.getTableHeader().setBackground(new Color(148, 207, 255));
    }


    // EFFECTS: constructs popup menu when right-clicking row in table with options to log hours, change reward,
    //          and delete habit
    public void constructPopupMenu() {
        popupMenu = new JPopupMenu();

        menuItemLogHabit = new JMenuItem("Log hours");
        menuItemChangeReward = new JMenuItem("Change reward");
        menuItemDeleteHabit = new JMenuItem("Delete habit");

        menuItemLogHabit.addActionListener(this);
        menuItemChangeReward.addActionListener(this);
        menuItemDeleteHabit.addActionListener(this);

        popupMenu.add(menuItemLogHabit);
        popupMenu.add(menuItemChangeReward);
        popupMenu.add(menuItemDeleteHabit);
    }

    // MODIFIES: this, habitList, habitListTableModel
    // EFFECTS: performs required actions for log habit, change reward, and delete habit buttons;
    //          if row is not selected, ArrayIndexOutOfBoundsException is caught and message dialog displayed
    @Override
    public void actionPerformed(ActionEvent event) {
        JMenuItem menu = (JMenuItem) event.getSource();

        if (menu == menuItemLogHabit) {
            try {
                logHabit();
            } catch (ArrayIndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(habitListPanel, "Error: Select row to log habit");
            }
        } else if (menu == menuItemChangeReward) {
            try {
                changeReward();
            } catch (ArrayIndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(habitListPanel, "Error: Select row to change reward");
            }
        } else if (menu == menuItemDeleteHabit) {
            try {
                deleteHabit();
            } catch (ArrayIndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(habitListPanel, "Error: Select row to delete habit");
            }
        }
    }

    // MODIFIES: this, habitListTableModel, habitListTable, habitList
    // EFFECTS: shows input box when log habit is selected to log hours;
    //          gets input hours and applies it to habitList and habit table and model;
    //          if a reward is unlocked, displays reward message dialog;
    //          catches NumberFormatException if an integer is not inputted
    private void logHabit() {
        int column = 0;
        int row = habitListTable.getSelectedRow();
        String selectedHabitName = habitListTable.getModel().getValueAt(row, column).toString();
        try {
            String input = JOptionPane.showInputDialog("Enter hours to log:");
            if (input != null) {
                int newHours = Integer.parseInt(input);

                showRewardPopup(selectedHabitName, newHours);

                int hoursCompletedColumn = 3;
                int hoursLeftColumn = 4;
                habitListTableModel.setValueAt(habitList.getHabit(selectedHabitName).getHoursCompleted(),
                        row, hoursCompletedColumn);
                habitListTableModel.setValueAt(habitList.getHabit(selectedHabitName).getHoursLeftForReward(),
                        row, hoursLeftColumn);
                habitListTableModel.fireTableDataChanged();

            } else {
                JOptionPane.showMessageDialog(habitListPanel, "Need to enter hours to log habit");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(habitListPanel, "Hours entered need to be an integer");
        }
    }

    // MODIFIES: habitListPanel
    // EFFECTS: if hours needed to obtain rewards are reached, displays reward message dialog with rewards unlocked
    //          and number of rewards unlocked
    private void showRewardPopup(String selectedHabitName, int newHours) {
        Map<String, Integer> rewardNameAndTimesCompleted = habitList.logHabit(selectedHabitName, newHours);
        String rewardName = habitList.getHabit(selectedHabitName).getRewardName();
        int completedTimes = rewardNameAndTimesCompleted.get(rewardName);

        ImageIcon icon = new ImageIcon("image/trophy.png");
        if (completedTimes > 0) {
            JOptionPane.showMessageDialog(habitListPanel, "Reward(s) unlocked: " + rewardName
                    + " x" + completedTimes, "Rewards", JOptionPane.INFORMATION_MESSAGE, icon);
        }
    }

    // MODIFIES: this, habitListTableModel, habitListTable, habitList, habit
    // EFFECTS: for selected row, shows input dialogs to enter reward and hours needed;
    //          if input is not null, changes reward and hours for habit in habitList;
    //          updates habitListTableModel with new reward name, hours needed, and hours left for reward;
    //          if hours inputted is not an integer, catches NumberFormatException and displays error message dialog
    private void changeReward() {
        int column = 0;
        int row = habitListTable.getSelectedRow();
        String selectedHabitName = habitListTable.getModel().getValueAt(row, column).toString();

        try {
            String newRewardName = JOptionPane.showInputDialog("Enter new reward:");
            String input = JOptionPane.showInputDialog("Enter hours needed to obtain reward:");
            int newRewardHours = Integer.parseInt(input);
            habitList.changeReward(selectedHabitName, newRewardName, newRewardHours);
            updateColumnsChangeReward(row, selectedHabitName);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(habitListPanel, "Hours entered need to be an integer");
        }
    }

    // MODIFIES: habitListTableModel
    // EFFECTS: updates values after reward is changed
    private void updateColumnsChangeReward(int row, String selectedHabitName) {
        int rewardNameColumn = 1;
        int rewardHoursColumn = 2;
        int hoursLeftColumn = 4;
        habitListTableModel.setValueAt(habitList.getHabit(selectedHabitName).getRewardName(),
                row, rewardNameColumn);
        habitListTableModel.setValueAt(habitList.getHabit(selectedHabitName).getHoursNeededForReward(),
                row, rewardHoursColumn);
        habitListTableModel.setValueAt(habitList.getHabit(selectedHabitName).getHoursLeftForReward(),
                row, hoursLeftColumn);
        habitListTableModel.fireTableDataChanged();
    }

    // MODIFIES: this, HabitListTableModel, HabitListTable, HabitList
    // EFFECTS: gets user input of selected row and deletes selected habit off table model and habit list
    private void deleteHabit() {
        int column = 0;
        int row = habitListTable.getSelectedRow();
        String selectedHabitName = habitListTable.getModel().getValueAt(row, column).toString();
        habitList.deleteHabit(selectedHabitName);

        habitListTableModel.removeRow(row);
        habitListTableModel.fireTableDataChanged();
    }

    // EFFECTS: constructs top panel for instructions
    public void constructTopPanel() {
        topPanel = new JPanel(new BorderLayout());
        Border border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black));
        Border margin = new EmptyBorder(10, 10, 10, 10);
        JLabel label = new JLabel("Add new habit or right-click on a habit for more options", JLabel.CENTER);
        label.setForeground(new Color(224, 241, 255));
        topPanel.add(label);
        topPanel.setBorder(new CompoundBorder(border, margin));
        topPanel.setBackground(new Color(74, 103, 138));
    }

    // EFFECTS: constructs button panel and adds AddNewHabit button to panel
    public void constructBtnPanel() {
        btnPanel = new JPanel(new BorderLayout());
        Border border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black));
        Border margin = new EmptyBorder(10, 10, 10, 10);
        createBtnAddNewHabit();
        btnPanel.add(btnAddNewHabit);
        btnPanel.setBorder(new CompoundBorder(border, margin));
        btnPanel.setBackground(Color.WHITE);
    }

    // MODIFIES: this, habitList, habitListTable, habitListTableModel
    // EFFECTS: create new habit when button is pressed and input is added for name, reward, and reward hours;
    //          adds updated information to habitList and habit table model;
    //          catches NumberFormatException if an integer is not inputted for hoursNeededForReward
    public void createBtnAddNewHabit() {
        btnAddNewHabit = new JButton("Add New Habit");
        btnAddNewHabit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String habitName = JOptionPane.showInputDialog("Enter new habit name:");
                    String rewardName = JOptionPane.showInputDialog("Enter new reward:");
                    String inputHours = JOptionPane.showInputDialog("Enter hours needed for reward:");
                    int hoursNeededForReward =
                            Integer.parseInt(inputHours);

                    if (habitName != null && rewardName != null) {
                        habitList.addNewHabit(habitName, rewardName, hoursNeededForReward);
                        addHabitToTableModel(habitName);
                    } else {
                        JOptionPane.showMessageDialog(habitListPanel,
                                "Need to provide habit or reward name");
                    }
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(habitListPanel, "Hours need to be an integer");
                }
            }
        });
    }

    // MODIFIES: this, habitListTableModel
    // EFFECTS: adds habit as a new row in table model
    private void addHabitToTableModel(String habitName) {
        Object[] data = {habitList.getHabit(habitName).getHabitName(),
                habitList.getHabit(habitName).getRewardName(),
                habitList.getHabit(habitName).getHoursNeededForReward(),
                habitList.getHabit(habitName).getHoursCompleted(),
                habitList.getHabit(habitName).getHoursLeftForReward()};

        habitListTableModel.addRow(data);
        habitListTableModel.fireTableDataChanged();
    }


    // EFFECTS: creates habit tracker frame  and adds constructed tables, panels, and dialogs to frame;
    //          adds window listener to frame
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Habit Tracker");
        frame.setLayout(new BorderLayout());

        HabitTrackerFrame habitTrackerFrame = new HabitTrackerFrame();
        habitTrackerFrame.setOpaque(true);

        habitTrackerFrame.mainMenuDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        habitTrackerFrame.mainMenuDialog.setSize(500, 400);
        habitTrackerFrame.mainMenuDialog.setLocationRelativeTo(null);

        addHabitListTableToFrame(frame, habitTrackerFrame);

        frame.add(habitTrackerFrame.topPanel, BorderLayout.NORTH);
        frame.add(habitTrackerFrame.btnPanel, BorderLayout.SOUTH);

        habitTrackerFrame.mainMenuDialog.setVisible(true);

        frame.setSize(800, 600);
        frame.setBackground(new Color(74, 103, 128));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        addWindowListenerToFrame(frame, habitTrackerFrame);
    }

    // MODIFIES: frame
    // EFFECTS: adds habit list table to frame and sets size
    private static void addHabitListTableToFrame(JFrame frame, HabitTrackerFrame habitTrackerFrame) {
        frame.add(new JScrollPane(habitTrackerFrame.habitListTable,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
        habitTrackerFrame.habitListTable.setPreferredScrollableViewportSize(new Dimension(600, 400));
        habitTrackerFrame.habitListTable.setFillsViewportHeight(true);
    }

    // MODIFIES: frame
    // EFFECTS: adds window listener that asks to save file on exit and closes frame
    private static void addWindowListenerToFrame(JFrame frame, HabitTrackerFrame habitTrackerFrame) {
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int ret = JOptionPane.showConfirmDialog(frame, "Save habit list?", "Save this habit list?",
                        JOptionPane.YES_NO_CANCEL_OPTION);
                if (ret == JOptionPane.CANCEL_OPTION) {
                    // go back to main frame
                } else if (ret == JOptionPane.YES_OPTION) {
                    saveHabitList(habitTrackerFrame, frame);
                } else {
                    frame.dispose();
                    System.exit(0);
                }
            }
        });
    }

    // MODIFIES: frame, this, habitList
    // EFFECTS: saves habit list to file and closes frame
    private static void saveHabitList(HabitTrackerFrame habitTrackerFrame, JFrame frame) {
        try {
            habitTrackerFrame.jsonWriter.open();
            habitTrackerFrame.jsonWriter.write(habitTrackerFrame.habitList);
            habitTrackerFrame.jsonWriter.close();

            JOptionPane.showMessageDialog(frame, "Habit list saved to file: " + DESTINATION_FILE);
            frame.dispose();
            System.exit(0);
        } catch (FileNotFoundException e1) {
            JOptionPane.showMessageDialog(frame, "Unable to save to file: " + DESTINATION_FILE);
        }
    }

    // EFFECTS: runs application
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(HabitTrackerFrame::createAndShowGUI);
    }
}
