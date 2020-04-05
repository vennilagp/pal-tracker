package io.pivotal.pal.tracker;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PalTrackerApplication {
	@Autowired
	private DataSource dataSource;
	
	@Bean
	public TimeEntryRepository getTimeEntryRepository() {
		 
		
		return new JdbcTimeEntryRepository(this.dataSource);
	}
	public static void main(String[] args) {
        SpringApplication.run(PalTrackerApplication.class, args);
    }
}
