/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 *
 * @author Ming-Jheng Li
 *
 */
package tw.guid.local.service;

import javax.servlet.http.HttpServletRequest;

public interface LegacyService {

  public String authenticate(HttpServletRequest request);

  public String create(String prefix, String jsonHashes,
      HttpServletRequest request);

}
