/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 *
 * @author Ming-Jheng Li
 *
 */
package tw.guid.local.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.net.URISyntaxException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tw.guid.local.google.chart.DataTable;
import tw.guid.local.repository.AccountUsersRepository;
import tw.guid.local.repository.AssociationRepository;
import tw.guid.local.repository.InstitutePrefixRepository;
import tw.guid.local.repository.SubprimeGuidRepository;
import tw.guid.local.service.ApiService;

@RequestMapping("/guids/api")
@RestController
public class ApiController {

  @Autowired
  SubprimeGuidRepository spguidRepo;

  @Autowired
  AccountUsersRepository acctUserRepo;

  @Autowired
  AssociationRepository associationRepo;

  @Autowired
  InstitutePrefixRepository institutePrefixRepo;

  @Autowired
  ApiService apiService;

  @Autowired
  Environment env;

  @ResponseBody
  @RequestMapping("/prefix")
  Set<String> prefixLookup() {
    return apiService.prefixLookup();
  }

  @ResponseBody
  @RequestMapping("/hospital")
  Set<String> hospitalLookup() {
    return apiService.hospitalLookup();
  }

  @ResponseBody
  @RequestMapping("/doctor")
  Set<String> doctorLookup() {
    return apiService.doctorLookup();
  }

  @ResponseBody
  @RequestMapping("/validation")
  boolean validation(@RequestParam("spguid") String spguid)
      throws URISyntaxException {
    return apiService.validation(spguid);
  }

  @RequestMapping("/existence")
  boolean existence(@RequestParam("subprimeGuid") String subprimeGuid) {
    return apiService.existence(subprimeGuid);
  }

  @RequestMapping(value = "/total", method = GET)
  Integer countAllSubprimeGuid() {
    return apiService.countAllSubprimeGuid();
  }

  @RequestMapping(value = "/total-by-year", method = GET)
  Integer countSubprimeGuidByYear(@Param("year") Integer year) {
    return apiService.countSubprimeGuidByYear(year);
  }

  @RequestMapping(value = "/total-between", method = GET)
  Integer countSubprimeGuidBetween(@Param("start") Integer start,
      @Param("end") Integer end) {
    return apiService.countSubprimeGuidBetween(start, end);
  }

  @RequestMapping(value = "/line-chart", method = GET)
  DataTable lineChart(@Param("year") Integer year) {
    return apiService.lineChart(year);
  }

  @RequestMapping(value = "/line-chart-all", method = GET)
  DataTable lineChartAll(@Param("year") Integer year) {
    return apiService.lineChartAll(year);
  }

  @RequestMapping(value = "/line-chart-by-prefix", method = GET)
  DataTable lineChartByPrefix(@Param("prefix") String prefix,
      @Param("year") Integer year) {
    return apiService.lineChartByPrefix(prefix, year);
  }

  @RequestMapping(value = "/line-chart-between", method = GET)
  DataTable lineChartBetween(@Param("start") Integer start,
      @Param("end") Integer end) {
    return apiService.lineChartBetween(start, end);
  }

  @RequestMapping(value = "/line-chart-all-between", method = GET)
  DataTable lineChartAllBetween(@Param("start") Integer start,
      @Param("end") Integer end) {
    return apiService.lineChartAllBetween(start, end);
  }

  @RequestMapping(value = "/line-chart-between-by-prefix", method = GET)
  DataTable lineChartBetweenByPrefix(@Param("prefix") String prefix,
      @Param("start") Integer start, @Param("end") Integer end) {
    return apiService.lineChartBetweenByPrefix(prefix, start, end);
  }

  @RequestMapping(value = "/pie-chart-all", method = GET)
  DataTable pieChartAll() {
    return apiService.pieChartAll();
  }

  @RequestMapping(value = "/pie-chart-between", method = GET)
  DataTable pieChartBetween(@Param("start") Integer start,
      @Param("end") Integer end) {
    return apiService.pieChartBetween(start, end);
  }
}