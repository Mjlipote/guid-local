/*Copyright(c)2015 ReiMed Co.to present.*All rights reserved.**@author Ming-Jheng Li**/package tw.guid.local.controller;

import static com.google.common.collect.Lists.newArrayList;
import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.text.ParseException;
import java.time.Month;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.sf.rubycollect4j.RubyCollections;
import tw.guid.local.chart.ChartJsData;
import tw.guid.local.chart.ChartJsDataset;
import tw.guid.local.repository.AccountUsersRepository;
import tw.guid.local.repository.InstitutePrefixRepository;
import tw.guid.local.repository.PaperTrailRepository;
import tw.guid.local.repository.SubprimeGuidRepository;

@RequestMapping("/analysis")
@RestController
public class WebAnalysisController {

  @Autowired
  AccountUsersRepository acctUserRepo;
  @Autowired
  PaperTrailRepository paperTrailRepo;
  @Autowired
  InstitutePrefixRepository institutePrefixRepo;
  @Autowired
  SubprimeGuidRepository subprimeGuidRepo;

  @RequestMapping(value = "/lookup", method = GET)
  ChartJsData<Integer> analysisLookup(@Param("start") Integer start,
      @Param("end") Integer end) throws ParseException {
    ChartJsData<Integer> prefixStat = new ChartJsData<>();
    prefixStat.setLabels(ra(Month.values()).map(m -> m.toString()));

    for (String p : institutePrefixRepo.getAllPrefix()) {
      if (subprimeGuidRepo.findByPrefix(p).size() > 0) {
        List<Integer> counts = newArrayList();
        for (int i = 1; i <= 12; i++) {
          counts.add(subprimeGuidRepo.countByPrefixAndCreatedAtBetween(p,
              RubyCollections.date(start, i, 1),
              RubyCollections.date(end, i, 31).endOfMonth()));
        }
        ChartJsDataset<Integer> data = new ChartJsDataset<>();
        data.setLabel(p);
        data.setData(counts);
        prefixStat.getDatasets().add(data);
      }
    }
    return prefixStat;
  }

}
