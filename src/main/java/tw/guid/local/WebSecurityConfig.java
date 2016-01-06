/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 *
 * @author Ming-Jheng Li
 *
 */
package tw.guid.local;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import tw.guid.local.web.CustomAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  CustomAuthenticationProvider customAuthenticationProvider;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/register", "/comparison", "/trail", "/batch/comparison",
            "/remove", "/users", "/institutes", "/institutes/*", "/association",
            "/association/*")
        .hasAuthority("ROLE_ADMIN")
        .antMatchers("/*", "/guids/*", "/users/*", "/users/changepassword/*")
        .hasAnyAuthority("ROLE_ADMIN", "ROLE_USER").antMatchers("/guid/**/*")
        .permitAll().and().formLogin().loginPage("/login").permitAll().and()
        .logout().permitAll();
    http.csrf().disable();
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth)
      throws Exception {
    auth.authenticationProvider(customAuthenticationProvider);
  }

}
