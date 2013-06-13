package thingummybob.resource.jdbc;

import java.sql.Connection;
import java.util.concurrent.atomic.AtomicBoolean;

import thingummybob.circuitbreaker.ResourceUnavailableException;
import thingummybob.resource.AbstractManagedResource;
import thingummybob.resource.ManagedResource;
import thingummybob.resource.ResourceStatus;

public class JdbcConnection extends AbstractManagedResource {

  private Connection connection;
  private ConnectionProvider provider;
  
  public JdbcConnection(ConnectionProvider provider) {
    this.provider = provider;
  }
    
  @Override
  public boolean isAvailable() {
    return true;
  }

  @Override
  public void initialize() {
    if (!isDisconnected()) return;
    try {
      this.status = ResourceStatus.INITIALIZING;
      this.connection = provider.getConnection();
      this.status = ResourceStatus.INITIALIZED;
    } catch (Exception e) {
      this.status = ResourceStatus.DISCONNECTED;
      return; 
    }
  }

  public void tearDown() {
    try {
      connection.close();
    } catch (Exception e) {
      
    } finally {
      this.connection = null;
      this.status = ResourceStatus.DISCONNECTED;
    }
  }

  public Connection getConnection() {
    if (null == connection) throw new ResourceUnavailableException();
    return connection;
  }

}
