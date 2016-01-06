/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 *
 * @author Ming-Jheng Li
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
