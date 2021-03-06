/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 *
 * @author Ming-Jheng Li
 *
 */
package tw.guid.local.service;

import java.util.Set;

import tw.guid.local.google.chart.DataTable;

public interface ApiService {

  public Set<String> prefixLookup();

  public Set<String> hospitalLookup();

  public Set<String> doctorLookup();

  public boolean validation(String spguid);

  public boolean existence(String subprimeGuid);

  public Integer countAllSubprimeGuid();

  public Integer countSubprimeGuidByYear(Integer year);

  public Integer countSubprimeGuidBetween(Integer start, Integer end);

  public DataTable lineChart(Integer year);

  public DataTable lineChartAll(Integer year);

  public DataTable lineChartByPrefix(String prefix, Integer year);

  public DataTable lineChartBetween(Integer start, Integer end);

  public DataTable lineChartAllBetween(Integer start, Integer end);

  public DataTable lineChartBetweenByPrefix(String prefix, Integer start,
      Integer end);

  public DataTable pieChartAll();

  public DataTable pieChartBetween(Integer start, Integer end);
}
