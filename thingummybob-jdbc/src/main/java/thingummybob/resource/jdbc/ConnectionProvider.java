package thingummybob.resource.jdbc;

import java.sql.Connection;

public interface ConnectionProvider {

  Connection getConnection() throws Exception;

}
