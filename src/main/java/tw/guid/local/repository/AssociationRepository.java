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

import tw.guid.local.entity.Association;

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
