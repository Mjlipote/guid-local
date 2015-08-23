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
package tw.guid.local.models.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.google.common.base.MoreObjects;

/**
 * 
 * {@link RestfulAuditTrail} is a JPA entity class. It is designed to log the
 * activities of RESTful APIs.
 *
 */
@Entity(name = "restful_audit_trail")
public class RestfulAuditTrail extends Timestampable implements Serializable {

  private static final long serialVersionUID = 1L;

  public static final String ID = "id";
  public static final String USER_ID = "userId";
  public static final String REMOTE_ADDR = "remoteAddr";
  public static final String ACTION = "action";
  public static final String RESOURCE = "resource";
  public static final String STATUS_CODE = "statusCode";

  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private long id;

  @Column(name = "user_id")
  private String userId;

  @Column(name = "remote_addr", nullable = false)
  private String remoteAddr;

  @Column(name = "action", nullable = false)
  private String action;

  @Column(name = "resource", nullable = false)
  private String resource;

  @Column(name = "status_code", nullable = false)
  private int statusCode;

  /**
   * Returns the DB primary key.
   * 
   * @return the DB primary key
   */
  public long getId() {
    return id;
  }

  /**
   * Sets the DB primary key.
   * 
   * @param id
   *          the DB primary key
   */
  public void setId(long id) {
    this.id = id;
  }

  /**
   * Returns the user id of the RESTful API requester.
   * 
   * @return the user id of the RESTful API requester
   */
  public String getUserId() {
    return userId;
  }

  /**
   * Sets the user id of the RESTful API requester.
   * 
   * @param userId
   *          the user id of the RESTful API requester
   */
  public void setUserId(String userId) {
    this.userId = userId;
  }

  /**
   * Returns the remote address of the RESTful API requester.
   * 
   * @return the remote address of the RESTful API requester
   */
  public String getRemoteAddr() {
    return remoteAddr;
  }

  /**
   * Sets the remote address of the RESTful API requester.
   * 
   * @param remoteAddr
   *          the remote address of the RESTful API requester
   */
  public void setRemoteAddr(String remoteAddr) {
    this.remoteAddr = remoteAddr;
  }

  /**
   * Returns the action of the RESTful API request.
   * 
   * @return the action of the RESTful API request
   */
  public String getAction() {
    return action;
  }

  /**
   * Sets the action of the RESTful API request.
   * 
   * @param action
   *          the action of the RESTful API request
   */
  public void setAction(String action) {
    this.action = action;
  }

  /**
   * Returns the resource URL of the RESTful API request.
   * 
   * @return the resource URL of the RESTful API request
   */
  public String getResource() {
    return resource;
  }

  /**
   * Sets the resource URL of the RESTful API request.
   * 
   * @param resource
   *          the resource URL of the RESTful API request
   */
  public void setResource(String resource) {
    this.resource = resource;
  }

  /**
   * Returns the HTTP status code of the RESTful API request.
   * 
   * @return the HTTP status code of the RESTful API request
   */
  public int getStatusCode() {
    return statusCode;
  }

  /**
   * Sets the HTTP status code of the RESTful API request.
   * 
   * @param statusCode
   *          the HTTP status code of the RESTful API request
   */
  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) return true;
    if (!(other instanceof RestfulAuditTrail)) return false;
    RestfulAuditTrail castOther = (RestfulAuditTrail) other;
    return Objects.equals(id, castOther.id)
        && Objects.equals(userId, castOther.userId)
        && Objects.equals(remoteAddr, castOther.remoteAddr)
        && Objects.equals(action, castOther.action)
        && Objects.equals(resource, castOther.resource)
        && Objects.equals(statusCode, castOther.statusCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, userId, remoteAddr, action, resource, statusCode);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add(ID, id).add(USER_ID, userId)
        .add(REMOTE_ADDR, remoteAddr).add(ACTION, action)
        .add(RESOURCE, resource).add(STATUS_CODE, statusCode).toString();
  }

}
