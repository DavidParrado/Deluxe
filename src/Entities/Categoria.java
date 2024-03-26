package Entities;

import Database.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Categoria implements Entity {
  private final Connection connection;

  public Categoria() {
    connection = DatabaseConnection.getInstance().getConnection();
  }
  @Override
  public void find() {
    try {
      Statement statement = connection.createStatement();
      ResultSet categorias = statement.executeQuery("SELECT * FROM categoria");

      while (categorias.next()) {

        String id_categoria= categorias.getString("id_categoria");
        String nombre = categorias.getString("nombre");
        String descripcion = categorias.getString("descripcion");

        System.out.println(id_categoria + " " + nombre + " " + descripcion);

      }
    } catch(SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void create() {

  }

  @Override
  public void update() {

  }

  @Override
  public void delete() {

  }
}
