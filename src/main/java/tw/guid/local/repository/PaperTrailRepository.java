/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 *
 * @author Ming-Jheng Li
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
      requestURI.add(paperTrail.getRequestUri());
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
