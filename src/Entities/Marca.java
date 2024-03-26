package Entities;

import Database.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Marca implements Entity {
  private final Connection connection;
  public Marca() {
    connection = DatabaseConnection.getInstance().getConnection();
  }
  @Override
  public void find() {
    try {
      Statement statement = this.connection.createStatement();
      ResultSet marcas = statement.executeQuery("SELECT * FROM marca");
      while (marcas.next()) {
        String id_marca= marcas.getString("id_marca");
        String nombre = marcas.getString("nombre");
        String descripcion = marcas.getString("descripcion");
        String pais = marcas.getString("pais");

        System.out.println(id_marca + " " + nombre + " " + descripcion + " " + pais);
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
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
