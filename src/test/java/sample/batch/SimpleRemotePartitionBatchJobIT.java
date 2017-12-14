package sample.batch;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import sample.configuration.BatchConfiguration;
import sample.configuration.JmsConfiguration;
import sample.configuration.PersistenceConfiguration;




public class SimpleRemotePartitionBatchJobIT extends AbstractBatchIT{
	
	@Autowired JdbcOperations jdbcOperations;
	@Autowired JobLauncher jobLauncher;
	@Autowired @Qualifier("simpleRemotePartitionBatchJob") Job simpleRemotePartitionBatchJob;
	
	
	@Test(timeout=60000) public void test() throws Exception{
		
		ExecutorService executorService = Executors.newFixedThreadPool(2);	
	
		AbstractApplicationContext slaveOneContext = startNewSlave(executorService);
		AbstractApplicationContext slaveTwoContext = startNewSlave(executorService);
		
		
	try {
	
		JobExecution jobExecution = jobLauncher.run(simpleRemotePartitionBatchJob, new JobParameters());
		BatchStatus batchStatus =  jobExecution.getStatus();
				
		Assert.assertEquals(BatchStatus.COMPLETED, batchStatus);
		Assert.assertEquals(10, jdbcOperations.queryForObject("select count(*) from BATCH_SAMPLE", Integer.class).intValue()); 
		Assert.assertEquals(10, jdbcOperations.queryForObject("select count(*) from BATCH_SAMPLE_OUTPUT", Integer.class).intValue()); 
		Assert.assertEquals(2, jdbcOperations.queryForObject("select count(distinct run_id) from BATCH_SAMPLE_OUTPUT", Integer.class).intValue()); 
		
		
	}finally {
		slaveOneContext.close();
		slaveTwoContext.close();
		
	}
	
} 
	
	
}
