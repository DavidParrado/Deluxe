package Factories;

import Entities.Categoria;
import Entities.Marca;
import Entities.Producto;
import Entities.Usuario;

public interface EntityFactoryInterface {
  Marca createMarcaEntity();
  Usuario createUsuarioEntity();
  Producto createProductoEntity();
  Categoria createCategoriaEntity();
}
