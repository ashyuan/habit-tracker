package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// A mouse listener for HabitList table
// References: https://www.codejava.net/java-se/swing/jtable-popup-menu-example

public class HabitListTableMouseListener extends MouseAdapter {
    private JTable table;

    // EFFECTS: constructs new mouse listener with given table
    public HabitListTableMouseListener(JTable table) {
        this.table = table;
    }

    // EFFECTS: selects row where mouse is clicked
    @Override
    public void mouseReleased(MouseEvent event) {
        Point point = event.getPoint();
        int currentRow = table.rowAtPoint(point);
        if (currentRow >= 0 && currentRow < table.getRowCount()) {
            table.setRowSelectionInterval(currentRow, currentRow);
        } else {
            table.clearSelection();
        }
    }
}

