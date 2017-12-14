package sample.configuration;


import javax.persistence.EntityManagerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

@Configuration
@Profile({"default", "local"})
@EnableTransactionManagement
public class TransactionManagementDefaultConfiguration {
	
	@Bean  public JtaTransactionManager transactionManager() {
		return new JtaTransactionManager();
	}
	
//	 @Bean
//	   public PlatformTransactionManager transactionManager(EntityManagerFactory emf){
//	      JpaTransactionManager transactionManager = new JpaTransactionManager(emf);
//
//	      return transactionManager;
//	 }
}
