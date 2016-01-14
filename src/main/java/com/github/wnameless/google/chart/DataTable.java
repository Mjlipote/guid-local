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

import static com.google.common.collect.Lists.newArrayList;
import static net.sf.rubycollect4j.RubyCollections.ra;

import java.util.List;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import net.sf.rubycollect4j.RubyArray;

public final class DataTable {

  private final List<Column> cols = newArrayList();

  private final List<Row> rows = newArrayList();

  public DataTable() {}

  public DataTable(ColumnValueCollect<?>... columnValueCollects) {
    for (ColumnValueCollect<?> col : columnValueCollects) {
      cols.add(col.getColumn());
    }
    RubyArray<RubyArray<Value<?>>> vals =
        ra(columnValueCollects).map(cvc -> cvc.getValues()).transpose();
    vals.each(val -> rows.add(new Row(val)));
  }

  public List<Column> getCols() {
    return cols;
  }

  public List<Row> getRows() {
    return rows;
  }

  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof DataTable)) {
      return false;
    }
    DataTable castOther = (DataTable) other;
    return Objects.equal(cols, castOther.cols)
        && Objects.equal(rows, castOther.rows);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(cols, rows);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("cols", cols).add("rows", rows)
        .toString();
  }

}
