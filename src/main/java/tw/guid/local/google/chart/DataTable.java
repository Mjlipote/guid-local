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

  private final List<ColumnDescription> cols;

  private final List<Cells> rows;

  public DataTable() {
    this.cols = newArrayList();
    this.rows = newArrayList();
  }

  public DataTable(List<ColumnDescription> cols, List<Cells> rows) {
    this.cols = cols;
    this.rows = rows;
  }

  public DataTable(ColumnDescription col, Cells row) {
    this(newArrayList(col), newArrayList(row));
  }

  public DataTable(List<ColumnDescription> cols, Cells row) {
    this(cols, newArrayList(row));
  }

  public DataTable(ColumnDescription col, List<Cells> rows) {
    this(newArrayList(col), rows);
  }

  public DataTable addColumn(ColumnDescription column) {
    this.cols.add(column);
    return this;
  }

  public DataTable addRow(Cells cells) {
    this.rows.add(cells);
    return this;
  }

  /**
   * @return the cols
   */
  public List<ColumnDescription> getCols() {
    return cols;
  }

  /**
   * @return the rows
   */
  public List<Cells> getRows() {
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
