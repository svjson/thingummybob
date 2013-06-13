package thingummybob.circuitbreaker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class CircuitBreakerIncovationHandler implements InvocationHandler {

  private CircuitBreaker circuitBreaker;
  private Object proxiedInstance;

  public CircuitBreakerIncovationHandler(CircuitBreaker circuitBreaker, Object proxiedInstance) {
    this.circuitBreaker = circuitBreaker;
    this.proxiedInstance = proxiedInstance;
  }

  @Override
  public Object invoke(Object proxyInstance, Method method, Object[] arguments) throws Throwable {
    return circuitBreaker.handleMethodInvocation(proxiedInstance, method, arguments);
  }

}
