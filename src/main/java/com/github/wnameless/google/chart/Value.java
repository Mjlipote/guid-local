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

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

@JsonInclude(NON_NULL)
public final class Value<T> {

  private T v;

  private String f;

  private Map<String, Object> p;

  public Value(T v) {
    this.v = v;
  }

  public T getV() {
    return v;
  }

  public void setV(T v) {
    this.v = v;
  }

  public String getF() {
    return f;
  }

  public void setF(String f) {
    this.f = f;
  }

  public Map<String, Object> getP() {
    return p;
  }

  public void setP(Map<String, Object> p) {
    this.p = p;
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) return true;
    if (!(other instanceof Value)) return false;
    Value<?> castOther = (Value<?>) other;
    return Objects.equal(v, castOther.v) && Objects.equal(f, castOther.f)
        && Objects.equal(p, castOther.p);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(v, f, p);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("v", v).add("f", f).add("p", p)
        .toString();
  }

}
