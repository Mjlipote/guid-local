/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 *
 * @author Ming-Jheng Li
 *
 */
package tw.guid.local.service;

import static com.google.common.collect.Lists.newArrayList;
import static net.sf.rubycollect4j.RubyCollections.ra;

import java.time.Month;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import net.sf.rubycollect4j.RubyCollections;
import tw.guid.local.google.chart.Cols;
import tw.guid.local.google.chart.DataTable;
import tw.guid.local.google.chart.Rows;
import tw.guid.local.google.chart.Type;
import tw.guid.local.google.chart.Value;
import tw.guid.local.repository.InstitutePrefixRepository;
import tw.guid.local.repository.SubprimeGuidRepository;

public class AnalysisServiceImpl implements AnalysisService {

  private static final List<String> MONTH =
      ra(Month.values()).map(m -> m.toString());
  @Autowired
  private SubprimeGuidRepository subprimeGuidRepo;
  @Autowired
  private InstitutePrefixRepository institutePrefixRepo;

  @Override
  public Integer countAllSubprimeGuid() {
    return subprimeGuidRepo.findAll().size();
  }

  @Override
  public DataTable googleLineChartPrefixLookup(String prefix, Integer start,
      Integer end) {
    List<Cols> colsList =
        newArrayList(new Cols("month", "Mounth", Type.STRING));
    List<Rows> rowsList = newArrayList();
    for (int i = 0; i < 12; i++) {
      Value<String> value = new Value<>();
      value.setV(MONTH.get(i));
      Rows row = new Rows(newArrayList(value));
      rowsList.add(row);
    }
    colsList.add(new Cols("prefix", prefix, Type.NUMBER));
    for (int i = 0; i < 12; i++) {
      Value<Integer> value = new Value<>();
      value.setV(subprimeGuidRepo.countByPrefixAndCreatedAtBetween(prefix,
          RubyCollections.date(start, i + 1).beginningOfMonth(),
          RubyCollections.date(end, i + 1).endOfMonth()));
      rowsList.get(i).getC().add(value);
    }
    DataTable dataTable = new DataTable(colsList, rowsList);
    return dataTable;
  }

  @Override
  public DataTable googleLineChartLookup(Integer start, Integer end) {
    List<Cols> colsList =
        newArrayList(new Cols("month", "Mounth", Type.STRING));
    List<Rows> rowsList = newArrayList();
    for (int i = 0; i < 12; i++) {
      Value<String> value = new Value<>();
      value.setV(MONTH.get(i));
      Rows row = new Rows(newArrayList(value));
      rowsList.add(row);
    }
    for (String p : institutePrefixRepo.getAllPrefix()) {
      if (subprimeGuidRepo.findByPrefix(p).size() > 0) {
        colsList.add(new Cols("prefix", p, Type.NUMBER));
        for (int i = 0; i < 12; i++) {
          Value<Integer> value = new Value<>();
          value.setV(subprimeGuidRepo.countByPrefixAndCreatedAtBetween(p,
              RubyCollections.date(start, i + 1).beginningOfMonth(),
              RubyCollections.date(end, i + 1).endOfMonth()));
          rowsList.get(i).getC().add(value);
        }
      }
    }
    DataTable dataTable = new DataTable(colsList, rowsList);
    return dataTable;
  }

}
