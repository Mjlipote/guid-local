/*
 *
 * Copyright 2015 Wei-Ming Wu
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
package tw.guid.local.model;

import static com.google.common.collect.Sets.newLinkedHashSet;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

import com.google.common.base.MoreObjects;

public final class GuidSet<D extends GuidDecodable> {

  private Set<D> set = newLinkedHashSet();

  public GuidSet() {}

  public GuidSet(Collection<D> set) {
    this.set = newLinkedHashSet(set);
  }

  public Set<D> getSet() {
    return set;
  }

  public void setSet(Set<D> set) {
    this.set = set;
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) return true;
    if (!(other instanceof GuidSet)) return false;
    GuidSet<?> castOther = (GuidSet<?>) other;
    return Objects.equals(set, castOther.set);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(set);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("set", set).toString();
  }

}
