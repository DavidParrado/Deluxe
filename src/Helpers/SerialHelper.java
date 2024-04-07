package Helpers;

import Database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SerialHelper {
  private final Connection connection;
  public SerialHelper() {
    this.connection = DatabaseConnection.getInstance().getConnection();
  }
  public int getSerial(String campoId, String tabla) {
    try {
      String maxQuery = "SELECT MAX(" + campoId + ") FROM " + tabla;
      PreparedStatement maxStatement = connection.prepareStatement(maxQuery);
      ResultSet resultSet = maxStatement.executeQuery();

      // There are 3 values: maxId => previousValue; maxId + 1 => currentValue; maxId + 2 => nextValue;
      int maxId = 0;
      if (resultSet.next()) {
        maxId = resultSet.getInt(1);
      }

      PreparedStatement restartStatement = connection.prepareStatement("SELECT setval('" + tabla + "_" + campoId + "_seq', ?, false)");
      restartStatement.setInt(1, maxId + 1);
      restartStatement.execute();
      return maxId + 1;
    } catch(SQLException e) {
      System.out.println(e.getMessage());
      return -1;
    }
  }

  public void setSerial() {

  }
}
