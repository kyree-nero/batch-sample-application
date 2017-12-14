package sample.configuration;

import java.util.Properties;
import java.util.UUID;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import bitronix.tm.resource.jdbc.PoolingDataSource;

public class JmsJndiTestSupport {
	public final String BROKER_URL ="vm://transport";//"tcp://localhost:61616";
	
	public final String JNDI_CONNECTIONFACTORY = "jmsConnectionFactory";
	public final String JNDI_BATCH = "batchQueue";
	
	
	public SimpleNamingContextBuilder initializeJNDI() throws Exception{
		System.out.println("...initialize JNDI");
		SimpleNamingContextBuilder context = SimpleNamingContextBuilder.getCurrentContextBuilder();
		
		
		if(context == null) {
			context = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
		}
		
		boolean foundObject  = false;
		try {
			new InitialContext().lookup(JNDI_CONNECTIONFACTORY);
			foundObject  = true;
		}catch(NamingException namingException) {
			foundObject  = false;
		}
		
		if(foundObject) {
			return context;
		}
		
	
		BrokerService brokerService = new BrokerService();
		brokerService.setBrokerName("localhost");
		brokerService.setUseJmx(false);
		brokerService.setUseShutdownHook(false);
		brokerService.setPersistent(false);
		brokerService.setTransportConnectorURIs(new String[] {BROKER_URL});
		brokerService.start();
		
		ActiveMQConnectionFactory jmsConnectionFactory = new ActiveMQConnectionFactory();
		jmsConnectionFactory.setBrokerURL(BROKER_URL);
		jmsConnectionFactory.afterPropertiesSet();
		
		context.bind("jmsConnectionFactory", jmsConnectionFactory);
		
		ActiveMQQueue batchQueue = new ActiveMQQueue("batchQueue");
		context.bind("jmsBatchQueue", batchQueue);
		
		
		return context;
		
	}
	
	public void cleanupJNDI() throws NamingException{
		System.out.println("...cleanup JNDI");
		SimpleNamingContextBuilder.emptyActivatedContextBuilder();
	}
}
