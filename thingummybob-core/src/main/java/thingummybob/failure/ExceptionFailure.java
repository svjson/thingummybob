package thingummybob.failure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExceptionFailure extends AbstractFailure {

  private List<Class<? extends Exception>> exceptionTypes = new ArrayList<Class<? extends Exception>>();

  public ExceptionFailure(Class<? extends Exception> exception) {
    this.exceptionTypes.add(exception);
  }
  
  public ExceptionFailure(Class<? extends Exception>... exceptions) {
    this.exceptionTypes.addAll(Arrays.asList(exceptions));
  }

  @Override
  public boolean isFailureType(Throwable t) {
    for (Class<? extends Exception> exceptionType : exceptionTypes) {
      if (t.getClass().equals(exceptionType)) return true;
    }
    return false;
  }
  
}
