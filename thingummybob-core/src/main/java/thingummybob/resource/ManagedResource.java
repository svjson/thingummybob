package thingummybob.resource;

public interface ManagedResource {

  boolean isAvailable();
  
  boolean isInitialized();
  
  void tearDown();
  
  void initialize();

  boolean isInitializing();

  boolean isDisconnected();
  
}
