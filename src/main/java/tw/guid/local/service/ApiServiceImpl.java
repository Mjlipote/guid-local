/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 *
 * @author Ming-Jheng Li
 *
 */
package tw.guid.local.service;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import tw.guid.local.repository.AssociationRepository;
import tw.guid.local.repository.InstitutePrefixRepository;
import tw.guid.local.repository.SubprimeGuidRepository;

public class ApiServiceImpl implements ApiService {

  @Autowired
  private SubprimeGuidRepository subprimeGuidRepo;
  @Autowired
  private AssociationRepository associationRepo;
  @Autowired
  private InstitutePrefixRepository institutePrefixRepo;

  /**
   * Get all prefix List
   * 
   * @return
   */
  @Override
  public Set<String> prefixLookup() {
    return institutePrefixRepo.getAllPrefix();
  }

  @Override
  public Set<String> hospitalLookup() {
    return associationRepo.getAllHospital();
  }

  @Override
  public Set<String> doctorLookup() {
    return associationRepo.getAllDoctor();
  }

  @Override
  public boolean validation(String spguid) {

    return spguid.split("-")[1].length() == 8;
  }

  /**
   * 確認是否存在於 local server 資料庫
   * 
   * @param subprimeGuid
   * @return
   */
  @Override
  public boolean existence(String subprimeGuid) {
    checkNotNull(subprimeGuid, "subprimeGuid can't be null");

    return subprimeGuidRepo.findBySpguid(subprimeGuid) != null;

  }

}
