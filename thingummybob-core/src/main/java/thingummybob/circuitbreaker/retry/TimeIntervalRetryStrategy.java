package thingummybob.circuitbreaker.retry;

import java.util.concurrent.TimeUnit;

public class TimeIntervalRetryStrategy implements RetryStrategy {

  public final TimeUnit timeUnit;
  public final Long millisInterval;
  
  private long lastAttempt = System.currentTimeMillis();
  
  public TimeIntervalRetryStrategy(Integer interval, TimeUnit timeUnit) {
    this.timeUnit = timeUnit;
    this.millisInterval = timeUnit.toMillis(interval);
  }
  
  @Override
  public void notifyCall() { }

  @Override
  public boolean isReadyForRetry() {
    return System.currentTimeMillis() - lastAttempt >= millisInterval;
  }

  @Override
  public void reset() {
    lastAttempt = System.currentTimeMillis();
  }

  
  
}
