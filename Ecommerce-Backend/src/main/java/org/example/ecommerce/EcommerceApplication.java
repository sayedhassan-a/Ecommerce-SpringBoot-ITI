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
        Dotenv dotenv = Dotenv.load(); // Load .env file
        System.setProperty("SQL_DB_URL", dotenv.get("SQL_DB_URL"));
        System.setProperty("SQL_DB_USERNAME", dotenv.get("SQL_DB_USERNAME"));
        System.setProperty("SQL_DB_PASSWORD", dotenv.get("SQL_DB_PASSWORD"));
        System.setProperty("MONGODB_URL", dotenv.get("MONGODB_URL"));
        SpringApplication.run(EcommerceApplication.class, args);
    }

}
