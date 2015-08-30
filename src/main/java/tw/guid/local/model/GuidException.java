/**
 *
 * @author Wei-Ming Wu
 *
 *
 * Copyright 2014 Wei-Ming Wu
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package tw.guid.local.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;

/**
 * 
 * {@link GuidException} is used to represent any exception happened in the GUID
 * web service.
 *
 */
public final class GuidException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final HttpStatus httpStatus;
  private final List<String> errorMessages;

  /**
   * Creates a {@link GuidException}.
   *
   * @param httpStatus
   *          of this exception
   * @param errorMessages
   *          of exception details
   */
  public GuidException(HttpStatus httpStatus, List<String> errorMessages) {
    this.httpStatus = checkNotNull(httpStatus);
    this.errorMessages = ImmutableList.copyOf(errorMessages);
  }

  public GuidException(HttpStatus httpStatus, String... errorMessages) {
    this.httpStatus = checkNotNull(httpStatus);
    this.errorMessages = ImmutableList.copyOf(errorMessages);
  }

  /**
   * Creates a {@link GuidException} without error messages.
   *
   * @param httpStatus
   *          of this exception
   */
  public GuidException(HttpStatus httpStatus) {
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
