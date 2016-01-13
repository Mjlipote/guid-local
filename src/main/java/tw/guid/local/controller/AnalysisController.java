/*Copyright(c)2015 ReiMed Co.to present.*All rights reserved.**@author Ming-Jheng Li**/package tw.guid.local.controller;

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

  @RequestMapping(value = "/totalNumber", method = GET)
  Integer countAllSubprimeGuid() {
    return analysisService.countAllSubprimeGuid();
  }

  @RequestMapping(value = "/googleLineChart", method = GET)
  DataTable googleLineChartLookup(@Param("start") Integer start,
      @Param("end") Integer end) {
    return analysisService.googleLineChartLookup(start, end);
  }

  @RequestMapping(value = "/googleLineChartPrefix", method = GET)
  DataTable googleLineChartPrefixLookup(@Param("prefix") String prefix,
      @Param("start") Integer start, @Param("end") Integer end) {
    return analysisService.googleLineChartPrefixLookup(prefix, start, end);
  }

}
