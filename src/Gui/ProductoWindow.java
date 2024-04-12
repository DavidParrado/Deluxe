package Gui;

import Entities.Producto;
import Factories.EntityFactory;
import Helpers.PdfGenerator;
import Helpers.SerialHelper;
import Params.ProductoParams;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class ProductoWindow extends JFrame {
  private DefaultTableModel tableModel;
  private JTable table;
  private JButton addButton;
  private JButton editButton;
  private JButton deleteButton;
  private JButton pdfButton;
  private JButton saveButton;

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

  public ProductoWindow() {
    setTitle("Producto Management");
    setSize(800, 600);
    setLocationRelativeTo(null);

    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50));

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());

    tableModel = new DefaultTableModel();
    table = new JTable(tableModel);
    JScrollPane scrollPane = new JScrollPane(table);
    mainPanel.add(scrollPane, BorderLayout.CENTER);

    JPanel buttonPanel = new JPanel();
    addButton = new JButton("Add");
    editButton = new JButton("Edit");
    deleteButton = new JButton("Delete");
    pdfButton = new JButton("Descargar PDF");
    saveButton = new JButton("Save");
    saveButton.setVisible(false);
    buttonPanel.add(addButton);
    buttonPanel.add(editButton);
    buttonPanel.add(deleteButton);
    buttonPanel.add(pdfButton);
    buttonPanel.add(saveButton);
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);

    JPanel headerPanel = new JPanel(new BorderLayout());

    JLabel titleLabel = new JLabel("Productos");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
    titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
    titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    headerPanel.add(titleLabel, BorderLayout.NORTH);

    JPanel inputPanel = new JPanel(new GridLayout(4, 2,10,5));
    inputPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    inputPanel.add(new JLabel("Nombre:"));
    nombreField = new JTextField();
    inputPanel.add(nombreField);
    inputPanel.add(new JLabel("Descripción:"));
    descripcionField = new JTextField();
    inputPanel.add(descripcionField);
    inputPanel.add(new JLabel("Precio:"));
    precioField = new JTextField();
    inputPanel.add(precioField);
    inputPanel.add(new JLabel("Talla:"));
    tallaField = new JTextField();
    inputPanel.add(tallaField);
    inputPanel.add(new JLabel("Color:"));
    colorField = new JTextField();
    inputPanel.add(colorField);
    inputPanel.add(new JLabel("Cantidad:"));
    cantidadField = new JTextField();
    inputPanel.add(cantidadField);
    inputPanel.add(new JLabel("URL Imagen:"));
    urlImagenField = new JTextField();
    inputPanel.add(urlImagenField);
    inputPanel.add(new JLabel("ID Marca:"));
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
      }
    });

    saveButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        saveProducto();
      }
    });

    deleteButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        deleteProducto();
      }
    });

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
      displayError("All fields are required");
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

    saveButton.setVisible(false);
    JOptionPane.showMessageDialog(this, "Producto added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
  }

  private void editProducto() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(ProductoWindow.this, "Please select a row to edit", "Error", JOptionPane.ERROR_MESSAGE);
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

    // Show the save button
    saveButton.setVisible(true);
  }


  private void saveProducto() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
      displayError("Please select a row to edit");
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

    // Clear input fields
    clearInputFields();

    // Hide the save button
    saveButton.setVisible(false);

    // Display success message
    JOptionPane.showMessageDialog(ProductoWindow.this, "Product updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
  }

  private void deleteProducto() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
      displayError("Please select a row to delete");
      return;
    }

    // Retrieve id to delete expected row
    String id = tableModel.getValueAt(selectedRow, 0).toString();

    // Display confirmation dialog before deleting
    int confirm = JOptionPane.showConfirmDialog(ProductoWindow.this, "Are you sure you want to delete this product?", "Confirmation", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
      try {
        producto.delete(id);
      } catch (Exception e) {
        displayError("Failed to delete the product. Please try again.");
        return;
      }

      // Remove the selected row from the table model
      tableModel.removeRow(selectedRow);

      // Clear input fields
      clearInputFields();
      saveButton.setVisible(false);

      // Display success message
      JOptionPane.showMessageDialog(ProductoWindow.this, "Product deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
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

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      new ProductoWindow();
    });
  }
}
