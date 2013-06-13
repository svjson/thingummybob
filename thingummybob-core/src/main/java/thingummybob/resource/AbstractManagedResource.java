package thingummybob.resource;

public abstract class AbstractManagedResource implements ManagedResource {
  
  protected ResourceStatus status = ResourceStatus.DISCONNECTED;

  @Override
  public boolean isInitialized() {
    return status == ResourceStatus.INITIALIZED; 
  }

  @Override
  public boolean isInitializing() {
    return status == ResourceStatus.INITIALIZING;
  }
  
  @Override
  public boolean isDisconnected() {
    return status == ResourceStatus.DISCONNECTED;
  }
  
}
