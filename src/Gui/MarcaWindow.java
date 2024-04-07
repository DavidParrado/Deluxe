package Gui;

import Entities.Marca;
import Factories.EntityFactory;
import Helpers.SerialHelper;
import Params.MarcaParams;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
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
  private EntityFactory entityFactory = new EntityFactory();
  private Marca marca = entityFactory.createMarcaEntity();

  public MarcaWindow() {
    setTitle("Marca Management");
    setSize(800, 600);
    // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
    Vector<Vector<Object>> data = new Vector<>();
    Vector<String> columnNames = new Vector<>();
    columnNames.add("ID");
    columnNames.add("Nombre");
    columnNames.add("Descripción");
    columnNames.add("País");

    ResultSet marcas = marca.find();

    try {
      while (marcas.next()) {
        Vector<Object> row = new Vector<>();
        row.add(marcas.getObject("id_marca"));
        row.add(marcas.getObject("nombre"));
        row.add(marcas.getObject("descripcion"));
        row.add(marcas.getObject("pais"));
        data.add(row);
      }
      tableModel.setDataVector(data, columnNames);
      marcas.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  // Method to add a new marca to the table
  private void addMarca() {
    String nombre = nombreField.getText();
    String descripcion = descripcionField.getText();
    String pais = paisField.getText();

    // Validate input fields
    if (nombre.isEmpty() || descripcion.isEmpty() || pais.isEmpty()) {
      displayError("All fields are required");
      return;
    }

    int id = new SerialHelper().getSerial("id_marca", "marca");

    if(id == 0 ) {
      displayError("No se pudo ejecutar esta operacion vuelve a intentarlo");
      return;
    }

    try {
      marca.create(new MarcaParams(nombre,descripcion,pais));
    } catch (Exception e) {
      displayError("No se pudo insertar ningun dato, vuelve a intentarlo");
      return;
    }

    // Add new row to the table
    Vector<String> row = new Vector<>();
    row.add(id+""); // Empty ID, to be filled by database
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
  }

  private void saveMarca() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
      displayError("Please select a row to edit");
      return;
    }

    // Retrieve id to update expected row
    String id = tableModel.getValueAt(selectedRow,0).toString();

    // Retrieve modified data from input fields
    String nombre = nombreField.getText();
    String descripcion = descripcionField.getText();
    String pais = paisField.getText();

    try {
      marca.update(id, new MarcaParams(nombre,descripcion,pais));
    } catch (Exception e) {
      displayError("No se pudo completar la operacion intentalo de nuevo");
      return;
    }

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
      displayError("Please select a row to delete");
      return;
    }

    // Retrieve id to update expected row
    String id = tableModel.getValueAt(selectedRow,0).toString();

    // Display confirmation dialog before deleting
    int confirm = JOptionPane.showConfirmDialog(MarcaWindow.this, "Are you sure you want to delete this marca?", "Confirmation", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
      try {
        marca.delete(id);
      } catch (Exception e) {
        displayError("No se pudo completar la operacion, intentalo de nuevo");
        return;
      }
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

  private void displayError(String message) {
    JOptionPane.showMessageDialog(MarcaWindow.this, message, "Error", JOptionPane.ERROR_MESSAGE);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      new MarcaWindow();
    });
  }
}
