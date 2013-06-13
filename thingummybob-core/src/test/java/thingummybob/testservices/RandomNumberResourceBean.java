package thingummybob.testservices;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class RandomNumberResourceBean implements RandomNumberResource {

  private AtomicInteger callCounter = new AtomicInteger(0);
  
  private Random randomizer = new Random();
  private boolean inError;

  @Override
  public Integer getRandomNumber() {
    callCounter.incrementAndGet();
    if (inError) {
      throw new RuntimeException("Injected fault");
    }
    return randomizer.nextInt();
  }

  public void throwExceptionOnMethodCall() {
    this.inError = true;
  }
  
  public Integer getCallCounter() {
    return callCounter.get();
  }

}
