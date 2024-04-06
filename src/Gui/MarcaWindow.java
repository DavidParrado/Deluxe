package Gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class MarcaWindow extends JFrame {
  private DefaultTableModel tableModel;
  private JTable table;
  private JButton addButton;
  private JButton editButton;
  private JButton deleteButton;
  private JButton saveButton;

  // Input fields for new marca
  private JTextField nombreField;
  private JTextField descripcionField;
  private JTextField paisField;

  public MarcaWindow() {
    setTitle("Marca Management");
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50)); // Add padding

    // Main content panel
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());

    // Table to display database rows
    tableModel = new DefaultTableModel();
    table = new JTable(tableModel);
    JScrollPane scrollPane = new JScrollPane(table);
    mainPanel.add(scrollPane, BorderLayout.CENTER);

    // Buttons for CRUD operations
    JPanel buttonPanel = new JPanel();
    addButton = new JButton("Add");
    editButton = new JButton("Edit");
    deleteButton = new JButton("Delete");
    saveButton = new JButton("Save");
    saveButton.setVisible(false);
    buttonPanel.add(addButton);
    buttonPanel.add(editButton);
    buttonPanel.add(deleteButton);
    buttonPanel.add(saveButton);
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);

    // Header panel for title and input
    JPanel headerPanel = new JPanel(new BorderLayout());

    // Title to indicate the current table
    JLabel titleLabel = new JLabel("Marcas");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
    titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
    titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    headerPanel.add(titleLabel, BorderLayout.NORTH);

    // Input fields for adding new marca
    JPanel inputPanel = new JPanel(new GridLayout(3, 2));
    inputPanel.add(new JLabel("Nombre:"));
    nombreField = new JTextField();
    inputPanel.add(nombreField);
    inputPanel.add(new JLabel("Descripción:"));
    descripcionField = new JTextField();
    inputPanel.add(descripcionField);
    inputPanel.add(new JLabel("País:"));
    paisField = new JTextField();
    inputPanel.add(paisField);
    headerPanel.add(inputPanel, BorderLayout.CENTER);

    mainPanel.add(headerPanel, BorderLayout.NORTH);


    panel.add(mainPanel, BorderLayout.CENTER);

    // Add action listener for add button
    addButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        addMarca();
      }
    });

    // Add action listener for edit button
    editButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        editMarca();
      }
    });

    // Add action listener for save button
    saveButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        saveMarca();
      }
    });

    // Add action listener for save button
    deleteButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        deleteMarca();
      }
    });

    // Initialize table with sample data
    initTable();

    setContentPane(panel);
    setVisible(true);
  }

  // Method to initialize the table with sample data
  private void initTable() {
    Vector<String> columnNames = new Vector<>();
    columnNames.add("ID");
    columnNames.add("Nombre");
    columnNames.add("Descripción");
    columnNames.add("País");

    Vector<Vector<String>> data = new Vector<>();
    Vector<String> row1 = new Vector<>();
    row1.add("1");
    row1.add("Brand 1");
    row1.add("Description 1");
    row1.add("Country 1");
    Vector<String> row2 = new Vector<>();
    row2.add("2");
    row2.add("Brand 2");
    row2.add("Description 2");
    row2.add("Country 2");

    data.add(row1);
    data.add(row2);

    tableModel.setDataVector(data, columnNames);
  }

  // Method to add a new marca to the table
  private void addMarca() {
    String nombre = nombreField.getText();
    String descripcion = descripcionField.getText();
    String pais = paisField.getText();

    // Validate input fields
    if (nombre.isEmpty() || descripcion.isEmpty() || pais.isEmpty()) {
      JOptionPane.showMessageDialog(this, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    // Add new row to the table
    Vector<String> row = new Vector<>();
    row.add(""); // Empty ID, to be filled by database
    row.add(nombre);
    row.add(descripcion);
    row.add(pais);
    tableModel.addRow(row);

    // Clear input fields
    nombreField.setText("");
    descripcionField.setText("");
    paisField.setText("");

    JOptionPane.showMessageDialog(this, "Marca added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
  }

  private void editMarca() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(MarcaWindow.this, "Please select a row to edit", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    // Retrieve data from the selected row
    String id = tableModel.getValueAt(selectedRow, 0).toString();
    String nombre = tableModel.getValueAt(selectedRow, 1).toString();
    String descripcion = tableModel.getValueAt(selectedRow, 2).toString();
    String pais = tableModel.getValueAt(selectedRow, 3).toString();

    // Populate input fields with the data from the selected row
    nombreField.setText(nombre);
    descripcionField.setText(descripcion);
    paisField.setText(pais);

    saveButton.setVisible(true);
    // Display a dialog or switch the UI to edit mode
    // You can display an "Update" button for the user to save the changes
    // Or directly update the row when the user finishes editing
  }

  private void saveMarca() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(MarcaWindow.this, "Please select a row to edit", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    // Retrieve modified data from input fields
    String nombre = nombreField.getText();
    String descripcion = descripcionField.getText();
    String pais = paisField.getText();

    // Update the corresponding row in the table with the new data
    tableModel.setValueAt(nombre, selectedRow, 1);
    tableModel.setValueAt(descripcion, selectedRow, 2);
    tableModel.setValueAt(pais, selectedRow, 3);

    // Clear input fields
    nombreField.setText("");
    descripcionField.setText("");
    paisField.setText("");

    saveButton.setVisible(false);
    // Display success message
    JOptionPane.showMessageDialog(MarcaWindow.this, "Marca updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
  }

  private void deleteMarca() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(MarcaWindow.this, "Please select a row to delete", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    // Display confirmation dialog before deleting
    int confirm = JOptionPane.showConfirmDialog(MarcaWindow.this, "Are you sure you want to delete this marca?", "Confirmation", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
      // Remove the selected row from the table model
      tableModel.removeRow(selectedRow);

      // Clear input fields
      nombreField.setText("");
      descripcionField.setText("");
      paisField.setText("");

      // Display success message
      JOptionPane.showMessageDialog(MarcaWindow.this, "Marca deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      new MarcaWindow();
    });
  }
}
