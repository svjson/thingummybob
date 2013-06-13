package thingummybob.circuitbreaker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import thingummybob.circuitbreaker.retry.CallCounterIntervalRetryStrategy;
import thingummybob.circuitbreaker.retry.RetryStrategy;
import thingummybob.failure.Failure;

public class BasicCircuitBreaker extends AbstractCircuitBreaker<BasicCircuitBreaker> {

  @Override
  public Object handleMethodInvocation(Object proxiedInstance, Method method, Object[] arguments) throws Exception {
    try {
      if (isOpen()) {
        retryStrategy.notifyCall();
        if (retryStrategy.isReadyForRetry()) {
          retryStrategy.reset();
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
      if (e.getTargetException() instanceof Exception) {
        throw (Exception) e;
      }
      throw e;
    }
  }
  
}
