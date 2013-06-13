package thingummybob.circuitbreaker;

import static org.fest.assertions.Assertions.assertThat;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import thingummybob.circuitbreaker.retry.CallCounterIntervalRetryStrategy;
import thingummybob.failure.ExceptionFailure;
import thingummybob.testservices.RandomNumberResource;
import thingummybob.testservices.RandomNumberResourceBean;

public class BasicCircuitBreakerTest {

  /* CircuitBreaker */
  CircuitBreaker circuitBreaker;
  
  /* Proxied implementation instance */
  RandomNumberResourceBean resourceBean;
  
  /* Dynamic CircuitBreaker Proxy */
  RandomNumberResource resource;
  
  @BeforeMethod
  public void setUp() {
    circuitBreaker = new BasicCircuitBreaker()
      .openOn(new ExceptionFailure(RuntimeException.class))
      .retryStrategy(new CallCounterIntervalRetryStrategy(10));
    
    resourceBean =  new RandomNumberResourceBean();
    
    CircuitBreakerProxyBuilder factory = new CircuitBreakerProxyBuilder()
      .proxyOf(RandomNumberResource.class)
      .target(resourceBean)
      .withCircuitBreaker(circuitBreaker);
    resource = factory.build();
  }
  
  @Test
  public void CircuitBreaker_proxy_should_successfully_proxy_regular_method_invocations() {
    Integer i = resource.getRandomNumber();
    assertThat(i).isNotNull();
  }
  
  @Test
  public void after_failure_occurs_CircuitBreaker_should_be_in_open_state() {
    assertThat(resource.getRandomNumber()).isNotNull();
    assertThat(circuitBreaker.isClosed()).isTrue();
    resourceBean.throwExceptionOnMethodCall();
    try {
      resource.getRandomNumber();
    } catch (Exception e) {
      System.out.println(String.format("Caught '%s' as expected", e));
    }
    
    assertThat(circuitBreaker.isClosed()).isFalse();
    assertThat(resourceBean.getCallCounter()).isEqualTo(2);
  }
  
  @Test
  public void when_CircuitBreaker_is_open_no_method_invocations_should_be_performed_on_resource_implementation() {
    circuitBreaker.open();
    
    for (int i = 0; i < 3; i++) {
      try { 
        resource.getRandomNumber();
      } catch (ResourceUnavailableException e) {
        // Do nothing
      }
    }
    
    assertThat(resourceBean.getCallCounter()).isEqualTo(0);
  }
  
  @Test
  public void when_open_CircuitBreaker_retry_interval_is_reached_it_should_let_a_single_probe_call_go_through() {
    circuitBreaker.open();
    resourceBean.throwExceptionOnMethodCall();
    
    for (int i=0; i < 20; i++) {
      try {
        resource.getRandomNumber();
      } catch (ResourceUnavailableException e) {
        // Do nothing
      }
    }
    
    assertThat(resourceBean.getCallCounter()).isEqualTo(2);
    assertThat(circuitBreaker.isClosed()).isFalse();
  }
  
  @Test
  public void when_open_CircuitBreaker_retry_retry_probe_is_reached_it_should_let_a_single_probe_call_go_through() {
    circuitBreaker.open();
    
    for (int i=0; i < 20; i++) {
      try {
        resource.getRandomNumber();
      } catch (ResourceUnavailableException e) {
        // Do nothing
      }
    }
    
    assertThat(resourceBean.getCallCounter()).isEqualTo(11);
    assertThat(circuitBreaker.isClosed()).isTrue();
  }
  
}
