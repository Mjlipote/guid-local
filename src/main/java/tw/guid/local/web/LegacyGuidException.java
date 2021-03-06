/* Copyright (c) 2015 ReiMed Co. to present.
* All rights reserved.
*
* @author Wei-Ming Wu
*
*/
package tw.guid.local.web;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;

/**
 * 
 * {@link LegacyGuidException} is used to represent any exception happened in
 * the GUID web service.
 *
 */
public final class LegacyGuidException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final HttpStatus httpStatus;
  private final List<String> errorMessages;

  /**
   * Creates a {@link LegacyGuidException}.
   *
   * @param httpStatus
   *          of this exception
   * @param errorMessages
   *          of exception details
   */
  public LegacyGuidException(HttpStatus httpStatus,
      List<String> errorMessages) {
    this.httpStatus = checkNotNull(httpStatus);
    this.errorMessages = ImmutableList.copyOf(errorMessages);
  }

  public LegacyGuidException(HttpStatus httpStatus, String... errorMessages) {
    this.httpStatus = checkNotNull(httpStatus);
    this.errorMessages = ImmutableList.copyOf(errorMessages);
  }

  /**
   * Creates a {@link LegacyGuidException} without error messages.
   *
   * @param httpStatus
   *          of this exception
   */
  public LegacyGuidException(HttpStatus httpStatus) {
    this.httpStatus = checkNotNull(httpStatus);
    this.errorMessages = ImmutableList.of();
  }

  /**
   * Returns a {@link HttpStatus}.
   *
   * @return a {@link HttpStatus}
   */
  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  /**
   * Returns a list of error messages.
   *
   * @return a list of error messages
   */
  public List<String> getErrorMessages() {
    return errorMessages;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("errorMessages", errorMessages)
        .toString();
  }

}
