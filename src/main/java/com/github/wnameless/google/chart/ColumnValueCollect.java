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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public final class ColumnValueCollect<T> {

  private final Column column;

  private final List<Value<T>> values = newArrayList();

  public ColumnValueCollect(Column column) {
    this.column = column;
  }

  public void add(T value) {
    checkArgument(isTypeSafe(value));
    values.add(new Value<T>(value));
  }

  public void addAll(Iterable<T> values) {
    int i = 0;
    for (T v : values) {
      checkArgument(isTypeSafe(v),
          "The value of idx " + i + " is not type safe");
      this.values.add(new Value<T>(v));
      i++;
    }
  }

  public Column getColumn() {
    return column;
  }

  public List<Value<T>> getValues() {
    return values;
  }

  private boolean isTypeSafe(T value) {
    if (value == null)
      return true;
    else
      return column.getType().getJavaDataType()
          .isAssignableFrom(value.getClass());
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) return true;
    if (!(other instanceof ColumnValueCollect)) return false;
    ColumnValueCollect<?> castOther = (ColumnValueCollect<?>) other;
    return Objects.equal(column, castOther.column)
        && Objects.equal(values, castOther.values);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(column, values);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("column", column)
        .add("values", values).toString();
  }

}
