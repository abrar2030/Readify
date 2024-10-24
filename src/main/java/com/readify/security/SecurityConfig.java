package com.readify.security;

import javax.sql.DataSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  private DataSource securityDataSource;

  public SecurityConfig(DataSource securityDataSource) {
    this.securityDataSource = securityDataSource;
  }

  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.jdbcAuthentication().dataSource(this.securityDataSource);
  }

  protected void configure(HttpSecurity http) throws Exception {
    ((HttpSecurity)
            ((FormLoginConfigurer)
                    ((HttpSecurity)
                            ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)
                                    ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)
                                            ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)
                                                    ((ExpressionUrlAuthorizationConfigurer
                                                                .AuthorizedUrl)
                                                            ((ExpressionUrlAuthorizationConfigurer
                                                                        .AuthorizedUrl)
                                                                    ((ExpressionUrlAuthorizationConfigurer
                                                                                .AuthorizedUrl)
                                                                            ((HttpSecurity)
                                                                                    http.csrf()
                                                                                        .disable())
                                                                                .authorizeRequests()
                                                                                .antMatchers(
                                                                                    new String[] {
                                                                                      "/"
                                                                                    }))
                                                                        .permitAll()
                                                                        .antMatchers(
                                                                            new String[] {
                                                                              "/h2-console"
                                                                            }))
                                                                .permitAll()
                                                                .antMatchers(
                                                                    new String[] {"/search"}))
                                                        .permitAll()
                                                        .antMatchers(new String[] {"/cart/**"}))
                                                .permitAll()
                                                .antMatchers(new String[] {"/book/**"}))
                                        .hasAuthority("ADMIN")
                                        .antMatchers(new String[] {"/orders/**"}))
                                .hasAuthority("ADMIN")
                                .and())
                        .formLogin()
                        .loginPage("/login")
                        .permitAll())
                .and())
        .logout()
        .logoutUrl("/logout")
        .logoutSuccessUrl("/");
    http.headers().frameOptions().disable();
  }
}
