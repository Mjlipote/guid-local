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
  public Integer countSubprimeGuidByYear(Integer year) {

    return subprimeGuidRepo.countByCreatedAtBetween(
        RubyCollections.date(year).beginningOfYear(),
        RubyCollections.date(year).endOfYear());
  }

  @Override
  public Integer countSubprimeGuidBetween(Integer start, Integer end) {
    if (end < start) {
      int temp = start;
      start = end;
      end = temp;
    }
    return subprimeGuidRepo.countByCreatedAtBetween(
        RubyCollections.date(start).beginningOfYear(),
        RubyCollections.date(end).endOfYear());
  }

  @Override
  public DataTable lineChart(Integer year) {
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
              RubyCollections.date(year, i + 1).beginningOfMonth(),
              RubyCollections.date(year, i + 1).endOfMonth()));
          rowsList.get(i).getC().add(value);
        }
      }
    }
    DataTable dataTable = new DataTable(colsList, rowsList);
    return dataTable;
  }

  @Override
  public DataTable lineChartAll(Integer year) {
    List<Cols> colsList =
        newArrayList(new Cols("month", "Mounth", Type.STRING));
    List<Rows> rowsList = newArrayList();
    for (int i = 0; i < 12; i++) {
      Value<String> value = new Value<>();
      value.setV(MONTH.get(i));
      Rows row = new Rows(newArrayList(value));
      rowsList.add(row);
    }
    colsList.add(new Cols("guid", "GUID", Type.NUMBER));
    for (int i = 0; i < 12; i++) {
      Value<Integer> value = new Value<>();
      value.setV(subprimeGuidRepo.countByCreatedAtBetween(
          RubyCollections.date(year, i + 1).beginningOfMonth(),
          RubyCollections.date(year, i + 1).endOfMonth()));
      rowsList.get(i).getC().add(value);
    }
    DataTable dataTable = new DataTable(colsList, rowsList);
    return dataTable;
  }

  @Override
  public DataTable lineChartByPrefix(String prefix, Integer year) {
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
          RubyCollections.date(year, i + 1).beginningOfMonth(),
          RubyCollections.date(year, i + 1).endOfMonth()));
      rowsList.get(i).getC().add(value);
    }
    DataTable dataTable = new DataTable(colsList, rowsList);
    return dataTable;
  }

  @Override
  public DataTable lineChartBetween(Integer start, Integer end) {
    if (end < start) {
      int temp = start;
      start = end;
      end = temp;
    }
    List<Cols> colsList = newArrayList(new Cols("year", "Year", Type.STRING));
    List<Rows> rowsList = newArrayList();
    for (int i = 0; i <= end - start; i++) {
      Value<String> value = new Value<>();
      value.setV(String.valueOf(start + i));
      Rows row = new Rows(newArrayList(value));
      rowsList.add(row);
    }
    for (String p : institutePrefixRepo.getAllPrefix()) {
      if (subprimeGuidRepo.findByPrefix(p).size() > 0) {
        colsList.add(new Cols("prefix", p, Type.NUMBER));
        for (int i = 0; i <= end - start; i++) {
          Value<Integer> value = new Value<>();
          value.setV(subprimeGuidRepo.countByPrefixAndCreatedAtBetween(p,
              RubyCollections.date(start + i).beginningOfYear(),
              RubyCollections.date(start + i).endOfYear()));
          rowsList.get(i).getC().add(value);
        }
      }
    }
    DataTable dataTable = new DataTable(colsList, rowsList);
    return dataTable;
  }

  @Override
  public DataTable lineChartAllBetween(Integer start, Integer end) {
    if (end < start) {
      int temp = start;
      start = end;
      end = temp;
    }
    List<Cols> colsList = newArrayList(new Cols("year", "Year", Type.STRING));
    List<Rows> rowsList = newArrayList();
    for (int i = 0; i <= end - start; i++) {
      Value<String> value = new Value<>();
      value.setV(String.valueOf(start + i));
      Rows row = new Rows(newArrayList(value));
      rowsList.add(row);
    }
    colsList.add(new Cols("guid", "GUID", Type.NUMBER));
    for (int i = 0; i <= end - start; i++) {
      Value<Integer> value = new Value<>();
      value.setV(subprimeGuidRepo.countByCreatedAtBetween(
          RubyCollections.date(start + i).beginningOfYear(),
          RubyCollections.date(start + i).endOfYear()));
      rowsList.get(i).getC().add(value);
    }
    DataTable dataTable = new DataTable(colsList, rowsList);
    return dataTable;
  }

  @Override
  public DataTable lineChartBetweenByPrefix(String prefix, Integer start,
      Integer end) {
    if (end < start) {
      int temp = start;
      start = end;
      end = temp;
    }
    List<Cols> colsList = newArrayList(new Cols("year", "Year", Type.STRING));
    List<Rows> rowsList = newArrayList();
    for (int i = 0; i <= end - start; i++) {
      Value<String> value = new Value<>();
      value.setV(String.valueOf(start + i));
      Rows row = new Rows(newArrayList(value));
      rowsList.add(row);
    }
    colsList.add(new Cols("prefix", prefix, Type.NUMBER));
    for (int i = 0; i <= end - start; i++) {
      Value<Integer> value = new Value<>();
      value.setV(subprimeGuidRepo.countByPrefixAndCreatedAtBetween(prefix,
          RubyCollections.date(start + i).beginningOfYear(),
          RubyCollections.date(start + i).endOfYear()));
      rowsList.get(i).getC().add(value);
    }
    DataTable dataTable = new DataTable(colsList, rowsList);
    return dataTable;
  }

  @Override
  public DataTable pieChartAll() {
    List<Cols> colsList =
        newArrayList(new Cols("prefix", "Prefix", Type.STRING),
            new Cols("number", "Number", Type.NUMBER));
    List<Rows> rowsList = newArrayList();
    for (String p : institutePrefixRepo.getAllPrefix()) {
      if (subprimeGuidRepo.findByPrefix(p).size() > 0) {
        Value<String> strValue = new Value<>();
        strValue.setV(p);
        Value<Integer> numValue = new Value<>();
        numValue.setV(subprimeGuidRepo.findByPrefix(p).size());
        Rows row = new Rows(newArrayList(strValue, numValue));
        rowsList.add(row);
      }
    }
    DataTable dataTable = new DataTable(colsList, rowsList);
    return dataTable;
  }

  @Override
  public DataTable pieChartBetween(Integer start, Integer end) {
    if (end < start) {
      int temp = start;
      start = end;
      end = temp;
    }
    List<Cols> colsList =
        newArrayList(new Cols("prefix", "Prefix", Type.STRING),
            new Cols("number", "Number", Type.NUMBER));
    List<Rows> rowsList = newArrayList();
    for (String p : institutePrefixRepo.getAllPrefix()) {
      if (subprimeGuidRepo.findByPrefix(p).size() > 0) {
        Value<String> strValue = new Value<>();
        strValue.setV(p);
        Value<Integer> numValue = new Value<>();
        numValue.setV(subprimeGuidRepo.countByPrefixAndCreatedAtBetween(p,
            RubyCollections.date(start).beginningOfYear(),
            RubyCollections.date(end).endOfYear()));
        Rows row = new Rows(newArrayList(strValue, numValue));
        rowsList.add(row);
      }
    }
    DataTable dataTable = new DataTable(colsList, rowsList);
    return dataTable;
  }

}
