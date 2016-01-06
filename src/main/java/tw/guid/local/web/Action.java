/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 *
 * @author Ming-Jheng Li
 *
 */
package tw.guid.local.web;

public enum Action {

  NEW(""), AUTHENTICATION("authentication"), EXISTENCE("existence"),
  COMPARISON("comparison"), VALIDATION("validation"), USERS("users"),
  GUIDS_NEW("guids"), GUIDS_COMPARISON("guids/comparison"),
  API_USERS("api/users"), API_EXISTENCE("api/existence"),
  API_VALIDATION("api/validation"), API_PREFIX("api/prefix");

  private String action;

  private Action(String action) {
    this.action = action;
  }

  @Override
  public String toString() {
    return action;
  }
}
