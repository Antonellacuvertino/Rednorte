package cl.duoc.rednorte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BffRednorteApplication {
    public static void main(String[] args) {
        SpringApplication.run(BffRednorteApplication.class, args);
    }
}