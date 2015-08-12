package tw.guid.local.controllers.aop;
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
// package tw.edu.ym.lab525.web.guidlocalserver.controllers.aop;
//
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.ControllerAdvice;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.ResponseBody;
// import org.springframework.web.bind.annotation.RestController;
//
// import tw.edu.ym.lab525.web.guidlocalserver.models.GuidErrors;
// import tw.edu.ym.lab525.web.guidlocalserver.models.GuidException;
// import tw.edu.ym.lab525.web.guidlocalserver.models.RestfulAudit;
//
/// **
// *
// * {@link RestfulExceptionHandlerAdvice} separates exception handlers from
/// each
// * rest controller to here.
// *
// */
// @ControllerAdvice(annotations = { RestController.class })
// public class RestfulExceptionHandlerAdvice {
//
// @Autowired
// private RestfulAudit restfulAudit;
//
// @ExceptionHandler(GuidException.class)
// public @ResponseBody GuidErrors handleError(HttpServletRequest req,
/// HttpServletResponse res,
// GuidException exception) {
// res.setStatus(restfulAudit.end(exception.getHttpStatus()));
//
// return new GuidErrors(exception.getErrorMessages());
// }
//
// }