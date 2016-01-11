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

public class ChartJsData<T> {

  private List<String> labels = newArrayList();
  private List<ChartJsDataset<T>> datasets = newArrayList();

  public List<String> getLabels() {
    return labels;
  }

  public void setLabels(List<String> labels) {
    this.labels = labels;
  }

  public List<ChartJsDataset<T>> getDatasets() {
    return datasets;
  }

  public void setDatasets(List<ChartJsDataset<T>> datasets) {
    this.datasets = datasets;
  }

  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof ChartJsData)) {
      return false;
    }
    ChartJsData<?> castOther = (ChartJsData<?>) other;
    return Objects.equal(labels, castOther.labels)
        && Objects.equal(datasets, castOther.datasets);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(labels, datasets);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("labels", labels)
        .add("datasets", datasets).toString();
  }

}
