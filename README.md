# Habits & Goals Tracker

### What the application does:

This application helps individuals stay on track with new habits, such as working out, reading a book, or
completing a coding project. Users can add as many habits as they want to their habit tracker and *track the number of
hours* they have completed each one. Users can also *set customized rewards* (i.e. "rest day" or "hang out with friends")
for each habit and set the amount of hours needed to complete the habit to obtain the reward. As people often pull out 
when their goal is not imminent, using short-term rewards helps keep people on-track and "reset" to follow through with 
their habits in the long-term.

**Possible uses for the application:**
- Tracking workout hours
- Recording hours of reading completed
- Logging number of hours you worked on a project

### Who can use it:
This application is designed for anyone who wants to keep track of anything ranging from personal habits to ensuring one 
finishes a project or a resolution. Essentially, this app helps keep people accountable as it tracks user's progress in 
whatever they want to achieve. This application is open to all as it can be personalized to track anything users want. 

### Why it interests me:
Personally, I have always struggled with starting new habits or following through with a New Year's resolution. So,
having a habit tracker that can be easily personalized and simple to use could help me follow through with my goals.
Since many individuals have the same challenges, I hope to design a multi-purpose tool to help them follow through with 
their goals. With first-hand experience with these struggles, I will include features that can help motivate
people to persist and complete their personal goals. Finally, creating a habits/goals tracker has the 
possibility of helping a wide range of people, so I aim to make the design simple and intuitive, so it is as inclusive 
as possible.

### User Stories:
- As a user, I want to be able to add a habit to my habits tracker list
- As a user, I want to be able to input the number of hours I have performed the habit
- As a user, I want to be able to view all my habits, the rewards and number of hours left to get the reward
- As a user, I want to be able to delete a habit or goal
- As a user, I want to be able to change a reward for an existing habit
- As a user, I want to be able to be asked when closing window to save habit list to file
- As a user, I want to be able to load my previously saved habit tracker from file

### Phase 4: Task 2:
- The Map interface is implemented in the HabitList class in the LogHabit() method
- The HabitTrackerApp and HabitTrackerFrame classes calls the LogHabit() method

### Phase 4: Task 3:
If I had more time to work on the project, I would split up the HabitTrackerFrame class, since it has low cohesion.
- I would create independent classes for each component of the over GUI. For instance, the main menu panel, the pop-up
menu, and the habit list table could each be their own classes, since they have distinct tasks. One is for loading the 
habit list, the other for editing habits, and the table is for displaying the habit list. 
- The createAndShowGUI method and the Main method should also be split into different classes to adhere to the single 
responsibility rule. 
- This would also improve readability of the code, as the HabitTrackerFrame class is pretty lengthy and has a lot of
different components that can be separated.

I would also add exceptions to my Habit and HabitList classes instead of checking the conditions using if statements or
relying on REQUIRES clauses to make those classes more robust. 
- For instance, I could throw an InvalidHoursException when I need to ensure that hours are greater than 0. 