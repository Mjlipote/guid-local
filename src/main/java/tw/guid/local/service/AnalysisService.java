/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 *
 * @author Ming-Jheng Li
 *
 */
package tw.guid.local.service;

import tw.guid.local.google.chart.DataTable;

public interface AnalysisService {

  public Integer countAllSubprimeGuid();

  public DataTable googleLineChartPrefixLookup(String prefix, Integer start,
      Integer end);

  public DataTable googleLineChartLookup(Integer start, Integer end);

}
