package org.example.ecommerce;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.ecommerce.repositories.CategoryRepository;
import org.example.ecommerce.services.CategoryService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
@EnableMongoRepositories
public class EcommerceApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load(); // Load .env file
        System.setProperty("SQL_DB_URL", dotenv.get("SQL_DB_URL"));
        System.setProperty("SQL_DB_USERNAME", dotenv.get("SQL_DB_USERNAME"));
        System.setProperty("SQL_DB_PASSWORD", dotenv.get("SQL_DB_PASSWORD"));
        System.setProperty("MONGO_DATABASE", dotenv.get("MONGO_DATABASE"));
        System.setProperty("MONGO_USER", dotenv.get("MONGO_USER"));
        System.setProperty("MONGO_PASSWORD", dotenv.get("MONGO_PASSWORD"));
        System.setProperty("MONGO_CLUSTER", dotenv.get("MONGO_CLUSTER"));
        SpringApplication.run(EcommerceApplication.class, args);
    }


}
