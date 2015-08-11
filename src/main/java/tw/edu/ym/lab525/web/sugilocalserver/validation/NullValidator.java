package tw.edu.ym.lab525.web.sugilocalserver.validation;

public final class NullValidator {

  private NullValidator() {}

  /**
   * 
   * Checks and validates the object reference is not null
   * 
   * @author Ming-Jheng Li
   * 
   * @param reference
   *          an object reference
   * @param errorMessage
   *          the exception message to use if the check fails;
   * @return non-null reference that was validated
   */
  public static <T> T checkNotNull(T reference, String errorMessage) {
    if (reference == null) {
      throw new NullPointerException(String.valueOf(errorMessage));
    }
    return reference;
  }

}
