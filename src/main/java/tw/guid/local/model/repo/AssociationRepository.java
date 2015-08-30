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

package tw.guid.local.model.repo;

import static com.google.common.collect.Sets.newHashSet;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import tw.guid.local.model.entity.Association;

public interface AssociationRepository extends JpaRepository<Association, Long>,
    JpaSpecificationExecutor<Association> {

  public Association findBySpguid(String spguid);

  public default Set<String> getAllHospital() {
    Set<String> hospitalSet = newHashSet();

    for (Association associationUser : findAll()) {
      hospitalSet.add(associationUser.getHospital());
    }
    return hospitalSet;
  }

  public default Set<String> getAllDoctor() {
    Set<String> doctorSet = newHashSet();

    for (Association associationUser : findAll()) {
      doctorSet.add(associationUser.getDoctor());
    }
    return doctorSet;
  }

}
