/**
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

package tw.guid.local.service;

import java.net.URISyntaxException;
import java.util.Set;

public interface ApiService {

  public Set<String> prefixLookup();

  public Set<String> hospitalLookup();

  public Set<String> doctorLookup();

  public boolean validation(String spguid) throws URISyntaxException;

  public boolean existence(String hashcode1, String hashcode2, String hashcode3)
      throws URISyntaxException;
}
