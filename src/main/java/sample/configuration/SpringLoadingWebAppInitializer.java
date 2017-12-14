package sample.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class SpringLoadingWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { ApplicationConfiguration.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		
		return null;
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] {"/"};
	}
	
	/*
	 * 
	 * (non-Javadoc)
	 * 
	 * This is helpful when using profiles in the real world.  All you need to is put a
	 * servlet.properties file in a maven profile configured directory and this will pick it 
	 * up and set the intended spring profile(s).  With out a file like in this sample 
	 * project no profile will be set as expected.
	 * 
	 * 
	 * @see org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer#createRootApplicationContext()
	 */
	@Override
	protected WebApplicationContext createRootApplicationContext() {
		WebApplicationContext context = super.createRootApplicationContext();
		Properties properties = readProperties("application.properties");
		
		if(properties != null) {
			String springProfilesPropertyValue = properties.getProperty("spring.profiles.active");
			if(springProfilesPropertyValue == null) {
				System.out.println("No value found for spring profiles property");
			}else {
				String[] springProfiles = springProfilesPropertyValue.split(",");
				for(String springProfile:springProfiles) {
					System.out.println("setting spring profile " + springProfile);
					
				}
				((AnnotationConfigWebApplicationContext)context).getEnvironment().setActiveProfiles(springProfiles);
			}
			
		}
		return context;
		
	}

	public Properties readProperties (String propertiesFileName) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();;
		InputStream inputStream = classLoader.getResourceAsStream(propertiesFileName);
		System.out.println("Reading properties  ("+propertiesFileName+")");
		
		try {
			if(inputStream == null) {
				System.out.println("Could not find properties ("+propertiesFileName+")");
				return null;
			}else {
				Properties properties = new Properties();
				properties.load(inputStream);
				return properties;
			}
		}catch(IOException ioException) {
			throw new RuntimeException("Could not load properties ("+propertiesFileName+") ");
		}
	}
	
}
