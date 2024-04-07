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

    // Create main title label
    JLabel mainTitleLabel = new JLabel("Deluxe");
    mainTitleLabel.setFont(new Font("Arial", Font.BOLD, 36));
    mainTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

    // Create welcome label
    JLabel welcomeLabel = new JLabel("Bienvenido selecciona el modulo al que quieres acceder");
    welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
    welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
    welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

    // Create buttons
    UsuarioButton = new JButton("Usuario");
    CategoriaButton = new JButton("Categoria");
    ProductoButton = new JButton("Producto");
    MarcaButton = new JButton("Marca");

    // Adding fonts buttons
    UsuarioButton.setFont(new Font("Montserrat", Font.BOLD, 18));
    CategoriaButton.setFont(new Font("Montserrat", Font.BOLD, 18));
    ProductoButton.setFont(new Font("Montserrat", Font.BOLD, 18));
    MarcaButton.setFont(new Font("Montserrat", Font.BOLD, 18));

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
        CategoriaWindow categoriaWindow = new CategoriaWindow();
        categoriaWindow.setVisible(true);
      }
    });

    ProductoButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ProductoWindow productoWindow = new ProductoWindow();
        productoWindow.setVisible(true);
      }
    });

    MarcaButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        MarcaWindow marcaWindow = new MarcaWindow();
        marcaWindow.setVisible(true);
      }
    });

    // Create a panel to hold the main title label, welcome label, and buttons
    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50)); // Add padding

    JPanel titlePanel = new JPanel(new BorderLayout());
    titlePanel.add(mainTitleLabel, BorderLayout.NORTH);

    titlePanel.add(welcomeLabel, BorderLayout.CENTER);
    mainPanel.add(titlePanel, BorderLayout.NORTH);

    JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10)); // Add vertical spacing between buttons
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // Add top padding
    buttonPanel.add(UsuarioButton);
    buttonPanel.add(CategoriaButton);
    buttonPanel.add(ProductoButton);
    buttonPanel.add(MarcaButton);
    mainPanel.add(buttonPanel, BorderLayout.CENTER);

    // Add the panel to the frame
    getContentPane().add(mainPanel, BorderLayout.CENTER);
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
