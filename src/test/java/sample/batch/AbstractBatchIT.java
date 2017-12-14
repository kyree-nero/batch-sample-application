package sample.batch;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import org.junit.Test;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import sample.configuration.BatchConfiguration;
import sample.configuration.JmsConfiguration;
import sample.configuration.PersistenceConfiguration;

@ContextConfiguration(loader=AnnotationConfigContextLoader.class, 
classes= {
		PersistenceConfiguration.class, 
		JmsConfiguration.class,
		BatchConfiguration.class
}
)
@ActiveProfiles({"test", "master"})
public abstract class AbstractBatchIT extends AbstractJNDIResourceIT {
	@Autowired JobLauncher jobLauncher;
	@Autowired ConfigurableEnvironment env;
	
	
	protected AbstractApplicationContext startNewSlave(ExecutorService executorService) {
		AnnotationConfigApplicationContext slaveContext = new AnnotationConfigApplicationContext();
		Callable<Integer> slave = createSlave(slaveContext);
		executorService.submit(slave);
		return slaveContext;
	}
	
	private Callable<Integer> createSlave(AnnotationConfigApplicationContext context){
		return new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {
				context.register(
						PersistenceConfiguration.class, 
						JmsConfiguration.class,
						BatchConfiguration.class
				);
				ConfigurableEnvironment convenv = (ConfigurableEnvironment)context.getEnvironment();
				convenv.merge(env);
				context.getEnvironment().setActiveProfiles("test", "slave");
				context.refresh();
				return 0;
			}
			
		};
	}
}
