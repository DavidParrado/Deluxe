package Gui;

import Entities.Usuario;
import Factories.EntityFactory;
import Helpers.*;
import Params.UsuarioParams;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class UsuarioWindow extends JFrame {
  private static UsuarioWindow instance;
  private JPanel panel;
  private JPanel mainPanel;
  private JPanel headerPanel;
  private JPanel inputPanel;
  private JPanel buttonPanel;
  private DefaultTableModel tableModel;
  private JTable table;
  private JButton addButton;
  private JButton editButton;
  private JButton deleteButton;
  private JButton pdfButton;
  private JButton saveButton;
  private JButton exitEditModeButton;

  // Labels for inputs
  private JLabel titleLabel;
  private JLabel nombreLabel = new JLabel("Nombre:");
  private JLabel apellidoLabel = new JLabel("Apellido:");
  private JLabel correoLabel = new JLabel("Correo:");
  private JLabel contrasenaLabel = new JLabel("Contraseña:");
  private JLabel direccionLabel = new JLabel("Dirección:");
  private JLabel telefonoLabel = new JLabel("Teléfono:");

  // Input fields for new usuario
  private JTextField nombreField;
  private JTextField apellidoField;
  private JTextField correoField;
  private JTextField contrasenaField;
  private JTextField direccionField;
  private JTextField telefonoField;
  private EntityFactory entityFactory = new EntityFactory();
  private Usuario usuario = entityFactory.createUsuarioEntity();
  private PdfGenerator pdfGenerator = new PdfGenerator();
  private PasswordEncryptor encryptor = new PasswordEncryptor();
  private TableHelper tableHelper;
  private JButton[] editModeButtons;
  private JButton[] operationButtons;

  public UsuarioWindow() {
    setTitle("Usuario Management");
    setSize(800, 600);
    // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    panel = new JPanel();
    panel.setLayout(new BorderLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50)); // Add padding

    // Main content panel
    mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());

    // Table to display database rows
    tableModel = new DefaultTableModel();
    table = new JTable(tableModel) {
      @Override
      public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component comp = super.prepareRenderer(renderer, row, column);
        if (isRowSelected(row)) {
          // rgb(184, 207, 229); or b8cfe5;
          comp.setBackground(new Color(184,207,229)); // Change background color of selected row
        } else {
          comp.setBackground(Color.WHITE); // Reset background color for other rows
        }
        return comp;
      }
    };
    tableHelper = new TableHelper(table);

    JScrollPane scrollPane = new JScrollPane(table);
    mainPanel.add(scrollPane, BorderLayout.CENTER);

    // Buttons for CRUD operations
    buttonPanel = new JPanel();
    addButton = new JButton("Agregar");
    editButton = new JButton("Editar");
    deleteButton = new JButton("Eliminar");
    pdfButton = new JButton("Descargar PDF");
    saveButton = new JButton("Guardar cambios");
    saveButton.setVisible(false);
    exitEditModeButton = new JButton("Salir modo edicion");
    exitEditModeButton.setVisible(false);

    operationButtons = new JButton[]{addButton, editButton, deleteButton, pdfButton};
    editModeButtons = new JButton[]{saveButton, exitEditModeButton};

    buttonPanel.add(addButton);
    buttonPanel.add(editButton);
    buttonPanel.add(deleteButton);
    buttonPanel.add(pdfButton);
    buttonPanel.add(saveButton);
    buttonPanel.add(exitEditModeButton);

    mainPanel.add(buttonPanel, BorderLayout.SOUTH);

    // Header panel for title and input
    headerPanel = new JPanel(new BorderLayout());

    // Title to indicate the current table
    titleLabel = new JLabel("Usuarios");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
    titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
    titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    headerPanel.add(titleLabel, BorderLayout.NORTH);

    // Input fields for adding new usuario
    inputPanel = new JPanel(new GridLayout(3, 2,10,5));
    inputPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    inputPanel.add(nombreLabel);
    nombreField = new JTextField();
    inputPanel.add(nombreField);
    inputPanel.add(apellidoLabel);
    apellidoField = new JTextField();
    inputPanel.add(apellidoField);
    inputPanel.add(correoLabel);
    correoField = new JTextField();
    inputPanel.add(correoField);
    inputPanel.add(contrasenaLabel);
    contrasenaField = new JPasswordField();
    inputPanel.add(contrasenaField);
    inputPanel.add(direccionLabel);
    direccionField = new JTextField();
    inputPanel.add(direccionField);
    inputPanel.add(telefonoLabel);
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

    // Add action listener for pdf button
    pdfButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ResultSet usuarios = usuario.find();
        String[] headers = {"ID", "Correo", "Nombre", "Apellido", "Direccion", "Telefono", "Rol"};
        String [] fields = { "id_usuario", "correo", "nombre", "apellido", "direccion", "telefono", "rol" };
        String filename = "usuarios";

        pdfGenerator.downloadPdf(usuarios,headers,fields, filename);
        displayMessage("Pdf generado correctamente.");
      }
    });

    // Add action listener for save button
    saveButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        saveUsuario();
      }
    });

    // Add action listener for save button
    exitEditModeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        tableHelper.exitEditMode(operationButtons, editModeButtons);
        clearInputFields();
      }
    });

    // Add action listener for delete button
    deleteButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        deleteUsuario();
      }
    });

    // Apply theme
    applyFontColor(Theme.fontColor);
    applyButtonColor(Theme.buttonColor);
    applyBackgroundColor(Theme.backgroundColor);

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

    String contrasenaEncriptada = encryptor.encryptPassword(contrasena);

    // Validate input fields
    if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || direccion.isEmpty() || telefono.isEmpty()) {
      displayError("Todos los campos son requeridos");
      return;
    }

    int id = new SerialHelper().getSerial("id_usuario", "usuario");

    if(id < 0) {
      displayError("No se pudo ejecutar esta operacion vuelve a intentarlo");
      return;
    }
    try {
      usuario.create(new UsuarioParams(correo, contrasenaEncriptada, nombre, apellido, direccion, telefono, "Usuario"));
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

    displayMessage("Usuario insertado correctamente");
  }

  private void editUsuario() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
      displayError("Selecciona una fila para editar");
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

    // Start edit mode
    tableHelper.enterEditMode(editModeButtons, operationButtons);

  }

  private void saveUsuario() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
      displayError("Selecciona una fila para editar");
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
      displayError("Revisa los campos vacios, recuerda que es opcional actualizar la contraseña");
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

    // Exit edit mode
    tableHelper.exitEditMode(operationButtons, editModeButtons);

    // Clear input fields
    clearInputFields();

    // Display success message
    displayMessage("Usuario actualizado correctamente");
  }


  private void deleteUsuario() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
      displayError("Selecciona una fila para eliminar");
      return;
    }

    // Retrieve id to delete expected row
    String id = tableModel.getValueAt(selectedRow, 0).toString();

    // Display confirmation dialog before deleting
    int confirm = JOptionPane.showConfirmDialog(UsuarioWindow.this, "Estas seguro que deseas eliminar este usuario?", "Confirmation", JOptionPane.YES_NO_OPTION);
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

      // Display success message
      displayMessage("Usuario eliminado correctamente");
    }
  }


  private void displayError(String message) {
    JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
  }

  private void displayMessage(String message) {
    JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
  }

  private void clearInputFields() {
    nombreField.setText("");
    apellidoField.setText("");
    direccionField.setText("");
    telefonoField.setText("");
    contrasenaField.setText("");
    correoField.setText("");
  }

  public void applyBackgroundColor(Color backgroundColor) {
    StyleHelper.setBackgroundColor(new JPanel[]{panel,mainPanel,headerPanel,inputPanel,buttonPanel},backgroundColor);
  }

  public void applyButtonColor(Color buttonColor) {
    StyleHelper.setBackgroundColor(new JButton[]{addButton,editButton,deleteButton,saveButton,pdfButton,exitEditModeButton},buttonColor);
  }

  public void applyFontColor(Color fontColor) {
    StyleHelper.setFontColor(new JLabel[]{titleLabel},fontColor);
    StyleHelper.setFontColor(new JComponent[]{addButton,editButton,deleteButton,saveButton,pdfButton,exitEditModeButton},fontColor);
    StyleHelper.setFontColor(new JComponent[]{nombreLabel,apellidoLabel,correoLabel,contrasenaLabel,direccionLabel,telefonoLabel},fontColor);
  }

  public static UsuarioWindow getInstance() {
    if(instance == null) {
      instance = new UsuarioWindow();
    }
    return instance;
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      new UsuarioWindow();
    });
  }
}
