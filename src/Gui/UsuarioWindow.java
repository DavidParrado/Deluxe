package Gui;

import Entities.Usuario;
import Helpers.SerialHelper;
import Params.UsuarioParams;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class UsuarioWindow extends JFrame {
  private DefaultTableModel tableModel;
  private JTable table;
  private JButton addButton;
  private JButton editButton;
  private JButton deleteButton;
  private JButton saveButton;

  // Input fields for new usuario
  private JTextField nombreField;
  private JTextField apellidoField;
  private JTextField correoField;
  private JTextField contrasenaField;
  private JTextField direccionField;
  private JTextField telefonoField;
  private Usuario usuario;

  public UsuarioWindow() {
    setTitle("Usuario Management");
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
    JLabel titleLabel = new JLabel("Usuarios");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
    titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
    titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    headerPanel.add(titleLabel, BorderLayout.NORTH);

    // Input fields for adding new usuario
    JPanel inputPanel = new JPanel(new GridLayout(3, 2,10,5));
    inputPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    inputPanel.add(new JLabel("Nombre:"));
    nombreField = new JTextField();
    inputPanel.add(nombreField);
    inputPanel.add(new JLabel("Apellido:"));
    apellidoField = new JTextField();
    inputPanel.add(apellidoField);
    inputPanel.add(new JLabel("Correo:"));
    correoField = new JTextField();
    inputPanel.add(correoField);
    inputPanel.add(new JLabel("Contraseña:"));
    contrasenaField = new JTextField();
    inputPanel.add(contrasenaField);
    inputPanel.add(new JLabel("Dirección:"));
    direccionField = new JTextField();
    inputPanel.add(direccionField);
    inputPanel.add(new JLabel("Teléfono:"));
    telefonoField = new JTextField();
    inputPanel.add(telefonoField);
    headerPanel.add(inputPanel, BorderLayout.CENTER);

    mainPanel.add(headerPanel, BorderLayout.NORTH);

    panel.add(mainPanel, BorderLayout.CENTER);

    // Add action listener for add button
    addButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        addUsuario();
      }
    });

    // Add action listener for edit button
    editButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        editUsuario();
      }
    });

    // Add action listener for save button
    saveButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        saveUsuario();
      }
    });

    // Add action listener for delete button
    deleteButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        deleteUsuario();
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
    columnNames.add("Apellido");
    columnNames.add("Correo");
    columnNames.add("Dirección");
    columnNames.add("Teléfono");

    usuario = new Usuario();

    ResultSet usuarios = usuario.find();

    try {
      while (usuarios.next()) {
        Vector<Object> row = new Vector<>();
        row.add(usuarios.getObject("id_usuario"));
        row.add(usuarios.getObject("nombre"));
        row.add(usuarios.getObject("apellido"));
        row.add(usuarios.getObject("correo"));
        row.add(usuarios.getObject("direccion"));
        row.add(usuarios.getObject("telefono"));
        data.add(row);
      }
      tableModel.setDataVector(data, columnNames);
      usuarios.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  // Method to add a new usuario to the table
  private void addUsuario() {
    String nombre = nombreField.getText();
    String apellido = apellidoField.getText();
    String correo = correoField.getText();
    String contrasena = contrasenaField.getText();
    String direccion = direccionField.getText();
    String telefono = telefonoField.getText();

    // Validate input fields
    if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || direccion.isEmpty() || telefono.isEmpty()) {
      displayError("All fields are required");
      return;
    }

    int id = new SerialHelper().getSerial("id_usuario", "usuario");

    if(id < 0) {
      displayError("No se pudo ejecutar esta operacion vuelve a intentarlo");
      return;
    }
    try {
      usuario.create(new UsuarioParams(correo, contrasena, nombre, apellido, direccion, telefono, "Usuario"));
    } catch (Exception e) {
      displayError(e.getMessage());
      return;
    }

    // Add new row to the table
    Vector<String> row = new Vector<>();
    row.add(id+""); // Empty ID, to be filled by database
    row.add(nombre);
    row.add(apellido);
    row.add(correo);
    row.add(direccion);
    row.add(telefono);
    tableModel.addRow(row);

    // Clear input fields
    clearInputFields();

    saveButton.setVisible(false);

    JOptionPane.showMessageDialog(this, "Usuario added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
  }

  private void editUsuario() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
      displayError("Please select a row to edit");
      return;
    }

    // Retrieve data from the selected row
    String nombre = tableModel.getValueAt(selectedRow, 1).toString();
    String apellido = tableModel.getValueAt(selectedRow, 2).toString();
    String correo = tableModel.getValueAt(selectedRow, 3).toString();
    String direccion = tableModel.getValueAt(selectedRow, 4).toString();
    String telefono = tableModel.getValueAt(selectedRow, 5).toString();

    // Populate input fields with the data from the selected row
    nombreField.setText(nombre);
    apellidoField.setText(apellido);
    direccionField.setText(direccion);
    telefonoField.setText(telefono);
    correoField.setText(correo);

    saveButton.setVisible(true);
    // Display a dialog or switch the UI to edit mode
    // You can display an "Update" button for the user to save the changes
  }

  private void saveUsuario() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
      displayError("Please select a row to save changes");
      return;
    }

    // Retrieve id to update expected row
    String id = tableModel.getValueAt(selectedRow, 0).toString();

    // Retrieve modified data from input fields
    String nombre = nombreField.getText();
    String apellido = apellidoField.getText();
    String direccion = direccionField.getText();
    String telefono = telefonoField.getText();
    String correo = correoField.getText();
    String contrasena = contrasenaField.getText();

    // Validate input fields
    if (nombre.isEmpty() || apellido.isEmpty() || direccion.isEmpty() || telefono.isEmpty() || correo.isEmpty()) {
      displayError("All fields are required");
      return;
    }

    try {
      // Update the usuario using the provided id and new data
      usuario.update(id, new UsuarioParams(correo, contrasena, nombre, apellido, direccion, telefono, "Usuario"));
    } catch (Exception e) {
      displayError(e.getMessage());
      return;
    }

    // Update the corresponding row in the table with the new data
    tableModel.setValueAt(nombre, selectedRow, 1);
    tableModel.setValueAt(apellido, selectedRow, 2);
    tableModel.setValueAt(correo, selectedRow, 3);
    tableModel.setValueAt(direccion, selectedRow, 4);
    tableModel.setValueAt(telefono, selectedRow, 5);

    // Clear input fields
    clearInputFields();

    saveButton.setVisible(false);
    // Display success message
    JOptionPane.showMessageDialog(UsuarioWindow.this, "Usuario updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
  }


  private void deleteUsuario() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
      displayError("Please select a row to delete");
      return;
    }

    // Retrieve id to delete expected row
    String id = tableModel.getValueAt(selectedRow, 0).toString();

    // Display confirmation dialog before deleting
    int confirm = JOptionPane.showConfirmDialog(UsuarioWindow.this, "Are you sure you want to delete this usuario?", "Confirmation", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
      try {
        // Delete the usuario using the provided id
        usuario.delete(id);
      } catch (Exception e) {
        displayError(e.getMessage());
        return;
      }

      // Remove the selected row from the table model
      tableModel.removeRow(selectedRow);

      // Clear input fields
      clearInputFields();

      // Display success message
      JOptionPane.showMessageDialog(UsuarioWindow.this, "Usuario deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
  }


  private void displayError(String message) {
    JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
  }

  private void clearInputFields() {
    nombreField.setText("");
    apellidoField.setText("");
    direccionField.setText("");
    telefonoField.setText("");
    contrasenaField.setText("");
    correoField.setText("");
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      new UsuarioWindow();
    });
  }
}
