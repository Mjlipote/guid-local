/*
 *
 * @author Ming-Jheng Li
 *
 *
 * Copyright 2015 Ming-Jheng Li
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package tw.guid.local.helper;

import static net.sf.rubycollect4j.RubyCollections.ra;

import java.util.List;
import java.util.Map;

import com.github.wnameless.workbookaccessor.WorkbookReader;

import tw.guid.client.PII;
import tw.guid.client.field.BaseNationalId;
import tw.guid.client.field.Birthday;
import tw.guid.client.field.Name;
import tw.guid.client.field.NationalId;
import tw.guid.client.field.Sex;
import tw.guid.client.field.TWNationalId;
import tw.guid.client.field.validation.TWNationalIdValidator;
import tw.guid.local.util.NameSplitter;

public final class BatchSubprimeGuidCreator {

  public static final List<String> EXCEL_HEADER =
      ra("ID", "SUBJECTID", "MRN", "GIID", "GIIDCOUNTRY", "SEX", "MOB", "DOB",
          "YOB", "Dr", "HP", "FULLNAME").freeze();

  private BatchSubprimeGuidCreator() {}

  public static boolean isContainsItem(WorkbookReader reader) {
    for (String str : EXCEL_HEADER) {
      if (!reader.getHeader().contains(str)) return false;
    }
    return true;
  }

  public static boolean isEmptyOnEachRow(Map<String, String> row) {
    if (row.get("ID").isEmpty() || row.get("SUBJECTID").isEmpty()
        || row.get("FULLNAME").isEmpty() || row.get("MOB").isEmpty()
        || row.get("DOB").isEmpty() || row.get("YOB").isEmpty()
        || row.get("GIID").isEmpty() || row.get("GIIDCOUNTRY").isEmpty()
        || row.get("SEX").isEmpty()) {
      return true;
    } else {
      return false;
    }
  }

  public static boolean isValidateSocialId(Map<String, String> row) {
    if (TWNationalIdValidator.validate(row.get("GIID"))
        && row.get("GIIDCOUNTRY").equals("TW")) {
      return true;
    } else {
      return false;
    }
  }

  public static PII rowToPII(Map<String, String> row) {
    Name name = NameSplitter.split(row.get("FULLNAME"));
    Sex sex = row.get("SEX").equals("M") ? Sex.MALE : Sex.FEMALE;
    Birthday birthday = new Birthday(Integer.valueOf(row.get("YOB")),
        Integer.valueOf(row.get("MOB")), Integer.valueOf(row.get("DOB")));
    NationalId nationalId =
        row.get("GIIDCOUNTRY").equals("TW") ? new TWNationalId(row.get("GIID"))
            : new BaseNationalId(row.get("GIID"));
    return new PII.Builder(name, sex, birthday, nationalId).build();
  }

}
