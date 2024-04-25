package Helpers;

import javax.swing.*;
import java.awt.*;

public class StyleHelper {
  public void setElementColor(JButton[] buttons, Color selectedColor) {
    for (JButton button : buttons) {
      button.setBackground(selectedColor);
    }
  }

  public void setButtonTextColor(JButton[] buttons, Color selectedColor) {
    for (JButton button : buttons) {
      button.setForeground(selectedColor);
    }
  };
  public void setLabelTextColor(JLabel[] labels, Color selectedColor) {
    for (JLabel label : labels) {
      label.setForeground(selectedColor);
    }
  };

  public void setPanelColor(JPanel[] panels, Color selectedColor) {
    for (JPanel panel : panels) {
      panel.setBackground(selectedColor);
    }
  }


}
