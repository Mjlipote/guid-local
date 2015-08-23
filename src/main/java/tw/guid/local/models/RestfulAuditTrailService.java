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

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * {@link RestfulAuditTrailService} is designed to record the activities of
 * RESTful APIs.
 *
 */
public interface RestfulAuditTrailService {

  /**
   * Records a RESTful request.
   *
   * @param action
   *          of a RESTful request
   * @param resource
   *          of a RESTful request
   * @param httpStatus
   *          of a RESTful request
   */
  public void audit(RequestMethod action, String resource,
      HttpStatus httpStatus);

}
