package Entities;

import Database.DatabaseConnection;
import Params.MarcaParams;

import java.sql.*;

public class Marca implements Entity<MarcaParams> {
  private final Connection connection;
  public Marca() {
    connection = DatabaseConnection.getInstance().getConnection();
  }
  @Override
  public ResultSet find() {
    try {
      Statement statement = this.connection.createStatement();
      ResultSet marcas = statement.executeQuery("SELECT * FROM marca order by id_marca");
      return marcas;
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  @Override
  public void create(MarcaParams params) throws Exception {
    try {
      String sql = "INSERT INTO marca (nombre,descripcion,pais) VALUES (?,?,?)";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setString(1,params.nombre);
      statement.setString(2,params.descripcion);
      statement.setString(3, params.pais);

      statement.executeUpdate();

    } catch (SQLException e) {
      System.out.println(e.getMessage());
      throw new Exception(e);
    }
  }

  @Override
  public void update(String id, MarcaParams params) throws Exception {
    try {
      String sql = "UPDATE marca SET nombre = ?, descripcion = ?, pais = ? WHERE id_marca = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setString(1, params.nombre);
      statement.setString(2, params.descripcion);
      statement.setString(3, params.pais);
      statement.setInt(4, Integer.parseInt(id));

      statement.executeUpdate();

    } catch(Exception e){
      System.out.println(e.getMessage());
      throw new Exception(e);
    }
  }

  @Override
  public void delete(String id) throws Exception {
    try {
      String sql = "DELETE FROM marca WHERE id_marca = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setInt(1, Integer.parseInt(id));

      statement.executeUpdate();

    } catch (SQLException e) {
      System.out.println(e.getMessage());
      throw new Exception(e);
    }
  }

}
