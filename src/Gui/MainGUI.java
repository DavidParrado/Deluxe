package Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGUI extends JFrame {
  private JButton UsuarioButton;
  private JButton CategoriaButton;
  private JButton ProductoButton;
  private JButton MarcaButton;

  public MainGUI() {
    setTitle("Database Operations");
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    // Create buttons
    UsuarioButton = new JButton("Usuario");
    CategoriaButton = new JButton("Categoria");
    ProductoButton = new JButton("Producto");
    MarcaButton = new JButton("Marca");

    // Add action listeners to buttons
    UsuarioButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        UsuarioWindow usuarioWindow = new UsuarioWindow();
        usuarioWindow.setVisible(true);
      }
    });

    CategoriaButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(null, "Categoria button clicked");
      }
    });

    ProductoButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(null, "Producto button clicked");
      }
    });

    MarcaButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        MarcaWindow marcaWindow = new MarcaWindow();
        marcaWindow.setVisible(true);
      }
    });

    // Create a panel to hold the buttons
    JPanel buttonPanel = new JPanel(new GridLayout(4, 1));
    buttonPanel.add(UsuarioButton);
    buttonPanel.add(CategoriaButton);
    buttonPanel.add(ProductoButton);
    buttonPanel.add(MarcaButton);

    // Add the panel to the frame
    getContentPane().add(buttonPanel, BorderLayout.CENTER);
  }
  public static void main(String[] args) {
    // Create and show the GUI
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        MainGUI mainGUI = new MainGUI();
        mainGUI.setVisible(true);
      }
    });
  }
}