/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 *
 * @author Ming-Jheng Li
 *
 */
package tw.guid.local.google.chart;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Type {

  STRING, NUMBER, BOOLEAN, DATE, DATETIME;

  @JsonCreator
  public static Type fromLiteral(String literal) {
    return Type.valueOf(literal.toUpperCase());
  }

  @JsonValue
  public String toLiteral() {
    return this.toString().toLowerCase();
  }

}
