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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class GoogleChart {

  private GoogleChart() {}

  public static DataTable dataTable(ColumnValueCollect<?>... collects) {
    return new DataTable(collects);
  }

  public static void main(String[] args) throws JsonProcessingException {
    ObjectMapper om = new ObjectMapper();

    ColumnValueCollect<String> cvc =
        new ColumnValueCollect<>(new Column("id", "lbl", DataType.STRING));
    cvc.add("haha");
    cvc.add("yaya");

    DataTable dt = GoogleChart.dataTable(cvc);
    System.out.println(om.writeValueAsString(dt));
  }

}
