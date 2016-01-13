/*
*
* @author Ming-Jheng Li
*
*
* Copyright 2015 Ming-Jheng Li
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
package tw.guid.local.google.chart;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

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

}
