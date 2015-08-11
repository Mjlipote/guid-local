/****@author Ming-Jheng Li***Copyright 2014 Ming-Jheng Li**Licensed under the Apache License,Version 2.0(the"License");you may not*use this file except in compliance with the License.You may obtain a copy/of*the License at**http://www.apache.org/licenses/LICENSE-2.0
**Unless required by applicable law or agreed to in writing,software*distributed under the License is distributed on an"AS IS"BASIS,WITHOUT*WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.See the*License for the specific language governing permissions and limitations/under*the License.**/
package tw.edu.ym.lab525.web.guidlocalserver;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("tw.edu.ym.lab525.web.guidlocalserver.models.repo")
@PropertySource({ "classpath:jdbc.properties", "classpath:hibernate.properties" })
public class PersistenceConfig {

  @Autowired
  private Environment env;

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
    emf.setDataSource(dataSource());
    emf.setPackagesToScan("tw.edu.ym.lab525.web.guidlocalserver.models.entity");

    JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    emf.setJpaVendorAdapter(vendorAdapter);
    emf.setJpaProperties(hibernateProperties());

    return emf;
  }

  @SuppressWarnings("serial")
  private Properties hibernateProperties() {
    return new Properties() {

      {
        setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto", "create"));
        setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
        setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql", "true"));
        setProperty("hibernate.globally_quoted_identifiers",
            env.getProperty("hibernate.globally_quoted_identifiers", "true"));
      }
    };
  }

  @Bean
  public DataSource dataSource() {
    HikariDataSource ds = new HikariDataSource();
    ds.setMaximumPoolSize(env.getProperty("jdbc.pool.size", int.class));
    ds.setDriverClassName(env.getProperty("jdbc.driverClassName"));
    ds.setJdbcUrl(env.getProperty("jdbc.url"));
    ds.setUsername(env.getProperty("jdbc.username"));
    ds.setPassword(env.getProperty("jdbc.password"));

    return ds;
  }

  @Autowired
  @Bean
  public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(emf);

    return transactionManager;
  }

  @Bean
  public PersistenceExceptionTranslationPostProcessor

  exceptionTranslationPostProcessor() {
    return new PersistenceExceptionTranslationPostProcessor();
  }

  @Bean
  public static PersistenceAnnotationBeanPostProcessor annotationBeanPostProcessor() {
    return new PersistenceAnnotationBeanPostProcessor();
  }

  @Bean
  public EmbeddedServletContainerFactory servletContainer() {
    TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() {

      @Override
      protected void postProcessContext(Context context) {
        SecurityConstraint securityConstraint = new SecurityConstraint();
        securityConstraint.setUserConstraint("CONFIDENTIAL");
        SecurityCollection collection = new SecurityCollection();
        collection.addPattern("/*");
        securityConstraint.addCollection(collection);
        context.addConstraint(securityConstraint);
      }
    };

    tomcat.addAdditionalTomcatConnectors(initiateHttpConnector());
    return tomcat;
  }

  private Connector initiateHttpConnector() {
    Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
    connector.setScheme("http");
    connector.setPort(8080);
    connector.setSecure(false);
    connector.setRedirectPort(8443);

    return connector;
  }

}
