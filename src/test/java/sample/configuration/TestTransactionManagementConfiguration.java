package sample.configuration;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import bitronix.tm.BitronixTransactionManager;
import bitronix.tm.TransactionManagerServices;

@Configuration
@EnableTransactionManagement
public class TestTransactionManagementConfiguration {
	
	private bitronix.tm.Configuration btmConfig(){
		bitronix.tm.Configuration configuration = TransactionManagerServices.getConfiguration();
//		configuration.setServerId("TXMGR");
		return configuration;
	}
//	
	@Bean//(destroyMethod="shutdown") 
	public BitronixTransactionManager bitronixTransactionManager() throws Exception{
		btmConfig();
		BitronixTransactionManager transactionManager = TransactionManagerServices.getTransactionManager();
		transactionManager.setTransactionTimeout(120);
		return transactionManager;
	}

	@Bean public JtaTransactionManager transactionManager(UserTransaction userTransaction) throws Exception{
		TransactionManager btmTransactionManager = bitronixTransactionManager();
		JtaTransactionManager bean = new JtaTransactionManager();
		bean.setTransactionManager(btmTransactionManager);
		bean.setUserTransaction(userTransaction);
		bean.setAllowCustomIsolationLevels(true);
		bean.setDefaultTimeout(0);
		return bean;
	}
}
