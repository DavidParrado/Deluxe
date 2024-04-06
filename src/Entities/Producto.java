package Entities;

import Database.DatabaseConnection;
import Params.ProductoParams;

import java.sql.*;

public class Producto implements Entity<ProductoParams> {

  private final Connection connection;

  public Producto() {
    connection = DatabaseConnection.getInstance().getConnection();
  }

  @Override
  public ResultSet find() {
    try {
      Statement statement = connection.createStatement();
      ResultSet productos = statement.executeQuery("SELECT * FROM producto");
      return productos;
    } catch(Exception e) {
      System.out.print(e);
      return null;
    }
  }

  @Override
  public void create(ProductoParams params) throws Exception {
    try {
      String sql = "INSERT INTO producto (nombre, descripcion, precio, talla, color, cantidad, url_imagen, id_marca) VALUES (?,?,?,?,?,?,?,?)";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setString(1,params.nombre);
      statement.setString(2,params.descripcion);
      statement.setString(3,params.precio);
      statement.setString(4,params.talla);
      statement.setString(5,params.color);
      statement.setString(6,params.cantidad);
      statement.setString(7,params.url_imagen);
      statement.setInt(8,Integer.parseInt(params.id_marca));
      statement.executeUpdate();

    } catch (SQLException e) {
      System.out.println(e.getMessage());
      throw new Exception(e);
    }
  }

  @Override
  public void update(String id, ProductoParams params) throws Exception {
    try {
      String sql = "UPDATE producto SET nombre = ?, descripcion = ?, precio = ?, talla = ?, color = ?, cantidad = ?, url_imagen = ?, id_marca = ? WHERE id_producto = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setString(1,params.nombre);
      statement.setString(2,params.descripcion);
      statement.setString(3,params.precio);
      statement.setString(4,params.talla);
      statement.setString(5,params.color);
      statement.setString(6,params.cantidad);
      statement.setString(7,params.url_imagen);
      statement.setInt(8,Integer.parseInt(params.id_marca));
      statement.setInt(9,Integer.parseInt(id));
      statement.executeUpdate();

    } catch (SQLException e) {
      System.out.println(e.getMessage());
      throw new Exception(e);
    }
  }

  @Override
  public void delete(String id) throws Exception {
    try {
      String sql = "DELETE FROM producto WHERE id_producto = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setInt(1, Integer.parseInt(id));

      statement.executeUpdate();

    } catch (SQLException e) {
      System.out.println(e.getMessage());
      throw new Exception(e);
    }
  }
}
