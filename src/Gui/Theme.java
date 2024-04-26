package Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Theme extends JPanel {
  public static Color backgroundColor = new Color(238, 238, 238);
  public static Color fontColor = Color.BLACK;
  public static Color buttonColor = new Color(205, 222, 237);

  public Theme(MainGUI mainGUI) {
    setLayout(new FlowLayout(FlowLayout.LEFT));

    // Background theme
    JLabel backgroundColorIndicator = new JLabel();
    backgroundColorIndicator.setPreferredSize(new Dimension(50, 50));
    backgroundColorIndicator.setOpaque(true);
    backgroundColorIndicator.setBackground(backgroundColor);
    backgroundColorIndicator.setBorder(BorderFactory.createLineBorder(Color.black));
    add(backgroundColorIndicator);

    JButton backgroundColorButton = new JButton("Tema");
    backgroundColorButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Color selectedColor = JColorChooser.showDialog(mainGUI, "Selecciona el color de fondo", backgroundColor);
        if (selectedColor != null) {
          setBackground(selectedColor);
          backgroundColor = selectedColor;
          backgroundColorIndicator.setBackground(selectedColor);
          mainGUI.applyBackgroundColor(selectedColor);

          if( mainGUI.marcaWindow != null) mainGUI.marcaWindow.applyBackgroundColor(selectedColor);
          if( mainGUI.usuarioWindow != null) mainGUI.usuarioWindow.applyBackgroundColor(selectedColor);
          if( mainGUI.productoWindow != null) mainGUI.productoWindow.applyBackgroundColor(selectedColor);
          if( mainGUI.categoriaWindow != null) mainGUI.categoriaWindow.applyBackgroundColor(selectedColor);
        }
      }
    });
    add(backgroundColorButton);

    // Font theme
    JLabel fontColorIndicator = new JLabel();
    fontColorIndicator.setPreferredSize(new Dimension(50, 50));
    fontColorIndicator.setOpaque(true);
    fontColorIndicator.setBackground(fontColor);
    fontColorIndicator.setBorder(BorderFactory.createLineBorder(Color.black));
    add(fontColorIndicator);

    JButton fontColorButton = new JButton("Letra");
    fontColorButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Color selectedColor = JColorChooser.showDialog(mainGUI, "Selecciona el color de la letra", backgroundColor);
        if (selectedColor != null) {
          fontColor = selectedColor;
          fontColorIndicator.setBackground(selectedColor);
          mainGUI.applyFontColor(selectedColor);

          if( mainGUI.marcaWindow != null) mainGUI.marcaWindow.applyFontColor(selectedColor);
          if( mainGUI.usuarioWindow != null) mainGUI.usuarioWindow.applyFontColor(selectedColor);
          if( mainGUI.productoWindow != null) mainGUI.productoWindow.applyFontColor(selectedColor);
          if( mainGUI.categoriaWindow != null) mainGUI.categoriaWindow.applyFontColor(selectedColor);
        }
      }
    });
    add(fontColorButton);

    // Button theme
    JLabel buttonColorIndicator = new JLabel();
    buttonColorIndicator.setPreferredSize(new Dimension(50, 50));
    buttonColorIndicator.setOpaque(true);
    buttonColorIndicator.setBackground(buttonColor);
    buttonColorIndicator.setBorder(BorderFactory.createLineBorder(Color.black));
    add(buttonColorIndicator);

    JButton buttonColorButton = new JButton("Botones");
    buttonColorButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Color selectedColor = JColorChooser.showDialog(mainGUI, "Selecciona el color de la letra", backgroundColor);
        if (selectedColor != null) {
          buttonColor = selectedColor;
          buttonColorIndicator.setBackground(buttonColor);
          mainGUI.applyButtonColor(selectedColor);

          if( mainGUI.marcaWindow != null) mainGUI.marcaWindow.applyButtonColor(selectedColor);
          if( mainGUI.usuarioWindow != null) mainGUI.usuarioWindow.applyButtonColor(selectedColor);
          if( mainGUI.productoWindow != null) mainGUI.productoWindow.applyButtonColor(selectedColor);
          if( mainGUI.categoriaWindow != null) mainGUI.categoriaWindow.applyButtonColor(selectedColor);
        }
      }
    });
    add(buttonColorButton);
  }

}
