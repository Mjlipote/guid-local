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

public class PublicGuid implements GuidDecodable {

  @Pattern(regexp = "[A-Z0-9]{3,10}")
  private String prefix;

  @Pattern(regexp = "[A-Z0-9]{8}")
  private String code;

  public PublicGuid() {}

  public PublicGuid(String prefix, String code) {
    this.prefix = prefix;
    this.code = code;
  }

  public PublicGuid(GuidDecodable guidDecodable) {
    prefix = guidDecodable.getPrefix().toUpperCase();
    code = guidDecodable.getCode().toUpperCase();
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
  public String getCode() {
    return code;
  }

  @Override
  public void setCode(String code) {
    this.code = code.toUpperCase();
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) return true;
    if (!(other instanceof PublicGuid)) return false;
    PublicGuid castOther = (PublicGuid) other;
    return Objects.equals(prefix, castOther.prefix)
        && Objects.equals(code, castOther.code);
  }

  @Override
  public int hashCode() {
    return Objects.hash(prefix, code);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("prefix", prefix)
        .add("code", code).toString();
  }

}
