/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 *
 * @author Ming-Jheng Li
 *
 */
package tw.guid.local.web;

import java.util.List;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

/**
 * 
 * {@link GuidErrors} is designed to hold error messages of the GUID service. It
 * is immutable.
 *
 */
public final class GuidErrors {

  private final List<String> errors;

  /**
   * Creates a {@link GuidErrors}.
   *
   * @param errors
   *          a list of error messages
   */
  public GuidErrors(List<String> errors) {
    this.errors = ImmutableList.copyOf(errors);
  }

  /**
   * Returns a list of error messages.
   *
   * @return a list of error messages
   */
  public List<String> getErrors() {
    return errors;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof GuidErrors) {
      GuidErrors err = (GuidErrors) o;
      return Objects.equal(errors, err.errors);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(errors);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("errors", errors).toString();
  }

}
