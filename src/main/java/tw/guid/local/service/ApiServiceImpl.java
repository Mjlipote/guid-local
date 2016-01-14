/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 *
 * @author Ming-Jheng Li
 *
 */
package tw.guid.local.service;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;
import static net.sf.rubycollect4j.RubyCollections.ra;

import java.time.Month;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import net.sf.rubycollect4j.RubyCollections;
import tw.guid.local.google.chart.Cells;
import tw.guid.local.google.chart.ColumnDescription;
import tw.guid.local.google.chart.DataTable;
import tw.guid.local.google.chart.Type;
import tw.guid.local.google.chart.Value;
import tw.guid.local.repository.AssociationRepository;
import tw.guid.local.repository.InstitutePrefixRepository;
import tw.guid.local.repository.SubprimeGuidRepository;

public class ApiServiceImpl implements ApiService {

  private static final List<String> MONTH =
      ra(Month.values()).map(m -> m.toString());

  @Autowired
  private SubprimeGuidRepository subprimeGuidRepo;
  @Autowired
  private AssociationRepository associationRepo;
  @Autowired
  private InstitutePrefixRepository institutePrefixRepo;

  /**
   * Get all prefix List
   * 
   * @return
   */
  @Override
  public Set<String> prefixLookup() {
    return institutePrefixRepo.getAllPrefix();
  }

  @Override
  public Set<String> hospitalLookup() {
    return associationRepo.getAllHospital();
  }

  @Override
  public Set<String> doctorLookup() {
    return associationRepo.getAllDoctor();
  }

  @Override
  public boolean validation(String spguid) {
    return spguid.split("-")[1].length() == 8;
  }

  /**
   * 確認是否存在於 local server 資料庫
   * 
   * @param subprimeGuid
   * @return
   */
  @Override
  public boolean existence(String subprimeGuid) {
    checkNotNull(subprimeGuid, "subprimeGuid can't be null");
    return subprimeGuidRepo.findBySpguid(subprimeGuid) != null;
  }

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
    List<ColumnDescription> colsList =
        newArrayList(new ColumnDescription("month", "Mounth", Type.STRING));
    List<Cells> rowsList = newArrayList();
    for (int i = 0; i < 12; i++) {
      Value<String> value = new Value<>();
      value.setV(MONTH.get(i));
      Cells row = new Cells(newArrayList(value));
      rowsList.add(row);
    }
    for (String p : institutePrefixRepo.getAllPrefix()) {
      if (subprimeGuidRepo.findByPrefix(p).size() > 0) {
        colsList.add(new ColumnDescription("prefix", p, Type.NUMBER));
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
    List<ColumnDescription> colsList =
        newArrayList(new ColumnDescription("month", "Mounth", Type.STRING));
    List<Cells> rowsList = newArrayList();
    for (int i = 0; i < 12; i++) {
      Value<String> value = new Value<>();
      value.setV(MONTH.get(i));
      Cells row = new Cells(newArrayList(value));
      rowsList.add(row);
    }
    colsList.add(new ColumnDescription("guid", "GUID", Type.NUMBER));
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
    List<ColumnDescription> colsList =
        newArrayList(new ColumnDescription("month", "Mounth", Type.STRING));
    List<Cells> rowsList = newArrayList();
    for (int i = 0; i < 12; i++) {
      Value<String> value = new Value<>();
      value.setV(MONTH.get(i));
      Cells row = new Cells(newArrayList(value));
      rowsList.add(row);
    }
    colsList.add(new ColumnDescription("prefix", prefix, Type.NUMBER));
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
    List<ColumnDescription> colsList =
        newArrayList(new ColumnDescription("year", "Year", Type.STRING));
    List<Cells> rowsList = newArrayList();
    for (int i = 0; i <= end - start; i++) {
      Value<String> value = new Value<>();
      value.setV(String.valueOf(start + i));
      Cells row = new Cells(newArrayList(value));
      rowsList.add(row);
    }
    for (String p : institutePrefixRepo.getAllPrefix()) {
      if (subprimeGuidRepo.findByPrefix(p).size() > 0) {
        colsList.add(new ColumnDescription("prefix", p, Type.NUMBER));
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
    List<ColumnDescription> colsList =
        newArrayList(new ColumnDescription("year", "Year", Type.STRING));
    List<Cells> rowsList = newArrayList();
    for (int i = 0; i <= end - start; i++) {
      Value<String> value = new Value<>();
      value.setV(String.valueOf(start + i));
      Cells row = new Cells(newArrayList(value));
      rowsList.add(row);
    }
    colsList.add(new ColumnDescription("guid", "GUID", Type.NUMBER));
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
    List<ColumnDescription> colsList =
        newArrayList(new ColumnDescription("year", "Year", Type.STRING));
    List<Cells> rowsList = newArrayList();
    for (int i = 0; i <= end - start; i++) {
      Value<String> value = new Value<>();
      value.setV(String.valueOf(start + i));
      Cells row = new Cells(newArrayList(value));
      rowsList.add(row);
    }
    colsList.add(new ColumnDescription("prefix", prefix, Type.NUMBER));
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
    List<ColumnDescription> colsList =
        newArrayList(new ColumnDescription("prefix", "Prefix", Type.STRING),
            new ColumnDescription("number", "Number", Type.NUMBER));
    List<Cells> rowsList = newArrayList();
    for (String p : institutePrefixRepo.getAllPrefix()) {
      if (subprimeGuidRepo.findByPrefix(p).size() > 0) {
        Value<String> strValue = new Value<>();
        strValue.setV(p);
        Value<Integer> numValue = new Value<>();
        numValue.setV(subprimeGuidRepo.findByPrefix(p).size());
        Cells row = new Cells(newArrayList(strValue, numValue));
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
    List<ColumnDescription> colsList =
        newArrayList(new ColumnDescription("prefix", "Prefix", Type.STRING),
            new ColumnDescription("number", "Number", Type.NUMBER));
    List<Cells> rowsList = newArrayList();
    for (String p : institutePrefixRepo.getAllPrefix()) {
      if (subprimeGuidRepo.findByPrefix(p).size() > 0) {
        Value<String> strValue = new Value<>();
        strValue.setV(p);
        Value<Integer> numValue = new Value<>();
        numValue.setV(subprimeGuidRepo.countByPrefixAndCreatedAtBetween(p,
            RubyCollections.date(start).beginningOfYear(),
            RubyCollections.date(end).endOfYear()));
        Cells row = new Cells(newArrayList(strValue, numValue));
        rowsList.add(row);
      }
    }
    DataTable dataTable = new DataTable(colsList, rowsList);
    return dataTable;
  }
}
