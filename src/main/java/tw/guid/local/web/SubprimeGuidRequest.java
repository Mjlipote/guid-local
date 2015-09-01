/**
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

import java.util.List;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class SubprimeGuidRequest {

  private String prefix;
  private List<String> guidHash;

  public SubprimeGuidRequest() {}

  public SubprimeGuidRequest(String prefix, List<String> guidHash) {
    this.prefix = prefix;
    this.guidHash = guidHash;
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

  /**
   * @return the guidHash
   */
  public List<String> getGuidHash() {
    return guidHash;
  }

  /**
   * @param guidHash
   *          the guidHash to set
   */
  public void setGuidHash(List<String> guidHash) {
    this.guidHash = guidHash;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof SubprimeGuidRequest) {
      SubprimeGuidRequest sgr = (SubprimeGuidRequest) o;
      return Objects.equal(prefix, sgr.prefix)
          && Objects.equal(guidHash, sgr.guidHash);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(prefix, guidHash);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("prefix", prefix)
        .add("guidHash", guidHash).toString();
  }

}
