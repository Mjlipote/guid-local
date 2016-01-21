/*
 *
 * Copyright 2016 Wei-Ming Wu
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
package com.github.wnameless.google.chart;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public final class Column {

  private final String id;

  private final String label;

  private final DataType type;

  public Column(String id, String label, DataType type) {
    this.id = id;
    this.label = label;
    this.type = type;
  }

  public String getId() {
    return id;
  }

  public String getLabel() {
    return label;
  }

  public DataType getType() {
    return type;
  }

  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof Column)) {
      return false;
    }
    Column castOther = (Column) other;
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
