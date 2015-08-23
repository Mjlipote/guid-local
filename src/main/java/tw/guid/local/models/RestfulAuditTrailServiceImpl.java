/**
 *
 * @author Wei-Ming Wu
 *
 *
 * Copyright 2014 Wei-Ming Wu
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
package tw.guid.local.models;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import tw.guid.local.models.entity.RestfulAuditTrail;
import tw.guid.local.models.repo.RestfulAuditTrailRepository;

/**
 * 
 * {@link RestfulAuditTrailServiceImpl} implements
 * {@link RestfulAuditTrailService}.
 *
 */
@Service
@Transactional
public class RestfulAuditTrailServiceImpl implements RestfulAuditTrailService {

  @Autowired
  private RestfulAuditTrailRepository restfulAuditTrailRepo;

  @Override
  public void audit(RequestMethod action, String resource,
      HttpStatus httpStatus) {
    RestfulAuditTrail restfulAuditTrail = new RestfulAuditTrail();
    restfulAuditTrail.setUserId(getUserTypedId());
    restfulAuditTrail.setRemoteAddr(getRemoteAddr());
    restfulAuditTrail.setAction(action.name());
    restfulAuditTrail.setResource(resource);
    restfulAuditTrail.setStatusCode(httpStatus.value());

    restfulAuditTrailRepo.save(restfulAuditTrail);
  }

  private String getRemoteAddr() {
    ServletRequestAttributes sra =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    HttpServletRequest req = sra.getRequest();

    return req.getRemoteAddr();
  }

  private String getUserTypedId() {
    Authentication token =
        (Authentication) SecurityContextHolder.getContext().getAuthentication();

    if (token != null) return token.getName();

    return null;
  }

}
