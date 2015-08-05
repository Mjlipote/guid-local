/// **
// *
// * @author Wei-Ming Wu
// *
// *
// * Copyright 2014 Wei-Ming Wu
// *
// * Licensed under the Apache License, Version 2.0 (the "License"); you may not
// * use this file except in compliance with the License. You may obtain a copy
/// of
// * the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
// * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
// * License for the specific language governing permissions and limitations
/// under
// * the License.
// *
// */
// package tw.edu.ym.lab525.web.guidlocalserver.models;
//
// import static com.google.common.base.Preconditions.checkNotNull;
//
// import javax.servlet.http.HttpSession;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.stereotype.Component;
// import org.springframework.web.bind.annotation.RequestMethod;
// import org.springframework.web.context.request.RequestContextHolder;
// import org.springframework.web.context.request.ServletRequestAttributes;
//
/// **
// *
// * {@link RestfulAudit} is a utility class for auditing RESTful API requests.
// *
// */
// @Component
// public final class RestfulAudit {
//
// private static final String RESTFUL_ACTION = "restfulAction";
// private static final String RESTFUL_RESOURCE = "restfulResource";
//
// @Autowired
// private RestfulAuditTrailService restfulAuditTrailService;
//
// /**
// * Starts an audit.
// *
// * @param action
// * of a RESTful request
// * @param resource
// * of a RESTful request
// */
// public void start(RequestMethod action, String resource) {
// HttpSession session = getSession();
// session.setAttribute(RESTFUL_ACTION, checkNotNull(action));
// session.setAttribute(RESTFUL_RESOURCE, checkNotNull(resource));
// }
//
// private HttpSession getSession() {
// ServletRequestAttributes sra = (ServletRequestAttributes)
/// RequestContextHolder.getRequestAttributes();
//
// return sra.getRequest().getSession();
// }
//
// private RequestMethod getActionInSession() {
// return (RequestMethod) getSession().getAttribute(RESTFUL_ACTION);
// }
//
// private String getRecourceInSession() {
// return (String) getSession().getAttribute(RESTFUL_RESOURCE);
// }
//
// /**
// * Ends an audit.
// *
// * @param status
// * of a RESTful request
// * @return the http status code
// */
// public int end(HttpStatus status) {
// restfulAuditTrailService.audit(getActionInSession(), getRecourceInSession(),
/// checkNotNull(status));
//
// return status.value();
// }
//
// }
