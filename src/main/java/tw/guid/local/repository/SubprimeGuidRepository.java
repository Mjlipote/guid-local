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

package tw.guid.local.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.guid.local.entity.SubprimeGuid;
import tw.guid.local.web.SubprimeGuidRequest;

public interface SubprimeGuidRepository
    extends JpaRepository<SubprimeGuid, Long> {

  public SubprimeGuid findByHashcode1AndHashcode2AndHashcode3AndPrefix(
      String hashcode1, String hashcode2, String hashcode3, String prefix);

  public Set<SubprimeGuid> findByHashcode1AndHashcode2AndHashcode3(
      String hashcode1, String hashcode2, String hashcode3);

  public default Set<SubprimeGuid> findBySubprimeGuidRequest(
      SubprimeGuidRequest req) {
    return findByHashcode1AndHashcode2AndHashcode3(
        req.getGuidHash().get(0).substring(0, 128),
        req.getGuidHash().get(1).substring(0, 128),
        req.getGuidHash().get(2).substring(0, 128));
  }

  public default boolean isExist(SubprimeGuidRequest req) {

    return findByHashcode1AndHashcode2AndHashcode3AndPrefix(
        req.getGuidHash().get(0).substring(0, 128),
        req.getGuidHash().get(1).substring(0, 128),
        req.getGuidHash().get(2).substring(0, 128), req.getPrefix()) != null;
  }

  public default String getSubprimeGuidBySubprimeGuidRequest(
      SubprimeGuidRequest req) {

    return findByHashcode1AndHashcode2AndHashcode3AndPrefix(
        req.getGuidHash().get(0).substring(0, 128),
        req.getGuidHash().get(1).substring(0, 128),
        req.getGuidHash().get(2).substring(0, 128), req.getPrefix())
            .getSpguid();

  }

}
