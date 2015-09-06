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

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.github.wnameless.spring.papertrail.PaperTrailCrudRepository;

import tw.guid.local.entity.PaperTrail;

public interface PaperTrailRepository
    extends PaperTrailCrudRepository<PaperTrail, Long>,
    JpaSpecificationExecutor<PaperTrail> {

  public List<PaperTrail> findByUserId(String userId);

  public List<PaperTrail> findByUserIdAndCreatedAtBetween(String userId,
      Date startDate, Date endDate);

  public List<PaperTrail> findByCreatedAtBetween(Date startDate, Date endDate);

  public default Set<String> getAllUserId() {
    Set<String> userId = newHashSet();

    for (PaperTrail paperTrail : findAll()) {
      userId.add(paperTrail.getUserId());
    }
    return userId;
  }

  public default Set<String> getAllRemoteAddr() {
    Set<String> remoteAddr = newHashSet();

    for (PaperTrail paperTrail : findAll()) {
      remoteAddr.add(paperTrail.getRemoteAddr());
    }
    return remoteAddr;
  }

  public default Set<String> getAllRequestURI() {
    Set<String> requestURI = newHashSet();

    for (PaperTrail paperTrail : findAll()) {
      requestURI.add(paperTrail.getRequestURI());
    }
    return requestURI;
  }

  public default Set<Integer> getAllHttpStatus() {
    Set<Integer> httpStatus = newHashSet();

    for (PaperTrail paperTrail : findAll()) {
      httpStatus.add(paperTrail.getHttpStatus());
    }
    return httpStatus;
  }

}
