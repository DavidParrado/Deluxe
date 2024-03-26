package Database;

import Entities.Categoria;
import Entities.Marca;
import Entities.Producto;
import Entities.Usuario;

import java.util.Scanner;

public class DatabaseOperation {
  Usuario usuario;
  Marca marca;
  Producto producto;
  Categoria categoria;

  public DatabaseOperation() {
    usuario = new Usuario();
    marca = new Marca();
    producto = new Producto();
    categoria = new Categoria();
  }
  public void find() {
    Scanner sc = new Scanner(System.in);
    int opcion = 0;
    while(opcion != 1 && opcion != 2 && opcion != 3 && opcion != 4) {
      System.out.println("\nIngresa un número para seleccionar la consulta:");
      System.out.println("1. Consultar usuarios");
      System.out.println("2. Consultar productos");
      System.out.println("3. Consultar marcas");
      System.out.println("4. Consultar categorías");
      System.out.print("\nTabla a consultar: ");
      opcion = sc.nextInt();
    }

    System.out.println("\nResultados: ");
    System.out.println();
    switch(opcion) {
      case 1:
        this.usuario.find();
        break;
      case 2:
        this.producto.find();
        break;
      case 3:
        this.marca.find();
        break;
      case 4:
        this.categoria.find();
        break;
      default:
        System.out.println("Opción no válida");
    }
  }

  public void create() {
    Scanner sc = new Scanner(System.in);
    boolean valido = false;
    int[] opcionesValidas = {1};
    while(!valido) {
      System.out.println("\nEstas son las tablas en las que puedes insertar datos");
      System.out.println("1. Usuarios");
      System.out.println();
      System.out.print("Tabla a seleccionar: ");
      valido = this.verificarOpciones(sc.nextInt(), opcionesValidas);
      sc.nextLine();
    }
    usuario.create();
  }

  public void update() {
    Scanner sc = new Scanner(System.in);
    boolean valido = false;
    int[] opcionesValidas = {1};
    while(!valido) {
      System.out.println("\nEstas son las tablas que puedes actualizar datos");
      System.out.println("1. Usuarios");
      System.out.print("\nTabla a actualizar: ");
      valido = this.verificarOpciones(sc.nextInt(),opcionesValidas);
    }
    usuario.update();
  }

  public void delete() {
    System.out.println("Hey this is delete menu");
    usuario.delete();
  }

  public boolean verificarOpciones(int opt, int[] optValidas) {
    boolean esValido = false;
    for (int opcion : optValidas) {
      if(opt == opcion) esValido = true;
    }
    return esValido;
  }
}
