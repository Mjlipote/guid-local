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
package tw.guid.local.chart;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public final class ChartJsDataset<T> {

  private String label;
  private List<T> data = newArrayList();

  public List<T> getData() {
    return data;
  }

  public void setData(List<T> data) {
    this.data = data;
  }

  /**
   * @return the label
   */
  public String getLabel() {
    return label;
  }

  /**
   * @param label
   *          the label to set
   */
  public void setLabel(String label) {
    this.label = label;
  }

  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof ChartJsDataset)) {
      return false;
    }
    ChartJsDataset<?> castOther = (ChartJsDataset<?>) other;
    return Objects.equal(label, castOther.label)
        && Objects.equal(data, castOther.data);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(label, data);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("label", label)
        .add("data", data).toString();
  }

}
