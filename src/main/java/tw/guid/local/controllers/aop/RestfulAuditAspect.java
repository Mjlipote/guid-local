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
// import static net.sf.rubycollect4j.RubyCollections.newRubyArray;
// import static net.sf.rubycollect4j.RubyCollections.rs;
// import static org.springframework.http.HttpStatus.OK;
//
// import java.io.File;
// import java.lang.annotation.Annotation;
// import java.lang.reflect.Method;
// import java.util.Arrays;
// import java.util.List;
//
// import org.aspectj.lang.JoinPoint;
// import org.aspectj.lang.annotation.AfterReturning;
// import org.aspectj.lang.annotation.Aspect;
// import org.aspectj.lang.annotation.Before;
// import org.aspectj.lang.annotation.Pointcut;
// import org.aspectj.lang.reflect.MethodSignature;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.stereotype.Component;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestMethod;
// import org.springframework.web.bind.annotation.ResponseStatus;
//
// import net.sf.rubycollect4j.RubyArray;
// import net.sf.rubycollect4j.block.TransformBlock;
// import tw.edu.ym.lab525.web.guidlocalserver.models.RestfulAudit;
//
/// **
// *
// * {@link RestfulAuditAspect} uses Spring AOP to audit each request of RESTful
// * APIs activities.
// *
// */
// @Component
// @Aspect
// public class RestfulAuditAspect {
//
// @Autowired
// private RestfulAudit restfulAudit;
//
// @Pointcut("within(@org.springframework.web.bind.annotation.RestController
/// *)")
// public void restControllerBean() {}
//
// @Pointcut("execution(* *(..))")
// public void methodPointcut() {}
//
// @Before("restControllerBean() && methodPointcut()")
// public void beforeMethodInRestController(JoinPoint joinPoint) {
// String resource = replacePathVariables(getResource(joinPoint), joinPoint);
// restfulAudit.start(getAction(joinPoint), resource);
// }
//
// private String replacePathVariables(String resource, JoinPoint joinPoint) {
// MethodSignature sig = (MethodSignature) joinPoint.getSignature();
// Method m = sig.getMethod();
//
// RubyArray<List<String>> pathVarAry = newRubyArray();
// for (int i = 0; i < m.getParameterAnnotations().length; i++) {
// Annotation[] paramAnnos = m.getParameterAnnotations()[i];
// for (Annotation paramAnno : paramAnnos) {
// if (paramAnno instanceof PathVariable) {
// PathVariable pv = (PathVariable) paramAnno;
// pathVarAry
// .add(Arrays.asList(pv.value(), joinPoint.getArgs()[i] == null ? "" :
/// joinPoint.getArgs()[i].toString()));
// }
// }
// }
//
// List<String> pathVariableNames = getPathVariableNames(resource);
// if (pathVariableNames.size() == 1 && pathVarAry.size() == 1 &&
/// pathVarAry.get(0).get(0).equals("")) {
// return resource.replace("{" + pathVariableNames.get(0) + "}",
/// pathVarAry.get(0).get(1));
// }
//
// for (String pathVar : pathVariableNames) {
// if (pathVarAry.assoc(pathVar) != null) {
// resource = resource.replace("{" + pathVar + "}",
/// pathVarAry.assoc(pathVar).last());
// }
// }
//
// return resource;
// }
//
// private List<String> getPathVariableNames(String resource) {
// return rs(resource).scan("\\{[^}]+\\}").map(new TransformBlock<String,
/// String>() {
//
// @Override
// public String yield(String item) {
// return item.substring(1, item.length() - 1);
// }
//
// });
// }
//
// private RequestMethod getAction(JoinPoint joinPoint) {
// MethodSignature sig = (MethodSignature) joinPoint.getSignature();
// Method m = sig.getMethod();
// RequestMapping rm = m.getAnnotation(RequestMapping.class);
// if (rm == null)
// return null;
//
// return rm.method().length > 0 ? rm.method()[0] : null;
// }
//
// private String getResource(JoinPoint joinPoint) {
// MethodSignature sig = (MethodSignature) joinPoint.getSignature();
// Method m = sig.getMethod();
// Class<?> klass = m.getDeclaringClass();
//
// RequestMapping classRM = klass.getAnnotation(RequestMapping.class);
// RequestMapping methodRM = m.getAnnotation(RequestMapping.class);
// String classRMValue = getRequestMappingValue(classRM);
// String methodRMValue = getRequestMappingValue(methodRM);
//
// return joinPaths(classRMValue, methodRMValue);
// }
//
// private String joinPaths(String path1, String path2) {
// File file1 = new File(path1);
// File file2 = new File(file1, path2);
//
// return file2.getPath();
// }
//
// private String getRequestMappingValue(RequestMapping requestMapping) {
// return requestMapping.value().length == 0 ? "" : requestMapping.value()[0];
// }
//
// @AfterReturning("restControllerBean() && methodPointcut()")
// public void afterReturningMethodInRestController(JoinPoint joinPoint) {
// restfulAudit.end(getStatus(joinPoint));
// }
//
// private HttpStatus getStatus(JoinPoint joinPoint) {
// MethodSignature sig = (MethodSignature) joinPoint.getSignature();
// Method m = sig.getMethod();
// ResponseStatus status = m.getAnnotation(ResponseStatus.class);
//
// return status == null ? OK : status.value();
// }
//
// }
