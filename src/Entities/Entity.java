package Entities;

import java.sql.ResultSet;

public interface Entity<T> {
  public ResultSet find();
  public void create(T params) throws Exception;
  public void update(String id, T params) throws Exception;
  public void delete(String id) throws Exception;
}
