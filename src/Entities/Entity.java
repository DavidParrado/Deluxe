package Entities;

import java.sql.Connection;

public interface Entity {
  public void find();
  public void create();
  public void update();
  public void delete();
}
