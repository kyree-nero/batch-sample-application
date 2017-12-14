package sample.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;


@Configuration
@Profile("test")
@Import(TransactionManagementConfigurationExtendedImportSelector.class)
public class TransactionManagementConfigurationExtender {

}
