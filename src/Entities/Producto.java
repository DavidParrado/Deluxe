package Entities;

import Database.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Producto implements Entity {

  private final Connection connection;

  public Producto() {
    connection = DatabaseConnection.getInstance().getConnection();
  }

  @Override
  public void find() {
    try {
      Statement statement = connection.createStatement();
      ResultSet productos = statement.executeQuery("SELECT * FROM producto");

      while (productos.next()) {

        String id_producto= productos.getString("id_producto");
        String nombre = productos.getString("nombre");
        String descripcion = productos.getString("descripcion");
        String precio = productos.getString("precio");
        String talla = productos.getString("talla");
        String color = productos.getString("color");
        String cantidad = productos.getString("cantidad");
        String url_imagen = productos.getString("url_imagen");
        String id_marca = productos.getString("id_marca");

        System.out.println(id_producto + " " + nombre + " " + descripcion + " " + precio + " " + talla + " " + color + " " + cantidad + " " + url_imagen + " " + id_marca + " ");

      }

    } catch(Exception e) {
      System.out.print(e);
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
