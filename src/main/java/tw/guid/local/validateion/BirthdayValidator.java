/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 *
 * @author Ming-Jheng Li
 *
 */
package tw.guid.local.validateion;

public class BirthdayValidator {

  private final Integer yearOfBirth;
  private final Integer monthOfBirth;
  private final Integer dayOfBirth;
  private String meg;

  public BirthdayValidator(int yearOfBirth, int monthOfBirth, int dayOfBirth) {
    this.yearOfBirth = yearOfBirth;
    this.monthOfBirth = monthOfBirth;
    this.dayOfBirth = dayOfBirth;
  }

  public boolean isValidate() {
    if (yearOfBirth < 1910 || yearOfBirth > 2100) {
      meg = "出生年份必須介於 1910 到 2100.";
      return false;
    } else if (monthOfBirth < 1 || monthOfBirth > 12) {
      meg = "出生月份必須介於 1 到 12.";
      return false;
    } else if (monthOfBirth == 1 && (dayOfBirth < 1 || dayOfBirth > 31)) {
      meg = "1 月份的出生日必須介於 1 到 31。";
      return false;
    } else if (monthOfBirth == 2 && yearOfBirth % 4 == 0
        && (dayOfBirth < 1 || dayOfBirth > 29)) {
      meg = "潤 2 月份的出生日必須介於 1 到 29.";
      return false;
    } else if (monthOfBirth == 2 && (dayOfBirth < 1 || dayOfBirth > 28)) {
      meg = "2 月份的出生日必須介於 1 到 28.";
      return false;
    } else if (monthOfBirth == 3 && (dayOfBirth < 1 || dayOfBirth > 31)) {
      meg = "3 月份的出生日必須介於 1 到 31。";
      return false;
    } else if (monthOfBirth == 4 && (dayOfBirth < 1 || dayOfBirth > 30)) {
      meg = "4 月份的出生日必須介於 1 到 30。";
      return false;
    } else if (monthOfBirth == 5 && (dayOfBirth < 1 || dayOfBirth > 31)) {
      meg = "5 月份的出生日必須介於 1 到 31。";
      return false;
    } else if (monthOfBirth == 6 && (dayOfBirth < 1 || dayOfBirth > 30)) {
      meg = "6 月份的出生日必須介於 1 到 30。";
      return false;
    } else if (monthOfBirth == 7 && (dayOfBirth < 1 || dayOfBirth > 31)) {
      meg = "7 月份的出生日必須介於 1 到 31。";
      return false;
    } else if (monthOfBirth == 8 && (dayOfBirth < 1 || dayOfBirth > 31)) {
      meg = "8 月份的出生日必須介於 1 到 31。";
      return false;
    } else if (monthOfBirth == 9 && (dayOfBirth < 1 || dayOfBirth > 30)) {
      meg = "9 月份的出生日必須介於 1 到 30。";
      return false;
    } else if (monthOfBirth == 10 && (dayOfBirth < 1 || dayOfBirth > 31)) {
      meg = "10 月份的出生日必須介於 1 到 31。";
      return false;
    } else if (monthOfBirth == 11 && (dayOfBirth < 1 || dayOfBirth > 30)) {
      meg = "11 月份的出生日必須介於 1 到 30。";
      return false;
    } else if (monthOfBirth == 12 && (dayOfBirth < 1 || dayOfBirth > 31)) {
      meg = "12 月份的出生日必須介於 1 到 31。";
      return false;
    } else {
      return true;
    }
  }

  /**
   * @return the meg
   */
  public String getMeg() {
    return meg;
  }

}
