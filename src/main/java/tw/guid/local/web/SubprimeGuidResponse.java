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
package tw.guid.local.web;

import com.google.common.base.MoreObjects;

public class SubprimeGuidResponse {

  String spguid;
  String prefix;

  public SubprimeGuidResponse() {

  }

  public SubprimeGuidResponse(String spguid, String prefix) {
    this.spguid = spguid;
    this.prefix = prefix;
  }

  /**
   * @return the spguid
   */
  public String getSpguid() {
    return spguid;
  }

  /**
   * @param spguid
   *          the spguid to set
   */
  public void setSpguid(String spguid) {
    this.spguid = spguid;
  }

  /**
   * @return the prefix
   */
  public String getPrefix() {
    return prefix;
  }

  /**
   * @param prefix
   *          the prefix to set
   */
  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((prefix == null) ? 0 : prefix.hashCode());
    result = prime * result + ((spguid == null) ? 0 : spguid.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    SubprimeGuidResponse other = (SubprimeGuidResponse) obj;
    if (prefix == null) {
      if (other.prefix != null) return false;
    } else if (!prefix.equals(other.prefix)) return false;
    if (spguid == null) {
      if (other.spguid != null) return false;
    } else if (!spguid.equals(other.spguid)) return false;
    return true;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("spguid", spguid)
        .add("prefix", prefix).toString();
  }

}
