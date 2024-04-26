package Gui;

import Entities.Producto;
import Factories.EntityFactory;
import Helpers.PdfGenerator;
import Helpers.SerialHelper;
import Helpers.StyleHelper;
import Helpers.TableHelper;
import Params.ProductoParams;

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

public class ProductoWindow extends JFrame {
  private static ProductoWindow instance;
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
  private JLabel precioLabel = new JLabel("Precio:");
  private JLabel tallaLabel = new JLabel("Talla:");
  private JLabel colorLabel = new JLabel("Color:");
  private JLabel cantidadLabel = new JLabel("Cantidad:");
  private JLabel urlImagenLabel = new JLabel("URL Imagen:");
  private JLabel idMarcaLabel = new JLabel("ID Marca:");

  private JTextField nombreField;
  private JTextField descripcionField;
  private JTextField precioField;
  private JTextField tallaField;
  private JTextField colorField;
  private JTextField cantidadField;
  private JTextField urlImagenField;
  private JTextField idMarcaField;

  private EntityFactory entityFactory = new EntityFactory();
  private Producto producto = entityFactory.createProductoEntity();
  private PdfGenerator pdfGenerator = new PdfGenerator();
  private TableHelper tableHelper;
  private JButton[] editModeButtons;
  private JButton[] operationButtons;

