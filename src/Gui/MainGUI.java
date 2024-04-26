package Gui;

import Helpers.StyleHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGUI extends JFrame {
  private JPanel mainPanel;
  private JPanel titlePanel;
  private JPanel buttonPanel;
  private JLabel mainTitleLabel;
  private JLabel welcomeLabel;
  private JButton UsuarioButton;
  private JButton CategoriaButton;
  private JButton ProductoButton;
  private JButton MarcaButton;
  public UsuarioWindow usuarioWindow;
  public CategoriaWindow categoriaWindow;
  public ProductoWindow productoWindow;
  public MarcaWindow marcaWindow;

  public MainGUI() {
    setTitle("Database Operations");
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    // Create main title label
    mainTitleLabel = new JLabel("Deluxe");
    mainTitleLabel.setFont(new Font("Arial", Font.BOLD, 36));
    mainTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

    // Create welcome label
    welcomeLabel = new JLabel("Bienvenido selecciona el modulo al que quieres acceder");
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
        usuarioWindow = UsuarioWindow.getInstance();
        usuarioWindow.setVisible(true);
      }
    });

    CategoriaButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        CategoriaWindow categoriaWindow = CategoriaWindow.getInstance();
        categoriaWindow.setVisible(true);
      }
    });

    ProductoButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ProductoWindow productoWindow = ProductoWindow.getInstance();
        productoWindow.setVisible(true);
      }
    });

    MarcaButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        MarcaWindow marcaWindow = MarcaWindow.getInstance();
        marcaWindow.setVisible(true);
      }
    });

    // Create a panel to hold the main title label, welcome label, and buttons
    mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50)); // Add padding

    titlePanel = new JPanel(new BorderLayout());
    titlePanel.add(mainTitleLabel, BorderLayout.NORTH);

    titlePanel.add(welcomeLabel, BorderLayout.CENTER);
    mainPanel.add(titlePanel, BorderLayout.NORTH);

    buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10)); // Add vertical spacing between buttons
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // Add top padding
    buttonPanel.add(UsuarioButton);
    buttonPanel.add(CategoriaButton);
    buttonPanel.add(ProductoButton);
    buttonPanel.add(MarcaButton);
    mainPanel.add(buttonPanel, BorderLayout.CENTER);

    // Color picker
    Theme theme = new Theme(this);
    mainPanel.add(theme, BorderLayout.SOUTH);

    // Add the panel to the frame
    getContentPane().add(mainPanel, BorderLayout.CENTER);

    applyFontColor(Theme.fontColor);
    applyButtonColor(Theme.buttonColor);
    applyBackgroundColor(Theme.backgroundColor);

  }

  public void applyBackgroundColor(Color backgroundColor) {
    StyleHelper.setBackgroundColor(new JPanel[]{mainPanel,titlePanel,buttonPanel},backgroundColor);
  }

  public void applyButtonColor(Color buttonColor) {
    StyleHelper.setBackgroundColor(new JButton[]{UsuarioButton,CategoriaButton,ProductoButton,MarcaButton},buttonColor);
  }

  public void applyFontColor(Color fontColor) {
    StyleHelper.setFontColor(new JLabel[]{mainTitleLabel,welcomeLabel},fontColor);
    StyleHelper.setFontColor(new JComponent[]{UsuarioButton,CategoriaButton,ProductoButton,MarcaButton},fontColor);
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
