/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 *
 * @author Ming-Jheng Li
 *
 */
package tw.guid.local.google.chart;

import java.util.List;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public final class Rows {

  private final List<Value<?>> c;

  public Rows(List<Value<?>> c) {
    this.c = c;
  }

  /**
   * @return the c
   */
  public List<Value<?>> getC() {
    return c;
  }

  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof Rows)) {
      return false;
    }
    Rows castOther = (Rows) other;
    return Objects.equal(c, castOther.c);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(c);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("c", c).toString();
  }

}
