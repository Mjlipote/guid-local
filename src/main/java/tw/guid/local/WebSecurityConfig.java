/**
 *
 * @author Ming-Jheng Li
 *
 *
 * Copyright 2014 Ming-Jheng Li
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
package tw.guid.local;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

import tw.guid.local.models.CustomAuthenticationProvider;

@Configuration
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  CustomAuthenticationProvider customAuthenticationProvider;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests().antMatchers("/register", "/comparison")
        .hasAuthority("ROLE_ADMIN").antMatchers("/*")
        .hasAnyAuthority("ROLE_ADMIN", "ROLE_USER").antMatchers("/guid/api/*")
        .permitAll().and().formLogin().loginPage("/login").permitAll().and()
        .logout().permitAll().and().csrf().disable();

    // http.authorizeRequests().anyRequest().permitAll().and().csrf().disable()
    // .httpBasic();
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth)
      throws Exception {
    auth.authenticationProvider(customAuthenticationProvider);
  }

}
