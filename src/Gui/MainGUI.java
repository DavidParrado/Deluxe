package Gui;

import Helpers.StyleHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGUI extends JFrame {
  private JButton UsuarioButton;
  private JButton CategoriaButton;
  private JButton ProductoButton;
  private JButton MarcaButton;
  private JButton colorButton;
  private JLabel colorIndicator;
  private JButton textColorButton;
  private JLabel textColorIndicator;
  private JButton buttonColorButton;
  private JLabel buttonColorIndicator;
  private UsuarioWindow usuarioWindow;
  private StyleHelper styleHelper = new StyleHelper();

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
        usuarioWindow = new UsuarioWindow();
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

    // Create color picker
    JPanel colorPickerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    colorIndicator = new JLabel();
    colorIndicator.setPreferredSize(new Dimension(50, 50)); // Set preferred size for color indicator
    colorIndicator.setOpaque(true); // Set to true to allow background color
    colorIndicator.setBackground(new Color(238,238,238)); // Default color
    colorIndicator.setBorder(BorderFactory.createLineBorder(Color.black));
    // Create color button
    colorButton = new JButton("Tema");
    colorButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Color selectedColor = JColorChooser.showDialog(MainGUI.this, "Select Color", Color.WHITE);
        if (selectedColor != null) {
          getContentPane().setBackground(selectedColor);
          colorIndicator.setBackground(selectedColor);
          buttonPanel.setBackground(selectedColor);
          titlePanel.setBackground(selectedColor);
          mainPanel.setBackground(selectedColor);
          colorPickerPanel.setBackground(selectedColor);
            usuarioWindow.applyThemeColor(selectedColor);
        }
      }
    });
    colorPickerPanel.add(colorIndicator);
    colorPickerPanel.add(colorButton);

    textColorIndicator = new JLabel();
    textColorIndicator.setPreferredSize(new Dimension(50, 50)); // Set preferred size for color indicator
    textColorIndicator.setOpaque(true); // Set to true to allow background color
    textColorIndicator.setBackground(Color.BLACK); // Default color
    textColorIndicator.setBorder(BorderFactory.createLineBorder(Color.black));
    // Create color button
    textColorButton = new JButton("Letra");
    textColorButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Color selectedColor = JColorChooser.showDialog(MainGUI.this, "Select Color", Color.BLACK);
        if (selectedColor != null) {
          getContentPane().setBackground(selectedColor);
          textColorIndicator.setBackground(selectedColor);
          mainTitleLabel.setForeground(selectedColor);
          welcomeLabel.setForeground(selectedColor);
          UsuarioButton.setForeground(selectedColor);
          CategoriaButton.setForeground(selectedColor);
          MarcaButton.setForeground(selectedColor);
          ProductoButton.setForeground(selectedColor);
          usuarioWindow.applyFontColor(selectedColor);
//          colorPickerPanel.setBackground(selectedColor);
        }
      }
    });
    colorPickerPanel.add(textColorIndicator);
    colorPickerPanel.add(textColorButton);

    buttonColorIndicator = new JLabel();
    buttonColorIndicator.setPreferredSize(new Dimension(50, 50)); // Set preferred size for color indicator
    buttonColorIndicator.setOpaque(true); // Set to true to allow background color
    buttonColorIndicator.setBackground(new Color(205, 222, 237)); // Default color
    buttonColorIndicator.setBorder(BorderFactory.createLineBorder(Color.black));
    // Create color button
    buttonColorButton = new JButton("Botones");
    buttonColorButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Color selectedColor = JColorChooser.showDialog(MainGUI.this, "Select Color", Color.BLACK);
        if (selectedColor != null) {
          getContentPane().setBackground(selectedColor);
          buttonColorIndicator.setBackground(selectedColor);
          UsuarioButton.setBackground(selectedColor);
          CategoriaButton.setBackground(selectedColor);
          MarcaButton.setBackground(selectedColor);
          ProductoButton.setBackground(selectedColor);
          usuarioWindow.applyButtonColor(selectedColor);
//          colorPickerPanel.setBackground(selectedColor);
        }
      }
    });
    colorPickerPanel.add(buttonColorIndicator);
    colorPickerPanel.add(buttonColorButton);

    // Add color button to the panel
//    JPanel colorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
//    colorPanel.add(colorButton);
//    mainPanel.add(colorPanel, BorderLayout.SOUTH);
    mainPanel.add(colorPickerPanel, BorderLayout.SOUTH);

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
