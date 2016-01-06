/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 *
 * @author Ming-Jheng Li
 *
 */
package tw.guid.local.web;

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
import tw.guid.local.repository.AccountUsersRepository;

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