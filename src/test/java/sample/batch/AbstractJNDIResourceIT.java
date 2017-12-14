package sample.batch;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

import sample.configuration.JdbcJndiTestSupport;
import sample.configuration.JmsJndiTestSupport;

public abstract class AbstractJNDIResourceIT extends AbstractBaseIT{

	protected static SimpleNamingContextBuilder context = null;
	
	@BeforeClass
	public static void beforeClass() throws Exception {
		
		new JdbcJndiTestSupport().initializeJNDI();
		new JmsJndiTestSupport().initializeJNDI();
		
		context = SimpleNamingContextBuilder.getCurrentContextBuilder();
	}
	
	@AfterClass
	public static void afterClass() throws Exception {
		
		//new JdbcJndiTestSupport().cleanupJNDI();
		
	}
}
