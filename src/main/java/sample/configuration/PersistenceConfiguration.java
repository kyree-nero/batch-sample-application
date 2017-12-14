package sample.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;



@Configuration
@Import({
	PersistenceBaseConfiguration.class, 
	TransactionManagementConfiguration.class
})

public class PersistenceConfiguration {
	
}
