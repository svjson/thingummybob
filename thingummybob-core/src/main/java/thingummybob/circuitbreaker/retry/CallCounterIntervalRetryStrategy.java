package thingummybob.circuitbreaker.retry;

import java.util.concurrent.atomic.AtomicInteger;

public class CallCounterIntervalRetryStrategy implements RetryStrategy {

  private Integer retryInterval;
  private AtomicInteger callCounter = new AtomicInteger(0);

  public CallCounterIntervalRetryStrategy(int interval) {
    this.retryInterval = interval;
  }

  @Override
  public void notifyCall() {
    callCounter.incrementAndGet();
  }

  @Override
  public boolean isReadyForRetry() {
    return callCounter.get() >= retryInterval;
  }

  @Override
  public void reset() {
    callCounter.set(0);
  } 
  
}
