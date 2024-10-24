package com.readify.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
@PropertySource(value = {"classpath:application.properties"})
public class WebMvcConfiguration {
  @Value(value = "${spring.datasource.driver-class-name}")
  private String driver;

  @Value(value = "${spring.datasource.url}")
  private String dbURL;

  @Value(value = "${spring.datasource.username}")
  private String dbUsername;

  @Value(value = "${spring.datasource.password}")
  private String dbPassword;

  @Value(value = "${connection.pool.initialPoolSize}")
  private int connPoolInitSize;

  @Value(value = "${connection.pool.minPoolSize}")
  private int connPoolMinSize;

  @Value(value = "${connection.pool.maxPoolSize}")
  private int connPoolMaxSize;

  @Value(value = "${connection.pool.maxIdleTime}")
  private int connPoolMaxIdleTime;

  @Bean
  public MessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource =
        new ReloadableResourceBundleMessageSource();
    messageSource.addBasenames(new String[] {"classpath:messages"});
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }

  @Bean
  public DataSource securityDataSource() {
    ComboPooledDataSource securityDataSource = new ComboPooledDataSource();
    try {
      securityDataSource.setDriverClass(this.driver);
    } catch (PropertyVetoException exc) {
      throw new RuntimeException(exc);
    }
    securityDataSource.setJdbcUrl(this.dbURL);
    securityDataSource.setUser(this.dbUsername);
    securityDataSource.setPassword(this.dbPassword);
    securityDataSource.setInitialPoolSize(this.connPoolInitSize);
    securityDataSource.setMinPoolSize(this.connPoolMinSize);
    securityDataSource.setMaxPoolSize(this.connPoolMaxSize);
    securityDataSource.setMaxIdleTime(this.connPoolMaxIdleTime);
    return securityDataSource;
  }
}
