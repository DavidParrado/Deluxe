import Gui.MainGUI;

public class Main {
  public static void main(String[] args) {
    try {
      // Singleton ✅, Adapter ✅, Factory method ✅
      MainGUI gui = new MainGUI();
      gui.setVisible(true);
    } catch (Exception e) {
      System.out.print(e.getMessage());
    }
  }
}
