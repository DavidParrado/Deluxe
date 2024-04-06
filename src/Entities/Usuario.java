package Entities;

import Database.DatabaseConnection;
import Helpers.SerialHelper;
import Params.UsuarioParams;

import java.sql.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Usuario implements Entity<UsuarioParams> {

  private final Connection connection;
  public Usuario() {
    connection = DatabaseConnection.getInstance().getConnection();
  }
  @Override
  public ResultSet find() {
    try {
      Statement statement = connection.createStatement();
      ResultSet usuarios = statement.executeQuery("SELECT * FROM usuario");
      return usuarios;
    } catch (SQLException e) {
      System.out.print(e.getMessage());
      return null;
    }
  }

  @Override
  public void create(UsuarioParams params) throws Exception {
    try {

      this.validarEmail(params.correo);
      this.validarContra(params.contrasena);

      new SerialHelper().getSerial("id_usuario", "usuario");

      String sql = "INSERT INTO usuario (correo,contrasena,nombre,apellido,direccion,telefono,rol) VALUES (?,?,?,?,?,?,?)";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setString(1,params.correo);
      statement.setString(2,params.contrasena);
      statement.setString(3,params.nombre);
      statement.setString(4,params.apellido);
      statement.setString(5,params.direccion);
      statement.setString(6,params.telefono);
      statement.setString(7,"Usuario");
      statement.executeUpdate();

    } catch(Exception e) {
      System.out.print(e.getMessage());
      throw new Exception(e);
    }
  }

  @Override
  public void update(String id, UsuarioParams params) throws Exception {
    try {
      this.validarEmail(params.correo);
      this.validarContra(params.contrasena);

      String sql = "UPDATE usuario SET correo = ?, contrasena = ?, nombre = ?, apellido = ?, direccion = ?, telefono = ? WHERE id_usuario = ?";
      PreparedStatement statement = connection.prepareStatement(sql);

      statement.setString(1,params.correo);
      statement.setString(2,params.contrasena);
      statement.setString(3,params.nombre);
      statement.setString(4,params.apellido);
      statement.setString(5,params.direccion);
      statement.setString(6,params.telefono);
      statement.setInt(7, Integer.parseInt(id));

      statement.executeUpdate();

    } catch(Exception e){
      System.out.println(e.getMessage());
      throw new Exception(e);
    }
  }

  @Override
  public void delete(String id) throws Exception {
    try {
      String sql = "DELETE FROM usuario WHERE id_usuario = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setInt(1, Integer.parseInt(id));

      statement.executeUpdate();

    } catch (SQLException e) {
      System.out.println("Error al eliminar el usuario: " + e.getMessage());
      throw new Exception(e);
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
