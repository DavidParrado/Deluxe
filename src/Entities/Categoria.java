package Entities;

import Database.DatabaseConnection;
import Params.CategoriaParams;

import java.sql.*;

public class Categoria implements Entity<CategoriaParams> {
  private final Connection connection;

  public Categoria() {
    connection = DatabaseConnection.getInstance().getConnection();
  }
  @Override
  public ResultSet find() {
    try {
      Statement statement = connection.createStatement();
      ResultSet categorias = statement.executeQuery("SELECT * FROM categoria");
      return categorias;
    } catch(SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public void create(CategoriaParams params) throws Exception {
    try {
      String sql = "INSERT INTO categoria (nombre,descripcion) VALUES (?,?)";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setString(1,params.nombre);
      statement.setString(2,params.descripcion);
      statement.executeUpdate();

    } catch (SQLException e) {
      System.out.println(e.getMessage());
      throw new Exception(e);
    }
  }

  @Override
  public void update(String id, CategoriaParams params) throws Exception {
    try {
      String sql = "UPDATE categoria SET nombre = ?, descripcion = ? WHERE id_categoria = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setString(1, params.nombre);
      statement.setString(2, params.descripcion);
      statement.setInt(3, Integer.parseInt(id));

      statement.executeUpdate();

    } catch(Exception e){
      System.out.println(e.getMessage());
      throw new Exception(e);
    }
  }

  @Override
  public void delete(String id) throws Exception {
    try {
      String sql = "DELETE FROM categoria WHERE id_categoria = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setInt(1, Integer.parseInt(id));

      statement.executeUpdate();

    } catch (SQLException e) {
      System.out.println(e.getMessage());
      throw new Exception(e);
    }
  }
}
