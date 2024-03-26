import Database.DatabaseConnection;
import Database.DatabaseOperation;

import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    try {
      // Singleton ✅, Adapter ✅, Factory method ❌, Abstract method ❌
      DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
      DatabaseOperation operation = new DatabaseOperation();

      Scanner sc = new Scanner(System.in);

      int opcion = 10;

      while(opcion != 0) {
        System.out.println("\nIngresa un número para seleccionar el tipo de operación que quieres realizar:");
        System.out.println("0. Salir");
        System.out.println("1. Consultar");
        System.out.println("2. Insertar");
        System.out.println("3. Actualizar");
        System.out.println("4. Eliminar");
        System.out.print("\nOperacion a realizar: ");
        opcion = sc.nextInt();

        switch (opcion) {
          case 0:
            System.out.println("Saliendo del programa...");
            break;
          case 1:
            operation.find();
            break;
          case 2:
            operation.create();
            break;
          case 3:
            operation.update();
            break;
          case 4:
            operation.delete();
            break;
          default:
            System.out.println("Opción no válida. Por favor, selecciona una opción del menú.");
            break;
        }
      }

      databaseConnection.closeConnection();

    } catch (Exception e) {
      System.out.print(e.getMessage());
    }
  }
}
