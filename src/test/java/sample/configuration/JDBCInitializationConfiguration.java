package sample.configuration;

import java.io.InputStream;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

@Configuration
public class JDBCInitializationConfiguration {
	
	@Value("sampleDS") String jndiName;
	@Value("org/springframework/batch/core/schema-drop-h2.sql") Resource dropBatchScriptResource;
	@Value("org/springframework/batch/core/schema-h2.sql") Resource addBatchScriptResource;
	
	@Bean public DataSource dataSource() {
	    JndiDataSourceLookup lookup = new JndiDataSourceLookup();
	    return lookup.getDataSource(jndiName);
	}
	@Bean JdbcOperations jdbcOperations(DataSource dataSource) {
		return new JdbcTemplate(dataSource());
	}
	@Bean NamedParameterJdbcOperations namedParameterJdbcOperations() {
		return new NamedParameterJdbcTemplate(dataSource());
	}
	
	
	@Bean public DataSourceInitializer dataSourceInitializer() {
		DataSourceInitializer bean = new DataSourceInitializer();
		bean.setDataSource(dataSource());
		bean.setDatabasePopulator(databasePopulator());
		return bean;
	}
	
	@Bean 
	public ResourceDatabasePopulator databasePopulator() {
		ResourceDatabasePopulator bean = new ResourceDatabasePopulator();
		String[] locations = {
				"/META-INF/data/h2/drops.sql",
				"/META-INF/data/h2/tables.sql",
				"/META-INF/data/h2/inserts.sql"
				
				
		};
		for(String location : locations) {
			System.out.println("loading JDBC ddl " + location);
			InputStream inputStream = getClass().getResourceAsStream(location);
			bean.addScript(new InputStreamResource(inputStream));
		}
		bean.addScript(dropBatchScriptResource);
		bean.addScript(addBatchScriptResource);
		return bean;
	}
}
