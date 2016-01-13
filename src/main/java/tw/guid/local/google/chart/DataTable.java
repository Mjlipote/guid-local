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

public final class DataTable {

  private final List<Cols> cols;

  private final List<Rows> rows;

  public DataTable(List<Cols> cols, List<Rows> rows) {
    this.cols = cols;
    this.rows = rows;
  }

  public DataTable(Cols col, Rows row) {
    this(newArrayList(col), newArrayList(row));
  }

  public DataTable(List<Cols> cols, Rows row) {
    this(cols, newArrayList(row));
  }

  public DataTable(Cols col, List<Rows> rows) {
    this(newArrayList(col), rows);
  }

  /**
   * @return the cols
   */
  public List<Cols> getCols() {
    return cols;
  }

  /**
   * @return the rows
   */
  public List<Rows> getRows() {
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
