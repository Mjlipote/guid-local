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

import static com.google.common.collect.Sets.newHashSet;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import tw.guid.local.entity.InstitutePrefix;

public interface InstitutePrefixRepository
    extends JpaRepository<InstitutePrefix, Long>,
    JpaSpecificationExecutor<InstitutePrefix> {

  public InstitutePrefix findByInstitute(String institute);

  public default Set<String> getAllInstitute() {
    Set<String> instituteSet = newHashSet();

    for (InstitutePrefix institutePrefix : findAll()) {
      instituteSet.add(institutePrefix.getInstitute());
    }
    return instituteSet;
  }

  public default Set<String> getAllPrefix() {
    Set<String> instituteSet = newHashSet();

    for (InstitutePrefix institutePrefix : findAll()) {
      instituteSet.add(institutePrefix.getPrefix());
    }
    return instituteSet;
  }
}
