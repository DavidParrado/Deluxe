package Entities;

import Database.DatabaseConnection;

import java.sql.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Usuario implements Entity {

  private final Connection connection;
  public Usuario() {
    connection = DatabaseConnection.getInstance().getConnection();
  }
  @Override
  public void find() {
    try {
      Statement statement = connection.createStatement();
      ResultSet usuarios = statement.executeQuery("SELECT * FROM usuario");

      while (usuarios.next()) {
        String id_usuario= usuarios.getString("id_usuario");
        String correo = usuarios.getString("correo");
        String contrasena = usuarios.getString("contrasena");
        String nombre = usuarios.getString("nombre");
        String apellido = usuarios.getString("apellido");
        String direccion = usuarios.getString("direccion");
        String telefono = usuarios.getString("telefono");
        String rol = usuarios.getString("rol");

        System.out.println(id_usuario + " " + correo + " " + contrasena + " " + nombre + " " + apellido + " " + direccion + " " + telefono + " " + rol);
      }
    } catch (SQLException e) {
      System.out.print(e.getMessage());
    }
  }

  @Override
  public void create() {
    try {
      Scanner sc = new Scanner(System.in);
      System.out.println("\nIngresa tu nombre");
      String nombre = sc.nextLine();
      System.out.println("Ingresa tu apellido");
      String apellido = sc.nextLine();
      System.out.println("Ingresa tu direccion");
      String direccion = sc.nextLine();
      System.out.println("Ingresa tu telefono");
      String telefono = sc.nextLine();
      System.out.println("Ingresa tu correo");
      String correo = sc.next();
      this.validarEmail(correo);
      System.out.println("Ingresa tu contrasena");
      String contrasena = sc.next();
      this.validarContra(contrasena);

      this.reiniciarSerial("id_usuario", "usuario");
      String sql = "INSERT INTO usuario (correo,contrasena,nombre,apellido,direccion,telefono,rol) VALUES (?,?,?,?,?,?,?)";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setString(1,correo);
      statement.setString(2,contrasena);
      statement.setString(3,nombre);
      statement.setString(4,apellido);
      statement.setString(5,direccion);
      statement.setString(6,telefono);
      statement.setString(7,"Usuario");
      int filasInsertadas = statement.executeUpdate();
      if (filasInsertadas > 0) {
        System.out.println("¡Datos insertados correctamente!");
      } else {
        System.out.println("No se ha insertado ningun dato.");
      }
    } catch(Exception e) {
      System.out.print(e.getMessage());
    }
  }

  @Override
  public void update() {
    Scanner sc = new Scanner(System.in);
    System.out.println("\nEstos son los usuarios que puedes actualizar: ");
    System.out.println();
    this.find();
    System.out.print("\nID del usuario a actualizar: ");
    int idUsuario = sc.nextInt();
    sc.nextLine();

    System.out.println("Ingrese el nuevo nombre:");
    String nuevoNombre = sc.nextLine();
    System.out.println("Ingrese el nuevo apellido:");
    String nuevoApellido = sc.nextLine();
    System.out.println("Ingrese la nueva dirección:");
    String nuevaDireccion = sc.nextLine();
    System.out.println("Ingrese el nuevo teléfono:");
    String nuevoTelefono = sc.nextLine();

    try {
      System.out.println("Ingrese el nuevo correo:");
      String nuevoCorreo = sc.nextLine();
      this.validarEmail(nuevoCorreo);

      System.out.println("Ingrese la nueva contraseña:");
      String nuevaContrasena = sc.nextLine();
      this.validarContra(nuevaContrasena);

      String sql = "UPDATE usuario SET correo = ?, contrasena = ?, nombre = ?, apellido = ?, direccion = ?, telefono = ? WHERE id_usuario = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setString(1, nuevoCorreo);
      statement.setString(2, nuevaContrasena);
      statement.setString(3, nuevoNombre);
      statement.setString(4, nuevoApellido);
      statement.setString(5, nuevaDireccion);
      statement.setString(6, nuevoTelefono);
      statement.setInt(7, idUsuario);

      int filasActualizadas = statement.executeUpdate();
      if (filasActualizadas > 0) {
        System.out.println("¡Datos actualizados correctamente!");
      } else {
        System.out.println("No se pudieron actualizar los datos. El ID de usuario proporcionado no existe.");
      }
    } catch(Exception e){
      System.out.println(e.getMessage());
    }
  }

  @Override
  public void delete() {
    try {
      Scanner sc = new Scanner(System.in);
      System.out.println("\nIngrese el ID del usuario que desea eliminar:");
      System.out.println("Aqui esta la lista\n");
      this.find();
      System.out.print("\nID del usuario a eliminar: ");
      int idUsuario = sc.nextInt();

      String sql = "DELETE FROM usuario WHERE id_usuario = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setInt(1, idUsuario);

      int filasEliminadas = statement.executeUpdate();
      if (filasEliminadas > 0) {
        System.out.println("¡Usuario eliminado correctamente!");
      } else {
        System.out.println("No se pudo eliminar el usuario. El ID de usuario proporcionado no existe.");
      }
    } catch (SQLException e) {
      System.out.println("Error al eliminar el usuario: " + e.getMessage());
    }
  }

  public boolean verificarOpciones(int opt, int[] optValidas) {
    boolean esValido = false;
    for (int opcion : optValidas) {
      if(opt == opcion) esValido = true;
    }
    return esValido;
  }

  public void reiniciarSerial(String campoId, String tabla) {
    try {
      String maxQuery = "SELECT MAX(" + campoId + ") FROM " + tabla;
      PreparedStatement maxStatement = connection.prepareStatement(maxQuery);
      ResultSet resultSet = maxStatement.executeQuery();

      int maxId = 0;
      if (resultSet.next()) {
        maxId = resultSet.getInt(1);
      }

      PreparedStatement restartStatement = connection.prepareStatement("SELECT setval('" + tabla + "_" + campoId + "_seq', ?, false)");
      restartStatement.setInt(1, maxId + 1);
      restartStatement.execute();

    } catch(SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public void validarEmail(String emailStr) throws Exception {
    Pattern emailRegex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    Matcher matcher = emailRegex.matcher(emailStr);
    if(!matcher.matches()) throw new Exception("Email no válido");
  }

  public void validarContra(String contra) throws Exception {
    if(contra.length() < 8) throw new Exception("Contraseña debe tener al menos 8 cáracteres");
  }

}
