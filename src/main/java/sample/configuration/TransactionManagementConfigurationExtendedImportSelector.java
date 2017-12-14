package sample.configuration;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/*
 * This is what I do when a test class needs to switch out for a runtime 
 * class in the test context only.  The test context usually has test scoped 
 * libraries associated with it, which can NOT be used in the main runtime 
 * library set.  This usage of an ImportSelector gets around that.
 */
public class TransactionManagementConfigurationExtendedImportSelector implements ImportSelector{

	@Override
	public String[] selectImports(AnnotationMetadata arg0) {
		return new String[] {"sample.configuration.TestTransactionManagementConfiguration"};
	}

}
