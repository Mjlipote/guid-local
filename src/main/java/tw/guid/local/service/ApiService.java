/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 *
 * @author Ming-Jheng Li
 *
 */
package tw.guid.local.service;

import java.util.Set;

public interface ApiService {

  public Set<String> prefixLookup();

  public Set<String> hospitalLookup();

  public Set<String> doctorLookup();

  public boolean validation(String spguid);

  public boolean existence(String subprimeGuid);
}
