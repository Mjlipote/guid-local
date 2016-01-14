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

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DataType {

  STRING(String.class), NUMBER(Number.class), BOOLEAN(Boolean.class),
  DATE(Date.class), DATETIME(Date.class);

  private Class<?> type;

  private DataType(Class<?> type) {
    this.type = type;
  }

  public Class<?> getJavaDataType() {
    return type;
  }

  @JsonCreator
  public static DataType fromLiteral(String literal) {
    return DataType.valueOf(literal.toUpperCase());
  }

  @JsonValue
  public String toLiteral() {
    return this.toString().toLowerCase();
  }

}
