package Params;

public class UsuarioParams {
  public String correo;
  public String contrasena;
  public String nombre;
  public String apellido;
  public String direccion;
  public String telefono;
  public String rol;
  public UsuarioParams(String correo,String contrasena, String nombre, String apellido, String direccion, String telefono, String rol) {
    this.correo = correo;
    this.contrasena = contrasena;
    this.nombre = nombre;
    this.apellido = apellido;
    this.direccion = direccion;
    this.telefono = telefono;
    this.rol = rol;
  }
}
