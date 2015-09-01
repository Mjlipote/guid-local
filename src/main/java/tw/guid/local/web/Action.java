/**
 *
 * @author Ming-Jheng Li
 *
 *
 *         Copyright 2015 Ming-Jheng Li
 *
 *         Licensed under the Apache License, Version 2.0 (the "License"); you
 *         may not use this file except in compliance with the License. You may
 *         obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an "AS IS" BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *         implied. See the License for the specific language governing
 *         permissions and limitations under the License.
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