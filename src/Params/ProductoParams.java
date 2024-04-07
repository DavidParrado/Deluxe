package Params;

public class ProductoParams {
  public String nombre;
  public String descripcion;
  public String precio;
  public String talla;
  public String color;
  public String cantidad;
  public String url_imagen;
  public String id_marca;
  public ProductoParams(String nombre, String descripcion, String precio, String talla, String color, String cantidad, String url_imagen, String id_marca) {
    this.nombre = nombre;
    this.descripcion = descripcion;
    this.precio = precio;
    this.talla = talla;
    this.color = color;
    this.cantidad = cantidad;
    this.url_imagen = url_imagen;
    this.id_marca = id_marca;
  }
}
