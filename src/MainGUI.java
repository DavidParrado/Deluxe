import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGUI extends JFrame {
  private JButton consultarButton;
  private JButton insertarButton;
  private JButton actualizarButton;
  private JButton eliminarButton;

  public MainGUI() {
    setTitle("Database Operations");
    setSize(400, 300);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    // Create buttons
    consultarButton = new JButton("Consultar");
    insertarButton = new JButton("Insertar");
    actualizarButton = new JButton("Actualizar");
    eliminarButton = new JButton("Eliminar");

    // Add action listeners to buttons
    consultarButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Call the find() method or open a new window for the find operation
        JOptionPane.showMessageDialog(null, "Consultar button clicked");
      }
    });

    insertarButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Call the create() method or open a new window for the create operation
        JOptionPane.showMessageDialog(null, "Insertar button clicked");
      }
    });

    actualizarButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Call the update() method or open a new window for the update operation
        JOptionPane.showMessageDialog(null, "Actualizar button clicked");
      }
    });

    eliminarButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Call the delete() method or open a new window for the delete operation
        JOptionPane.showMessageDialog(null, "Eliminar button clicked");
      }
    });

    // Create a panel to hold the buttons
    JPanel buttonPanel = new JPanel(new GridLayout(4, 1));
    buttonPanel.add(consultarButton);
    buttonPanel.add(insertarButton);
    buttonPanel.add(actualizarButton);
    buttonPanel.add(eliminarButton);

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