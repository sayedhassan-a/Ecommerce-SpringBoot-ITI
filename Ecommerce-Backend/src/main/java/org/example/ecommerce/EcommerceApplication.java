package org.example.ecommerce;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
public class EcommerceApplication {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("SQL_DB_URL", dotenv.get("SQL_DB_URL"));
        System.setProperty("SQL_DB_USERNAME", dotenv.get("SQL_DB_USERNAME"));
        System.setProperty("SQL_DB_PASSWORD", dotenv.get("SQL_DB_PASSWORD"));
        SpringApplication.run(EcommerceApplication.class, args);
    }

}
