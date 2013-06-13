package thingummybob.circuitbreaker;

import java.lang.reflect.Proxy;


public class CircuitBreakerProxyBuilder {

  private Class<?> proxiedInterface;
  private Object proxiedInstance;
  private CircuitBreaker circuitBreaker;

  public CircuitBreakerProxyBuilder proxyOf(Class<?> proxyInterface) {
    this.proxiedInterface = proxyInterface;
    return this;
  }

  public CircuitBreakerProxyBuilder target(Object proxiedInstance) {
    this.proxiedInstance = proxiedInstance;
    return this;
  }

  @SuppressWarnings("unchecked")
  public <T> T build() {
    return (T) Proxy.newProxyInstance(
      getClass().getClassLoader(), 
      new Class[] { proxiedInterface },
      new CircuitBreakerIncovationHandler(
        circuitBreaker,
        proxiedInstance
      ) 
    );
  }

  public CircuitBreakerProxyBuilder withCircuitBreaker(CircuitBreaker circuitBreaker) {
    this.circuitBreaker = circuitBreaker;
    return this;
  }

}
