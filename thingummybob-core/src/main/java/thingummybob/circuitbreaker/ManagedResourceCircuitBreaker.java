package thingummybob.circuitbreaker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import thingummybob.failure.Failure;
import thingummybob.resource.ManagedResource;

public class ManagedResourceCircuitBreaker extends AbstractCircuitBreaker<ManagedResourceCircuitBreaker> {

  private ManagedResource resource;
  
  public ManagedResourceCircuitBreaker(ManagedResource resource) {
    this.resource = resource;
  }
  
  @Override
  public void open() {
    super.open();
    resource.tearDown();
  }

  @Override
  public Object handleMethodInvocation(Object proxiedInstance, Method method, Object[] arguments) throws Exception {
    try {
      if (isOpen()) {
        retryStrategy.notifyCall();
        if (retryStrategy.isReadyForRetry()) {
          retryStrategy.reset();
          if (resource.isAvailable() && !resource.isInitializing()) {
            try {
              resource.initialize();
              if (resource.isDisconnected()) {
                throw new ResourceUnavailableException();
              }
            } catch (Exception e) {
              throw new ResourceUnavailableException(e);
            }
          }
          Object val = method.invoke(proxiedInstance, arguments);
          this.close();
          return val;
        }
        throw new ResourceUnavailableException();
      }
      return method.invoke(proxiedInstance, arguments);
    } catch (InvocationTargetException e) {
      for (Failure failure : failures) {
        if (failure.isFailureType(e.getTargetException())) {
          this.open();
          throw new ResourceUnavailableException(e.getTargetException());
        } 
      }
      if (e.getTargetException() instanceof ResourceUnavailableException) throw (ResourceUnavailableException) e.getTargetException();
      if (e.getTargetException() instanceof Exception) {
        throw (Exception) e;
      }
      throw e;
    } catch (Exception e) {
      throw e;
    }
  }

}
