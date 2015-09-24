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

import java.util.Objects;

import javax.validation.constraints.Pattern;

import com.google.common.base.MoreObjects;

public class PrefixedHashBundle implements GuidEncodable {

  @Pattern(regexp = "[A-Z0-9]{3,10}")
  private String prefix;

  @Pattern(regexp = "[A-Z0-9]{128}")
  private String hash1;

  @Pattern(regexp = "[A-Z0-9]{128}")
  private String hash2;

  @Pattern(regexp = "[A-Z0-9]{128}")
  private String hash3;

  public PrefixedHashBundle() {}

  public PrefixedHashBundle(String prefix, String hash1, String hash2,
      String hash3) {
    this.prefix = prefix.toUpperCase();
    this.hash1 = hash1.toUpperCase();
    this.hash2 = hash2.toUpperCase();
    this.hash3 = hash3.toUpperCase();
  }

  public PrefixedHashBundle(GuidEncodable guidEncodable) {
    prefix = guidEncodable.getPrefix().toUpperCase();
    hash1 = guidEncodable.getHash1().toUpperCase();
    hash2 = guidEncodable.getHash2().toUpperCase();
    hash3 = guidEncodable.getHash3().toUpperCase();
  }

  @Override
  public String getPrefix() {
    return prefix;
  }

  @Override
  public void setPrefix(String prefix) {
    this.prefix = prefix.toUpperCase();
  }

  @Override
  public String getHash1() {
    return hash1;
  }

  @Override
  public void setHash1(String hash1) {
    this.hash1 = hash1.toUpperCase();
  }

  @Override
  public String getHash2() {
    return hash2;
  }

  @Override
  public void setHash2(String hash2) {
    this.hash2 = hash2.toUpperCase();
  }

  @Override
  public String getHash3() {
    return hash3;
  }

  @Override
  public void setHash3(String hash3) {
    this.hash3 = hash3.toUpperCase();
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) return true;
    if (!(other instanceof PrefixedHashBundle)) return false;
    PrefixedHashBundle castOther = (PrefixedHashBundle) other;
    return Objects.equals(prefix, castOther.prefix)
        && Objects.equals(hash1, castOther.hash1)
        && Objects.equals(hash2, castOther.hash2)
        && Objects.equals(hash3, castOther.hash3);
  }

  @Override
  public int hashCode() {
    return Objects.hash(prefix, hash1, hash2, hash3);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("prefix", prefix)
        .add("hash1", hash1).add("hash2", hash2).add("hash3", hash3).toString();
  }

}
