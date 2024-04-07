package Gui;

import Entities.Categoria;
import Helpers.SerialHelper;
import Params.CategoriaParams;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class CategoriaWindow extends JFrame {
  private DefaultTableModel tableModel;
  private JTable table;
  private JButton addButton;
  private JButton editButton;
  private JButton deleteButton;
  private JButton saveButton;

  // Input fields for new categoria
  private JTextField nombreField;
  private JTextField descripcionField;
  private Categoria categoria;

  public CategoriaWindow() {
    setTitle("Categoria Management");
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
    saveButton = new JButton("Save");
    saveButton.setVisible(false);
    buttonPanel.add(addButton);
    buttonPanel.add(editButton);
    buttonPanel.add(deleteButton);
    buttonPanel.add(saveButton);
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);

    JPanel headerPanel = new JPanel(new BorderLayout());

    JLabel titleLabel = new JLabel("Categorias");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
    titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
    titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    headerPanel.add(titleLabel, BorderLayout.NORTH);

    JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 5));
    inputPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    inputPanel.add(new JLabel("Nombre:"));
    nombreField = new JTextField();
    inputPanel.add(nombreField);
    inputPanel.add(new JLabel("Descripción:"));
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

    categoria = new Categoria();

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
      displayError("All fields are required");
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

    saveButton.setVisible(false);

    JOptionPane.showMessageDialog(this, "Categoria added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
  }
  private void editCategoria() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
      displayError("Please select a row to edit");
      return;
    }

    String nombre = tableModel.getValueAt(selectedRow, 1).toString();
    String descripcion = tableModel.getValueAt(selectedRow, 2).toString();

    nombreField.setText(nombre);
    descripcionField.setText(descripcion);

    saveButton.setVisible(true);
  }

  private void saveCategoria() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
      displayError("Please select a row to save changes");
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

    clearInputFields();

    saveButton.setVisible(false);

    JOptionPane.showMessageDialog(CategoriaWindow.this, "Categoria updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
  }

  private void deleteCategoria() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
      displayError("Please select a row to delete");
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
      saveButton.setVisible(false);

      JOptionPane.showMessageDialog(CategoriaWindow.this, "Categoria deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
  }

  private void clearInputFields() {
    nombreField.setText("");
    descripcionField.setText("");
  }

  private void displayError(String message) {
    JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      new CategoriaWindow();
    });
  }
}
