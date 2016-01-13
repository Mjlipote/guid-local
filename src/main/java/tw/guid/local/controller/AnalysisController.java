/*Copyright(c)2015 ReiMed Co.to present.*All rights reserved.**@author Ming-Jheng Li**/
package tw.guid.local.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.guid.local.google.chart.DataTable;
import tw.guid.local.repository.AccountUsersRepository;
import tw.guid.local.repository.InstitutePrefixRepository;
import tw.guid.local.repository.PaperTrailRepository;
import tw.guid.local.repository.SubprimeGuidRepository;
import tw.guid.local.service.AnalysisService;

@RequestMapping("/analysis")
@RestController
public class AnalysisController {

  @Autowired
  AccountUsersRepository acctUserRepo;
  @Autowired
  PaperTrailRepository paperTrailRepo;
  @Autowired
  InstitutePrefixRepository institutePrefixRepo;
  @Autowired
  SubprimeGuidRepository subprimeGuidRepo;
  @Autowired
  AnalysisService analysisService;

  @RequestMapping(value = "/total", method = GET)
  Integer countAllSubprimeGuid() {
    return analysisService.countAllSubprimeGuid();
  }

  @RequestMapping(value = "/total-by-year", method = GET)
  Integer countSubprimeGuidByYear(@Param("year") Integer year) {
    return analysisService.countSubprimeGuidByYear(year);
  }

  @RequestMapping(value = "/total-between", method = GET)
  Integer countSubprimeGuidBetween(@Param("start") Integer start,
      @Param("end") Integer end) {
    return analysisService.countSubprimeGuidBetween(start, end);
  }

  @RequestMapping(value = "/line-chart", method = GET)
  DataTable lineChart(@Param("year") Integer year) {
    return analysisService.lineChart(year);
  }

  @RequestMapping(value = "/line-chart-all", method = GET)
  DataTable lineChartAll(@Param("year") Integer year) {
    return analysisService.lineChartAll(year);
  }

  @RequestMapping(value = "/line-chart-by-prefix", method = GET)
  DataTable lineChartByPrefix(@Param("prefix") String prefix,
      @Param("year") Integer year) {
    return analysisService.lineChartByPrefix(prefix, year);
  }

  @RequestMapping(value = "/line-chart-between", method = GET)
  DataTable lineChartBetween(@Param("start") Integer start,
      @Param("end") Integer end) {
    return analysisService.lineChartBetween(start, end);
  }

  @RequestMapping(value = "/line-chart-all-between", method = GET)
  DataTable lineChartAllBetween(@Param("start") Integer start,
      @Param("end") Integer end) {
    return analysisService.lineChartAllBetween(start, end);
  }

  @RequestMapping(value = "/line-chart-between-by-prefix", method = GET)
  DataTable lineChartBetweenByPrefix(@Param("prefix") String prefix,
      @Param("start") Integer start, @Param("end") Integer end) {
    return analysisService.lineChartBetweenByPrefix(prefix, start, end);
  }

  @RequestMapping(value = "/pie-chart-all", method = GET)
  DataTable pieChartAll() {
    return analysisService.pieChartAll();
  }

  @RequestMapping(value = "/pie-chart-between", method = GET)
  DataTable pieChartBetween(@Param("start") Integer start,
      @Param("end") Integer end) {
    return analysisService.pieChartBetween(start, end);
  }
}
