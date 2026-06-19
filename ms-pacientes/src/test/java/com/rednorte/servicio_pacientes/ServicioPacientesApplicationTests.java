package com.rednorte.servicio_pacientes;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:pacientes-test;DB_CLOSE_DELAY=-1",
		"spring.jpa.hibernate.ddl-auto=create-drop"
})
class ServicioPacientesApplicationTests {

	@Test
	void contextLoads() {
	}

}
