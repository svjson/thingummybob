package thingummybob.circuitbreaker;

public class ResourceUnavailableException extends RuntimeException {

  private static final long serialVersionUID = -5532283241242323536L;

  public ResourceUnavailableException() {
  }

  public ResourceUnavailableException(Throwable cause) {
    super(cause);
  }
 
}
