package Gui;

import Entities.Marca;
import Factories.EntityFactory;
import Helpers.PdfGenerator;
import Helpers.SerialHelper;
import Helpers.StyleHelper;
import Helpers.TableHelper;
import Params.MarcaParams;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class MarcaWindow extends JFrame {
  private static MarcaWindow instance;
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

  // Labels
  private JLabel titleLabel;
  private JLabel nombreLabel = new JLabel("Nombre:");
  private JLabel descripcionLabel = new JLabel("Descripción:");
  private JLabel paisLabel = new JLabel("País:");

  // Input fields for new marca
  private JTextField nombreField;
  private JTextField descripcionField;
  private JTextField paisField;
  private EntityFactory entityFactory = new EntityFactory();
  private Marca marca = entityFactory.createMarcaEntity();
  private PdfGenerator pdfGenerator = new PdfGenerator();
  private TableHelper tableHelper;
  private JButton[] editModeButtons;
  private JButton[] operationButtons;

  public MarcaWindow() {
    setTitle("Marca Management");
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
    titleLabel = new JLabel("Marcas");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
    titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
    titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    headerPanel.add(titleLabel, BorderLayout.NORTH);

    // Input fields for adding new marca
    inputPanel = new JPanel(new GridLayout(3, 2,10,5));
    inputPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    inputPanel.add(nombreLabel);
    nombreField = new JTextField();
    inputPanel.add(nombreField);
    inputPanel.add(descripcionLabel);
    descripcionField = new JTextField();
    inputPanel.add(descripcionField);
    inputPanel.add(paisLabel);
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

    // Add action listener for pdf button
    pdfButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ResultSet marcas = marca.find();
        String[] headers = {"ID", "Nombre", "Descripcion", "Pais" };
        String [] fields = { "id_marca", "nombre", "descripcion", "pais" };
        String filename = "marcas";

        pdfGenerator.downloadPdf(marcas,headers,fields, filename);
        displayMessage("Pdf generado correctamente.");
      }
    });

    // Add action listener for save button
    saveButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        saveMarca();
      }
    });

    exitEditModeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        tableHelper.exitEditMode(operationButtons, editModeButtons);
        clearInputFields();
      }
    });

    deleteButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        deleteMarca();
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
      displayError("Todos los campos son requeridos");
      return;
    }

    int id = new SerialHelper().getSerial("id_marca", "marca");

    if(id < 0 ) {
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
    clearInputFields();

    displayMessage("Marca insertada correctamente");
  }

  private void editMarca() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
      displayError("Selecciona una fila para editar");
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

    // Start edit mode
    tableHelper.enterEditMode(editModeButtons, operationButtons);
  }

  private void saveMarca() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
      displayError("Selecciona una fila para editar");
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
      displayError(e.getMessage());
      return;
    }

    // Update the corresponding row in the table with the new data
    tableModel.setValueAt(nombre, selectedRow, 1);
    tableModel.setValueAt(descripcion, selectedRow, 2);
    tableModel.setValueAt(pais, selectedRow, 3);

    // Exit edit mode
    tableHelper.exitEditMode(operationButtons, editModeButtons);

    // Clear input fields
    clearInputFields();

    // Display success message
    displayMessage("Marca insertada correctamente");
  }

  private void deleteMarca() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
      displayError("Selecciona una fila para eliminar");
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
        displayError(e.getMessage());
        return;
      }
      // Remove the selected row from the table model
      tableModel.removeRow(selectedRow);

      // Clear input fields
      clearInputFields();

      // Display success message
      displayMessage("Marca eliminada correctamente");
    }
  }

  private void displayError(String message) {
    JOptionPane.showMessageDialog(MarcaWindow.this, message, "Error", JOptionPane.ERROR_MESSAGE);
  }

  private void displayMessage(String message) {
    JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
  }

  private void clearInputFields() {
    nombreField.setText("");
    descripcionField.setText("");
    paisField.setText("");
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
    StyleHelper.setFontColor(new JComponent[]{nombreLabel,descripcionLabel,paisLabel},fontColor);
  }

  public static MarcaWindow getInstance() {
    if(instance == null) {
      instance = new MarcaWindow();
    }
    return instance;
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      new MarcaWindow();
    });
  }
}
