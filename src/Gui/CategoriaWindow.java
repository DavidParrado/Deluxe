package Gui;

import Entities.Categoria;
import Factories.EntityFactory;
import Helpers.PdfGenerator;
import Helpers.SerialHelper;
import Helpers.StyleHelper;
import Helpers.TableHelper;
import Params.CategoriaParams;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class CategoriaWindow extends JFrame {
  private static CategoriaWindow instance;
  private JPanel panel;
  private JPanel mainPanel;
  private JPanel inputPanel;
  private JPanel headerPanel;
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

  // Input fields for new categoria
  private JTextField nombreField;
  private JTextField descripcionField;
  private EntityFactory entityFactory = new EntityFactory();
  private Categoria categoria = entityFactory.createCategoriaEntity();
  private PdfGenerator pdfGenerator = new PdfGenerator();
  private TableHelper tableHelper;
  private JButton[] editModeButtons;
  private JButton[] operationButtons;

  public CategoriaWindow() {
    setTitle("Categoria Management");
    setSize(800, 600);
    setLocationRelativeTo(null);

    panel = new JPanel();
    panel.setLayout(new BorderLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50));

    mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());

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

    headerPanel = new JPanel(new BorderLayout());

    titleLabel = new JLabel("Categorias");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
    titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
    titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    headerPanel.add(titleLabel, BorderLayout.NORTH);

    inputPanel = new JPanel(new GridLayout(2, 2, 10, 5));
    inputPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    inputPanel.add(nombreLabel);
    nombreField = new JTextField();
    inputPanel.add(nombreField);
    inputPanel.add(descripcionLabel);
    descripcionField = new JTextField();
    inputPanel.add(descripcionField);
    headerPanel.add(inputPanel, BorderLayout.CENTER);

    mainPanel.add(headerPanel, BorderLayout.NORTH);

    panel.add(mainPanel, BorderLayout.CENTER);

    addButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        addCategoria();
      }
    });

    editButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        editCategoria();
      }
    });

    pdfButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ResultSet categorias = categoria.find();
        String[] headers = {"ID", "Nombre", "Descripcion" };
        String [] fields = { "id_categoria", "nombre", "descripcion" };
        String filename = "categorias";

        pdfGenerator.downloadPdf(categorias,headers,fields, filename);
        displayMessage("Pdf generado correctamente.");
        String filePath = System.getProperty("user.dir") + File.separator + filename + ".pdf";
        File file = new File(filePath);
        if(file.exists()) {
          try {
            Desktop.getDesktop().open(file);
          } catch (IOException er) {
            System.out.println("Error opening file: " + er.getMessage());
          }
        }
      }
    });

    exitEditModeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        tableHelper.exitEditMode(operationButtons, editModeButtons);
        clearInputFields();
      }
    });

    saveButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        saveCategoria();
      }
    });

    deleteButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        deleteCategoria();
      }
    });

    // Apply theme
    applyFontColor(Theme.fontColor);
    applyButtonColor(Theme.buttonColor);
    applyBackgroundColor(Theme.backgroundColor);

    initTable();

    setContentPane(panel);
    setVisible(true);
  }

  private void initTable() {
    Vector<Vector<Object>> data = new Vector<>();
    Vector<String> columnNames = new Vector<>();
    columnNames.add("ID");
    columnNames.add("Nombre");
    columnNames.add("Descripción");

    ResultSet categorias = categoria.find();

    try {
      while (categorias.next()) {
        Vector<Object> row = new Vector<>();
        row.add(categorias.getObject("id_categoria"));
        row.add(categorias.getObject("nombre"));
        row.add(categorias.getObject("descripcion"));
        data.add(row);
      }
      tableModel.setDataVector(data, columnNames);
      categorias.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  private void addCategoria() {
    String nombre = nombreField.getText();
    String descripcion = descripcionField.getText();

    if (nombre.isEmpty() || descripcion.isEmpty()) {
      displayError("Todos los campos son requeridos");
      return;
    }

    int id = new SerialHelper().getSerial("id_categoria", "categoria");

    if(id < 0) {
      displayError("No se pudo ejecutar esta operacion vuelve a intentarlo");
      return;
    }

    try {
      categoria.create(new CategoriaParams(nombre, descripcion));
    } catch (Exception e) {
      displayError(e.getMessage());
      return;
    }

    Vector<String> row = new Vector<>();
    row.add(String.valueOf(id));
    row.add(nombre);
    row.add(descripcion);
    tableModel.addRow(row);

    clearInputFields();

    displayMessage("Categoria insertada correctamente");
  }
  private void editCategoria() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
      displayError("Selecciona una fila para editar");
      return;
    }

    String nombre = tableModel.getValueAt(selectedRow, 1).toString();
    String descripcion = tableModel.getValueAt(selectedRow, 2).toString();

    nombreField.setText(nombre);
    descripcionField.setText(descripcion);

    // Start edit mode
    tableHelper.enterEditMode(editModeButtons, operationButtons);
  }

  private void saveCategoria() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
      displayError("Selecciona una fila para editar");
      return;
    }

    String id = tableModel.getValueAt(selectedRow, 0).toString();
    String nombre = nombreField.getText();
    String descripcion = descripcionField.getText();

    if (nombre.isEmpty() || descripcion.isEmpty()) {
      displayError("All fields are required");
      return;
    }

    try {
      categoria.update(id, new CategoriaParams(nombre, descripcion));
    } catch (Exception e) {
      displayError(e.getMessage());
      return;
    }

    tableModel.setValueAt(nombre, selectedRow, 1);
    tableModel.setValueAt(descripcion, selectedRow, 2);

    // Exit edit mode
    tableHelper.exitEditMode(operationButtons, editModeButtons);

    // Clear input fields
    clearInputFields();

    JOptionPane.showMessageDialog(CategoriaWindow.this, "Categoria updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
  }

  private void deleteCategoria() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
      displayError("Selecciona una fila para eliminar");
      return;
    }

    String id = tableModel.getValueAt(selectedRow, 0).toString();

    int confirm = JOptionPane.showConfirmDialog(CategoriaWindow.this, "Are you sure you want to delete this categoria?", "Confirmation", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
      try {
        categoria.delete(id);
      } catch (Exception e) {
        displayError(e.getMessage());
        return;
      }

      tableModel.removeRow(selectedRow);
      clearInputFields();

      displayMessage("Categoria eliminada correctamente");
    }
  }

  private void clearInputFields() {
    nombreField.setText("");
    descripcionField.setText("");
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
    StyleHelper.setFontColor(new JComponent[]{nombreLabel,descripcionLabel},fontColor);
  }

  private void displayError(String message) {
    JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
  }

  private void displayMessage(String message) {
    JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
  }

  public static CategoriaWindow getInstance() {
    if(instance == null) {
      instance = new CategoriaWindow();
    }
    return instance;
  }


  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      new CategoriaWindow();
    });
  }
}
