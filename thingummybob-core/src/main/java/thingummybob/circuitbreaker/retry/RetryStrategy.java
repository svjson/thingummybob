package thingummybob.circuitbreaker.retry;

public interface RetryStrategy {

  void notifyCall();

  boolean isReadyForRetry();

  void reset();

}
