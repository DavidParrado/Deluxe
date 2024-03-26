package Factories;

import Entities.Categoria;
import Entities.Marca;
import Entities.Producto;
import Entities.Usuario;

public class EntityFactory implements EntityFactoryInterface {

  @Override
  public Marca createMarcaEntity() {
    return new Marca();
  }

  @Override
  public Usuario createUsuarioEntity() {
    return new Usuario();
  }

  @Override
  public Producto createProductoEntity() {
    return new Producto();
  }

  @Override
  public Categoria createCategoriaEntity() {
    return new Categoria();
  }
}
