/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 *
 * @author Ming-Jheng Li
 *
 */
package tw.guid.local.google.chart;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public final class ColumnDescription {

  private final String id;

  private final String label;

  private final Type type;

  public ColumnDescription(String id, String label, Type type) {
    this.id = id;
    this.label = label;
    this.type = type;
  }

  /**
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * @return the label
   */
  public String getLabel() {
    return label;
  }

  /**
   * @return the type
   */
  public Type getType() {
    return type;
  }

  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof ColumnDescription)) {
      return false;
    }
    ColumnDescription castOther = (ColumnDescription) other;
    return Objects.equal(id, castOther.id)
        && Objects.equal(label, castOther.label)
        && Objects.equal(type, castOther.type);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id, label, type);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("id", id).add("label", label)
        .add("type", type).toString();
  }

}
