package thingummybob.circuitbreaker;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import thingummybob.circuitbreaker.retry.RetryStrategy;
import thingummybob.failure.Failure;

public abstract class AbstractCircuitBreaker<CircuitBreakerImpl> implements CircuitBreaker {

  private AtomicBoolean openState = new AtomicBoolean(false);
  
  protected RetryStrategy retryStrategy;
  protected List<Failure> failures = new ArrayList<Failure>();
  
  @Override
  public void open() {
    this.openState.set(true);
  }

  @Override
  public void close() {
    this.openState.set(false);
  }
  
  @Override
  public boolean isClosed() {
    return !openState.get();
  }
  
  @Override
  public boolean isOpen() {
    return openState.get();
  }

  @SuppressWarnings("unchecked")
  public CircuitBreakerImpl openOn(Failure failure) {
    this.failures.add(failure);
    return (CircuitBreakerImpl) this;
  }

  @SuppressWarnings("unchecked")
  public CircuitBreakerImpl retryStrategy(RetryStrategy retryStrategy) {
    this.retryStrategy = retryStrategy;
    return (CircuitBreakerImpl) this;
  }  

  

}