  public ProductoWindow() {
    setTitle("Producto Management");
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

    titleLabel = new JLabel("Productos");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
    titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
    titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    headerPanel.add(titleLabel, BorderLayout.NORTH);

    inputPanel = new JPanel(new GridLayout(4, 2,10,5));
    inputPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    inputPanel.add(nombreLabel);
    nombreField = new JTextField();
    inputPanel.add(nombreField);
    inputPanel.add(descripcionLabel);
    descripcionField = new JTextField();
    inputPanel.add(descripcionField);
    inputPanel.add(precioLabel);
    precioField = new JTextField();
    inputPanel.add(precioField);
    inputPanel.add(tallaLabel);
    tallaField = new JTextField();
    inputPanel.add(tallaField);
    inputPanel.add(colorLabel);
    colorField = new JTextField();
    inputPanel.add(colorField);
    inputPanel.add(cantidadLabel);
    cantidadField = new JTextField();
    inputPanel.add(cantidadField);
    inputPanel.add(urlImagenLabel);
    urlImagenField = new JTextField();
    inputPanel.add(urlImagenField);
    inputPanel.add(idMarcaLabel);
    idMarcaField = new JTextField();
    inputPanel.add(idMarcaField);
    headerPanel.add(inputPanel, BorderLayout.CENTER);

    mainPanel.add(headerPanel, BorderLayout.NORTH);

    panel.add(mainPanel, BorderLayout.CENTER);

    addButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        addProducto();
      }
    });

    editButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        editProducto();
      }
    });
    pdfButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ResultSet productos = producto.find();
        String[] headers = {"ID", "Nombre", "Descripcion", "Precio", "Talla", "Color", "Cantidad", "URL", "ID Marca"};
        String [] fields = { "id_producto", "nombre", "descripcion", "precio", "talla", "color", "cantidad", "url_imagen", "id_marca" };
        String filename = "productos";

        pdfGenerator.downloadPdf(productos,headers,fields, filename);
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

    saveButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        saveProducto();
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
        deleteProducto();
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
    columnNames.add("Precio");
    columnNames.add("Talla");
    columnNames.add("Color");
    columnNames.add("Cantidad");
    columnNames.add("URL Imagen");
    columnNames.add("ID Marca");

    ResultSet productos = producto.find();

    try {
      while (productos.next()) {
        Vector<Object> row = new Vector<>();
        row.add(productos.getObject("id_producto"));
        row.add(productos.getObject("nombre"));
        row.add(productos.getObject("descripcion"));
        row.add(productos.getObject("precio"));
        row.add(productos.getObject("talla"));
        row.add(productos.getObject("color"));
        row.add(productos.getObject("cantidad"));
        row.add(productos.getObject("url_imagen"));
        row.add(productos.getObject("id_marca"));
        data.add(row);
      }
      tableModel.setDataVector(data, columnNames);
      productos.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  private void addProducto() {
    // Retrieve input values
    String nombre = nombreField.getText();
    String descripcion = descripcionField.getText();
    String precio = precioField.getText();
    String talla = tallaField.getText();
    String color = colorField.getText();
    String cantidad = cantidadField.getText();
    String urlImagen = urlImagenField.getText();
    String idMarca = idMarcaField.getText();

    // Validate input fields
    if (nombre.isEmpty() || descripcion.isEmpty() || precio.isEmpty() || talla.isEmpty() || color.isEmpty() || cantidad.isEmpty() || urlImagen.isEmpty() || idMarca.isEmpty()) {
      displayError("Todos los campos son requeridos");
      return;
    }

    int id = new SerialHelper().getSerial("id_producto", "producto");

    if(id < 0) {
      displayError("No se pudo ejecutar esta operacion vuelve a intentarlo");
      return;
    }

    try {
      ProductoParams params = new ProductoParams(nombre, descripcion, precio, talla, color, cantidad, urlImagen, idMarca);
      producto.create(params);
    } catch (Exception e) {
      displayError(e.getMessage());
      return;
    }
    // Add new row to the table
    Vector<String> row = new Vector<>();
    row.add(id+""); // Empty ID, to be filled by database
    row.add(nombre);
    row.add(descripcion);
    row.add(precio);
    row.add(talla);
    row.add(color);
    row.add(cantidad);
    row.add(urlImagen);
    row.add(idMarca);
    tableModel.addRow(row);

    // Clear input fields
    clearInputFields();

    displayMessage("Producto insertado correctamente");
  }

  private void editProducto() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
      displayError("Selecciona una fila para editar");
      return;
    }

    // Retrieve data from the selected row
    String id = tableModel.getValueAt(selectedRow, 0).toString();
    String nombre = tableModel.getValueAt(selectedRow, 1).toString();
    String descripcion = tableModel.getValueAt(selectedRow, 2).toString();
    String precio = tableModel.getValueAt(selectedRow, 3).toString();
    String talla = tableModel.getValueAt(selectedRow, 4).toString();
    String color = tableModel.getValueAt(selectedRow, 5).toString();
    String cantidad = tableModel.getValueAt(selectedRow, 6).toString();
    String urlImagen = tableModel.getValueAt(selectedRow, 7).toString();
    String idMarca = tableModel.getValueAt(selectedRow, 8).toString();

    // Populate input fields with the data from the selected row
    nombreField.setText(nombre);
    descripcionField.setText(descripcion);
    precioField.setText(precio);
    tallaField.setText(talla);
    colorField.setText(color);
    cantidadField.setText(cantidad);
    urlImagenField.setText(urlImagen);
    idMarcaField.setText(idMarca);

    // Start edit mode
    tableHelper.enterEditMode(editModeButtons, operationButtons);
  }

  private void saveProducto() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
      displayError("Selecciona una fila para editar");
      return;
    }

    // Retrieve id to update expected row
    String id = tableModel.getValueAt(selectedRow, 0).toString();

    // Retrieve modified data from input fields
    String nombre = nombreField.getText();
    String descripcion = descripcionField.getText();
    String precio = precioField.getText();
    String talla = tallaField.getText();
    String color = colorField.getText();
    String cantidad = cantidadField.getText();
    String urlImagen = urlImagenField.getText();
    String idMarca = idMarcaField.getText();

    try {
      producto.update(id, new ProductoParams(nombre, descripcion, precio, talla, color, cantidad, urlImagen, idMarca));
    } catch (Exception e) {
      displayError(e.getMessage());
      return;
    }

    // Update the corresponding row in the table with the new data
    tableModel.setValueAt(nombre, selectedRow, 1);
    tableModel.setValueAt(descripcion, selectedRow, 2);
    tableModel.setValueAt(precio, selectedRow, 3);
    tableModel.setValueAt(talla, selectedRow, 4);
    tableModel.setValueAt(color, selectedRow, 5);
    tableModel.setValueAt(cantidad, selectedRow, 6);
    tableModel.setValueAt(urlImagen, selectedRow, 7);
    tableModel.setValueAt(idMarca, selectedRow, 8);

    // Exit edit mode
    tableHelper.exitEditMode(operationButtons, editModeButtons);

    // Clear input fields
    clearInputFields();

    // Display success message
    displayMessage("Producto actualizado correctamente");
  }

  private void deleteProducto() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
      displayError("Selecciona una fila para eliminar");
      return;
    }

    // Retrieve id to delete expected row
    String id = tableModel.getValueAt(selectedRow, 0).toString();

    // Display confirmation dialog before deleting
    int confirm = JOptionPane.showConfirmDialog(ProductoWindow.this, "Estas seguro que deseas eliminar este producto", "Confirmation", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
      try {
        producto.delete(id);
      } catch (Exception e) {
        displayError("Error al eliminar este producto. Intenta de nuevo.");
        return;
      }

      // Remove the selected row from the table model
      tableModel.removeRow(selectedRow);

      // Clear input fields
      clearInputFields();

      // Display success message
      displayMessage("Producto insertado correctamente");
    }
  }

  private void displayError(String message) {
    JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
  }

  private void displayMessage(String message) {
    JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
  }

  private void updateTable() {
    // Method to update the table after adding, editing, or deleting a producto
    initTable();
  }

  private void clearInputFields() {
    nombreField.setText("");
    descripcionField.setText("");
    precioField.setText("");
    tallaField.setText("");
    colorField.setText("");
    cantidadField.setText("");
    urlImagenField.setText("");
    idMarcaField.setText("");
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
    StyleHelper.setFontColor(new JComponent[]{nombreLabel,descripcionLabel,precioLabel,tallaLabel,colorLabel,cantidadLabel,urlImagenLabel,idMarcaLabel},fontColor);
  }

  public static ProductoWindow getInstance() {
    if(instance == null) {
      instance = new ProductoWindow();
    }
    return instance;
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      new ProductoWindow();
    });
  }
}
