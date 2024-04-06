import javax.swing.*;

public class MainWindow extends JFrame {

  public MainWindow() {
    setTitle("CRUD Application");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(800, 600); // Set the initial size of the window
    setLocationRelativeTo(null); // Center the window on the screen

    // Create a menu bar
    JMenuBar menuBar = new JMenuBar();

    // Create menu items for each entity
    String[] entities = {"Marca", "Categoria", "Usuario", "Producto", "Pedido", "Item Pedido", "Carrito",
    "Producto Carrito", "Pago", "Resena", "Producto Categoria", "Envio"};
    for (String entity : entities) {
      JMenuItem menuItem = new JMenuItem(entity);
      menuItem.addActionListener(e -> openEntityWindow(entity));
      menuBar.add(menuItem);
    }

    setJMenuBar(menuBar); // Set the menu bar to the frame
    setVisible(true); // Make the window visible
  }

  private void openEntityWindow(String entity) {
    // Open a new window or panel for CRUD operations related to the selected entity
    // Implement this method based on your specific requirements
    System.out.println("Opening window for entity: " + entity);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      new MainWindow();
    });
  }
}