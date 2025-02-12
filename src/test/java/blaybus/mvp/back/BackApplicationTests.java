package blaybus.mvp.back;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

@SpringBootTest
class BackApplicationTests {

	@Autowired
	private Environment environment;

	@Test
	void checkDatabaseConfig() {
		System.out.println("DB URL: " + environment.getProperty("spring.datasource.url"));
		System.out.println("DB Username: " + environment.getProperty("spring.datasource.username"));
	}

}
