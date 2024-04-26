package Helpers;

import javax.swing.*;
import java.awt.*;

public class StyleHelper {
  public static <T extends JComponent> void setBackgroundColor(T[] elements, Color selectedColor) {
    for (T element : elements) {
      element.setBackground(selectedColor);
    }
  }
  public static <T extends JComponent> void setFontColor(T[] elements, Color selectedColor) {
    for (T element : elements) {
      element.setForeground(selectedColor);
    }
  }

}
