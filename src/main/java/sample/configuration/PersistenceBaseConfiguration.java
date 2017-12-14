package sample.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

@Configuration
public class PersistenceBaseConfiguration {
@Value("sampleDS") public String jndiName;
	
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
}
