package thingummybob.circuitbreaker;

import java.lang.reflect.Method;

public interface CircuitBreaker {

  void open();
  
  void close();
  
  boolean isOpen();

  boolean isClosed();

  Object handleMethodInvocation(Object proxiedInstance, Method method, Object[] arguments) throws Exception;
  
}