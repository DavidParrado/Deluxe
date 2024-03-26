package Database;

import java.sql.Connection;
import java.sql.SQLException;

import org.postgresql.ds.PGSimpleDataSource;
public class DatabaseConnection {
  private static DatabaseConnection instance;
  private final Connection connection;

  private DatabaseConnection() {
    try {
      PGSimpleDataSource ds = new PGSimpleDataSource();
      ds.setUrl("jdbc:postgresql://modest-chum-13898.7tt.aws-us-east-1.cockroachlabs.cloud:26257/deluxe");
      ds.setUser("juan");
      ds.setPassword("Al3IMQ8NHtGnVoX_jR3ytw");
      connection = ds.getConnection();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static DatabaseConnection getInstance() {
    if(instance == null) {
      instance = new DatabaseConnection();
    }
    return instance;
  }
  public Connection getConnection() {
    return this.connection;
  }

  public void closeConnection() {
    if( connection != null ) {
      try {
        connection.close();
        System.out.println("Cerrando conexion");
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
  }

}
