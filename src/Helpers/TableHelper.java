package Helpers;

import javax.swing.*;

public class TableHelper {
  private JTable table;
  public TableHelper(JTable table) {
    this.table = table;
  }

  private void disableTable() {
    // Disable table selection and editing
    table.setEnabled(false);
    table.setRowSelectionAllowed(false);
    table.setColumnSelectionAllowed(false);
    table.setCellSelectionEnabled(false);
  }

  private void enableTable() {
    // Enable table selection and editing
    table.setEnabled(true);
    table.setRowSelectionAllowed(true);
    table.setColumnSelectionAllowed(true);
    table.setCellSelectionEnabled(true);
  }

  public void enterEditMode(JButton[] buttonsToShow, JButton[] buttonsToDisable) {
    // Disable table so the user cannot select any other row
    disableTable();

    // Enable buttons so the user can interact with
    for (JButton button : buttonsToShow) {
      button.setVisible(true);
    }

    // Disable buttons
    for (JButton button : buttonsToDisable) {
      button.setEnabled(false);
    }
  }

  public void exitEditMode(JButton[] buttonsToEnable, JButton[] buttonsToHide) {
    // Take back normal behavior
    enableTable();

    // Enable buttons so the user can interact with
    for (JButton button : buttonsToEnable) {
      button.setEnabled(true);
    }

    // Disable buttons
    for (JButton button : buttonsToHide) {
      button.setVisible(false);
    }

    // Removing row selection
    table.clearSelection();
  }
}
