/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 *
 * @author Ming-Jheng Li
 *
 */
package tw.guid.local.google.chart;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public final class Cells {

  private final List<Value<?>> c;

  public Cells(List<Value<?>> c) {
    this.c = c;
  }

  public Cells() {
    this.c = newArrayList();
  }

  public Cells addValue(Value<?> value) {
    this.c.add(value);
    return this;
  }

  /**
   * @return the c
   */
  public List<Value<?>> getC() {
    return c;
  }

  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof Cells)) {
      return false;
    }
    Cells castOther = (Cells) other;
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
