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
package tw.guid.local.model;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import tw.guid.local.helper.HashcodeCreator;
import tw.guid.local.model.repo.AccountUsersRepository;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

  @Autowired
  AccountUsersRepository acctUserRepo;

  @Override
  public Authentication authenticate(Authentication authentication)
      throws AuthenticationException {
    String username = authentication.getName();
    String password =
        HashcodeCreator.getSha512(authentication.getCredentials().toString());

    if (acctUserRepo.findByUsernameAndPasswordAndRole(username, password,
        Role.ROLE_ADMIN) != null) {
      List<GrantedAuthority> grantedAuths = newArrayList();

      grantedAuths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
      Authentication auth = new UsernamePasswordAuthenticationToken(username,
          password, grantedAuths);

      return auth;
    } else if (acctUserRepo.findByUsernameAndPasswordAndRole(username, password,
        Role.ROLE_USER) != null) {
      List<GrantedAuthority> grantedAuths = newArrayList();
      grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
      Authentication auth = new UsernamePasswordAuthenticationToken(username,
          password, grantedAuths);

      return auth;
    } else {
      return null;
    }

  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }

}